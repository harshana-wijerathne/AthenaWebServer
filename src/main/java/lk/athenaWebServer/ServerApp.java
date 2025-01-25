package lk.athenaWebServer;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerApp {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(80);
        System.out.println("Server started on port 80");
    }
}
