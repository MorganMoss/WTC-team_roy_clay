package za.co.wethinkcode.server.handler.world.entity.movable.robot;

import za.co.wethinkcode.server.handler.world.entity.movable.Movable;

public class Robot extends Movable {
    private String name;

    //TODO: Get rid of hard-coded variables here. Change visibility
    public int shots= 5;

    private int shield = 3;

//    public String getPrint = "";

    public Robot(String name) {
        this.name = name;
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

    @Override
    public String collidedWith(Movable entity) {
        return null;
    }

//    public IWorld getWorld() {
//        return this.IWorld;
//    }
//
//    public Position getPosition() {
//       return this.getWorld().getPosition();
//    }
}