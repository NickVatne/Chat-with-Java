package chatModules;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final String host = "localhost";
    private static final int portNumber = 3434;

    private String chatUsername;
    private String chatServerHost;
    private int serverPort;
    private Scanner InputScanner;

    public static void main(String[] args) {
        String name = "";
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter you desired username: ");
        while (name.equals("") || name.trim().equals("")) {
            name = scan.nextLine();
            if (name.trim().equals("")) {
                System.out.println("Invalid name, please enter another one");
            }
        }
        Client client = new Client(name, host, portNumber);
        client.startClient(scan);
    }

    private Client(String chatUsername, String host, int portNumber) {
        this.chatUsername = chatUsername;
        this.chatServerHost = host;
        this.serverPort = portNumber;
    }

    private void startClient(Scanner scan) {
        try {
            Socket socket = new Socket(chatServerHost, serverPort);
            Thread.sleep(1000);

            CrossHandling crossHandling = new CrossHandling(socket, chatUsername);
            Thread serverAccess = new Thread(crossHandling);
            serverAccess.start();
            while (serverAccess.isAlive()) {
                if (scan.hasNextLine()) {
                    if (scan.hasNextLine()) {
                        crossHandling.addMoreMessage(scan.nextLine());
                    }
                }
            }
        } catch (InterruptedException | IOException ie) {
            ie.printStackTrace();
        }

    }

}
