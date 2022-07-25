package za.co.wethinkcode.acceptancetests;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.acceptancetests.protocoldrivers.MockServer;

public class DatabaseTests {
    @Test
    void saveTheWorld(){
        MockServer.startServer("-s=2 -o=1,1,-1,-1", "save Test_World\nN\n");
        System.out.println(MockServer.result());
    }

    @Test
    void loadTheWorld(){
        MockServer.startServer("-s=2 -o=1,1,-1,-1", "save Test_World\nY\n");
        System.out.println(MockServer.result());
        MockServer.startServer("", "load Test_World\n");
        System.out.println(MockServer.result());
    }

    @Test
    void manyWorldsInterpretation(){
        MockServer.startServer("-s=2 -pt=1,1,-1,-1", "save 1\nY\n");
        System.out.println(MockServer.result());

        MockServer.startServer("-s=20 -o=1,1,-1,-1", "save 2\nY\n");
        System.out.println(MockServer.result());

        MockServer.startServer("", "load 2\nload 1\n");
        System.out.println(MockServer.result());

    }
}
