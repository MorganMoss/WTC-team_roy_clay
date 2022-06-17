package za.co.wethinkcode.server.command;

import za.co.wethinkcode.server.world.entity.movable.robot.Robot;

public class ShutdownCommand extends Command {
    public ShutdownCommand() {
        super("off");
    }

    @Override
    public boolean execute(Robot target) {
        target.setStatus("Shutting down...");
        return false;
    }
}
