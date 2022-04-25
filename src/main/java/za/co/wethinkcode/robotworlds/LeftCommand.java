package za.co.wethinkcode.robotworlds;

public class LeftCommand extends Command{

    @Override
    public boolean execute(Robot target) {
        target.getWorld().updateDirection(false);
        target.setStatus("Turned left.");
        return true;
    }

    public LeftCommand() {
        super("left");
    }
}
