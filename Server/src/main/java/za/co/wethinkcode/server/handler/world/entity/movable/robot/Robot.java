package za.co.wethinkcode.server.handler.world.entity.movable.robot;

import za.co.wethinkcode.server.handler.world.entity.movable.Movable;

import java.awt.*;

public class Robot extends Movable {
    private String name;

    //TODO: Get rid of hard-coded variables here. Change visibility
    public int shots= 5;

    private int shield = 3;

//    public String getPrint = "";

    public Robot(String name) {
        //TODO: add position.
        super(null);
        this.name = name;
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
        return "Obstructed";
    }


    public int shot(){
        return this.shots-=1;
    }


    public  void reload(){
        this.shots=5;
    }


//    public String getStatus() {
//        return this.status;
//    }


//    public ArrayList<String> getHistory() {
//        return this.History;
//    }


//    public void appendToHistory(String command) {
//        this.History.add(command);
//    }


//    public boolean handleCommand(Command command) {
//
//        return command.execute(this);
//    }


//    @Override
//    public String toString() {
//       return "[" + this.getWorld().getPosition().getX() + "," + this.getWorld().getPosition().getY() + "] "
//               + this.name + "> " + this.status;
//    }


//    public void setStatus(String status) {
//        this.status = status;
//    }
//
    public String getName() {
        return name;
    }


//    public IWorld getWorld() {
//        return this.IWorld;
//    }
//
//    public Position getPosition() {
//       return this.getWorld().getPosition();
//    }
}