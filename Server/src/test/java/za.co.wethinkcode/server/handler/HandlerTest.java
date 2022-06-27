package za.co.wethinkcode.server.handler;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import za.co.wethinkcode.Request;
import za.co.wethinkcode.server.handler.Handler;

import java.util.ArrayList;
import java.util.List;

public class HandlerTest {

    //TODO:
    // Test ExecuteRequest():
    // - Check all scenarios:
    // -> INTERNAL_ERROR
    // -> ROBOT_NOT_FOUND
    // -> INVALID_ARGUMENTS
    // -> INVALID_COMMAND
    // -> OK
    // Test Run():
    // -> add a request
    // -> try get a response back

    @Test
    void testExecuteRequestUnlaunchedRobot(){
        System.out.println(new Request("HAL", "LAUNCH", new ArrayList<>()).serialize());
        assertNotNull(Handler.executeRequest(null));
    }

    @Test
    void testExecuteRequestInvalidCommand(){
        assertNotNull(Handler.executeRequest(null));
    }

    @Test
    void testExecuteRequestLaunchedRobot(){
        assertNotNull(Handler.executeRequest(null));
    }

    @Test
    void testExecuteRequestValidCommand(){
        assertNotNull(Handler.executeRequest(null));
    }
}