package za.co.wethinkcode.server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import za.co.wethinkcode.server.handler.Handler;

//TODO: Write Tests for the Handler's public method
public class HandlerTest {
    @Test
    void testExecuteRequestLaunchedRobot(){
        assertNotNull(Handler.getHandler().executeRequest(null));
    }

    @Test
    void testExecuteRequestUnlaunchedRobot(){
        assertNotNull(Handler.getHandler().executeRequest(null));
    }

    @Test
    void testExecuteRequestValidCommand(){
        assertNotNull(Handler.getHandler().executeRequest(null));
    }

    @Test
    void testExecuteRequestInvalidCommand(){
        assertNotNull(Handler.getHandler().executeRequest(null));
    }
}
