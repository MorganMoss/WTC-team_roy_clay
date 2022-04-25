package za.co.wethinkcode.robotworlds;


public abstract class Command {
    private final String name;
    private String argument;


    public abstract boolean execute(Robot target);

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
                return new SprintCommand(args[1]);
            case "replay":
                return new ReplayCommand(args.length == 2 ? args[1] : "");
            default:
                throw new IllegalArgumentException("Unsupported command: " + instruction);
        }
    }
}

