package chatServers;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientUtils implements Runnable {
    private Socket socket;
    private PrintWriter clientO;
    private ChatServer server;

    ClientUtils(ChatServer server, Socket socket){
        this.server = server;
        this.socket = socket;
    }
    private PrintWriter getWriter(){
        return clientO;
    }
    @Override
    public void run(){
        try {
            this.clientO = new PrintWriter(socket.getOutputStream(), false);
            Scanner in = new Scanner(socket.getInputStream());

            while (!socket.isClosed()){
                if (in.hasNextLine()){
                    String input = in.nextLine();
                    System.out.println(input);
                    for (ClientUtils clientCount : server.getClients()){
                        PrintWriter clientCountIt = clientCount.getWriter();
                        if (clientCountIt != null){
                            clientCountIt.write(input + "\r\n");
                            clientCountIt.flush();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
