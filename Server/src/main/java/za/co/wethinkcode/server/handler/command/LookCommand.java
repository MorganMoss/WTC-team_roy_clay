package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.server.handler.world.Position;
import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;

//TODO: Implement correct functionality.
public class LookCommand extends Command {



    public boolean execute(Robot target) {

        Position robotPosition = target.getWorld().getPosition();
        return true;
    }
    public LookCommand() {

        super("look");
    }
}
