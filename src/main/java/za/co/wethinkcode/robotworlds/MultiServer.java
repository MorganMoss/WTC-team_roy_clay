package za.co.wethinkcode.robotworlds;

import org.json.JSONObject;
import za.co.wethinkcode.robotworlds.world.IWorld;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class MultiServer {


    public static void main(String[] args) throws ClassNotFoundException, IOException {

        ServerSocket s = new ServerSocket( Server.PORT);
        System.out.println("Server running & waiting for client connections.");
        Scanner scanner = new Scanner(System.in);

        while(true) {
            try {
                Socket socket = s.accept();
                System.out.println("Connection: " + socket);
                Runnable r = new Server(socket);
                Thread task = new Thread(r);
                task.start();


                JSONObject out = null;
                int bly;
                int blx;
                int tly;
                int tlx;
                out = new JSONObject(scanner.nextLine());
                System.out.println("Input top left X");
                tlx = Integer.parseInt(scanner.nextLine());
                System.out.println("Input top left Y");
                tly = Integer.parseInt(scanner.nextLine());
                System.out.println("Input Bottom left X");
                blx = Integer.parseInt(scanner.nextLine());
                System.out.println("Input Bottom left Y");
                bly = Integer.parseInt(scanner.nextLine());

                Robot robot = (Robot) out.get("Robot");
                IWorld world = robot.getWorld();

                world.SetPositions(tlx, tly, blx, bly);


            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}