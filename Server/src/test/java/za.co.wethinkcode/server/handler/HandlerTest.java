package za.co.wethinkcode.server.handler;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import za.co.wethinkcode.server.handler.Handler;

//TODO: Write Tests for the Handler's public method
public class HandlerTest {
    @Test
    void testExecuteRequestUnlaunchedRobot(){
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
