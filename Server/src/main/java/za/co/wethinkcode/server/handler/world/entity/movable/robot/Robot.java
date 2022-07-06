package za.co.wethinkcode.server.handler.world.entity.movable.robot;

import za.co.wethinkcode.server.handler.world.entity.movable.Movable;

import java.awt.*;
import java.util.HashMap;

public class Robot extends Movable {
    private final int max_shield, max_shots;
    private final String name;
    private final RobotType type;

    private String status;
    private int current_shield, current_shots;

    public Robot(Point initial_position, String name, RobotType type, int max_shield, int max_shots) {
        super(initial_position);
        this.name = name;
        this.type = type;
        this.max_shield = max_shield;
        this.max_shots = max_shots;
        this.current_shield = max_shield;
        this.current_shots = max_shots;
        this.direction = Direction.NORTH;
        this.status = "NORMAL";
    }



    /**
     * A robot obstructs the path,
     * It forces another robot to move back to one position before this entity
     * @param entity The entity moving into the same space as this entity.
     * @return "Obstructed"
     */
    @Override
    public String collidedWith(Movable entity) {
        //entity should be moved to the closest empty block to their previous position.
        if (entity.getClass() == Robot.class){
            return "Obstructed";
        }
        //if entity is a bullet, lose shield
        current_shield--;

        if (current_shield <= 0){
            status = "DEAD";
        }

        return "Hit";
    }

    public void fire(){
        current_shots--;
    }

    public void reload(){
        status = "RELOAD";
        current_shots = max_shots;
    }

    public void repair(){
        status = "REPAIR";
        current_shield = max_shield;
    }

    public void setMine(){
        status = "SETMINE";
    }

    public HashMap<String, ?> getState(){
        return  new HashMap<>(){{
            put("position", new int[] {position.x, position.y});
            put("direction", direction.toString());
            put("shields", current_shield);
            put("shots", current_shield);
            put("status", status);
        }};
    }
}