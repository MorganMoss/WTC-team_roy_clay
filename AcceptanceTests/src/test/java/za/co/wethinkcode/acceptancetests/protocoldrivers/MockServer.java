package za.co.wethinkcode.acceptancetests.protocoldrivers;

import sun.misc.Signal;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MockServer {
    private static final String version = "1.0.1-SNAPSHOT";
    private static final String rootPath = (new File(System.getProperty("user.dir"))).getParent();
    private static final String serverJarPath = rootPath + "/libs/Server-"+version+".jar";
    private static final String command = "java -jar " + serverJarPath;

    private static String mockedInput = "";
    private static Process server;

    public static void startServer(String arg){
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", command + " " + arg);

        pb.redirectError(ProcessBuilder.Redirect.INHERIT);

        Signal.handle(new Signal("INT"),  // SIGINT
                signal -> {
            closeServer();
                    System.out.println("CLOSED SERVER UNEXPECTEDLY");
        });

        try {
            server = pb.start();
            if (!mockedInput.equals("")){
                String[] lines = mockedInput.split("\n");

                for (String line : lines){
                    injectInput(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//
//        try {
//            Thread.sleep(100);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static String waitForServerSetup(){
        Scanner scanner = new Scanner(server.getInputStream());
        StringBuilder result = new StringBuilder();
        String line;

        while (!(line = scanner.nextLine()).equals("Server is running & waiting for client connections.")){
            result.append(line).append("\n");
        }

        return result.toString();
    }

    public static void startServer(String arg, String input){
        mockedInput = input;
        startServer(arg);
    }

    public static String result(){
        injectInput("quit");
        String result = read();
        closeServer();
        return result;
    }

    public static void closeServer(){
        server.destroy();
        while (server.isAlive()) {
//            server.destroyForcibly();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String read() {
        InputStreamReader in = new InputStreamReader(server.getInputStream());
        StringBuilder result = new StringBuilder();
        try {
            while (true) {
                int i = in.read();

                if (i == -1) {
                    break;
                }
                result.append((char) ((byte) i));
            }
            return result.toString();
        } catch (IOException e) {
            System.out.println(e);
            return result.toString();
        }
    }

    public static void injectInput(String s){
        try {
            server.getOutputStream().write((s+"\n").getBytes(StandardCharsets.UTF_8));
            server.getOutputStream().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
