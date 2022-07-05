package za.co.wethinkcode.server;

import picocli.CommandLine;
import java.util.concurrent.Callable;
import java.lang.reflect.*;
@CommandLine.Command(
        name = "robots-world",
        mixinStandardHelpOptions = true,
        version = {"robots 1.0.0"},
        description = {"Starts the Robot World server"}
)
public final class Configuration implements Callable<Integer> {
    @CommandLine.Option(
            names = {"-p", "--port"},
            description = {"Port to listen for client connections"},
            defaultValue = "5000"
    )
    private static Integer PORT;

    @CommandLine.Option(
            names = {"-s", "--size"},
            description = {"Size of the world as one side of a square grid"},
            defaultValue = "1"
    )
    private static Integer SIZE;

    @CommandLine.Option(
            names = {"-o", "--obstacle"},
            description = {"Position of fixed obstacle as [x,y] coordinate in form 'x,y', or 'none' or 'random'"},
            defaultValue = "none"
    )
    private static String OBSTACLE;

    @CommandLine.Option(
            names = {"-pt", "--pit"},
            description = {"Position of fixed pit as [x,y] coordinate in form 'x,y', or 'none' or 'random'"},
            defaultValue = "none"
    )
    private static String PITS;

    @CommandLine.Option(
            names = {"-m", "--mine"},
            description = {"Position of fixed mine as [x,y] coordinate in form 'x,y', or 'none' or 'random'"},
            defaultValue = "none"
    )
    private static String MINES;

    @CommandLine.Option(
            names = {"-v", "--visibility"},
            description = {"Visibility for robot in nr of steps"},
            defaultValue = "10"
    )
    private static Integer VISIBILITY;

    @CommandLine.Option(
            names = {"-r", "--repair"},
            description = {"Duration for robot shield to repair"},
            defaultValue = "5"
    )
    private static Integer REPAIR;

    @CommandLine.Option(
            names = {"-l" , "--reload"},
            description = {"Instruct the robot to reload its weapons"},
            defaultValue = "7"
    )
    private static Integer RELOAD;

    @CommandLine.Option(
            names = {"-mt", "--mine_time"},
            description = {"Time it takes to place a mine"},
            defaultValue = "3"
    )
    private static Integer MINE;

    @CommandLine.Option(
            names = {"-shield", "--max_shield"},
            description = {"Maximum strength for robot shield"},
            defaultValue = "3"
    )
    private static Integer MAX_SHIELD;

    @CommandLine.Option(
            names = {"-shots", "--max_shots"},
            description = {"Maximum strength for robot shield"},
            defaultValue = "3"
    )
    private static Integer MAX_SHOTS;


    public static Integer port() {return PORT;}
    public static Integer size() {return  SIZE;}
    public static String obstacle() {return OBSTACLE;}
    public static String pits() {return PITS;}
    public static String mines() {return MINES;}
    public static Integer visibility() {return VISIBILITY;}
    public static Integer repair() {return  REPAIR;}
    public static Integer reload() {return  RELOAD;}
    public static Integer max_shield() {return MAX_SHIELD;}
    public static Integer max_shots() {return MAX_SHOTS;}
    public static Integer mine() { return MINE;}

    @Override
    public Integer call(){
        return 0;
    }

    /**
     * Initializes all initial variable values.
     */
    public static void setConfiguration(String[] args){
        int exitCodeServer = (new CommandLine(new Configuration())).execute(args);
        inspect();
    }

    /**
     * Dynamically lists all configuration variable values.
     */
    private static void inspect() {
        Field[] fields = Configuration.class.getDeclaredFields();
        System.out.printf("%d fields:%n", fields.length);
        Object o;
        for (Field field : fields) {
            try {
                String fieldName = field.getName().toLowerCase();
                System.out.println(fieldName + " = " + Configuration.class.getMethod(fieldName).invoke(Configuration.class));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Private constructor not to be used outside this class,
     * as it is treated as static
     */
    private Configuration(){}

    //TODO: for later implementation.
    //    /**
    //     * Dynamically load default values from a config file
    //     */
    //    private static String getDefaultValue(String field){return "";}
}