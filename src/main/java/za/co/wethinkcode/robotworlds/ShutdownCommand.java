package za.co.wethinkcode.robotworlds;

public class ShutdownCommand extends Command {
    public ShutdownCommand() {
        super("off");
    }

    @Override
    public boolean execute(Robot target) {
        target.setStatus("Shutting down...");
        return false;
    }
}
