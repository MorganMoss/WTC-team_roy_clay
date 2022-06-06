package za.co.wethinkcode.robotworlds;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import org.json.*;
import za.co.wethinkcode.robotworlds.maze.EmptyMaze;
import za.co.wethinkcode.robotworlds.world.AbstractWorld;
import za.co.wethinkcode.robotworlds.world.IWorld;

public class MainServerThread implements Runnable {

    public static final int PORT = 3333;

    DataInputStream din;
    DataOutputStream dout;
    Robot robot;
    IWorld world;
    String in;



    private final String clientMachine;
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
    void setposition (Robot robot ) throws IOException {
        int bry;
        int brx;
        int tly;
        int tlx;
        tlx = Integer.parseInt(getInput("Input top left X"));
        tly = Integer.parseInt(getInput("Input top left Y"));
        brx = Integer.parseInt(getInput("Input bottom right X"));
        bry = Integer.parseInt(getInput("Input bottom right Y"));

        IWorld world = robot.getWorld();
        world.SetPositions(tlx, tly, brx, bry);
    }



    public MainServerThread(Socket socket) throws IOException {
        clientMachine = socket.getInetAddress().getHostName();
        System.out.println("Connection from " + clientMachine);
        System.out.println("Waiting for client...");
        din=new DataInputStream(socket.getInputStream());
        dout=new DataOutputStream(socket.getOutputStream());

    }

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

                world = new AbstractWorld(new EmptyMaze());
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