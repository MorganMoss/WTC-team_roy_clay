package za.co.wethinkcode.server.command;


import za.co.wethinkcode.server.world.entity.movable.robot.Robot;

public abstract class Command {
    //TODO: remove name dependency
    private final String name;
    private String argument;


    public abstract boolean execute(Robot target);

    //TODO: remove name dependency
    public Command(String name){
        this.name = name.trim().toLowerCase();
        this.argument = "";
    }


    public Command(String name, String argument) {
        this(name);
        this.argument = argument.trim();
    }


    public String getName() {                                                                           //<2>
        return name;
    }


    public String getArgument() {
        return argument;
    }

    //TODO: Remove this, implement Handler in it's place
    public static Command create(String instruction) {
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

