package chatServers;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * Made by Nicolai V.
 * Date: 19.08.2019
 */

public class ChatServer {
    private static final int portNumber = 3434;
    private int serverPort;
    protected static List<ClientUtils> clients;

    public static void main(String[] args) {
        ChatServer server = new ChatServer(portNumber);
        server.startServer();
    }

    private void startServer() {
        clients = new ArrayList<ClientUtils>();
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
            acceptClients(serverSocket);
        } catch (IOException e) {
            System.err.println("Could not listen to" + serverPort);
            System.exit(1);
        }
    }

    public ChatServer(int portNumber) {
        this.serverPort = portNumber;
    }

    public List<ClientUtils> getClients() {
        return clients;
    }
    public void acceptClients(ServerSocket serverSocket){
        System.out.println("Server is now listening on port" + serverSocket.getLocalSocketAddress());
        while (true){
            try {
                Socket socket = serverSocket.accept();
                System.out.println("Accepting : " + socket.getRemoteSocketAddress());
                ClientUtils client = new ClientUtils(this, socket);
                Thread thread = new Thread(client);
                thread.start();
                clients.add(client);
            } catch (IOException io){
                System.out.println("We missed to accept" + serverPort);
            }
        }
    }

}
