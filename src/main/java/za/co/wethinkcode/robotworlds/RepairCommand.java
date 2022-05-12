package za.co.wethinkcode.robotworlds;

public class RepairCommand extends Command {

    public RepairCommand() {
        super("repair");
    }


    @Override
    public boolean execute(Robot target) {
        target.repair();
        return true;
    }
}
