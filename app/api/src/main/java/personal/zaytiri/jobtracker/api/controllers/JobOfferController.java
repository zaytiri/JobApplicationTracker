package personal.zaytiri.jobtracker.api.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import personal.zaytiri.jobtracker.api.domain.entities.JobOffer;
import personal.zaytiri.jobtracker.api.libraries.Jackson;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;

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
        JobOffer newJobOffer = new JobOffer().getInstance();

        Jackson convert = new Jackson();
        convert.fromJsonToObject(jobOffer, newJobOffer);

        boolean isCreated = newJobOffer.create();

        JSONObject obj = new JSONObject();
        obj.append("success", isCreated);

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
        JobOffer newJobOffer = new JobOffer().getInstance();
        newJobOffer.setId(id);

        Jackson convert = new Jackson();
        convert.fromJsonToObject(jobOffer, newJobOffer);

        boolean isCreated = newJobOffer.update();

        JSONObject obj = new JSONObject();
        obj.append("success", isCreated);

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

        JobOffer jobOffer = new JobOffer().getInstance();
        List<JobOffer> results = jobOffer.get(filters, null);

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

        JobOffer jobOfferToRemove = new JobOffer().getInstance();
        jobOfferToRemove.setId(id);

        boolean idDeleted = jobOfferToRemove.delete();

        JSONObject obj = new JSONObject();
        obj.append("success", idDeleted);

        return Response
                .ok()
                .entity(obj.toString())
                .build();
    }
}
