
package za.co.wethinkcode.server;


import picocli.CommandLine;
import za.co.wethinkcode.Request;
import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.world.AbstractWorld;
import za.co.wethinkcode.server.handler.world.World;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.PriorityQueue;
import java.util.Queue;


// This has the same functionality as multiserver,
// but does not work for more than 1 client.
// TODO: Complete world implementation and server receiving connections


public class Server {

    private AbstractWorld world;
    private static Queue<Request> requests = new PriorityQueue<Request>();

    public static void main(String[] args) {
        int exitCode = (new CommandLine((World.getInstance()))).execute(args);

        System.out.println("**** Initialising the Robot World");
        try {
            startRobotWorldServer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.exit(exitCode);
    }

    
    public static void addRequest(Request request){
        requests.add(request);
    }
    public static Response getResponse(String robot) {
        return null;
    }

    private static void startRobotWorldServer() throws IOException{
        //TODO: Should handle this exception and improve code below
        ServerSocket s = new ServerSocket(World.getPort());
        System.out.println("MainServerThread running & waiting for client connections.");

        while(true) {
            try {
                Socket socket = s.accept();
                Runnable runnable = new ServerClientCommunicator(socket);
                Thread task = new Thread(runnable);
                task.start();
            } catch(IOException clientFailedToConnect) {
                System.out.println("Failed to connect a client.");
            }
        }
    }
//
//    public static final int PORT = 3333;
//    private static Robot robot;
//    private final BufferedReader in;
//    private final PrintStream out;
//    private final String clientMachine;
//
//
//    public static void main(String args[])throws Exception{
//        Command command;
//
//        ServerSocket ss=new ServerSocket(3333);
//        Socket s=ss.accept();
//        DataInputStream din=new DataInputStream(s.getInputStream());
//        DataOutputStream dout=new DataOutputStream(s.getOutputStream());
//        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
//
//
//
//        String str="",str2="";
//        Scanner scanner = new Scanner(System.in);
//
//
//
//
//        while(!str2.equals("false")){
//            String instruction=din.readUTF();
//            command = Command.create(instruction);
//            Boolean shouldContinue = robot.handleCommand(command);
//            str2=br.readLine();
//            dout.writeUTF(String.valueOf(shouldContinue));
//            dout.flush();
//        }
//        din.close();
//        s.close();
//        ss.close();
//    }
//
//    public Server(Socket socket) throws IOException {
//        clientMachine = socket.getInetAddress().getHostName();
//        System.out.println("Connection from " + clientMachine);
//
//        out = new PrintStream(socket.getOutputStream());
//        in = new BufferedReader(new InputStreamReader(
//                socket.getInputStream()));
//        System.out.println("Waiting for client...");
//    }
//
//    @Override
//    public void run() {
//        try {
//            String messageFromClient;
//            while((messageFromClient = in.readLine()) != null) {
//                System.out.println("Message \"" + messageFromClient + "\" from " + clientMachine);
//                out.println("Thanks for this message: "+messageFromClient);
//            }
//        } catch(IOException ex) {
//            System.out.println("Shutting down single client server");
//        } finally {
//            closeQuietly();
//        }
//    }
//
//    private void closeQuietly() {
//        try { in.close(); out.close();
//        } catch(IOException ex) {}
//    }
}