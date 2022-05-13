package za.co.wethinkcode.robotworlds;

public class LookCommand extends Command {



    public boolean execute(Robot target) {

        Position robotPosition = target.getWorld().getPosition();
        return true;
    }
    public LookCommand() {

        super("look");
    }
}
