package za.co.wethinkcode.robotworlds;

import za.co.wethinkcode.robotworlds.world.IWorld;

public class ShootCommand extends Command{

    public ShootCommand() {
        super("shoot");
    }

//    public ShootCommand(String name, String argument) {
//        super(name, argument);
//    }

    @Override
    public boolean execute(Robot target) {

        Position robotPosition = target.getWorld().getPosition();
        System.out.println(String.valueOf(robotPosition.getX()+"--"+ robotPosition.getY()));

        for (int i = 0; i < 5; i++) {

        int nrSteps = Integer.parseInt(String.valueOf(i));

        if (target.getWorld().updatePosition(nrSteps).equals(IWorld.UpdateResponse.SUCCESS)){

                target.setStatus("bullet missed target by "+nrSteps+" steps.");
                target.getWorld().resetPosition(nrSteps);
                System.out.println(String.valueOf(robotPosition.getX()+"--"+ robotPosition.getY()));

            }else if (target.getWorld().updatePosition(nrSteps).equals(IWorld.UpdateResponse.FAILED_OUTSIDE_WORLD)) {

                target.setStatus("bullet missed target and went outside safe zone.");
                target.getWorld().resetPosition(nrSteps);
                System.out.println(String.valueOf(robotPosition.getX()+"--"+ robotPosition.getY()));

            }else if (target.getWorld().updatePosition(nrSteps).equals(IWorld.UpdateResponse.FAILED_OBSTRUCTED)) {

                System.out.println(String.valueOf(robotPosition.getX()+"--"+ robotPosition.getY()));
                target.getWorld().removeObstacle(target);
                target.getWorld().resetPosition(nrSteps);
                target.setStatus("bullet shot target in "+nrSteps+" steps.");
                System.out.println(String.valueOf(robotPosition.getX()+"--"+ robotPosition.getY()));

        }
        }
        return true;
    }
}
