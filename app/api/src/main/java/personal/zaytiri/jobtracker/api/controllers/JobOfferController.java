package personal.zaytiri.jobtracker.api.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import personal.zaytiri.jobtracker.api.database.operations.*;
import personal.zaytiri.jobtracker.api.database.requests.FindByTextOperationRequest;
import personal.zaytiri.jobtracker.api.database.requests.GetOperationRequest;
import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;
import personal.zaytiri.jobtracker.api.domain.entities.Status;
import personal.zaytiri.jobtracker.api.libraries.Jackson;
import personal.zaytiri.jobtracker.api.libraries.webscraper.WebScraper;
import personal.zaytiri.jobtracker.api.libraries.webscraper.WebScraperFactory;
import personal.zaytiri.jobtracker.api.mappers.JobOfferMapperImpl;
import personal.zaytiri.jobtracker.persistence.DatabaseShema;
import personal.zaytiri.jobtracker.persistence.models.JobOfferModel;
import personal.zaytiri.jobtracker.persistence.repositories.base.Repository;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;

import java.io.IOException;
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

        boolean isCreated = createOperation.execute(new JobOfferMapperImpl().entityToModel(newJobOffer));

        JSONObject obj = new JSONObject();
        obj.put("success", isCreated);

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
        JobOffer newJobOffer = new JobOffer();
        newJobOffer.setId(id);

        Jackson convert = new Jackson();
        convert.fromJsonToObject(jobOffer, newJobOffer);

        UpdateOperation<JobOfferModel> updateOperation = new UpdateOperation<>();
        updateOperation.setRepository(new Repository<>());

        boolean isUpdated = updateOperation.execute(new JobOfferMapperImpl().entityToModel(newJobOffer));

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

    

    @DELETE
    @Path("/remove/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") int id) {

        JobOffer jobOfferToRemove = new JobOffer();
        jobOfferToRemove.setId(id);

        DeleteOperation<JobOfferModel> deleteOperation = new DeleteOperation<>();
        deleteOperation.setRepository(new Repository<>());

        boolean isDeleted = deleteOperation.execute(new JobOfferMapperImpl().entityToModel(jobOfferToRemove));

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

        JobOffer scrapedJobOffer = null;

        WebScraper scraper = WebScraperFactory.findSuitableScraper(url);
        if(scraper == null){
            JSONObject noSuitableResponse = new JSONObject();
            noSuitableResponse.put("message", "no suitable scraper found.");
            return Response.ok().entity(noSuitableResponse.toString()).build();
        }

        try {
            scrapedJobOffer = scraper.process(url);
        } catch (IOException e) {
            obj.put("success", false);
            obj.put("message", e);
            return Response.ok().entity(obj).build();
        }

        return Response.ok().entity(new Jackson().fromObjectToJson(scrapedJobOffer)).build();
    }

    @GET
    @Path("/job-offer-by-status")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getJobOffersByStatus(){
        List<JobOffer> jobOffers = retrieveJobOffers("{}");
        List<Status> statuses = new StatusController().retrieveStatuses("{}");

        JSONArray array = new JSONArray();
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

        for (var elem : mapped.values()){
            array.put(elem);
        }

        return Response.ok().entity(array.toString()).build();
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

}
