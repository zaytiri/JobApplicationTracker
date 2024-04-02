package personal.zaytiri.jobtracker.api.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;
import personal.zaytiri.jobtracker.api.domain.entities.StorageOperations;
import personal.zaytiri.jobtracker.api.libraries.Jackson;
import personal.zaytiri.jobtracker.api.libraries.WebScraper;
import personal.zaytiri.jobtracker.api.mappers.JobOfferMapperImpl;
import personal.zaytiri.jobtracker.persistence.models.JobOfferModel;
import personal.zaytiri.jobtracker.persistence.repositories.JobOfferRepository;
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
}
