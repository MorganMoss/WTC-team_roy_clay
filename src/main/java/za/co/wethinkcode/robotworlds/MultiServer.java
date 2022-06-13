package za.co.wethinkcode.robotworlds;


import java.net.*;
import java.io.*;
// TODO: Move all serverside classes to a separate package.
// This needs more functionality, and should have ownership of the world
public class MultiServer {
    public static void main(String[] args) throws IOException {
        //TODO: Should handle this exception
        ServerSocket s = new ServerSocket( MainServerThread.PORT);
        System.out.println("MainServerThread running & waiting for client connections.");

        while(true) {
            try {
                Socket socket = s.accept();
                System.out.println("Connection: " + socket);
                Runnable r = new MainServerThread(socket);
                Thread task = new Thread(r);
                task.start();

            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}