package za.co.wethinkcode.server.command;

import za.co.wethinkcode.server.world.entity.movable.robot.Robot;

public class LeftCommand extends Command{

    @Override
    public boolean execute(Robot target) {
        target.getWorld().updateDirection(false);
        target.setStatus("Turned left.");
        return true;
    }

    public LeftCommand() {
        super("left");
    }
}
