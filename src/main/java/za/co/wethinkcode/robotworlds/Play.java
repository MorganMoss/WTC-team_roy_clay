package za.co.wethinkcode.robotworlds;

import za.co.wethinkcode.robotworlds.maze.*;
import za.co.wethinkcode.robotworlds.world.*;
import za.co.wethinkcode.robotworlds.world.AbstractWorld;

import java.util.Scanner;


public class Play {
    static Scanner scanner;

    public static void main(String[] args) {

        scanner = new Scanner(System.in);

        String name = getInput("What do you want to name your robot?");
        System.out.println("Hello Kiddo!");
        AbstractWorld world = worldSelector(args);
        Robot robot= new Robot(name,world);

        System.out.println(robot.toString());

        Command command;
        boolean shouldContinue = true;
        do {
            String instruction = getInput(robot.getName() + "> What must I do next?").strip().toLowerCase();
            try {
                command = Command.create(instruction);
                shouldContinue = robot.handleCommand(command);

                if (instruction.split(" ")[0].equalsIgnoreCase( "replay")
                        ||instruction.split(" ")[0].equalsIgnoreCase( "mazerun")){
                }else {
                    robot.appendToHistory(instruction);
                }
                if (command.getName()== "sprint"){
                    System.out.println(robot.getPrint.trim());
                    robot.getPrint = "";
                }
                else if (command.getName()== "replay" || command.getName()=="mazerun")
                {
                    System.out.println(robot.getPrint.trim());
//                    robot.getPrint = "";
                }
            } catch (IllegalArgumentException e) {
                robot.setStatus("Sorry, I did not understand '" + instruction + "'.");
            }
            System.out.println(robot);
        } while (shouldContinue);
    }


    private static String getInput(String prompt) {
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
