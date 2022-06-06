package za.co.wethinkcode.robotworlds;

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
