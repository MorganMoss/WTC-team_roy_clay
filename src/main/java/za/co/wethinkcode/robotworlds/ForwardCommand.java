package za.co.wethinkcode.robotworlds;

import za.co.wethinkcode.robotworlds.world.IWorld;

//TODO: Look at back command
public class ForwardCommand extends Command {

    @Override
    public boolean execute(Robot target) {
        int nrSteps = Integer.parseInt(getArgument());
        if (target.getWorld().updatePosition(nrSteps).equals(IWorld.UpdateResponse.SUCCESS)){
            target.setStatus("Moved forward by "+nrSteps+" steps.");
        }else if (target.getWorld().updatePosition(nrSteps).equals(IWorld.UpdateResponse.FAILED_OUTSIDE_WORLD)) {
            target.setStatus("Sorry, I cannot go outside my safe zone.");
        }else if (target.getWorld().updatePosition(nrSteps).equals(IWorld.UpdateResponse.FAILED_OBSTRUCTED)) {
            target.setStatus("Sorry, there is an obstacle in the way.");}
        return true;
    }

    public ForwardCommand(String argument) {
        super("forward", argument);
    }
}

