package za.co.wethinkcode.client;

import za.co.wethinkcode.Request;
import za.co.wethinkcode.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TODO: leave the client alone until we need to use it.
public class Play {
    static Scanner scanner = new Scanner(System.in);;

    public static void main(String[] args) throws Exception {
        //create socket
        Socket s = new Socket("localhost", 5000);

        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());

        String in;
        String name;

        do {
             name = getInput("What do you want to name your robot?");

            dout.writeUTF(new Request(name, "Launch", new ArrayList<>()).serialize());
            dout.flush();
            in = din.readUTF();
            System.out.println();
        } while (Response.deSerialize(in).getResult().equals("OK"));

        do {
            String[] instruction = getInput(name + "> What must I do next?").strip().toLowerCase().split(" ", 1);


            String command = instruction[0];
            String arguments = instruction[1];

            if (command.equals("quit")) {
                break;
            }

            Request request = new Request(name, command, List.of(arguments.split(" ")));

            dout.writeUTF(request.serialize());
            dout.flush();



            in = din.readUTF();

            System.out.println(in);
        } while (true);

        dout.close();
        s.close();
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