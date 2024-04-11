package personal.zaytiri.jobtracker.api.controllers;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import personal.zaytiri.jobtracker.api.database.DatabaseMigration;

@Path("db")
public class DatabaseMigrationController {
    @GET
    @Path("/check-migration")
    @Consumes(MediaType.TEXT_PLAIN)
    public void checkMigration() {
        new DatabaseMigration().checkIfNecessary();
    }
}
