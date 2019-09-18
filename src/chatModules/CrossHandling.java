package chatModules;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

class CrossHandling implements Runnable {
    private Socket socket;
    private String name;
    private boolean isAlive;
    private final LinkedList<String> toDoMessages;
    private boolean gotMessages = false;

    CrossHandling(Socket socket, String name){
        this.socket = socket;
        this.name = name;
        toDoMessages = new LinkedList<String>();
    }
    public void addMoreMessage(String message){
        synchronized (toDoMessages){
            gotMessages = true;
            toDoMessages.push(message);
        }
    }
    @Override
    public void run() {
        System.out.println("Welcome" + name);
        System.out.println("Local Port on :" + socket.getLocalPort());
        System.out.println("Server connected at " + socket.getRemoteSocketAddress() + ":" + socket.getPort());

        try {
            PrintWriter servOut = new PrintWriter(socket.getOutputStream(), false);
            InputStream serverIn = socket.getInputStream();
            Scanner servedIn = new Scanner(serverIn);

            while(!socket.isClosed()){
                if (serverIn.available() > 0 ){
                    if (servedIn.hasNextLine()){
                        System.out.println(servedIn.nextLine());
                    }
                }
                if (gotMessages){
                    // Empty the line in front
                    String nextmsg = "";
                    synchronized (toDoMessages){
                        nextmsg =  toDoMessages.pop();
                        gotMessages = !toDoMessages.isEmpty();
                    }
                    servOut.println(name + " > " + nextmsg);
                    servOut.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
