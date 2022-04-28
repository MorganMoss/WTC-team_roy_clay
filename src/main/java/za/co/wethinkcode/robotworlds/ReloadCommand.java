package za.co.wethinkcode.robotworlds;

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