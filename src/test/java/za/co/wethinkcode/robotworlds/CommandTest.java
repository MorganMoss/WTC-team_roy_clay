package za.co.wethinkcode.robotworlds;

import org.junit.jupiter.api.Test;
import za.co.wethinkcode.robotworlds.maze.EmptyMaze;
import za.co.wethinkcode.robotworlds.world.IWorld;
import za.co.wethinkcode.robotworlds.world.TextWorld;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

class CommandTest {

    @Test
    void getShutdownName() {
        Command test = new ShutdownCommand();
        assertEquals("off", test.getName());
    }

    @Test
    void executeShutdown() {
        Robot robot = new Robot("CrashTestDummy");
        Command shutdown = Command.create("shutdown");
        assertFalse(shutdown.execute(robot));
        assertEquals("Shutting down...", robot.getStatus());
    }

    @Test
    void executeFire() {
        IWorld maze = new TextWorld(new EmptyMaze());
        Robot robot = new Robot("CrashTestDummy", maze);
        Command Fire = Command.create("fire");
        assertTrue(Fire.execute(robot));
        Position expectedPosition = new Position(Robot.CENTRE.getX(), Robot.CENTRE.getY());
        assertEquals(expectedPosition, robot.getWorld().getPosition());
        assertEquals("bullet missed target by 4 steps.", robot.getStatus());
    }
    @Test
    void executeReload_1() {
        IWorld maze = new TextWorld(new EmptyMaze());
        Robot robot = new Robot("CrashTestDummy", maze);
        Command reload = Command.create("reload");
        Command shoot = Command.create("fire");
        shoot.execute(robot);
        assertTrue(reload.execute(robot));
        Position expectedPosition = new Position(Robot.CENTRE.getX(), Robot.CENTRE.getY());
        assertEquals(expectedPosition, robot.getWorld().getPosition());
        assertEquals("Shots reloaded. now you have 5 shots.", robot.getStatus());
    }

    @Test
    void executeReload() {
        IWorld maze = new TextWorld(new EmptyMaze());
        Robot robot = new Robot("CrashTestDummy", maze);
        Command reload = Command.create("reload");
        assertTrue(reload.execute(robot));
        Position expectedPosition = new Position(Robot.CENTRE.getX(), Robot.CENTRE.getY());
        assertEquals(expectedPosition, robot.getWorld().getPosition());
        assertEquals("you cannot reload, you have 5 shots.", robot.getStatus());
    }


    @Test
    void getForwardName() {
        ForwardCommand test = new ForwardCommand("100");
        assertEquals("forward", test.getName());
        assertEquals("100", test.getArgument());
    }

    @Test
    void executeForward() {
        IWorld maze = new TextWorld(new EmptyMaze());
        Robot robot = new Robot("CrashTestDummy", maze);
        Command forward100 = Command.create("forward 10");
        assertTrue(forward100.execute(robot));
        Position expectedPosition = new Position(Robot.CENTRE.getX(), Robot.CENTRE.getY() + 10);
        assertEquals(expectedPosition, robot.getWorld().getPosition());
        assertEquals("Moved forward by 10 steps.", robot.getStatus());
    }

    @Test
    void getHelpName() {
        Command test = new HelpCommand();                                                               //<1>
        assertEquals("help", test.getName());
    }

    @Test
    void executeHelp() {
        Robot robot = new Robot("CrashTestDummy");
        Command help = Command.create("help");
        assertTrue(help.execute(robot));
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
    void createCommand() {
        Command forward = Command.create("forward 10");                                                 //<1>
        assertEquals("forward", forward.getName());
        assertEquals("10", forward.getArgument());

        Command shutdown = Command.create("shutdown");                                                  //<2>
        assertEquals("off", shutdown.getName());

        Command help = Command.create("help");                                                          //<3>
        assertEquals("help", help.getName());

        Command back = Command.create("back 10");                                                 //<4>
        assertEquals("backward", back.getName());
        assertEquals("10", back.getArgument());

        Command right = Command.create("right");                                                 //<5>
        assertEquals("right", right.getName());

        Command left = Command.create("left");                                                 //<6>
        assertEquals("left", left.getName());

        Command shot = Command.create("fire");                                                 //<6>
        assertEquals("shoot", shot.getName());

        Command reload = Command.create("reload");                                                 //<6>
        assertEquals("reload", reload.getName());
    }

    @Test
    void createInvalidCommand() {
        try {
            Command forward = Command.create("say hello");                                              //<4>
            fail("Should have thrown an exception");                                                    //<5>
        } catch (IllegalArgumentException e) {
            assertEquals("Unsupported command: say hello", e.getMessage());                             //<6>
        }
    }
}
