package za.co.wethinkcode.server;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import picocli.CommandLine;
import za.co.wethinkcode.server.handler.world.World;
import za.co.wethinkcode.server.handler.world.entity.immovable.Mine;
import za.co.wethinkcode.server.handler.world.entity.immovable.Obstacle;
import za.co.wethinkcode.server.handler.world.entity.immovable.Pit;

import java.util.Hashtable;
import java.util.concurrent.Callable;
import java.lang.reflect.*;

import static java.lang.Math.round;

/**
 * Configuration Class stores all the values provided via CLI or a loaded set of values
 * It has default values.
 */
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
    public static Integer port() {return PORT;}

    @CommandLine.Option(
            names = {"-s", "--size"},
            description = {"Size of the world as one side of a square grid"},
            defaultValue = "1"
    )
    private static Integer SIZE;
    public static Integer size() {return  SIZE;}

    @CommandLine.Option(
            names = {"-o", "--obstacle"},
            description = {"Position of fixed obstacle as [x,y] coordinate in form 'x,y', or 'none' or 'random'"},
            defaultValue = "none"
    )
    private static String OBSTACLE;
    public static String obstacle() {return OBSTACLE;}

    @CommandLine.Option(
            names = {"-pt", "--pit"},
            description = {"Position of fixed pit as [x,y] coordinate in form 'x,y', or 'none' or 'random'"},
            defaultValue = "none"
    )
    private static String PITS;
    public static String pits() {return PITS;}

    @CommandLine.Option(
            names = {"-m", "--mine"},
            description = {"Position of fixed mine as [x,y] coordinate in form 'x,y', or 'none' or 'random'"},
            defaultValue = "none"
    )
    private static String MINES;
    public static String mines() {return MINES;}

    @CommandLine.Option(
            names = {"-v", "--visibility"},
            description = {"Visibility for robot in nr of steps"},
            defaultValue = "10"
    )
    private static Integer VISIBILITY;
    public static Integer visibility() {return VISIBILITY;}

    @CommandLine.Option(
            names = {"-r", "--repair"},
            description = {"Duration for robot shield to repair"},
            defaultValue = "5"
    )
    private static Integer REPAIR;
    public static Integer repair() {return  REPAIR;}

    @CommandLine.Option(
            names = {"-l" , "--reload"},
            description = {"Instruct the robot to reload its weapons"},
            defaultValue = "7"
    )
    private static Integer RELOAD;
    public static Integer reload() {return  RELOAD;}

    @CommandLine.Option(
            names = {"-mt", "--mine_time"},
            description = {"Time it takes to place a mine"},
            defaultValue = "3"
    )
    private static Integer MINE;
    public static Integer mine() { return MINE;}

    @CommandLine.Option(
            names = {"-shield", "--max_shield"},
            description = {"Maximum strength for robot shield"},
            defaultValue = "3"
    )
    private static Integer MAX_SHIELD;
    public static Integer max_shield() {return MAX_SHIELD;}

    @CommandLine.Option(
            names = {"-shots", "--max_shots"},
            description = {"Maximum strength for robot shield"},
            defaultValue = "3"
    )
    private static Integer MAX_SHOTS;
    public static Integer max_shots() {return MAX_SHOTS;}

    @CommandLine.Option(
            names = {"-saves", "--save_location"},
            description = {"Location of the saves database"},
            defaultValue = "Saves"
    )
    private static String SAVE_LOCATION;
    public static String save_location() { return SAVE_LOCATION;}

    @Override
    public Integer call(){
        return 0;
    }

    /**
     * Initializes all initial variable values.
     */
    public static void setConfiguration(String[] args){
        (new CommandLine(new Configuration())).execute(args);
        inspect();
    }

    /**
     * Dynamically lists all configuration variable values.
     */
    private static void inspect() {
        Field[] fields = Configuration.class.getDeclaredFields();
        System.out.println("Configuration set: ");

        for (Field field : fields) {
            try {
                String fieldName = field.getName().toLowerCase();
                field.setAccessible(true);
                System.out.println("\t" + fieldName + " = " + field.get(null));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * this function uses Google Gson
     * (a java data serialization package)
     * that takes this configurations instance and converts it into a String Json
     * @return a String Json of the configurations instance
     */
    public static String serialize(){
        Hashtable<String, Class<?>> Entities = new Hashtable<>(){{
            put("OBSTACLE", Obstacle.class);
            put("PITS", Pit.class);
            put("MINES", Mine.class);
        }};

        for (String fieldName : Entities.keySet()){

            Field field;

            try {
                field = Configuration.class.getField(fieldName);
            } catch (NoSuchFieldException ignored) {
                continue;
            }

            field.setAccessible(true);

            String value = "";

            try {
                value = (String) field.get(null);
            } catch (IllegalAccessException ignored) {}

            if (!value.isBlank()){
                value += ",";
            }

            value += World.getEntities(Entities.get(fieldName));

            try {
                field.set(null, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }



        return new Gson().toJson(storeFields());
    }

    /**
     * this function uses Google Gson
     * (a java data serialization package)
     * Takes a serialized configuration instance and loads values from it.
     * Can change the configuration during operation.
     * @param configuration_json serialized instance of this class
     */
    public static void loadConfiguration(String configuration_json){
        Hashtable<String, Object> values;

        try {
            values = new Gson().fromJson(configuration_json, Hashtable.class);
        } catch (JsonSyntaxException badJSON){
            System.out.println("Bad configuration loaded. Aborting . . .");
            System.err.println(badJSON.getMessage());
            System.exit(1);
            return;
        }

        if (values == null){
            return;
        }

        Field[] fields = Configuration.class.getDeclaredFields();

        for (Field field : fields){
            try {
                field.setAccessible(true);
                Object value = values.get(field.getName());
                Type fieldType = field.getType();

                if (fieldType.equals(Integer.class)){
                    field.set(null, (int) round((double) value));
                    continue;
                }

                field.set(null, value);
            } catch (IllegalAccessException e) {
                System.out.println("Bad configuration loaded. Aborting . . .");
                System.exit(1);
            }
        }

        inspect();

        World.reset();
    }

    private static Hashtable<String, Object> storeFields(){
        Hashtable<String, Object> values = new Hashtable<>();
        Field[] fields = Configuration.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                String fieldName = field.getName();

                if (fieldName.equals("instance") || fieldName.equals("values")) {
                    continue;
                }

                field.setAccessible(true);

                values.put(fieldName , field.get(null));
            } catch ( IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return values;
    }

    /**
     * Private constructor
     */
    private Configuration(){
    }

}