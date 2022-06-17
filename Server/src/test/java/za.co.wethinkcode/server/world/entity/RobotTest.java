package za.co.wethinkcode.server.world.entity;
import org.junit.jupiter.api.Test;
//import static org.junit.Assert.*;


import za.co.wethinkcode.robotworlds.maze.EmptyMaze;
import za.co.wethinkcode.robotworlds.world.IWorld;
import za.co.wethinkcode.robotworlds.world.TextWorld;
import za.co.wethinkcode.server.handler.Position;
import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;
import za.co.wethinkcode.server.command.*;

import static org.junit.jupiter.api.Assertions.*;

class RobotTest {
    IWorld maze = new TextWorld(new EmptyMaze());

    @Test
    void initialPosition() {
        Robot robot = new Robot("CrashTestDummy",maze);
        assertEquals(robot.getWorld().CENTRE, robot.getWorld().getPosition());
        assertEquals(IWorld.Direction.UP, robot.getWorld().getCurrentDirection());
    }

    @Test
    void dump() {
        Robot robot = new Robot("CrashTestDummy",maze);
        assertEquals("[0,0] CrashTestDummy> Ready", robot.toString());
    }

    @Test
    void shutdown() {
        Robot robot = new Robot("CrashTestDummy");
        ShutdownCommand command = new ShutdownCommand();
        assertFalse(robot.handleCommand(command));
    }

    @Test
    void forward() {
        Robot robot = new Robot("CrashTestDummy",maze);
        ForwardCommand command = new ForwardCommand("10");
        assertTrue(robot.handleCommand(command));
        Position expectedPosition = new Position(robot.getWorld().CENTRE.getX(), robot.getWorld().CENTRE.getY() + 10);
        assertEquals(expectedPosition, robot.getWorld().getPosition());
        assertEquals("Moved forward by 10 steps.", robot.getStatus());
        robot.handleCommand(Command.create("forward 10"));
        assertEquals("forward10",command.getName() + command.getArgument());
    }

    @Test
    void forwardforward() {
        Robot robot = new Robot("CrashTestDummy",maze);
        assertTrue(robot.handleCommand(new ForwardCommand("10")));
        assertTrue(robot.handleCommand(new ForwardCommand("5")));
        assertEquals("Moved forward by 5 steps.", robot.getStatus());
    }

    @Test
    void tooFarForward() {
        Robot robot = new Robot("CrashTestDummy",maze);
        assertTrue(robot.handleCommand(new ForwardCommand("1000")));
        assertEquals(robot.getWorld().CENTRE, robot.getWorld().getPosition());
        assertEquals("Sorry, I cannot go outside my safe zone.", robot.getStatus());
    }

    @Test
    void Back() {
        Robot robot = new Robot("CrashTestDummy",maze);
        assertTrue(robot.handleCommand(new BackCommand("10")));
        assertEquals("Moved back by 10 steps.", robot.getStatus());
    }

    @Test
    void help() {
        Robot robot = new Robot("CrashTestDummy");
        Command command = new HelpCommand();
        assertTrue(robot.handleCommand(command));
        assertEquals("I can understand these commands:\n" +
                "OFF  - Shut down robot\n" +
                "HELP - provide information about commands\n" +
                "FORWARD - move forward by specified number of steps, e.g. 'FORWARD 10'\n" +
                "BACK - move backward by specified number of steps, e.g. 'BACKWARD 10'\n" +
                "LEFT - turns left, e.g. 'TURNED LEFT'\n" +
                "FIRE - Shoot the opponent, e.g. 'Target shot\n" +
                "RIGHT - turns right, e.g. 'TURNED RIGHT'", robot.getStatus());
    }
    @Test
    void Right() {
        Robot robot = new Robot("CrashTestDummy",maze);
        assertTrue(robot.handleCommand(new RightCommand()));
        assertEquals("Turned right.", robot.getStatus());
    }

    @Test
    void Left() {
        Robot robot = new Robot("CrashTestDummy",maze);
        assertTrue(robot.handleCommand(new LeftCommand()));
        assertEquals("Turned left.", robot.getStatus());
    }

    @Test
    void Sprint() {
        Robot robot = new Robot("CrashTestDummy",maze);
        assertTrue(robot.handleCommand(new LaunchCommand("1")));
        assertEquals("Moved forward by 1 steps.", robot.getStatus());
    }
}