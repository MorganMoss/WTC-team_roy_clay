package za.co.wethinkcode.server.command;

import za.co.wethinkcode.server.world.entity.movable.robot.Robot;

//TODO: This does nothing, needs implementation
public class RepairCommand extends Command {

    public RepairCommand() {
        super("repair");
    }


    @Override
    public boolean execute(Robot target) {
        //target.repair(); //commented out to compile project and run acceptance test against reference server
        return true;
    }
}
