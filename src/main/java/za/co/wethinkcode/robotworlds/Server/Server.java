package za.co.wethinkcode.robotworlds.Server;

import za.co.wethinkcode.robotworlds.Command;
import za.co.wethinkcode.robotworlds.Robot;
import za.co.wethinkcode.robotworlds.world.AbstractWorld;

import java.net.*;
import java.io.*;
import java.util.Scanner;
import org.json.JSONObject;

import static za.co.wethinkcode.robotworlds.Play.getInput;
import static za.co.wethinkcode.robotworlds.Play.worldSelector;

public class Server implements Runnable{

    public static final int PORT = 3333;
    private static Robot robot;
    private final BufferedReader in;
    private final PrintStream out;
    private final String clientMachine;


    public static void main(String args[])throws Exception{
        Command command;

        ServerSocket ss=new ServerSocket(3333);
        Socket s=ss.accept();
        DataInputStream din=new DataInputStream(s.getInputStream());
        DataOutputStream dout=new DataOutputStream(s.getOutputStream());
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));



        String str="",str2="";
        Scanner scanner = new Scanner(System.in);




        while(!str2.equals("false")){
            String instruction=din.readUTF();
            command = Command.create(instruction);
            Boolean shouldContinue = robot.handleCommand(command);
            str2=br.readLine();
            dout.writeUTF(String.valueOf(shouldContinue));
            dout.flush();
        }
        din.close();
        s.close();
        ss.close();
    }

    public Server(Socket socket) throws IOException {
        clientMachine = socket.getInetAddress().getHostName();
        System.out.println("Connection from " + clientMachine);

        out = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        System.out.println("Waiting for client...");
    }

    @Override
    public void run() {
        try {
            String messageFromClient;
            while((messageFromClient = in.readLine()) != null) {
                System.out.println("Message \"" + messageFromClient + "\" from " + clientMachine);
                out.println("Thanks for this message: "+messageFromClient);
            }
        } catch(IOException ex) {
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