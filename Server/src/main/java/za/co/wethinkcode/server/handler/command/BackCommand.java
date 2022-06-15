package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.server.handler.world.World;
import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;

//Has the same code as Forward Command.
//TODO: make the world or server have a function that does this.
public class BackCommand extends Command {

    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());
        if (target.getWorld().updatePosition(-nrSteps).equals(World.UpdateResponse.SUCCESS)){
            target.setStatus("Moved back by "+nrSteps+" steps.");
        }else if (target.getWorld().updatePosition(-nrSteps).equals(World.UpdateResponse.FAILED_OUTSIDE_WORLD)) {
            target.setStatus("Sorry, I cannot go outside my safe zone.");
        }else if (target.getWorld().updatePosition(-nrSteps).equals(World.UpdateResponse.FAILED_OBSTRUCTED)) {
            target.setStatus("Sorry, there is an obstacle in the way.");}
        return true;
    }

    public BackCommand(String argument) {
        super("backward", argument);
    }
}
