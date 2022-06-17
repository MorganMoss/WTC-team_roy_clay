package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.Response;


public class FireCommand extends Command {

    /**
     * TODO
     * @return
     */
    @Override
    public Response execute() {
        //TODO: Should make an acceptance test for this,
        // then implement it correctly

        //        Position robotPosition = target.getWorld().getPosition();
//
//        if (target.shots > 0){
//            target.shot();
//            for (int i = 0; i < 5; i++) {
//                int nrSteps = Integer.parseInt(String.valueOf(i));
//        if (target.getWorld().updatePosition(nrSteps).equals(World.UpdateResponse.SUCCESS)){
//                target.setStatus("bullet missed target by "+nrSteps+" steps.");
//                target.getWorld().resetPosition(nrSteps);
//            }else if (target.getWorld().updatePosition(nrSteps).equals(World.UpdateResponse.FAILED_OUTSIDE_WORLD)) {
//                target.setStatus("bullet missed target and went outside safe zone.");
//                target.getWorld().resetPosition(nrSteps);
//            }else if (target.getWorld().updatePosition(nrSteps).equals(World.UpdateResponse.FAILED_OBSTRUCTED)) {
//            target.getWorld().removeObstacle(target);
//            target.getWorld().resetPosition(nrSteps);
//            target.setStatus("bullet shot target in " + nrSteps + " steps.");
//        }
//        }

//
//        else if (target.shots == 0){
//            target.setStatus("Shots left "+target.shots+" (no active attack available) please reload.");
//        }
//
//        return true;
//    }
        return null;
    }
}
