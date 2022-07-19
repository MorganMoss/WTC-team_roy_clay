package za.co.wethinkcode.server.handler.world;

import za.co.wethinkcode.server.configuration.BadConfigurationException;
import za.co.wethinkcode.server.handler.world.entity.Entity;
import za.co.wethinkcode.server.handler.world.entity.immovable.Edge;
import za.co.wethinkcode.server.handler.world.entity.immovable.Mine;
import za.co.wethinkcode.server.handler.world.entity.immovable.Obstacle;
import za.co.wethinkcode.server.handler.world.entity.immovable.Pit;
import za.co.wethinkcode.server.handler.world.entity.movable.Movable;
import za.co.wethinkcode.server.handler.world.entity.movable.robot.Robot;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.*;

import static za.co.wethinkcode.server.configuration.Configuration.*;

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
    public final Hashtable<Point, Entity> entityTable = new Hashtable<>();
    /**
     * Set of open positions in the world,
     * makes adding robots to random spaces easier
     */
    private final Set<Point> openPositions = new HashSet<>();

    /**
     * Creates a list of all available spaces in the world
     */
    private void createOpenPositions(){
        int start = - size()/2;
        int end = size()/2;
        for (int x = start; x <= end; x++) {
            for (int y = start; y <= end; y++) {
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
     * Updates value of x,y co-ordinates depending on whether an entity is found
     */
    public static Entity seek(Point startingPosition, int angle_degrees, int steps) {
        int x = startingPosition.x;
        int y = startingPosition.y;

        //updating temp value of y & x
        for (int i = 1; steps > 0 ? i <= steps : i >= steps; i += steps > 0 ? 1 : -1) {
            //get next position of robot if no obstruction
            double angle_radians = Math.toRadians(angle_degrees);

            x += Math.sin(angle_radians);
            y += Math.cos(angle_radians);

            //check if that position is not already occupied. Not occupied if entity is null, otherwise occupied
            Point position = new Point(x,y);

            Entity foundEntity = instance.entityTable.get(position);

            //if occupied, stop moving and return obstruction
            if (foundEntity != null) {
                return foundEntity;
            }

            if (!instance.openPositions.contains(position)){
                return new Edge(position);
            }
        }
        //moved all required steps without any obstructions along the way
        return null;
    }

    public static void updatePosition(Movable movable, Point newPosition){
        //Not actually moving
        if (movable.getPosition().equals(newPosition)){
            return;
        }

        //If bad position is given.
        if (!instance.openPositions.contains(newPosition)){
            throw new OutOfBoundsException();
        }

        removeEntity(movable.getPosition());
        movable.updatePosition(newPosition);
        addEntity(newPosition, movable);
    }

    public static void updatePosition(String robot, Point newPosition) {
        Movable movable = getRobot(robot);

        updatePosition(movable, newPosition);
    }

    private void addEntityOfType(Class<?> type, List<Point> positions){
        for (Point position : positions){
            try {
                addEntity(position, (Entity) type.getConstructor(Point.class).newInstance(position));
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException | OccupiedPositionException e) {
                throw new BadConfigurationException(e.toString());
            }
        }
    }

    /**
     * Parses the entity positions given in the configuration and converts it to a list of Points
     * @param entityPositions given in the configuration
     * @return a list of Points to put respective entities
     */
    private static ArrayList<Point> iterateThroughPredefinedPositions(String entityPositions){
        ArrayList<Point> positions = new ArrayList<>();

        if (!entityPositions.equalsIgnoreCase("none")) {
            String[] numbers = entityPositions.split(",");

            for (int i = 0; i < numbers.length - 1; i += 2) {
                try {
                    int x = Integer.parseInt(numbers[i]);
                    int y = Integer.parseInt(numbers[i + 1]);
                    positions.add(new Point(x, y));
                } catch (NumberFormatException badNumber) {
                    throw new BadConfigurationException(badNumber.toString());
                }
            }
        }

        return positions;
    }

    /**
     * Tries to add any predefined entities to positions given
     */
    private void addPredefinedImmovables(){
        addEntityOfType(Obstacle.class, iterateThroughPredefinedPositions(obstacle()));
        addEntityOfType(Pit.class, iterateThroughPredefinedPositions(pits()));
        addEntityOfType(Mine.class, iterateThroughPredefinedPositions(mines()));
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

    public static String getEntities(Class<?> entityClass){
        StringBuilder result = new StringBuilder();

        for (Point position : instance.entityTable.keySet()){
            if (instance.entityTable.get(position).getClass().equals(entityClass)){
                result.append(position.x).append(",").append(position.y).append(",");
            }
        }
        if (result.charAt(result.length()-1) == ','){
            result.deleteCharAt(result.length()-1);
        }

        return result.toString();
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
     * Resets world to initial values
     */
    public static void reset() {
        instance = new World();
        instance.createOpenPositions();
        instance.addPredefinedImmovables();

    }

    public static void dump(){
        int start = - size()/2 -1;
        int end = size()/2+1;
        for (int y = start; y <= end; y++) {
            for (int x = start; x <= end; x++) {
                if (!instance.openPositions.contains(new Point(x,y))){
                    System.out.print(instance.entityTable.getOrDefault(new Point(x,y), new Edge(new Point(x, y))).toString().charAt(0));
                } else {
                    System.out.print("_");
                }
            }
            System.out.println();
        }
    }
}

