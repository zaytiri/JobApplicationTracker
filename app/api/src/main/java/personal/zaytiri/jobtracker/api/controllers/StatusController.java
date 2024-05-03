package personal.zaytiri.jobtracker.api.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import personal.zaytiri.jobtracker.api.database.operations.*;
import personal.zaytiri.jobtracker.api.database.requests.GetOperationRequest;
import personal.zaytiri.jobtracker.api.domain.entities.JobOfferStatus;
import personal.zaytiri.jobtracker.api.domain.entities.Status;
import personal.zaytiri.jobtracker.api.libraries.Jackson;
import personal.zaytiri.jobtracker.api.mappers.JobOfferMapperImpl;
import personal.zaytiri.jobtracker.api.mappers.JobOfferStatusMapperImpl;
import personal.zaytiri.jobtracker.api.mappers.StatusMapperImpl;
import personal.zaytiri.jobtracker.persistence.DatabaseShema;
import personal.zaytiri.jobtracker.persistence.models.JobOfferStatusModel;
import personal.zaytiri.jobtracker.persistence.models.StatusModel;
import personal.zaytiri.jobtracker.persistence.repositories.base.Repository;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.enums.Operators;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    @GET
    @Path("/status-by-job-offer/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getStatusByJobId(@PathParam("id") int id) {

        GetStatusesByJobOfferIdOperation getOperation = new GetStatusesByJobOfferIdOperation();
        getOperation.setRepository(new Repository<>());

        List<JobOfferStatus> results = new JobOfferStatusMapperImpl().toEntity(getOperation.execute(id), false);

        String jsonToReturn = new Jackson().fromListToJson(results);

        return Response
                .ok()
                .entity(jsonToReturn)
                .build();
    }

    @DELETE
    @Path("/remove-from-job-offer/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeStatusFromJobOffer(@PathParam("id") int statusId) {
        JobOfferStatus statusToRemove = new JobOfferStatus();
        statusToRemove.setId(statusId);

        DeleteOperation<JobOfferStatusModel> deleteOperation = new DeleteOperation<>();
        deleteOperation.setRepository(new Repository<>());

        boolean isDeleted = deleteOperation.execute(new JobOfferStatusMapperImpl().entityToModel(statusToRemove));

        JSONObject jsonToReturn = new JSONObject();
        jsonToReturn.put("success", isDeleted);

        return Response
                .ok()
                .entity(jsonToReturn.toString())
                .build();
    }

    @PATCH
    @Path("/update-from-job-offer/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateStatusFromJobOffer(@PathParam("id") int id, String statusChangedAt) {
        Map<String, Pair<String, Object>> filters = new HashMap<>();
        filters.put(DatabaseShema.getINSTANCE().idColumnName, new Pair<>(Operators.EQUALS.value, id));

        GetOperation<JobOfferStatusModel> getOperation = new GetOperation<>();
        getOperation.setRepository(new Repository<>());

        GetOperationRequest<JobOfferStatusModel> getRequest = new GetOperationRequest<>();
        getRequest.setModel(new JobOfferStatusModel());
        getRequest.setFilters(filters);

        JobOfferStatus result = new JobOfferStatusMapperImpl().toEntity(getOperation.execute(getRequest), false).get(0);
        System.out.println(new JSONObject(statusChangedAt).getString("date"));
        result.setChangedAt(LocalDateTime.parse(new JSONObject(statusChangedAt).getString("date")));

        UpdateOperation<JobOfferStatusModel> updateOperation = new UpdateOperation<>();
        updateOperation.setRepository(new Repository<>());

        boolean isUpdated = updateOperation.execute(new JobOfferStatusMapperImpl().entityToModel(result));

        JSONObject obj = new JSONObject();
        obj.append("success", isUpdated);

        return Response
                .ok()
                .entity(obj.toString())
                .build();
    }
}
