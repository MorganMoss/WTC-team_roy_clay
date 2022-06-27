package za.co.wethinkcode.server.handler.world;

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

    private static volatile World instance = new World();

    private final Hashtable<String, Robot> robots = new Hashtable<>();
    private final Hashtable<Point, Entity> entityTable = new Hashtable<>();

    /**
     * Gets a random open position in the world
     * @return A position that contains no entity.
     */
    public static Point getOpenSpace() {
        boolean[][] availablePositions = new boolean[size()][size()];
        ArrayList<Point> openPositions = new ArrayList<>();


        for (Point occupiedPosition : instance.entityTable.keySet()){
            availablePositions[occupiedPosition.x][occupiedPosition.y] = true;
        }


        for (int x = 0; x < size(); x++){
            for (int y = 0; y < size(); y++){
                if (!availablePositions[x][y]){
                    //open position
                    openPositions.add(new Point(x,y));
                }
            }
        }

        if (openPositions.size() <= 0){
            return null;
        }

        Random r = new Random();
        int randomOpenPositionIndex = r.nextInt(openPositions.size());

        return openPositions.get(randomOpenPositionIndex);
    }

    public static Robot getRobot(String robot) {
        return instance.robots.get(robot);
    }

    public static void addRobot(String robotName, Robot robotEntity) {
        instance.robots.put(robotName, robotEntity);
        instance.entityTable.put(robotEntity.getPosition(), robotEntity);
    }

    public static void removeRobot(String robot) {
        try {
            Point robotPosition = instance.robots.remove(robot).getPosition();
            instance.entityTable.remove(robotPosition);
        } catch (NullPointerException ignored) {}
    }

    /**
     * Tries to add any predefined entities to positions given
     */
    private void addPredefinedImmovables(){
        List<String> predefined_immovable = List.of(obstacle(), pits(), mines());
        for (int j = 0; j<predefined_immovable.size(); j++) {
            String entity = predefined_immovable.get(j);

            if (entity.equalsIgnoreCase("none")
                    | !entity.matches("[\\d+,?]+")){
                continue;
            }

            int x, y;

            String[] numbers = entity.split(",");
            for (int i = 0; i < numbers.length-1; i+=2){
                x = Integer.parseInt(numbers[i]);
                y = Integer.parseInt(numbers[i+1]);

                if (x > size() | y > size()){
                    System.out.println("Bad obstacle, pit or mine position for this world size");
                    System.exit(2);
                }

                switch (j){
                    case 0:
                        entityTable.put(new Point(x,y), new Obstacle(new Point(x,y)));
                        break;
                    case 1:
                        entityTable.put(new Point(x,y), new Pit(new Point(x,y)));
                        break;
                    case 2:
                        entityTable.put(new Point(x,y), new Mine(new Point(x,y)));
                        break;
                }
            }
        }
    }

//    public

    private World(){
        //Use configuration values here to dictate properties of the world.
        addPredefinedImmovables();
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
    }
}

