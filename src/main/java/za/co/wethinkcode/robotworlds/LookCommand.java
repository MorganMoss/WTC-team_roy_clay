package za.co.wethinkcode.robotworlds;

import za.co.wethinkcode.robotworlds.world.IWorld;

public class LookCommand extends Command {



    public boolean execute(Robot target) {

//        Position robotPosition = target.getWorld().getPosition();
        for (int i = 0; i < 5; i++) {
            int nrSteps = Integer.parseInt(String.valueOf(i));
            if (target.getWorld().updatePosition(nrSteps).equals(IWorld.UpdateResponse.SUCCESS)){
                target.setStatus("direction :"+ target.getWorld().getCurrentDirection()+"type :Open space "+"distance :" +nrSteps+" steps.");
                target.getWorld().resetPosition(nrSteps);
            }else if (target.getWorld().updatePosition(nrSteps).equals(IWorld.UpdateResponse.FAILED_OUTSIDE_WORLD)) {
                target.setStatus("direction :"+ target.getWorld().getCurrentDirection()+"type :"+target.getWorld()+"."+" distance :" +nrSteps+" steps.");
                target.getWorld().resetPosition(nrSteps);
            }else if (target.getWorld().updatePosition(nrSteps).equals(IWorld.UpdateResponse.FAILED_OBSTRUCTED)) {
                target.setStatus("direction :"+ target.getWorld().getCurrentDirection()+"type :Obstacle "+"distance :" +nrSteps+" steps.");
                target.getWorld().resetPosition(nrSteps);
            }
        }return true;
    }
    public LookCommand() {
        super("look");
    }
}
