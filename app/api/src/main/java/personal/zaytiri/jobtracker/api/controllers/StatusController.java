package personal.zaytiri.jobtracker.api.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import personal.zaytiri.jobtracker.api.database.operations.CreateOperation;
import personal.zaytiri.jobtracker.api.database.operations.DeleteOperation;
import personal.zaytiri.jobtracker.api.database.operations.GetOperation;
import personal.zaytiri.jobtracker.api.database.operations.UpdateOperation;
import personal.zaytiri.jobtracker.api.database.requests.GetOperationRequest;
import personal.zaytiri.jobtracker.api.domain.entities.Status;
import personal.zaytiri.jobtracker.api.libraries.Jackson;
import personal.zaytiri.jobtracker.api.mappers.StatusMapperImpl;
import personal.zaytiri.jobtracker.persistence.models.StatusModel;
import personal.zaytiri.jobtracker.persistence.repositories.base.Repository;
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

        CreateOperation<StatusModel> createOperation = new CreateOperation<>();
        createOperation.setRepository(new Repository<>());

        boolean isCreated = createOperation.execute(new StatusMapperImpl().entityToModel(newStatus));
        boolean isCreated = createOperation.execute(new StatusMapperImpl().entityToModel(newStatus)).isCreated();

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

        UpdateOperation<StatusModel> updateOperation = new UpdateOperation<>();
        updateOperation.setRepository(new Repository<>());

        boolean isUpdated = updateOperation.execute(new StatusMapperImpl().entityToModel(updatedStatus));

        JSONObject obj = new JSONObject();
        obj.append("success", isUpdated);

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
        List<Status> filteredStatuses = retrieveStatuses(filtersJson);

        String jsonToReturn = new Jackson().fromListToJson(filteredStatuses);

        return Response
                .ok()
                .entity(jsonToReturn)
                .build();
    }

    public List<Status> retrieveStatuses(String filtersJson) {
        Map<String, Pair<String, Object>> filters = new HashMap<>();
        JSONObject filtersToConvert = new JSONObject(filtersJson);

        for (String key : filtersToConvert.keySet()) {
            JSONObject pair = (JSONObject) filtersToConvert.get(key);

            for (String keyPair : pair.keySet()) {
                filters.put(key, new Pair<>(keyPair, pair.get(keyPair)));
            }
        }

        GetOperation<StatusModel> getOperation = new GetOperation<>();
        getOperation.setRepository(new Repository<>());

        GetOperationRequest<StatusModel> getOperationRequest = new GetOperationRequest<>();
        getOperationRequest.setModel(new StatusModel());
        getOperationRequest.setFilters(filters);
        getOperationRequest.setOrderByColumn(null);

        List<Map<String, String>> results = getOperation.execute(getOperationRequest);
        return new StatusMapperImpl().toEntity(results, false);
    }

    @DELETE
    @Path("/remove/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response remove(@PathParam("id") int id) {
        Status statusToRemove = new Status();
        statusToRemove.setId(id);

        DeleteOperation<StatusModel> deleteOperation = new DeleteOperation<>();
        deleteOperation.setRepository(new Repository<>());

        boolean isDeleted = deleteOperation.execute(new StatusMapperImpl().entityToModel(statusToRemove));


        JSONObject obj = new JSONObject();
        obj.put("success", isDeleted);

        return Response
                .ok()
                .entity(obj.toString())
                .build();
    }
}
