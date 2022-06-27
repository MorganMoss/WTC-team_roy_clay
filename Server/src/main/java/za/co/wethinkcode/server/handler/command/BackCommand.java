package za.co.wethinkcode.server.handler.command;

import java.util.List;

public class BackCommand extends Movement{
    @Override
    public void setArguments(List<String> arguments) {
        super.setArguments(arguments);
        steps = -steps;
    }
}
