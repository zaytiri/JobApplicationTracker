package personal.zaytiri.jobtracker.api.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;
import personal.zaytiri.jobtracker.api.domain.entities.Status;
import personal.zaytiri.jobtracker.api.domain.entities.StorageOperations;
import personal.zaytiri.jobtracker.api.libraries.Jackson;
import personal.zaytiri.jobtracker.api.libraries.WebScraper;
import personal.zaytiri.jobtracker.api.mappers.JobOfferMapperImpl;
import personal.zaytiri.jobtracker.api.mappers.StatusMapperImpl;
import personal.zaytiri.jobtracker.persistence.models.JobOfferModel;
import personal.zaytiri.jobtracker.persistence.models.StatusModel;
import personal.zaytiri.jobtracker.persistence.repositories.JobOfferRepository;
import personal.zaytiri.jobtracker.persistence.repositories.StatusRepository;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        StorageOperations<JobOfferModel> operations = new StorageOperations<>();
        operations.setRepository(new JobOfferRepository());

        JobOfferModel model = new JobOfferMapperImpl().entityToModel(newJobOffer);
        boolean isCreated = operations.create(model);

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

        StorageOperations<JobOfferModel> operations = new StorageOperations<>();
        operations.setRepository(new JobOfferRepository());

        JobOfferModel model = new JobOfferMapperImpl().entityToModel(newJobOffer);
        boolean isCreated = operations.update(model);

        JSONObject obj = new JSONObject();
        obj.put("success", isCreated);

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
        Map<String, Pair<String, Object>> filters = new HashMap<>();
        JSONObject filtersToConvert = new JSONObject(filtersJson);

        for (String key : filtersToConvert.keySet()) {
            JSONObject pair = (JSONObject) filtersToConvert.get(key);

            for (String keyPair : pair.keySet()) {
                filters.put(key, new Pair<>(keyPair, pair.get(keyPair)));
            }
        }
        StorageOperations<JobOfferModel> operations = new StorageOperations<>();
        operations.setRepository(new JobOfferRepository());

        JobOfferModel model = new JobOfferModel();
        List<JobOffer> results = new JobOfferMapperImpl().toEntity(operations.get(model, filters, null), false);

        String jsonToReturn = new Jackson().fromListToJson(results);

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

        StorageOperations<JobOfferModel> operations = new StorageOperations<>();
        operations.setRepository(new JobOfferRepository());

        JobOfferModel model = new JobOfferMapperImpl().entityToModel(jobOfferToRemove);
        boolean isDeleted = operations.delete(model);

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
        WebScraper scrape = new WebScraper();

        JSONObject obj = new JSONObject();

        JobOffer scrapedJobOffer = null;
        try {
            scrapedJobOffer = scrape.process(url);
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
        StorageOperations<JobOfferModel> jobOfferOperations = new StorageOperations<>();
        jobOfferOperations.setRepository(new JobOfferRepository());

        JobOfferModel jobOfferModel = new JobOfferModel();
        List<JobOffer> jobOffers = new JobOfferMapperImpl().toEntity(jobOfferOperations.get(jobOfferModel, new HashMap<>(), null), false);

        StorageOperations<StatusModel> statusOperations = new StorageOperations<>();
        statusOperations.setRepository(new StatusRepository());

        StatusModel statusModel = new StatusModel();
        List<Status> statuses = new StatusMapperImpl().toEntity(statusOperations.get(statusModel, new HashMap<>(), null), false);

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
}
