package za.co.wethinkcode.server;

import static za.co.wethinkcode.server.Configuration.*;

import za.co.wethinkcode.Request;
import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.Handler;
import za.co.wethinkcode.server.handler.world.World;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    protected static volatile boolean running = true;

    public static boolean isRunning(){
        return running;
    }

    public static void main(String[] args) {
        Configuration.setConfiguration(args);
        System.out.println("**** Initialising the Robot World");
        Handler.setup();
        startRobotWorldServer();
    }

    private static void startRobotWorldServer(){
        Thread serverCommandHandler = new ServerCommandHandler();
        serverCommandHandler.start();

        try (
            ServerSocket s = new ServerSocket(port())
        ) {

            System.out.println("Server is running & waiting for client connections.");

            while(running) {
                try {
                    Socket socket = s.accept();
                    ClientCommunicator serverClientCommunicator = new ClientCommunicator(socket);

                } catch(IOException clientFailedToConnect) {
                    System.out.println("Failed to connect a client.");
                }
            }

        } catch(IOException serverFailedToOpen){
            System.out.println("Failed to start the server.");
            System.exit(1);
        }
    }

    public static void purge(String robot) {
        World.removeRobot(robot);
        System.out.println("Purged " + robot);
    }

    /**
     * Handles the commands for the serverside,
     * the commands are terminal based.
     */
    private static class ServerCommandHandler extends Thread{
        private static final Scanner in = new Scanner(System.in);

        //TODO:
        // Add methods for the implementation of the
        // other commands as methods within this class

        /**
         * Handles the input coming in to the server from commandline
         */
        @Override
        public void run() {
            String command = "";
            while (running) {
                try {
                    command = in.nextLine();
                } catch (NoSuchElementException ignored){
                    continue;
                }

                switch (command.split(" ")[0].toLowerCase()) {
                    case "exit":
                    case "quit":
                        Server.running = false;
                        break;
                    case "dump":
                        //TODO:
                        // Iterate through the worlds map
                        break;
                    case "robots":
                        //TODO:
                        // Iterate through the worlds robot list
                        break;
                    case "purge":
                        String robot = command.split("")[1];
                        //TODO:
                        // kill that robot
                        // Could be done by injecting
                        // an exit request for that robot
                        break;
                    default:
                        System.out.println("Invalid command");
                }
            }
            System.out.println("Server closing...");
            //TODO:
            // NotifyClients()
            System.exit(0);
        }
    }

//    private static class RequestResponseHandler
}

