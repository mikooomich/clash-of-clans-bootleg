package Client;


import java.io.*;
import java.net.ConnectException;
import java.net.Socket;


public class Client {

    static final String ip = "127.0.0.1";
    static final int port = 20230;
    static final String SEPARATOR = " ";
    private String clientName = "";

    public Client() throws IOException, InterruptedException {
        System.out.println("Hewwo world!");

        BufferedReader command = new BufferedReader(new InputStreamReader(System.in));

        // acquire a connection, bail if failed
        Socket connection = null;
        try {
            System.out.println("Enter your username:");
            this.clientName = command.readLine();
            connection = new Socket(ip, port);
        }
        catch (ConnectException error) {
            System.out.println(error.getMessage());
            return;
        }

        PrintWriter sendStuffToServer = new PrintWriter(connection.getOutputStream(), true); // client output
    //    System.out.println("Enter your username:");
    //    this.clientName = command.readLine();

        System.out.println(clientName + " has connected.");
        sendStuffToServer.println(this.clientName);
        String cmd[]; // command separated into args
        String whatToDo = "";

        final BufferedReader[] fromServer = {null};
//        PrintWriter sendStuffToServer = null;




        Socket finalConnection = connection;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fromServer[0] = new BufferedReader( new InputStreamReader(finalConnection.getInputStream())); // client input

                    String line; // message
//                    while ((line = fromServer[0].readLine()).compareTo("(*#^@*)#*&@%@^*#^)") != 0) {
                    while (!finalConnection.isClosed()) {
                        line = fromServer[0].readLine();
                        // print to client
                        System.out.println(line);
                    }

                    System.out.println("Client is ending now");
                    finalConnection.close();

                } catch (IOException e) {
                    System.out.println(e.getMessage());
                    System.out.println("Connection closed, listener will now exit.");
//                    System.exit(0);
                }
            }
        }).start();






        outer:
        while (true) {

            cmd = command.readLine().split(" ");
            whatToDo = cmd[0];

            try {
                if (whatToDo.compareTo("exit") == 0) {
                    sendStuffToServer.println("PROTOCOL_EXIT");
                    break outer;
                }


                // piece string back together with our separator
                String toSend = "";
                for (String segment: cmd) {
                    toSend += segment + SEPARATOR;
                }


                // send question, print response
                System.out.println("sending: " + toSend);
                sendStuffToServer.println(toSend);


            } // end try
            catch (ArrayIndexOutOfBoundsException error) {
                System.out.println("Blank message");
            }



            command = new BufferedReader(new InputStreamReader(System.in));

        }












        // horrible practices
        try {
            connection.close();
        }
        catch (Exception e) {

        }

        try {
            sendStuffToServer.close();
        }
        catch (Exception e) {
        }

        System.out.println("Closed");

    }



    public static void main(String args[]) throws Exception {
        System.out.println("Hewwo world");
        Client hewwo = new Client();
        System.out.println("Gwoodbye world");
    }



}
