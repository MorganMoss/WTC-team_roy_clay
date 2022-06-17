package za.co.wethinkcode.server.handler;

import za.co.wethinkcode.Request;
import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.command.*;

public class Handler {
    private static Handler handler = null;
    private Handler(){
    }

    public static Handler getHandler(){
        if (handler == null){
            handler = new Handler();
        }

        return handler;
    }

    public Response executeRequest(Request request){
        return null;
    }

    private Command create(String instruction) {
        String[] args = instruction.toLowerCase().trim().split(" ", 2);

        switch (args[0]){
            case "shutdown":
            case "off":
                return new ShutdownCommand();
            case "help":
                return new HelpCommand();
            case "forward":
                return new ForwardCommand(args[1]);
            case "back":
                return new BackCommand(args[1]);
            case "left":
                return new LeftCommand();
            case "right":
                return new RightCommand();
            case "sprint":
                return new LaunchCommand(args[1]);
            case "fire":
                return new FireCommand();
            case "look":
                return new LookCommand();
            case "reload":
                return new ReloadCommand();
            default:
                throw new IllegalArgumentException("Unsupported command: " + instruction);
        }
    }
}

