package Engine;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class WorkerThread implements Runnable {
    private Socket clientSocket = null;
    private Queue<String> msgsToBeSent;
    private UserInterface model;

    private String clientName = "";



    public WorkerThread(Socket clientSocket, String clientName) {
        this.clientName = clientName;
        this.clientSocket = clientSocket;
        this.msgsToBeSent = new LinkedList<>();
        model = new UserInterface(this);
    }


    /**
     * Send a message to the client, while the messages to send queue is not empty
     * @throws InterruptedException
     * @throws IOException
     */
    private synchronized void sendMessage() throws InterruptedException, IOException {

        while (!clientSocket.isClosed()) {
            // wait until there is a message is queue (producer-consumer like)
            while (msgsToBeSent.isEmpty()) {
//                System.out.println("enter send wait");
                wait();
//                System.out.println("leaving send wait");
            }


            // send message to client
            PrintStream toClient = new PrintStream(clientSocket.getOutputStream());

            String content;
            try { // queue can be empty but not reproducible....?????
                content = msgsToBeSent.remove();
                toClient.println(content);
                System.out.println("Sent to client: " + content + "\n\n\n\n");
            }
            catch (Exception e) {} // ruthlessly suppress error

        }
//        System.out.println("ENDED SENDER ");
    }


    /**
     * Read message from client, pass the command to the model
     * @throws InterruptedException
     * @throws IOException
     */
    private void readMessage() {
        try {
            if(!this.clientSocket.isClosed()) {
                InputStream fromClient = clientSocket.getInputStream();
                BufferedReader readerrrrrrrrrr = new BufferedReader(new InputStreamReader(fromClient));

                String message = readerrrrrrrrrr.readLine();
                if(message.equals("PROTOCOL_EXIT")) {
                    // close socket and buffered reader
                    this.clientSocket.close();
                    readerrrrrrrrrr.close();
                    return;
                }
                System.out.println("Received: " + message);
                getMsgsFromModel(message);
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.out.println("Connection closed with "+ getClientName() + " " + clientSocket.getInetAddress()+ ":" + clientSocket.getPort() + ", no connection to client.");
        }
    }


    @Override
    public void run() {

        // send messages thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(!clientSocket.isClosed()) {
                    try {
                        sendMessage();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                        System.out.println("Connection closed with "+ getClientName() + " " + clientSocket.getInetAddress()+ ":" + clientSocket.getPort() + ", no connection to client.");
                        break;
                    }
                }
            }
        }).start();


        // read messages from client thread
        while(true) {
            readMessage();
        }


        //System.out.println("ENDED WORKED THREAD");
    }


    /**
     * Enqueue a message to the queue of messages to be sent to the client
     * @param s
     */
    public void takeInMessage(String s) {
        this.msgsToBeSent.add(s);
        synchronized (this) {
            notifyAll();
        }
        System.out.println("Enqueueing message to send queue");
    }


    /**
     * Execute the client's command using the model
     * @param command
     */
    public void getMsgsFromModel(String command) {
        try {
            model.doAction(command);
        }
        catch (Exception e) {
            System.out.println("Something very bad happened in the model");
            e.printStackTrace();
            takeInMessage("Something very bad happened in the model " + e.getMessage());
        }
    }

    public String getClientName() {return this.clientName;}

    /**
     * Method to update the client socket that is connected to this thread, then restart the message reader/sender
     * Used for when a client is reconnecting
     * @param clientSocket
     */
    public void changeClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }


}
