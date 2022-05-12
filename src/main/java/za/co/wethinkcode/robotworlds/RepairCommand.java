package za.co.wethinkcode.robotworlds;

public class RepairCommand implements Command {

    public RepairCommand() {
        super("repair");
    }


    @Override
    public boolean execute(Robot target) {
        return false;
    }
}
