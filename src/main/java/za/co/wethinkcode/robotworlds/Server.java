package za.co.wethinkcode.robotworlds;

import za.co.wethinkcode.robotworlds.world.AbstractWorld;

import java.net.*;
import java.io.*;
import java.util.Scanner;

import static za.co.wethinkcode.robotworlds.Play.getInput;
import static za.co.wethinkcode.robotworlds.Play.worldSelector;

class Server{


    public static void main(String args[])throws Exception{
        Command command;

        ServerSocket ss=new ServerSocket(3333);
        Socket s=ss.accept();
        DataInputStream din=new DataInputStream(s.getInputStream());
        DataOutputStream dout=new DataOutputStream(s.getOutputStream());
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

        String str="",str2="";
        Scanner scanner = new Scanner(System.in);
        String name = getInput("What do you want to name your robot?");
        System.out.println("Hello Kiddo!");
        AbstractWorld world = worldSelector(args);

        Robot robot= new Robot(name,world);
        System.out.println(robot.toString());

        while(!str2.equals("false")){
            String instruction=din.readUTF();
            command = Command.create(instruction);
            Boolean shouldContinue = robot.handleCommand(command);
//            System.out.println("client says: "+str);
            str2=br.readLine();
            dout.writeUTF(str2);
            dout.flush();
        }
        din.close();
        s.close();
        ss.close();
    }
}