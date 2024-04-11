package personal.zaytiri.jobtracker.api.database;

import personal.zaytiri.jobtracker.persistence.DbConnection;
import personal.zaytiri.makeitexplicitlyqueryable.pairs.Pair;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.builders.InsertQueryBuilder;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.query.builders.SelectQueryBuilder;
import personal.zaytiri.makeitexplicitlyqueryable.sqlquerybuilder.querybuilder.schema.Table;

import java.io.File;
import java.util.*;

public class DatabaseMigration {

    public void checkIfNecessary() {
        DbConnection oldConnection = DbConnection.getInstance();
        int currentDatabaseVersion = oldConnection.getDatabaseVersionFromDb();

        if(currentDatabaseVersion >= oldConnection.getDatabaseVersion()){
            return;
        }

        Map<String, List<Map<String, String>>> dataToMigrate = new HashMap<>();

        SelectQueryBuilder selectQuery = new SelectQueryBuilder(oldConnection.open());
        selectQuery.setCloseConnection(false);
        for (Table table : oldConnection.getSchema().getTables()){
            if(table.getName().equals("version")){
                continue;
            }
            dataToMigrate.put(table.getName(), selectQuery.select().from(table).execute().getResult());
        }
        oldConnection.close();

        renameDatabaseFile(oldConnection);

        oldConnection.resetInstance();
        DbConnection newConnection = DbConnection.getInstance();

        var didInsertAll = false;
        while(!didInsertAll){

            newConnection.deleteData();
            newConnection.resetInstance();
            newConnection = DbConnection.getInstance();

            didInsertAll = migrateValuesIntoNewDatabase(newConnection, dataToMigrate);
        }
    }

    private boolean migrateValuesIntoNewDatabase(DbConnection newConnection, Map<String, List<Map<String, String>>> dataToMigrate){
        InsertQueryBuilder insertQuery = new InsertQueryBuilder(newConnection.open());
        insertQuery.setCloseConnection(false);
        for (Table table : newConnection.getSchema().getTables()) {
            var tableData = dataToMigrate.get(table.getName());
            if(tableData == null){
                continue;
            }
            for (var elem : tableData) {

                List<Pair<String, Object>> newValues = new ArrayList<>();
                for (var newColumn : table.getColumns()){
                    var existentColumn = elem.get(newColumn.getName());
                    newValues.add(new Pair<>(newColumn.getName(), existentColumn));
                }

                boolean isSuccess = insertQuery.insertInto(table).values(newValues).execute().isSuccess();

                if(!isSuccess){
                    newConnection.close();
                    return false;
                }
            }
        }
        newConnection.close();
        return true;
    }

    private void renameDatabaseFile(DbConnection oldConnection){
        File folder = new File(oldConnection.getParentPath());
        int numberOfBackupDatabaseFiles = Objects.requireNonNull(folder.listFiles()).length + 1;

        File oldFile = new File(oldConnection.getPath());
        File newFile = new File(oldConnection.getParentPath() + "\\" + oldConnection.getSchema().getName() + "_old-" + numberOfBackupDatabaseFiles + ".db");

        boolean renamed = oldFile.renameTo(newFile);

        // Check if the file was successfully renamed
        if (renamed) {
            System.out.println("File renamed successfully.");
        } else {
            System.out.println("Failed to rename the file.");
        }
    }
}