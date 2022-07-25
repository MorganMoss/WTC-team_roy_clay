package za.co.wethinkcode.server;

import static za.co.wethinkcode.server.ClientCommunicator.openCommunication;
import static za.co.wethinkcode.server.configuration.Configuration.port;

import za.co.wethinkcode.server.configuration.Configuration;
import za.co.wethinkcode.server.configuration.DatabaseManager;
import za.co.wethinkcode.server.handler.Handler;
import za.co.wethinkcode.server.handler.world.World;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    protected static volatile boolean running = true;
    private static Scanner input = new Scanner(System.in);
    public static void main(String[] args) {
        Configuration.setConfiguration(args);
        println("**** Initialising the Robot World");
        World.reset();
        Handler.setup();
        startRobotWorldServer();
    }

    public static void println(String s){
        Server.print(s+"\n");
    }

    public static String getInput(){
        try {
            return input.nextLine();
        } catch (NoSuchElementException e){
            return null;
        }
    }

    public static void print(Object s) {
        System.out.print(s);
        System.out.flush();
    }

    private static void startRobotWorldServer(){
        Thread serverCommandHandler = new ServerCommandHandler();
        serverCommandHandler.setName("Server Input Thread");
        serverCommandHandler.start();

        try (
            ServerSocket serverSocket = new ServerSocket(port())
        ) {

            println("Server is running & waiting for client connections.");

            while(running) {
                try {
                    Socket socket = serverSocket.accept();
                    openCommunication(socket);
                } catch(IOException clientFailedToConnect) {
                    println("Failed to connect a client.");
                }
                Thread.sleep(100);
            }

        } catch(IOException serverFailedToOpen){
            println("Failed to start the server.");
            System.exit(1);
        } catch (InterruptedException interrupted) {
            println("Server Interrupted. " + interrupted);
            System.exit(1);
        }
    }

    public static void purge(String robot) {
        World.removeRobot(robot);
        println("Purged " + robot);
    }



    /**
     * Handles the commands for the serverside,
     * the commands are terminal based.
     */
    private static class ServerCommandHandler extends Thread{
        /**
         * Handles the input coming in to the server from commandline
         */
        @Override
        public void run() {
            String command;
            while (running) {
                command = getInput();
                if (command == null){

                    continue;
                }
                switch (command.split(" ")[0].toLowerCase()) {
                    case "exit":
                    case "quit":
                        Server.running = false;
                        break;
                    case "dump":
                        World.dump();
                        break;
                    case "robots":
                        println(World.getRobots().toString());
                        break;
                    case "purge":
                        if (command.split(" ").length < 2) {
                            println("Please enter the name of the robot to be purged");
                            break;
                        }
                        String robot = command.split(" ")[1];
                        purge(robot);
                        break;
                    case "save":
                        if (command.split(" ").length < 2) {
                            println("Please enter the name of the saved world");
                            break;
                        }
                        DatabaseManager.save(command.split(" ")[1]);
                        break;
                    case "load":
                        if (command.split(" ").length < 2) {
                            println("Please enter the name of the saved world");
                            break;
                        }
                        DatabaseManager.load(command.split(" ")[1]);
                        break;
                    default:
                        println("Invalid command");
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            println("Server closing...");
            System.exit(0);
        }
    }
}

