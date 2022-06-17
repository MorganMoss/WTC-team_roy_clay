package za.co.wethinkcode.server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import org.json.*;
import za.co.wethinkcode.server.world.AbstractWorld;
import za.co.wethinkcode.server.world.World;
import za.co.wethinkcode.server.world.entity.movable.robot.Robot;
import za.co.wethinkcode.server.command.Command;
import za.co.wethinkcode.server.world.map.EmptyMap;

//TODO: Better name?
public class ServerClientCommunicator implements Runnable {

    public static final int PORT = 5000;
    // din and dout could probably have better names.
    DataInputStream din;
    DataOutputStream dout;
    Robot robot;
    // The client thread should not have ownership of the world
    //TODO: Remove this ownership
    World world;
    String in;
    //Never used outside of constructor
    private final String clientMachine;

    // This looks like a very bad place for this.
    // It is serverside client connection.
    // Seems to only be used in setposition, so could be easily removed
    // TODO: Consider removing this functionality
    static String getInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt);
        String input = scanner.nextLine();
        while (input.isBlank()) {
            System.out.println(prompt);
            input = scanner.nextLine();
        }
        return input;
    }

    //TODO: Consider deletion
    void setposition (Robot robot ) throws IOException {
        int bry;
        int brx;
        int tly;
        int tlx;
        tlx = Integer.parseInt(getInput("Input top left X"));
        tly = Integer.parseInt(getInput("Input top left Y"));
        brx = Integer.parseInt(getInput("Input bottom right X"));
        bry = Integer.parseInt(getInput("Input bottom right Y"));

        World world = robot.getWorld();
        world.SetPositions(tlx, tly, brx, bry);
    }



    public ServerClientCommunicator(Socket socket) throws IOException {
        clientMachine = socket.getInetAddress().getHostName();
        System.out.println("Connection from " + clientMachine);
        //Is this a necessary line?
        System.out.println("Waiting for client...");
        din=new DataInputStream(socket.getInputStream());
        dout=new DataOutputStream(socket.getOutputStream());

    }


    // Too many things are going on in here,
    // should be split into multiple methods
    //TODO: Consider splitting this up into more functions.
    public void run() {
        boolean cont;


        String instruction;


            try {
                //            System.out.println("qwertyuiop");
                //            System.out.println(din.readUTF());
                in = din.readUTF();

                JSONObject out = new JSONObject(in);
                System.out.println(out);
                String name = String.valueOf(out.get("name"));

                world = new AbstractWorld(new EmptyMap());
                robot = new Robot(name,world);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }



        while (true){
            try {
                in = din.readUTF();

                JSONObject out = new JSONObject(in);
                instruction= (out.getString("command"));

                Command command = Command.create(instruction);
                cont=robot.handleCommand(command);
                System.out.println("done thing");
                if (instruction.split(" ")[0].equalsIgnoreCase( "replay")
                        ||instruction.split(" ")[0].equalsIgnoreCase( "mazerun")){
                }else {
                    robot.appendToHistory(instruction);
                }
                //TODO: Improve the if statements
                // Sprint is not part of the specs.
                //TODO: Consider removing sprint
                if (command.getName()== "sprint"){
                    System.out.println(robot.getPrint.trim());
                    robot.getPrint = "";
                }

                dout.writeBoolean(cont);

                dout.flush();


            } catch (IOException ex) {
                System.out.println("Shutting down single client server");
            }
        }


    }


}