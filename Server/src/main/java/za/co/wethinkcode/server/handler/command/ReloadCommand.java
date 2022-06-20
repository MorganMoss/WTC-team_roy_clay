package za.co.wethinkcode.server.handler.command;

import za.co.wethinkcode.Response;

//This is not the spec required. TODO: Fix once an acceptance test is made.
public class ReloadCommand extends Command {

    /**
     * TODO
     * @return
     */
    @Override
    public Response execute() {
        //TODO: Should make an acceptance test for this,
        // then implement it correctly

//        if (target.shots < 5) {
//            target.reload();
//            target.setStatus("Shots reloaded. now you have "+target.shots+" shots.");
//
//        } else if (target.shots == 5) {
//            target.setStatus("you cannot reload, you have " + target.shots + " shots.");
//        }
//        return true;
        Response response = Response.createOK("Done");
        return addRobotState(response);
    }
}