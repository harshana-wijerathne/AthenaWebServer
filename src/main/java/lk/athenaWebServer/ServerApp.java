package lk.athenaWebServer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.security.spec.RSAOtherPrimeInfo;
import java.time.LocalDateTime;

public class ServerApp {
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(80);
        System.out.println("Server started on port 80");
        System.out.println("Waiting for connections...");

        while (true) {
            Socket localsocket = serverSocket.accept();
            System.out.println("Accepted connection from " + localsocket.getRemoteSocketAddress());

            new Thread(() -> {
                try {
                    InputStream inputStream = localsocket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(inputStream);
                    BufferedReader br = new BufferedReader(isr);

                    String commandLine = br.readLine();
                    String[] s = commandLine.split(" ");
                    String command = s[0];
                    String path = s[1];
                    System.out.println(command + " " + path);

                    String line;
                    String host=null;
                    while ((line = br.readLine())!= null && !line.isEmpty()){
                        if(line.split(":")[0].strip().equalsIgnoreCase("host")){
                            host = line.split(":")[1];
                            System.out.println(host);
                        }
                    }

                    OutputStream os = localsocket.getOutputStream();

                    String head = "";
                    Path index=null;
                    String content = "";



                    if(!command.equalsIgnoreCase("get")){
                        head = """
                                HTTP/1.1 405 Method Not Allowed
                                Server: Dep Server
                                Date:%s
                                Content-Type: text/html
                                
                                """.formatted(LocalDateTime.now());

                        os.write(head.getBytes());
                        os.flush();

                        content = """
                                <!DOCTYPE html>
                                <html>
                                <head>
                                <title>Dep Server</title>
                                </head>
                                <body>
                                <h1>500 Bad Request</h1>
                                <h6>copyright (c) Athena web Server</h6>
                                </body>
                                </html>
                                """;
                        os.write(content.getBytes());
                        os.flush();


                    }else if (host == null) {
                        head = """
                                HTTP/1.1 400 Bad Request
                                Server: Dep Server
                                Date:%s
                                Content-Type: text/html
                                
                                """.formatted(LocalDateTime.now());

                        os.write(head.getBytes());
                        os.flush();

                        content = """
                                <!DOCTYPE html>
                                <html>
                                <head>
                                <title>Dep Server</title>
                                </head>
                                <body>
                                <h1>400 Bad Request</h1>
                                </body>
                                </html>
                                """;
                        os.write(content.getBytes());
                        os.flush();

                    }


                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();

        }
    }
}
