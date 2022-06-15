package za.co.wethinkcode.server.handler.command;


import za.co.wethinkcode.server.handler.world.World;
import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;

//Out of spec
//TODO: Consider removal.
public class LaunchCommand extends Command {

        @Override
        public boolean execute(Robot target) {
            int nrSteps = Integer.parseInt(getArgument());
            while (nrSteps!=0) {

                if (target.getWorld().updatePosition(nrSteps).equals(World.UpdateResponse.SUCCESS)){
                    target.setStatus("Moved forward by "+nrSteps+" steps.");
                    nrSteps--;
                }
                else if (target.getWorld().updatePosition(nrSteps).equals(World.UpdateResponse.FAILED_OUTSIDE_WORLD)) {
                    target.setStatus("Sorry, I cannot go outside my safe zone.");
            }else if (target.getWorld().updatePosition(-nrSteps).equals(World.UpdateResponse.FAILED_OBSTRUCTED)) {
                    target.setStatus("Sorry, there is an obstacle in the way.");}
            if (nrSteps> 0){
                target.getPrint += "\n"+target;
            }
            }
            return true;
        }
        public LaunchCommand(String argument) {
            super("sprint", argument);
        }
    }
