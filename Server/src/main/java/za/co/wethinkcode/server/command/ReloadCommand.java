package za.co.wethinkcode.server.command;

import za.co.wethinkcode.server.world.entity.movable.robot.Robot;

//This is not the spec required. TODO: Fix once an acceptance test is made.
public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("reload");
    }


    @Override
    public boolean execute(Robot target) {
        if (target.shots < 5) {
            target.reload();
            target.setStatus("Shots reloaded. now you have "+target.shots+" shots.");

        } else if (target.shots == 5) {
            target.setStatus("you cannot reload, you have " + target.shots + " shots.");
        }
        return true;
    }
}