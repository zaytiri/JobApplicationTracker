package personal.zaytiri.jobtracker.api.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import personal.zaytiri.jobtracker.api.database.operations.*;
import personal.zaytiri.jobtracker.api.database.requests.CreateOperationResponse;
import personal.zaytiri.jobtracker.api.database.requests.FindByTextOperationRequest;
import personal.zaytiri.jobtracker.api.database.requests.GetOperationRequest;
import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;
import personal.zaytiri.jobtracker.api.domain.entities.JobOfferStatus;
import personal.zaytiri.jobtracker.api.domain.entities.Status;
import personal.zaytiri.jobtracker.api.libraries.Jackson;
import personal.zaytiri.jobtracker.api.libraries.webscraper.WebScraper;
import personal.zaytiri.jobtracker.api.libraries.webscraper.WebScraperFactory;
import personal.zaytiri.jobtracker.api.mappers.JobOfferMapperImpl;
import personal.zaytiri.jobtracker.api.mappers.JobOfferStatusMapperImpl;
import personal.zaytiri.jobtracker.api.mappers.SettingsMapperImpl;
import personal.zaytiri.jobtracker.api.statistics.*;
import personal.zaytiri.jobtracker.persistence.DatabaseShema;
import personal.zaytiri.jobtracker.persistence.models.JobOfferModel;
import personal.zaytiri.jobtracker.persistence.models.JobOfferStatusModel;
import personal.zaytiri.jobtracker.persistence.models.SettingsModel;
import personal.zaytiri.jobtracker.persistence.repositories.base.Repository;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.enums.Operators;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.enums.Order;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("job-offer")
public class JobOfferController {
    @POST
    @Path("/create")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(String jobOffer) {
        JobOffer newJobOffer = new JobOffer();

        Jackson convert = new Jackson();
        convert.fromJsonToObject(jobOffer, newJobOffer);

        CreateOperation<JobOfferModel> createOperation = new CreateOperation<>();
        createOperation.setRepository(new Repository<>());

        CreateOperationResponse createJobOfferResponse = createOperation.execute(new JobOfferMapperImpl().entityToModel(newJobOffer));

        if (createJobOfferResponse.isCreated() && newJobOffer.getStatusId() != 0) {
            CreateOperation<JobOfferStatusModel> createJobOfferStatusOperation = new CreateOperation<>();
            createJobOfferStatusOperation.setRepository(new Repository<>());

            JobOfferStatus newJobOfferStatus = new JobOfferStatus();
            newJobOfferStatus.setJobOfferId(createJobOfferResponse.getIdCreated());
            newJobOfferStatus.setStatusId(newJobOffer.getStatusId());
            newJobOfferStatus.setChangedAt(LocalDateTime.now());

            createJobOfferStatusOperation.execute(new JobOfferStatusMapperImpl().entityToModel(newJobOfferStatus));
        }

        JSONObject obj = new JSONObject();
        obj.put("success", createJobOfferResponse.isCreated());

        return Response
                .ok()
                .entity(obj.toString())
                .build();
    }

    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, String jobOffer) {
        Map<String, Pair<String, Object>> filters = new HashMap<>();
        filters.put(DatabaseShema.getINSTANCE().idColumnName, new Pair<>(Operators.EQUALS.value, id));

        GetOperation<JobOfferModel> getOperation = new GetOperation<>();
        getOperation.setRepository(new Repository<>());

        GetOperationRequest<JobOfferModel> getOperationRequest = new GetOperationRequest<>();
        getOperationRequest.setModel(new JobOfferModel());
        getOperationRequest.setFilters(filters);
        getOperationRequest.setOrderByColumn(null);

        List<Map<String, String>> results = getOperation.execute(getOperationRequest);
        var oldJobOffer = new JobOfferMapperImpl().toEntity(results, false).get(0);

        JobOffer newJobOffer = new JobOffer();
        newJobOffer.setId(id);

        Jackson convert = new Jackson();
        convert.fromJsonToObject(jobOffer, newJobOffer);

        UpdateOperation<JobOfferModel> updateOperation = new UpdateOperation<>();
        updateOperation.setRepository(new Repository<>());

        boolean isUpdated = updateOperation.execute(new JobOfferMapperImpl().entityToModel(newJobOffer));

        if (isUpdated && oldJobOffer.getStatusId() != newJobOffer.getStatusId() && newJobOffer.getStatusId() != 0) {
            CreateOperation<JobOfferStatusModel> createJobOfferStatusOperation = new CreateOperation<>();
            createJobOfferStatusOperation.setRepository(new Repository<>());

            JobOfferStatus newJobOfferStatus = new JobOfferStatus();
            newJobOfferStatus.setJobOfferId(id);
            newJobOfferStatus.setStatusId(newJobOffer.getStatusId());
            newJobOfferStatus.setChangedAt(LocalDateTime.now());

            createJobOfferStatusOperation.execute(new JobOfferStatusMapperImpl().entityToModel(newJobOfferStatus));
        }

        JSONObject obj = new JSONObject();
        obj.put("success", isUpdated);

        return Response
                .ok()
                .entity(obj.toString())
                .build();
    }

    @POST
    @Path("/get")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(String filtersJson) {
        List<JobOffer> filteredJobOffers = retrieveJobOffers(filtersJson);

        String jsonToReturn = new Jackson().fromListToJson(filteredJobOffers);

        return Response
                .ok()
                .entity(jsonToReturn)
                .build();
    }

    @GET
    @Path("/find/{text}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findJobOffer(@PathParam("text") String text) {
        CompletableFuture<List<JobOffer>> byCompany = CompletableFuture.supplyAsync(() -> findBy(text, DatabaseShema.getINSTANCE().companyColumnName));
        CompletableFuture<List<JobOffer>> byRole = CompletableFuture.supplyAsync(() -> findBy(text, DatabaseShema.getINSTANCE().roleColumnName));
        CompletableFuture<List<JobOffer>> byLocation = CompletableFuture.supplyAsync(() -> findBy(text, DatabaseShema.getINSTANCE().locationColumnName));

        CompletableFuture<List<JobOffer>> combinedFuture = CompletableFuture.allOf(byCompany, byRole, byLocation)
                .thenApply(v -> {
                    List<JobOffer> allJobOffers = Stream.of(byCompany, byRole, byLocation)
                            .map(CompletableFuture::join)
                            .flatMap(List::stream)
                            .toList();

                    return new ArrayList<>(allJobOffers.stream()
                            .collect(Collectors.toMap(
                                    JobOffer::getId, // Property to distinct by
                                    jobOffer -> jobOffer, // Identity function
                                    (existing, replacement) -> existing // Merge function
                            ))
                            .values());
                });

        List<JobOffer> foundJobOffers = new ArrayList<>();
        try {
            foundJobOffers = combinedFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        String jsonToReturn = new Jackson().fromListToJson(foundJobOffers);

        return Response
                .ok()
                .entity(jsonToReturn)
                .build();
    }

    private List<JobOffer> findBy(String text, String columnName){
        FindByTextOperation<JobOfferModel> findByTextOperation = new FindByTextOperation<>();
        findByTextOperation.setRepository(new Repository<>());

        FindByTextOperationRequest<JobOfferModel> findByTextOperationRequest = new FindByTextOperationRequest<>();
        findByTextOperationRequest.setModel(new JobOfferModel());
        findByTextOperationRequest.setTextToFind(text);
        findByTextOperationRequest.setColumnToFind(columnName);

        var results = findByTextOperation.execute(findByTextOperationRequest);

        return new JobOfferMapperImpl().toEntity(results, false);
    }

    @DELETE
    @Path("/remove/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") int id) {

        JobOffer jobOfferToRemove = new JobOffer();
        jobOfferToRemove.setId(id);

        DeleteOperation<JobOfferModel> deleteOperation = new DeleteOperation<>();
        deleteOperation.setRepository(new Repository<>());

        boolean isDeleted = deleteOperation.execute(new JobOfferMapperImpl().entityToModel(jobOfferToRemove));

        // also remove all rows that are associated with the removed status in joboffer-status table.
        if(isDeleted){
            Map<String, Pair<String, Object>> filters = new HashMap<>();
            filters.put(DatabaseShema.getINSTANCE().jobOfferIdColumnName, new Pair<>(Operators.EQUALS.value, id));

            GetOperation<JobOfferStatusModel> getOperation = new GetOperation<>();
            getOperation.setRepository(new Repository<>());

            GetOperationRequest<JobOfferStatusModel> getRequest = new GetOperationRequest<>();
            getRequest.setModel(new JobOfferStatusModel());
            getRequest.setFilters(filters);

            List<JobOfferStatus> results = new JobOfferStatusMapperImpl().toEntity(getOperation.execute(getRequest), false);

            DeleteOperation<JobOfferStatusModel> deleteJOSOperation = new DeleteOperation<>();
            deleteJOSOperation.setRepository(new Repository<>());
            for (JobOfferStatus res : results){
                JobOfferStatus toRemove = new JobOfferStatus();
                toRemove.setId(res.getId());
                deleteJOSOperation.execute(new JobOfferStatusMapperImpl().entityToModel(toRemove));
            }
        }

        JSONObject obj = new JSONObject();
        obj.put("success", isDeleted);

        return Response
                .ok()
                .entity(obj.toString())
                .build();
    }

    @POST
    @Path("/scrape")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response scrapeJobOffer(String url){
        JSONObject obj = new JSONObject();

        JobOffer scrapedJobOffer;

        WebScraper scraper = WebScraperFactory.findSuitableScraper(url);
        if(scraper == null){
            JSONObject noSuitableResponse = new JSONObject();
            noSuitableResponse.put("success", false);
            noSuitableResponse.put("error_id", 1);
            return Response.ok().entity(noSuitableResponse.toString()).build();
        }

        try {
            scrapedJobOffer = scraper.getGeneralJobInformation(url);
        } catch (IOException e) {
            obj.put("success", false);
            obj.put("message", e);

            if(e.getMessage().contains("429")){
                obj.put("error_id", 2);
            } else if(e.getMessage().contains("Malformed HTML.")){
                obj.put("error_id", 3);
            }

            return Response.ok().entity(obj).build();
        }

        return Response.ok().entity(new Jackson().fromObjectToJson(scrapedJobOffer)).build();
    }

    @PATCH
    @Path("/update-job-status")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateJobStatus(int id){
        Map<String, Pair<String, Object>> filters = new HashMap<>();
        filters.put(DatabaseShema.getINSTANCE().idColumnName, new Pair<>(Operators.EQUALS.value, id));

        GetOperation<JobOfferModel> getOperation = new GetOperation<>();
        getOperation.setRepository(new Repository<>());

        GetOperationRequest<JobOfferModel> getOperationRequest = new GetOperationRequest<>();
        getOperationRequest.setModel(new JobOfferModel());
        getOperationRequest.setFilters(filters);
        getOperationRequest.setOrderByColumn(null);

        List<Map<String, String>> results = getOperation.execute(getOperationRequest);
        var currentJobOffer = new JobOfferMapperImpl().toEntity(results, false).get(0);

        JSONObject obj = new JSONObject();

        WebScraper scraper = WebScraperFactory.findSuitableScraper(currentJobOffer.getLink());
        if(scraper == null){
            JSONObject noSuitableResponse = new JSONObject();
            noSuitableResponse.put("message", "no suitable scraper found.");
            return Response.ok().entity(noSuitableResponse.toString()).build();
        }

        boolean isJobClosed;
        try {
            isJobClosed = scraper.isJobClosed(currentJobOffer.getLink());
        } catch (IOException | RuntimeException e) {
            obj.put("success", false);
            obj.put("message", e);
            return Response.ok().entity(obj.toString()).build();
        }

        if(!isJobClosed){
            obj.put("success", false);
            return Response
                    .ok()
                    .entity(obj.toString())
                    .build();
        }

        GetOperation<SettingsModel> getSettingsOperation = new GetOperation<>();
        getSettingsOperation.setRepository(new Repository<>());

        GetOperationRequest<SettingsModel> getSettingsOperationRequest = new GetOperationRequest<>();
        getSettingsOperationRequest.setModel(new SettingsModel());
        getSettingsOperationRequest.setFilters(new HashMap<>());
        getSettingsOperationRequest.setOrderByColumn(null);

        List<Map<String, String>> settingsResults = getSettingsOperation.execute(getSettingsOperationRequest);

        var jobClosedStatus = new SettingsMapperImpl().toEntity(settingsResults, false).get(0).getClosedStatus(); // get users mapping for when a job is closed.

        boolean isUpdated = false;
        if (currentJobOffer.getStatusId() != jobClosedStatus && jobClosedStatus != 0) {
            CreateOperation<JobOfferStatusModel> createJobOfferStatusOperation = new CreateOperation<>();
            createJobOfferStatusOperation.setRepository(new Repository<>());

            JobOfferStatus newJobOfferStatus = new JobOfferStatus();
            newJobOfferStatus.setJobOfferId(id);
            newJobOfferStatus.setStatusId(jobClosedStatus);
            newJobOfferStatus.setChangedAt(LocalDateTime.now());

            isUpdated = createJobOfferStatusOperation.execute(new JobOfferStatusMapperImpl().entityToModel(newJobOfferStatus)).isCreated();
        }

        obj.put("success", isUpdated);

        return Response
                .ok()
                .entity(obj.toString())
                .build();
    }

    @GET
    @Path("/job-offer-by-status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJobOffersByStatus(){
        var mapped = retrieveJobOffersByStatus();

        JSONArray array = new JSONArray();
        for (var elem : mapped.values()){
            array.put(elem);
        }
        return Response.ok().entity(array.toString()).build();
    }

    @GET
    @Path("/statistics")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatistics(){
        Statistics stats = new Statistics();
        stats.addStatistic(new TotalAppliedJobsByMonth());
        stats.addStatistic(new TotalAppliedJobsByDay());
        stats.addStatistic(new TotalAppliedJobs());
        stats.addStatistic(new TotalJobs());
        stats.addStatistic(new TotalJobsByLatestStatus());
        stats.addStatistic(new TotalJobsByStatus());

        Map<String, List<IStatistic<JobOffer>>> calculatedStats = stats.process();

        var result = new Jackson().fromObjectToJson(calculatedStats);

        return Response.ok().entity(result).build();
    }

    private HashMap<Integer, JSONObject> retrieveJobOffersByStatus(){
        List<JobOffer> jobOffers = retrieveJobOffers("{}");
        List<Status> statuses = new StatusController().retrieveStatuses("{}");

        HashMap<Integer, JSONObject> mapped = new HashMap<>();

        JSONObject defaultStatus = new JSONObject();
        defaultStatus.put("id", 0);
        defaultStatus.put("name", "No Status");
        defaultStatus.put("cards", new JSONArray());
        mapped.put(0, defaultStatus);

        for (var status : statuses){
            JSONObject obj = new JSONObject();
            obj.put("id", status.getId());
            obj.put("name", status.getName());
            obj.put("cards", new JSONArray());
            mapped.put(status.getId(), obj);
        }

        for (var jobOffer : jobOffers){
            JSONObject obj = new JSONObject();
            obj.put("id", jobOffer.getId());
            obj.put("company", jobOffer.getCompany());
            obj.put("role", jobOffer.getRole());
            obj.put("appliedAt", jobOffer.getAppliedAt());

            JSONObject status = mapped.get(jobOffer.getStatusId());
            JSONArray cards = (JSONArray) status.get("cards");
            cards.put(obj);
            status.put("cards", cards);
        }

        return mapped;
    }
    private List<JobOffer> retrieveJobOffers(String filtersJson) {
        Map<String, Pair<String, Object>> filters = new HashMap<>();
        JSONObject filtersToConvert = new JSONObject(filtersJson);

        for (String key : filtersToConvert.keySet()) {
            JSONObject pair = (JSONObject) filtersToConvert.get(key);

            for (String keyPair : pair.keySet()) {
                filters.put(key, new Pair<>(keyPair, pair.get(keyPair)));
            }
        }

        GetOperation<JobOfferModel> getOperation = new GetOperation<>();
        getOperation.setRepository(new Repository<>());

        GetOperationRequest<JobOfferModel> getOperationRequest = new GetOperationRequest<>();
        getOperationRequest.setModel(new JobOfferModel());
        getOperationRequest.setFilters(filters);
        getOperationRequest.setOrderByColumn(null);

        List<Map<String, String>> results = getOperation.execute(getOperationRequest);
        return new JobOfferMapperImpl().toEntity(results, false);
    }

    @GET
    @Path("/diagram")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSankeymaticDiagram(){

        GetOperation<JobOfferModel> getOperation = new GetOperation<>();
        getOperation.setRepository(new Repository<>());

        GetOperationRequest<JobOfferModel> getOperationRequest = new GetOperationRequest<>();
        getOperationRequest.setModel(new JobOfferModel());
        getOperationRequest.setFilters(new HashMap<>());
        getOperationRequest.setOrderByColumn(null);

        List<JobOffer> jobOffers = new JobOfferMapperImpl().toEntity(getOperation.execute(getOperationRequest), false);

        Map<Integer, Integer> statistics = new HashMap<>();
        for (JobOffer jo : jobOffers){
            Map<String, Pair<String, Object>> filters = new HashMap<>();
            filters.put(DatabaseShema.getINSTANCE().jobOfferIdColumnName, new Pair<>(Operators.EQUALS.value, jo.getId()));

            GetOperation<JobOfferStatusModel> getJOSOperation = new GetOperation<>();
            getJOSOperation.setRepository(new Repository<>());

            GetOperationRequest<JobOfferStatusModel> getJOSOperationRequest = new GetOperationRequest<>();
            getJOSOperationRequest.setModel(new JobOfferStatusModel());
            getJOSOperationRequest.setFilters(filters);
            getJOSOperationRequest.setOrderByColumn(new Pair<>(Order.DESCENDING.value, DatabaseShema.getINSTANCE().changedAtColumnName));

            List<JobOfferStatus> jobOfferStatuses = new JobOfferStatusMapperImpl().toEntity(getJOSOperation.execute(getJOSOperationRequest), false);

            for (JobOfferStatus jos : jobOfferStatuses){
                int currentNumberOfJobOffers = statistics.getOrDefault(jos.getStatusId(), 0);

                statistics.put(jos.getStatusId(), ++currentNumberOfJobOffers);
            }
        }

        // todo: the current problem is how to automatically guess what order the status should be displayed
        //  without the user setting up the order. Maybe the idea is to create an algorithm to calculate the order, somehow?

        return Response.ok().entity("{}").build();
    }

}
