package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.Response;

import java.util.List;

public abstract class Movement extends Command {

    int steps;

    /**
     * TODO
     * @return
     */
    @Override
    public Response execute() {
        //TODO
//        int nrSteps = Integer.parseInt(getArgument());
//        if (target.getWorld().updatePosition(-nrSteps).equals(World.UpdateResponse.SUCCESS)){
//            target.setStatus("Moved back by "+nrSteps+" steps.");
//        }else if (target.getWorld().updatePosition(-nrSteps).equals(World.UpdateResponse.FAILED_OUTSIDE_WORLD)) {
//            target.setStatus("Sorry, I cannot go outside my safe zone.");
//        }else if (target.getWorld().updatePosition(-nrSteps).equals(World.UpdateResponse.FAILED_OBSTRUCTED)) {
//            target.setStatus("Sorry, there is an obstacle in the way.");}
//        return true;
        return null;
    }

    /**
     * Requires an integer that represents the steps the robot takes
     * @param arguments of the command in question
     * @throws CouldNotParseArgumentsException if the arguments are invalid for this command
     */
    @Override
    public void setArguments(List<?> arguments) {
        if (arguments.size() != 1){
            throw new CouldNotParseArgumentsException();
        }

        try {
            steps = (int) arguments.get(0);
        } catch (ClassCastException badArgument){
            throw new CouldNotParseArgumentsException();
        }
    }
}
