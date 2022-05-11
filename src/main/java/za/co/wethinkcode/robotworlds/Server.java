package za.co.wethinkcode.robotworlds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import org.json.*;
import za.co.wethinkcode.robotworlds.world.IWorld;

public class Server implements Runnable {

    public static final int PORT = 3333;
    private final BufferedReader in;
    private final PrintStream out;
    private final String clientMachine;



    public Server(Socket socket) throws IOException {
        clientMachine = socket.getInetAddress().getHostName();
        System.out.println("Connection from " + clientMachine);

        out = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        System.out.println("Waiting for client...");
    }

    public void run() {

        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        try {
            String messageFromClient;
            while ((messageFromClient = in.readLine()) != null) {
                if (messageFromClient == "off") {
                    closeQuietly();
                }
                if (input.equalsIgnoreCase("quit")) {
                    closeQuietly();
                    break;
                }
                System.out.println("Message \"" + messageFromClient + "\" from " + clientMachine);
            }
        } catch (IOException ex) {
            System.out.println("Shutting down single client server");
        } finally {
            closeQuietly();
        }


    }

    private void closeQuietly() {
        try { in.close(); out.close();
        } catch(IOException ex) {}
    }
}