package za.co.wethinkcode.server.handler;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import za.co.wethinkcode.Request;
import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.TestHelper;
import za.co.wethinkcode.server.handler.world.World;

import java.util.ArrayList;

public class HandlerTest {

    @BeforeAll
    static void startHandler(){
        TestHelper.modifyWorld(new String[]{});
        Handler.setup();
    }

    @BeforeEach
    void resetWorld(){
        World.reset();
    }

    @Test
    void testExecuteValidRequest(){
        Response r = Handler.executeRequest(new Request("HAL", "LAUNCH", new ArrayList<>(){{
            add("sniper");
            add("1");
            add("1");
        }}));

        assertNotNull(r);
        assertEquals("OK", r.getResult(), r.serialize());
    }

    @Test
    void testExecuteRequestInvalidCommand() {
        Handler.executeRequest(new Request("HAL", "LAUNCH", new ArrayList<>(){{
            add("sniper");
            add("1");
            add("1");
        }}));
        Response r = Handler.executeRequest(new Request("HAL", "LUNCH", new ArrayList<>()));
        assertEquals("Unsupported command", r.getData().get("message"));
    }

    @Test
    void testExecuteRequestInvalidRobot(){
        Response r = Handler.executeRequest(new Request("PAL", "STATE", new ArrayList<>()));
        assertEquals("Robot does not exist", r.getData().get("message"));
    }

    @Test
    void testExecuteRequestInvalidArguments(){
        Handler.executeRequest(new Request("HAL", "LAUNCH", new ArrayList<>(){{
            add("sniper");
            add("1");
            add("1");
        }}));
        Response r = Handler.executeRequest(new Request("HAL", "STATE", new ArrayList<>(){{add("bad");}}));
        assertEquals("Could not parse arguments", r.getData().get("message"));
    }

    @Test
    void testExecuteRequestInvalidRobotWithCommandThatDoesNotCare(){
        Response r = Handler.executeRequest(new Request("PAL", "WORLD", new ArrayList<>()));
        assertEquals("OK", r.getResult());
    }

    @Test
    void requestResponseLoopSuccessful(){
        Handler.addRequest("Client", new Request("PAL", "WORLD", new ArrayList<>()));

        Response r;

        do {
            r = Handler.getResponse("Client", "PAL");
        } while (r == null);

        assertEquals("OK", r.getResult());
    }
}
