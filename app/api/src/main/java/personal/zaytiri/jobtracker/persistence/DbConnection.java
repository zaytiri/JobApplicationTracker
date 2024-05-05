package personal.zaytiri.jobtracker.persistence;


import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.builders.CreateTableQueryBuilder;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.builders.InsertQueryBuilder;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.builders.SelectQueryBuilder;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.schema.Database;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.schema.Schema;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.schema.Table;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class DbConnection {
    private static DbConnection INSTANCE;
    private final Database schema;
    private String path;
    private String parentPath;
    private Connection connection;
    private int databaseVersion = 1;

    private DbConnection(String fileName) {
        connection = null;
        schema = Schema.getSchema(fileName);
        path = "";
        parentPath = getDbConnectionPath();
        if (schema == null) {
            System.err.println("Schema not correctly configured.");
            return;
        }

        path = parentPath + schema.getName() + ".db";

        createDatabase();
    }

    public String getPath() {
        return path;
    }

    public void close() {
        try {
            if (connection != null)
                connection.close();
        } catch (SQLException e) {
            // connection close failed.
            System.err.println(e.getMessage());
        }
    }

    public void createDatabase() {
        if(getDatabaseVersionFromDb() != -1){
            return;
        }

        CreateTableQueryBuilder query = new CreateTableQueryBuilder(open());
        query.setCloseConnection(false);

        for (Table tb : schema.getTables()) {
            query.create(tb);
            query.execute();
        }

        close();

        populateDefaultSettingsToDb();

        populateDefaultVersionToDbIfEmpty();
    }

    private void populateDefaultSettingsToDb(){
        InsertQueryBuilder insertQueryToSettings = new InsertQueryBuilder(open());
        Table settings = schema.getTable(DatabaseShema.getINSTANCE().settingsTableName);
        insertQueryToSettings.insertInto(settings).values(1, 0, 0, LocalDateTime.now(), LocalDateTime.now()).execute();
    }

    private void populateDefaultVersionToDbIfEmpty(){
        InsertQueryBuilder insertQuery = new InsertQueryBuilder(open());
        Table version = schema.getTable(DatabaseShema.getINSTANCE().versionTableName);
        insertQuery.insertInto(version).values(1, databaseVersion).execute();
    }
    public int getDatabaseVersion(){
        return databaseVersion;
    }

    public int getDatabaseVersionFromDb(){
        SelectQueryBuilder query = new SelectQueryBuilder(open());
        Table version = schema.getTable(DatabaseShema.getINSTANCE().versionTableName);
        var results = query.select().from(version).execute().getResult();
        if(results.isEmpty()){
            return -1;
        }
        return Integer.parseInt(results.get(0).get("version"));
    }

    public boolean deleteData() {
        File file = Path.of(path).toFile();
        boolean result = false;
        try {
            result = Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public Connection getConnection() {
        return connection;
    }

    public static DbConnection getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DbConnection("database");
        }
        return INSTANCE;
    }

    public Database getSchema() {
        return schema;
    }

    public Connection open() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + path);
        } catch (SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }

        return connection;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void resetInstance() {
        INSTANCE = null;
    }

    private String getDbConnectionPath() {
        String homeDir = System.getProperty("user.home");
        Path path = Paths.get(homeDir + "/jobtracker/data");
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        parentPath = path.toAbsolutePath() + "\\";
        return parentPath;
    }
}