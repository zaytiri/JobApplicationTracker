package personal.zaytiri.jobtracker.api.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import personal.zaytiri.jobtracker.api.domain.entities.Status;
import personal.zaytiri.jobtracker.api.domain.entities.StorageOperations;
import personal.zaytiri.jobtracker.api.libraries.Jackson;
import personal.zaytiri.jobtracker.api.mappers.StatusMapperImpl;
import personal.zaytiri.jobtracker.persistence.models.StatusModel;
import personal.zaytiri.jobtracker.persistence.repositories.StatusRepository;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("status")
public class StatusController {
    @POST
    @Path("/create")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(String status) {
        Status newStatus = new Status();

        Jackson convert = new Jackson();
        convert.fromJsonToObject(status, newStatus);

        StorageOperations<StatusModel> operations = new StorageOperations<>();
        operations.setRepository(new StatusRepository());

        StatusModel model = new StatusMapperImpl().entityToModel(newStatus);
        boolean isCreated = operations.create(model);

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
    public Response update(@PathParam("id") int id, String status) {
        Status updatedStatus = new Status();
        updatedStatus.setId(id);

        Jackson convert = new Jackson();
        convert.fromJsonToObject(status, updatedStatus);

        StorageOperations<StatusModel> operations = new StorageOperations<>();
        operations.setRepository(new StatusRepository());

        StatusModel model = new StatusMapperImpl().entityToModel(updatedStatus);
        boolean isCreated = operations.update(model);

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
        StorageOperations<StatusModel> operations = new StorageOperations<>();
        operations.setRepository(new StatusRepository());

        StatusModel model = new StatusModel();
        List<Status> results = new StatusMapperImpl().toEntity(operations.get(model, filters, null), false);

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
        Status statusToRemove = new Status();
        statusToRemove.setId(id);

        StorageOperations<StatusModel> operations = new StorageOperations<>();
        operations.setRepository(new StatusRepository());

        StatusModel model = new StatusMapperImpl().entityToModel(statusToRemove);
        boolean isDeleted = operations.delete(model);

        JSONObject obj = new JSONObject();
        obj.put("success", isDeleted);

        return Response
                .ok()
                .entity(obj.toString())
                .build();
    }
}
