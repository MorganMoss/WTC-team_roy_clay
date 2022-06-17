package za.co.wethinkcode.server.command;

import za.co.wethinkcode.server.world.entity.movable.robot.Robot;

//Help should be client and not server-side
//TODO: Make this a client side feature,
// remove it as a command
public class HelpCommand extends Command {

    public HelpCommand() {
        super("help");
    }

    @Override
    public boolean execute(Robot target) {
        target.setStatus("I can understand these commands:\n" +
                "OFF  - Shut down robot\n" +
                "HELP - provide information about commands\n" +
                "FORWARD - move forward by specified number of steps, e.g. 'FORWARD 10'\n" +
                "BACK - move backward by specified number of steps, e.g. 'BACKWARD 10'\n" +
                "LEFT - turns left, e.g. 'TURNED LEFT'\n" +
                "FIRE - Shoot the opponent, e.g. 'Target shot\n" +
                "RIGHT - turns right, e.g. 'TURNED RIGHT'");
        return true;
    }
}
