package za.co.wethinkcode.server;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.Hashtable;

import static java.lang.Math.round;
import static org.junit.jupiter.api.Assertions.*;

class DatabaseManagerTest {

    @Test
    void save() {
        Configuration.setConfiguration(new String[]{
                "-s=10",
                "-o=1,1", "-pt=2,2", "-m=3,3",
                "-v=3", "-l=2", "-r=2", "-mt=2",
                "-shield=1", "-shots=1",
                "-saves=TestTable"});
        DatabaseManager.save("Test");

        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + Configuration.save_location());
            System.out.println( "Connected to database" );
        } catch( SQLException e ){
            fail(e);
        }

        try( final Statement stmt = connection.createStatement() ){
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM saved_worlds WHERE save_name = \"Test\"");

            if (resultSet.isClosed()){
                fail( "No save found. Aborting . . . ");
            }

            Hashtable<String, Object> save = new Gson().fromJson(resultSet.getString("configuration_json"), Hashtable.class);

            stmt.executeUpdate("DROP TABLE saved_worlds");
            connection.close();

            for (String key: save.keySet()){
                Object value = save.get(key);
                try {
                    Object actual = Configuration.class.getMethod(key.toLowerCase()).invoke(null);
                    if (actual.getClass() == Integer.class){
                        value = (int) round((double) value);
                    }
                    assertEquals(actual.getClass().cast(value), actual);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    fail(e);
                }
            }
        }catch( SQLException e ){
            fail(e);
        }
    }

    @Test
    void load() {
        Configuration.setConfiguration(new String[]{
                "-s=10",
                "-o=1,1", "-pt=2,2", "-m=3,3",
                "-v=3", "-l=2", "-r=2", "-mt=2",
                "-shield=1", "-shots=1",
                "-saves=TestTable"});

        DatabaseManager.save("Test");

        Configuration.setConfiguration(new String[]{"-saves=TestTable"});

        DatabaseManager.load("Test");

        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + Configuration.save_location());
            System.out.println( "Connected to database" );
        } catch( SQLException e ){
            fail(e);
        }

        try( final Statement stmt = connection.createStatement() ){
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM saved_worlds WHERE save_name = \"Test\"");

            if (resultSet.isClosed()){
                fail( "No save found. Aborting . . . ");
            }

            Hashtable<String, Object> save = new Gson().fromJson(resultSet.getString("configuration_json"), Hashtable.class);

            stmt.executeUpdate("DROP TABLE saved_worlds;");
            connection.close();

            for (String key: save.keySet()){
                Object value = save.get(key);
                try {
                    Object actual = Configuration.class.getMethod(key.toLowerCase()).invoke(null);
                    if (actual.getClass() == Integer.class){
                        value = (int) round((double) value);
                    }

                    assertEquals(actual.getClass().cast(value), actual);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    fail(e);
                }
            }
        }catch( SQLException e ){
            fail(e);
        }
    }
}