package za.co.wethinkcode.acceptancetests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import za.co.wethinkcode.acceptancetests.protocoldrivers.MockServer;

import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseTests {
    @AfterEach
    void removeTestDB(){
        File myObj = new File("Saves");
        if (myObj.delete()) {
            System.out.println("Deleted the file: " + myObj.getName());
        } else {
            System.out.println("Failed to delete the file.");
        }
    }

    private void assertSaveSuccessful(String result, String save){
        assertTrue(
            result.contains("Saving this current server under the name: "+save), result
        );

        assertTrue(
            result.contains("Saving complete!"), result
        );
    }

    @Test
    void saveTheWorld(){
        MockServer.startServer("-s=2 -o=1,1,-1,-1", "save Test_World\n");
        assertSaveSuccessful(MockServer.result(), "Test_World");
    }

    @Test
    void loadTheWorld(){
        MockServer.startServer("-s=2 -o=1,1,-1,-1", "save Test_World\n");
        assertSaveSuccessful(MockServer.result(), "Test_World");

        MockServer.startServer("", "load Test_World\n");
        String result = MockServer.result();

        String[] comparison = result.split("Configuration set:");
        String[] original = comparison[0].split("\n");
        String[] loaded = comparison[1].split("\n");
        for (int i = 0; i < original.length; i++){
            if (!original[i].contains("=")){
                continue;
            }

            if (original[i].contains("size")){
                assertNotEquals(original[i], loaded[i]);
                assertEquals("\tsize = 2", loaded[i]);
                continue;
            }

            if (original[i].contains("obstacle")){
                assertNotEquals(original[i], loaded[i]);
                assertEquals("\tobstacle = 1,1,-1,-1", loaded[i]);
                continue;
            }

            assertEquals(original[i], loaded[i]);

        }

    }

    @Test
    void manyWorldsInterpretation(){
        MockServer.startServer("-s=2 -pt=1,1,-1,-1", "save 1\n");
        assertSaveSuccessful( MockServer.result(), "1");
        MockServer.startServer("-s=20 -o=1,1,-1,-1", "save 2\n");
        assertSaveSuccessful( MockServer.result(), "2");

        MockServer.startServer("", "load 2\nload 1\n");
        String result = MockServer.result();

        String[] comparison = result.split("Configuration set:");
        String[] original = comparison[0].split("\n");
        String[] loaded_1 = comparison[1].split("\n");
        String[] loaded_2 = comparison[2].split("\n");
        for (int i = 0; i < original.length; i++){
            if (!original[i].contains("=")){
                continue;
            }

            if (original[i].contains("size")){
                assertNotEquals(original[i], loaded_1[i]);
                assertNotEquals(original[i], loaded_2[i]);
                assertEquals("\tsize = 20", loaded_1[i]);
                assertEquals("\tsize = 2", loaded_2[i]);
                continue;
            }

            if (original[i].contains("obstacle")){
                assertNotEquals(original[i], loaded_1[i]);
                assertEquals(original[i], loaded_2[i]);
                assertEquals("\tobstacle = 1,1,-1,-1", loaded_1[i]);
                continue;
            }

            if (original[i].contains("pits")){
                assertEquals(original[i], loaded_1[i]);
                assertNotEquals(original[i], loaded_2[i]);
                assertEquals("\tpits = 1,1,-1,-1", loaded_2[i]);
                continue;
            }

            assertEquals(original[i], loaded_1[i]);
            assertEquals(original[i], loaded_2[i]);

        }
    }

    @Test
    void overwriteSave(){
        MockServer.startServer("-s=2 -o=1,1,-1,-1", "save Test_World\nsave Test_World\nY\n");
        String result = MockServer.result();
        String[] parts = result.split("Connected to database");
        assertSaveSuccessful(parts[1], "Test_World");
        System.out.println(parts[2]);
        assertSaveSuccessful(parts[2], "Test_World");
    }
}
