
package za.co.wethinkcode.server;


import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.net.*;
import java.io.*;
import java.util.concurrent.Callable;


// This has the same functionality as multiserver,
// but does not work for more than 1 client.
// TODO: Consider deletion

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



    private void startRobotWorldServer() throws IOException{
        //TODO: Should handle this exception and improve code below
        ServerSocket s = new ServerSocket( ServerClientCommunicator.PORT);
        System.out.println("MainServerThread running & waiting for client connections.");

        while(true) {
            try {
                Socket socket = s.accept();
                System.out.println("Connection: " + socket);
                Runnable r = new ServerClientCommunicator(socket);
                Thread task = new Thread(r);
                task.start();

            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * build command arguments, initialise and run server
     * @return terminating state for the server
     * @throws Exception handle any connection or invalid command arguments
     */
    @Override
    public Integer call() throws Exception {
        //TODO: build command options here

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