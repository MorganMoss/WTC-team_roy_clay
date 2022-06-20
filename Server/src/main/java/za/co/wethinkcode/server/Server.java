
package za.co.wethinkcode.server;


import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import za.co.wethinkcode.Request;
import za.co.wethinkcode.Response;
import za.co.wethinkcode.server.handler.world.AbstractWorld;
import za.co.wethinkcode.server.handler.world.World;
import za.co.wethinkcode.server.handler.world.map.EmptyMap;

import java.net.*;
import java.io.*;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Callable;


// This has the same functionality as multiserver,
// but does not work for more than 1 client.
// TODO: Complete world implementation and server receiving connections

@Command(
        name = "robots-world",
        mixinStandardHelpOptions = true,
        version = {"robots 1.0.0"},
        description = {"Starts the Robot World server"}
)
public class Server implements Callable<Integer> {

    public static void main(String[] args) {
        int exitCode = (new CommandLine((new Server()))).execute(args);
        System.exit(exitCode);
    }

    @Option(
            names = {"-s", "--size"},
            description = {"Size of the world as one side of a square grid"}
    )
    private int size = 1;

    @Option(
            names = {"-p", "--port"},
            description = {"Port to listen for client connections"}
    )
    private int port = 5000;

    @Option(
            names = {"-o", "--obstacle"},
            description = {"Position of fixed obstacle as [x,y] coordinate in form 'x,y', or 'none' or 'random'"}
    )
    private String obstacle = "none";

    @Option(
            names = {"-pt", "--pit"},
            description = {"Position of fixed pit as [x,y] coordinate in form 'x,y', or 'none' or 'random'"}
    )
    private String pits = "none";

    @Option(
            names = {"-m", "--mine"},
            description = {"Position of fixed mine as [x,y] coordinate in form 'x,y', or 'none' or 'random'"}
    )
    private String mines = "none";

    @Option(
            names = {"-v", "--visibility"},
            description = {"Visibility for robot in nr of steps"}
    )
    private int visibility = 10;

    @Option(
            names = {"-r", "--repair"},
            description = {"Duration for robot shield to repair"}
    )
    private int repair = 5;

    @Option(
            names = {"-l" , "--reload"},
            description = {"Instruct the robot to reload its weapons"}
    )
    private int reload = 7;

    @Option(
            names = {"-ht", "--hit"},
            description = {"Maximum strength for robot shield"}
    )
    private int hit = 3;
    private AbstractWorld world;

    private static Queue<Request> requests = new PriorityQueue<Request>();
    
    public static void addRequest(Request request){
        requests.add(request);
    }
    public static Response getResponse(String robot) {
        return null;
    }
    

    private void startRobotWorldServer() throws IOException{
        //TODO: Should handle this exception and improve code below
        ServerSocket s = new ServerSocket(port);
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

    private void creatingWorldConfig(){
        System.out.println("Creating World with the following configurations.. " +
                "\n[size: " + this.size + " x " + this.size +
                "\n, obstacles: " + this.obstacle +
                "\n, pits: " + this.pits +
                "\n, mines: " + this.mines +
                "\n, visibility: " + this.visibility +
                "\n, repair: " + this.repair +
                "\n, reload: " + this.reload +
                "\n, hit: " + this.hit + "]");
    }


    /**
     * build command arguments, initialise and run server
     * @return terminating state for the server
     * @throws Exception handle any connection or invalid command arguments
     */
    @Override
    public Integer call() throws Exception {
        //TODO: build command options here
        creatingWorldConfig();

        //Create the world based on config or arguments values
        this.world = new World(new EmptyMap());


        System.out.println("**** Initialising the Robot World");
        this.startRobotWorldServer();
        return 0;
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