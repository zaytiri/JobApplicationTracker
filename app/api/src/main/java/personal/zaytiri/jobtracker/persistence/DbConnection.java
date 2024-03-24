package personal.zaytiri.jobtracker.persistence;


import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.builders.CreateTableQueryBuilder;
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

public class DbConnection {
    private static DbConnection INSTANCE;
    private final Database schema;
    private String path;
    private Connection connection;

    private DbConnection(String fileName) {
        connection = null;
        schema = Schema.getSchema(fileName);
        path = "";
        if (schema == null) {
            System.err.println("Schema not correctly configured.");
            return;
        }

        path = getDbConnectionPath() + schema.getName() + ".db";
        createDatabase();
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
        CreateTableQueryBuilder query = new CreateTableQueryBuilder(open());
        query.setCloseConnection(false);

        for (Table tb : schema.getTables()) {
            query.create(tb);
            query.execute();
        }

        close();
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
        return path.toAbsolutePath() + "\\";
    }
}