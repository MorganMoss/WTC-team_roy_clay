package za.co.wethinkcode.robotworlds;

import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONStringer;
import za.co.wethinkcode.robotworlds.maze.*;
import za.co.wethinkcode.robotworlds.world.*;
import za.co.wethinkcode.robotworlds.world.AbstractWorld;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Play {
    static Scanner scanner;


    public static void main(String[] args) throws Exception {

        Socket s=new Socket("localhost",3333);
        DataInputStream din=new DataInputStream(s.getInputStream());
        DataOutputStream dout=new DataOutputStream(s.getOutputStream());
        JSONObject json = new JSONObject();



        scanner = new Scanner(System.in);
        String name = getInput("What do you want to name your robot?");
        System.out.println("Hello Kiddo!");
        AbstractWorld world = worldSelector(args);
        Robot robot = new Robot(name,world);
        System.out.println(robot.toString());

        json.put("name",name);
        json.put("world",world);

        dout.writeUTF(String.valueOf(json));
        Command command;

        boolean shouldContinue = true;
        do {
            String instruction = getInput(robot.getName() + "> What must I do next?").strip().toLowerCase();
            String str="";
            Boolean str2=true;
            try {

                dout.writeUTF(instruction);
                dout.flush();
                str2= Boolean.valueOf(din.readUTF());


//                System.out.println("Server says: ");
                command = Command.create(instruction);
                shouldContinue = str2.booleanValue();

                if (instruction.split(" ")[0].equalsIgnoreCase( "replay")
                        ||instruction.split(" ")[0].equalsIgnoreCase( "mazerun")){
                }else {
                    robot.appendToHistory(instruction);
                }
                if (command.getName()== "sprint"){
                    System.out.println(robot.getPrint.trim());
                    robot.getPrint = "";
                }
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

    public static AbstractWorld worldSelector(String[] args){
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
