package za.co.wethinkcode;

import org.json.JSONObject;
import za.co.wethinkcode.robotworlds.maze.*;
import za.co.wethinkcode.robotworlds.world.*;
import za.co.wethinkcode.robotworlds.world.AbstractWorld;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;

//TODO: leave the client alone until we need to use it.
public class Play {
    static Scanner scanner;
    

    public static void main(String[] args) throws Exception {
        int count = 0;
        scanner = new Scanner(System.in);
        //create socket
        Socket s=new Socket("localhost",3333);

        DataInputStream din=new DataInputStream(s.getInputStream());
        DataOutputStream dout=new DataOutputStream(s.getOutputStream());

        //set robot
        String name = getInput("What do you want to name your robot?");
        System.out.println("Hello Kiddo!");
        AbstractWorld world = worldSelector(args);
        Robot robot= new Robot(name,world);

        //creating and sending json with starting info for robot

        JSONObject json = new JSONObject();
        json.put("robot",robot);
        json.put("name",name);
        json.put("world",world);
        while (count ==0){
            count++;
            dout.writeUTF(json.toString());
            dout.flush();
        }




        boolean shouldContinue = true;
        do {
            String instruction = getInput(robot.getName() + "> What must I do next?").strip().toLowerCase();
            json.put("command",instruction);
            try {

                Command command = Command.create(instruction);
                shouldContinue=robot.handleCommand(command);



                dout.writeUTF(json.toString());
                dout.flush();

                String in;

                shouldContinue = din.readBoolean();
                System.out.println("doing thing");
//                System.out.println(shouldContinue);



            } catch (IllegalArgumentException e) {
                robot.setStatus("Sorry, I did not understand '" + instruction + "'.");
            }
            System.out.println(robot);
        } while (shouldContinue);
        dout.close();
        s.close();

    }




    public static String getInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(prompt);
        String input = scanner.nextLine();
        while (input.isBlank()) {
            System.out.println(prompt);
            input = scanner.nextLine();
        }
        return input;
    }

    public static AbstractWorld worldSelector(String []args){
        AbstractWorld world = null;

        if (args.length == 0 || args[0].equalsIgnoreCase("text"))
        {
            if (args.length == 0 || args.length==1 && args[0].equalsIgnoreCase("text") || args[1].equalsIgnoreCase("emptymaze") )
            {
                System.out.println("Loaded EmptyMaze.");
                 world = new TextWorld(new EmptyMaze());

            }
            else if(args[1].equalsIgnoreCase("simplemaze"))
            {
                System.out.println("Loaded SimpleMaze.");
                world = new TextWorld(new SimpleMaze());

            }
            else if(args[1].equalsIgnoreCase("randommaze"))
            {
                System.out.println("Loaded RandomMaze.");
                 world = new TextWorld(new RandomMaze());
            }}else if (args[0].equalsIgnoreCase("turtle"))
        {
            if ( args.length==1 && args[0].equalsIgnoreCase("turtle") ||args[1].equalsIgnoreCase("randommaze"))
            {
                System.out.println("Loaded RandomMaze.");
                 world = new TurtleWorld(new RandomMaze());

            }
            else if(args[1].equalsIgnoreCase("simplemaze"))
            {
                System.out.println("Loaded SimpleMaze.");
                 world = new TurtleWorld(new SimpleMaze());

            }
            else if(args[1].equalsIgnoreCase("emptymaze"))
            {
                System.out.println("Loaded EmptyMaze.");
                 world = new TurtleWorld(new EmptyMaze());

            }
        }
        return world;
    }
}
