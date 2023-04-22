package Engine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThreaded implements Runnable{

    static final String ip = "127.0.0.1";
    static final int serverPort = 20230;

    private boolean isStopped = false;
    private ServerSocket serverSocket = null;

    private String clientName = "";

    public static ArrayList<WorkerThread> workerThreads = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        ServerThreaded newServer = new ServerThreaded();
        new Thread(newServer).start();
    }




    @Override
    public void run() {

        // open server
        try {
            this.serverSocket = new ServerSocket(serverPort);
        }
        catch (IOException e) {
            throw new RuntimeException("Problem closing the server, please check error message: " + e.getMessage());
        }


        while (!isStopped()) {
            Socket clientSocket = null;

            try {
                clientSocket = this.serverSocket.accept();
                BufferedReader userName = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                clientName = userName.readLine();

                System.out.println("Connection accepted " + clientSocket.getInetAddress() + ":"+ clientSocket.getPort());
            } catch (IOException e) {
                throw new RuntimeException("Error connecting client, please check error message: " + e.getMessage());
            }
            if(workerThreads.isEmpty()) {
                WorkerThread villageWorker = new WorkerThread(clientSocket, clientName);
                workerThreads.add(villageWorker);
                System.out.println("New worker thread");
                new Thread(villageWorker).start();
            } else {
                boolean isNew = true;
                for (WorkerThread w : workerThreads) {
                    if(w.getClientName().compareTo(clientName) == 0) {
                        isNew = false;
                        w.changeClientSocket(clientSocket);
                        System.out.println("Changed client socket to new one");
                        System.out.println("RESTARTING CONNECTION WITH "+ " " + clientSocket.getInetAddress()+ ":" + clientSocket.getPort());
                    }
                }
                if(isNew) {
                    WorkerThread villageWorker = new WorkerThread(clientSocket, clientName);
                    workerThreads.add(villageWorker);
                    System.out.println("New worker thread");
//                    new Thread(villageWorker).start();
                    villageWorker.changeClientSocket(clientSocket);
                    System.out.println("STARTED NEW CONNECTION WITH "+ " " + clientSocket.getInetAddress()+ ":" + clientSocket.getPort());
                }
            }

        }
        System.out.println("Server has stopped");

    }


    private synchronized boolean isStopped() {return this.isStopped;}




}
