package za.co.wethinkcode.robotworlds;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help");
    }

    @Override
    public boolean execute(Robot target) {
        target.setStatus("I can understand these commands:\n" +
                "OFF  - Shut down robot\n" +
                "HELP - provide information about commands\n" +
                "FORWARD - move forward by specified number of steps, e.g. 'FORWARD 10'\n" +
                "BACK - move backward by specified number of steps, e.g. 'BACKWARD 10'\n" +
                "LEFT - turns left, e.g. 'TURNED LEFT'\n" +
                "RIGHT - turns right, e.g. 'TURNED RIGHT'\n"+
                "REPLAY - replays the commands\n" +
                "REPLAY REVERSED - replays the commands reversed\n" +
                "REPLAY 1 - replays the last 1 commands\n" +
                "REPLAY 2-1 - replays the last 1 commands\n" +
                "REPLAY REVERSED 2-1 - replays the last 1 commands reversed'\n"+
                "REPLAY REVERSED 1- replays the last 1 commands reversed");
        return true;
    }
}
