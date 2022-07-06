package za.co.wethinkcode.server.handler.world;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import za.co.wethinkcode.server.BadConfigurationException;
import za.co.wethinkcode.server.handler.world.entity.Entity;
import za.co.wethinkcode.server.handler.world.entity.immovable.Mine;
import za.co.wethinkcode.server.handler.world.entity.immovable.Obstacle;
import za.co.wethinkcode.server.handler.world.entity.immovable.Pit;
import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;

import static za.co.wethinkcode.server.Configuration.*;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The world is a container that manages Entities,
 * Has a fixed 2 dimensional size that contains all the Entities.
 *
 */
public class World {


    private static volatile World instance = initializeWorld();

    /**
     * Lookup Table for existing robots
     */
    private final Hashtable<String, Robot> robots = new Hashtable<>();
    /**
     * Map of all entities that exist in the world
     */
    private final Hashtable<Point, Entity> entityTable = new Hashtable<>();
    /**
     * Set of open positions in the world,
     * makes adding robots to random spaces easier
     */
    private final Set<Point> openPositions = new HashSet<>();

    /**
     * Creates a list of all available spaces in the world
     */
    private void createOpenPositions(){
        for (int x = 0; x < size(); x++) {
            for (int y = 0; y < size(); y++) {
                openPositions.add(new Point(x, y));
            }
        }
    }

    /**
     * Gets a random open position in the world
     * @return A position that contains no entity.
     * Returns null if no positions available
     */
    public static Point getOpenPosition() {
        if (instance.openPositions.size() <= 0){
            return null;
        }

        return (Point) instance.openPositions.toArray()[new Random().nextInt(instance.openPositions.size())];
    }

    /**
     * Gets the robot object mapped to the robot name given
     * @param robot's name
     * @return the instance mapped to that name
     */
    public static Robot getRobot(String robot) {
        return instance.robots.get(robot);
    }

    /**
     * Adds an entity to a specific position
     * and marks that position as occupied.
     * @param position of the entity
     * @param entity added
     * @throws OccupiedPositionException if the space is already occupied,
     * {@link OutOfBoundsException} if the position given is outside the region defined by size.
     */
    public static void addEntity(Point position, Entity entity){
        boolean availableOnMap = instance.entityTable.getOrDefault(position, null) == null;
        boolean outOfBounds = !instance.openPositions.contains(position);

        if (availableOnMap && !outOfBounds){
            instance.entityTable.put(position, entity);
            instance.openPositions.remove(position);
            return;
        }

        if (!availableOnMap)
            throw new OccupiedPositionException();

        throw new OutOfBoundsException();
    }

    /**
     * Removes the entity at the given position
     * @param position of the entity
     */
    public static void removeEntity(Point position){
        instance.entityTable.remove(position);
        instance.openPositions.add(position);
    }

    /**
     * Adds a robot to both the robot lookup table and the map of entities
     */
    public static void addRobot(String robotName, Robot robotEntity) {
        instance.robots.put(robotName, robotEntity);
        addEntity(robotEntity.getPosition(), robotEntity);
    }

    /**
     * Removes a robot to both the robot lookup table and the map of entities
     */
    public static void removeRobot(String robot) {
        try {
            Point robotPosition = instance.robots.remove(robot).getPosition();
            removeEntity(robotPosition);
        } catch (NullPointerException ignored) {}
    }

    /**
     * Get the list of all robots in this world
     * @return the names of all robots
     */
    public static Set<String> getRobots() {
        return instance.robots.keySet();
    }

    /**
     * Parses the entity positions given in the configuration and converts it to a list of Points
     * @param entityPositions given in the configuration
     * @return a list of Points to put respective entities
     */
    private static ArrayList<Point> iterateThroughPredefinedPositions(String entityPositions){
        String[] numbers = entityPositions.split(",");
        ArrayList<Point> positions = new ArrayList<>();

        for (int i = 0; i < numbers.length-1; i+=2){
            int x = Integer.parseInt(numbers[i]);
            int y = Integer.parseInt(numbers[i+1]);

            if (x > size() | y > size()){
                throw new BadConfigurationException("Predefined position given is out of bounds for this world size");
            }

            positions.add(new Point(x,y));
        }

        return positions;
    }

    /**
     * Tries to add any predefined entities to positions given
     */
    private void addPredefinedImmovables(){
        List<String> predefined_immovable = List.of(obstacle(), pits(), mines());

        for (int j = 0; j<predefined_immovable.size(); j++) {
            String entityPositions = predefined_immovable.get(j);

            if (entityPositions.equalsIgnoreCase("none")){
                continue;
            }

            if (!entityPositions.matches("[\\d+,?]+")){
                throw new BadConfigurationException("Badly constructed arguments for predefined entities.");
            }


            Entity constructedEntity = null;
            for (Point position: iterateThroughPredefinedPositions(entityPositions)){
                switch (j){
                    case 0:
                        constructedEntity = new Obstacle(position);
                        break;
                    case 1:
                        constructedEntity = new Pit(position);
                        break;
                    case 2:
                        constructedEntity = new Mine(position);
                        break;
                }

                addEntity(position, constructedEntity);
            }
        }
    }

    /**
     * Made private, as this is a singleton class.
     * The public static members manage the instance of the world
     */
    private World(){
    }

    /**
     * Called to instantiate the world's instance
     * Used as a workaround, since the constructor cannot directly call reset()
     * @return that instance
     */
    private static World initializeWorld() {
        reset();
        return instance;
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

    /**
     * this function uses Google Gson
     * (a java data serialization package)
     * that takes this worlds instance and converts it into a String Json
     * @return a String Json of the worlds instance
     */
    public static String serialize(){
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().create();
        return gson.toJson(instance);
    }

    /**
     * this function uses Google Gson
     * (a java data serialization package)
     * A saved instance of a world (in a serialized form)
     * overwrites the current world instance
     * @param world_json given by a database
     */
    public static void loadWorld(String world_json){
        try {
            instance = new Gson().fromJson(world_json, World.class);

            //Removing old robots
            for (String robot : instance.robots.keySet()){
                removeRobot(robot);
            }
        } catch (JsonSyntaxException badJSON){
            System.out.println("Bad world loaded. Aborting . . .");
            System.err.println(badJSON);
            System.exit(1);
        }
    }

    /**
     * Resets world to initial values
     */
    public static void reset() {
        instance = new World();
        instance.createOpenPositions();
        instance.addPredefinedImmovables();
    }
}

