package za.co.wethinkcode.server.configuration;

import za.co.wethinkcode.server.Server;

import java.sql.*;
import java.util.Objects;
import java.util.Scanner;

import static za.co.wethinkcode.server.configuration.Configuration.save_location;

/**
 * DbTest is a small command-line tool used to check that we can connect to a SQLite database.
 *
 * By default, (without any command-line arguments) it attempts to create a SQLite table in an in-memory database.
 * If it succeeds, we assume that all the working parts we need to use SQLite databases are in place and working.
 *
 * The only command-line argument this app understands is
 *  `-f <filename>`
 *  which tells that application to create the test table in a real (disk-resident) database named by the given
 *  filename. Note that the application _does not delete_ the named file, but leaves it in the filesystem
 *  for later examination if desired.
 */
public class DatabaseManager {
    private static final String DISK_DB_URL = "jdbc:sqlite:";
    private static final String table = "saved_worlds";

    private static Connection connection;

    /**
     * Tries to open a database at the given URL.
     * If there is no save_worlds table present, this will create it.
     */
    private static void openDatabase(){
        try {
            connection = DriverManager.getConnection(DISK_DB_URL + save_location()); // locates where database is/saves it
            Server.println( "Connected to database" );
        } catch( SQLException e ){
            System.err.println( e.getMessage() );
            return;
        }

        createTable();
    }

    private static void closeDatabase(){
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println( e.getMessage() );
        }
    }


    /**
     * Takes the current data in the server and stores it in a database
     * @param save name of the instance
     */
    public static void save(String save){
        if (Objects.equals(save, "")){
            Server.println("Failed! Please enter save name");
        }

        String configuration_json = Configuration.serialize();

        openDatabase();

        Server.println("Saving this current server under the name: " + save);

        try( final Statement stmt = connection.createStatement() ){
            if (!
                stmt.executeQuery(
                "SELECT * FROM " + table + " WHERE save_name = \""+save+"\""
            ).isClosed()){
                Server.println( "Found existing save of that name!");

                Server.println("Overwrite? (Y/N) : ");

                if (!Server.getInput().equalsIgnoreCase("Y")){
                    Server.println("Aborting save . . .");
                    return;
                }

                stmt.executeUpdate(
                "UPDATE "+ table + " " +
                "SET configuration_json = '"+configuration_json+"'" +
                "WHERE save_name = " + save);
            } else {
                stmt.executeUpdate(
                        "INSERT INTO "+ table +" " +
                                "(" +
                                "save_name," +
                                "configuration_json" +
                                ") " +

                                "VALUES " +
                                "(" +
                                "\"" + save + "\", " +
                                "'" + configuration_json + "'" +
                                ")"
                );
            }



            Server.println( "Saving complete!" );
        }catch( SQLException e ){
            System.err.println( e.getMessage() );

        }

        closeDatabase();
    }

    public static void load(String save) {
        openDatabase();

        try( final Statement stmt = connection.createStatement() ){
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM " + table + " WHERE save_name = \""+save+"\"");

            if (resultSet.isClosed()){
                Server.println( "No save found. Aborting . . . ");
                return;
            }

            Server.println("Loading " + resultSet.getString("save_name") + " . . .");

            Configuration.loadConfiguration(resultSet.getString("configuration_json"));

            Server.println( "Loading complete!" );
        }catch( SQLException e ){
            System.err.println( e.getMessage() );
        }


        closeDatabase();
    }


    /**
     * Creates the saved_worlds in the database,
     * if it is not already present
     */
    private static void createTable() {
        try( final Statement stmt = connection.createStatement() ){
            if (!
                stmt.executeQuery(
                "SELECT name FROM sqlite_master " +
                    "WHERE type='table' AND name='"+ table +"';"
            ).isClosed()){
                Server.println( "Found existing table!");
                return;
            }

            Server.println( "Table not found, creating table . . .");
            stmt.executeUpdate(
            "CREATE TABLE IF NOT EXISTS " +
                    table +" " +
                    "(" +
                        "save_name          STRING NOT NULL, " +
                        "configuration_json STRING NOT NULL, " +
                        "PRIMARY KEY (save_name)" +
                    ")"
            );
            Server.println( "Success creating table!");

        }catch( SQLException e ){
            System.err.println( e.getMessage() );
        }
    }

    private DatabaseManager() {}
}
