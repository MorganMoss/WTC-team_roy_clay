package za.co.wethinkcode.server.handler.world;

import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import za.co.wethinkcode.server.handler.world.map.EmptyMap;
import za.co.wethinkcode.server.handler.world.map.Map;

import java.util.concurrent.Callable;


@Command(
        name = "robots-world",
        mixinStandardHelpOptions = true,
        version = {"robots 1.0.0"},
        description = {"Starts the Robot World server"}
)
public class World extends AbstractWorld implements Callable<Integer> {

    @Option(
            names = {"-s", "--size"},
            description = {"Size of the world as one side of a square grid"}
    )
    private int size = 1;

    @Option(
            names = {"-p", "--port"},
            description = {"Port to listen for client connections"}
    )
    private static int port = 5000;

    @Option(
            names = {"-o", "--obstacle"},
            description = {"Position of fixed obstacle as [x,y] coordinate in form 'x,y', or 'none' or 'random'"}
    )
    private String obstacle = "none";

    @Option(
            names = {"-pt", "--pit"},
            description = {"Position of fixed pit as [x,y] coordinate in form 'x,y', or 'none' or 'random'"}
    )
    private String pits = "none";

    //TODO: Move blocks position and path responsibility to the world.
    @Option(
            names = {"-m", "--mine"},
            description = {"Position of fixed mine as [x,y] coordinate in form 'x,y', or 'none' or 'random'"}
    )
    private String mines = "none";

    @Option(
            names = {"-v", "--visibility"},
            description = {"Visibility for robot in nr of steps"}
    )
    private int visibility = 10;

    @Option(
            names = {"-r", "--repair"},
            description = {"Duration for robot shield to repair"}
    )
    private int repair = 5;

    @Option(
            names = {"-l" , "--reload"},
            description = {"Instruct the robot to reload its weapons"}
    )
    private int reload = 7;

    @Option(
            names = {"-ht", "--hit"},
            description = {"Maximum strength for robot shield"}
    )
    private int hit = 3;


    //TODO: Move blocks position and path responsibility to the world.

    private static volatile World instance = null;

    private World(){
        super(new EmptyMap());
    }

    public static World getInstance(){
        if(instance == null){
            synchronized (World.class){
                if(instance == null){
                    instance = new World();
                }
            }
        }
        return instance;
    }

    private void creatingWorldConfig(){
        System.out.println("Creating World with the following configurations.. " +
                "\n[size: " + this.size + " x " + this.size +
                "\n, obstacles: " + this.obstacle +
                "\n, pits: " + this.pits +
                "\n, mines: " + this.mines +
                "\n, visibility: " + this.visibility +
                "\n, repair: " + this.repair +
                "\n, reload: " + this.reload +
                "\n, hit: " + this.hit + "]");
    }

    /**
     * build command arguments, initialise and run server
     * @return terminating state for the server
     * @throws Exception handle any connection or invalid command arguments
     */
    @Override
    public Integer call() throws Exception {
        //TODO: build command options here
        creatingWorldConfig();

        //create and return the world with the config values

        return 0;
    }

    public static int getPort(){
        return port;
    }
}
