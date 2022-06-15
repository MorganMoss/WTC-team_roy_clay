package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;

public class RightCommand extends Command {

    @Override
    public boolean execute(Robot target) {
        target.getWorld().updateDirection(true);
        target.setStatus("Turned right.");

        return true;
    }

    public RightCommand() {
        super("right");
    }
}
