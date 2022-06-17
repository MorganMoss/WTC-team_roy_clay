package za.co.wethinkcode.server.command;

import za.co.wethinkcode.server.world.Position;
import za.co.wethinkcode.server.world.entity.movable.robot.Robot;

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
