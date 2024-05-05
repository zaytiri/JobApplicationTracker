package personal.zaytiri.jobtracker.api.controllers;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.json.JSONObject;
import personal.zaytiri.jobtracker.api.database.operations.CreateOperation;
import personal.zaytiri.jobtracker.api.database.operations.GetOperation;
import personal.zaytiri.jobtracker.api.database.operations.UpdateOperation;
import personal.zaytiri.jobtracker.api.database.requests.GetOperationRequest;
import personal.zaytiri.jobtracker.api.domain.entities.Settings;
import personal.zaytiri.jobtracker.api.libraries.Jackson;
import personal.zaytiri.jobtracker.api.mappers.SettingsMapperImpl;
import personal.zaytiri.jobtracker.persistence.models.SettingsModel;
import personal.zaytiri.jobtracker.persistence.repositories.base.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("settings")
public class SettingsController {
    @PUT
    @Path("/update/{id}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, String settings) {
        Settings updatedSettings = new Settings();
        updatedSettings.setId(id);

        Jackson convert = new Jackson();
        convert.fromJsonToObject(settings, updatedSettings);

        UpdateOperation<SettingsModel> updateOperation = new UpdateOperation<>();
        updateOperation.setRepository(new Repository<>());

        boolean isUpdated = updateOperation.execute(new SettingsMapperImpl().entityToModel(updatedSettings));

        JSONObject obj = new JSONObject();
        obj.append("success", isUpdated);

        return Response
                .ok()
                .entity(obj.toString())
                .build();
    }

    @POST
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        GetOperation<SettingsModel> getOperation = new GetOperation<>();
        getOperation.setRepository(new Repository<>());

        GetOperationRequest<SettingsModel> getOperationRequest = new GetOperationRequest<>();
        getOperationRequest.setModel(new SettingsModel());
        getOperationRequest.setFilters(new HashMap<>());
        getOperationRequest.setOrderByColumn(null);

        List<Map<String, String>> results = getOperation.execute(getOperationRequest);
        List<Settings> filteredSettings = new SettingsMapperImpl().toEntity(results, false);

        String jsonToReturn = new Jackson().fromListToJson(filteredSettings);

        return Response
                .ok()
                .entity(jsonToReturn)
                .build();
    }
}
