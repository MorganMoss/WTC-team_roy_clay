package za.co.wethinkcode.client;

import za.co.wethinkcode.Request;
import za.co.wethinkcode.Response;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


//TODO: leave the client alone until we need to use it.
public class Play {
    private static BufferedReader requestIn;

    private static PrintStream responseOut;

    static Scanner scanner = new Scanner(System.in);;

    public static void main(String[] args) throws Exception {
        //create socket
        Socket socket = new Socket("localhost", 5000);

        requestIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        responseOut = new PrintStream(socket.getOutputStream());

        String in;
        String name;

//        do {
         name = getInput("What do you want to name your robot?");
//            dout.writeUTF();
//            dout.flush();
//            in = din.readUTF();
//            System.out.println();
//        } while (Response.deSerialize(in).getResult().equals("OK"));

        do {
            String[] instruction = getInput(name + "> What must I do next?").strip().toLowerCase().split(" ", 2);

            String arguments = "";
            String command = instruction[0];
            if (instruction.length == 2){
                arguments = instruction[1];
            }

            if (command.equals("quit")) {
                break;
            }

            List<String> arg =  List.of(arguments.split(" "));

            if (arguments.equals("")){
                arg = new ArrayList<>();
            }

            System.out.println(command);
            System.out.println(arg);

            Request request = new Request(name, command, arg);

            responseOut.println(request.serialize());
            responseOut.flush();



            in = requestIn.readLine();

            System.out.println(in);
        } while (true);

        requestIn.close();
        responseOut.close();
    }

    public static String getInput(String prompt) {
        System.out.println(prompt);

        String input = scanner.nextLine();

        while (input.isBlank()) {
            System.out.println(prompt);
            input = scanner.nextLine();
        }

        return input;
    }
}