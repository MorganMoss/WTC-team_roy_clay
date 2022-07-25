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
            return "";
        }
    }

    public static void print(Object s) {
        System.out.print(s);
        System.out.flush();
    }

    /**
     * Opens up connections and server input.
     */
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

    /**
     * Kills a robot in the world.
     * @param robot to be PURGED!!!
     */
    public static void purge(String robot) {
        World.removeRobot(robot);
        println("Purged " + robot);
    }



    /**
     * Handles the commands for the serverside,
     * the commands are terminal based.
     */
    private static class ServerCommandHandler extends Thread{
        private boolean noArgs(String command){
            return command.split(" ").length < 2;
        }

        private void quit(){
            Server.running = false;
        }

        private void printRobots(){
            println(World.getRobots().toString());
        }

        private void purge(String command){
            if (noArgs(command)) {
                println("Please enter the name of the robot to be purged");
                return;
            }

            String robot = command.split(" ")[1];
            purge(robot);
        }

        private void save(String command){
            if (noArgs(command)) {
                println("Please enter the name of the saved world");
                return;
            }

            DatabaseManager.save(command.split(" ")[1]);
        }

        private void load(String command){
            if (noArgs(command)) {
                println("Please enter the name of the saved world");
                return;
            }

            DatabaseManager.load(command.split(" ")[1]);
        }

        /**
         * Takes a command string and executes a command corresponding to the command given
         * @param command given by user
         */
        private void handleCommand(String command) {
            switch (command.split(" ")[0].toLowerCase()) {
                case "exit":
                case "quit":
                    quit();
                    break;
                case "dump":
                    World.dump();
                    break;
                case "robots":
                    printRobots();
                    break;
                case "purge":
                    purge(command);
                    break;
                case "save":
                    save(command);
                    break;
                case "load":
                    load(command);
                    break;
                default:
                    println("Invalid command");
            }
        }

        /**
         * Handles the input coming in to the server from commandline
         */
        @Override
        public void run() {
            while (running) {
                handleCommand(getInput());
            }

            println("Server closing...");
            System.exit(0);
        }
    }
}

