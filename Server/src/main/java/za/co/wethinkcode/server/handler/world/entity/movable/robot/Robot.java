package za.co.wethinkcode.server.handler.world.entity.movable.robot;

import org.json.JSONObject;
import za.co.wethinkcode.server.command.Command;
import za.co.wethinkcode.server.handler.world.IWorld;
import za.co.wethinkcode.server.handler.world.Position;

import java.util.ArrayList;


public class Robot extends JSONObject {

    public static final Position CENTRE = new Position(0,0);

    private String status;
    private String name;
    private ArrayList<String> History;
    public za.co.wethinkcode.server.handler.world.IWorld IWorld;

    //TODO: Get rid of hard-coded variables here. Change visibility
    public int shots= 5;

    private int shield = 3;

    public String getPrint = "";

    public Robot(String name, IWorld IWorld) {
        this.name = name;
        this.status = "Ready";
        this.History = new ArrayList<>();
        this.IWorld = IWorld;
    }


    public Robot(String name) {
        this.name = name;
        this.status = "Ready";
        this.History = new ArrayList<>();
    }


    public int shot(){
        return this.shots=this.shots-1;
    }


    public  void reload(){
        this.shots=5;
    }


    public String getStatus() {
        return this.status;
    }


    public ArrayList<String> getHistory() {
        return this.History;
    }


    public void appendToHistory(String command) {
        this.History.add(command);
    }


    public boolean handleCommand(Command command) {

        return command.execute(this);
    }


    @Override
    public String toString() {
       return "[" + this.getWorld().getPosition().getX() + "," + this.getWorld().getPosition().getY() + "] "
               + this.name + "> " + this.status;
    }


    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public IWorld getWorld() {
        return this.IWorld;
    }

    public Position getPosition() {
       return this.getWorld().getPosition();
    }
}