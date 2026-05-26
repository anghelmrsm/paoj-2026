package com.pao.laboratory13.exercise2;

import com.pao.laboratory13.exercise1.ProtocolEngine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) throws Exception {
        int configuredPort = args.length > 0 ? Integer.parseInt(args[0]) : 0;
        ServerSocket server = new ServerSocket(configuredPort);
        int port = server.getLocalPort();
        CountDownLatch clientsDone = new CountDownLatch(2);
        ExecutorService sessions = Executors.newFixedThreadPool(2);
        Thread acceptor = new Thread(() -> {
            try (ServerSocket ignored = server) {
                for (int i = 0; i < 2; i++) {
                    Socket socket = server.accept();
                    sessions.submit(() -> handle(socket, clientsDone));
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        System.out.println("[SERVER] Listening on port " + port);
        acceptor.start();
        Thread client1 = client("CLIENT-1", port, Arrays.asList("AUTH alice", "OPEN", "SEND hi", "HISTORY", "CLOSE"));
        Thread client2 = client("CLIENT-2", port, Arrays.asList("AUTH bob", "OPEN", "BROADCAST news", "CLOSE"));
        client1.start();
        client2.start();
        client1.join();
        client2.join();
        clientsDone.await();
        acceptor.join();
        sessions.shutdown();
        System.out.println("[SERVER] All clients done. Shutting down.");
    }

    private static void handle(Socket socket, CountDownLatch latch) {
        ProtocolEngine engine = new ProtocolEngine();
        try (Socket client = socket;
             BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
             PrintWriter writer = new PrintWriter(client.getOutputStream(), true)) {
            String command;
            while ((command = reader.readLine()) != null) {
                writer.println(engine.execute(command));
            }
        } catch (Exception ex) {
            System.out.println("[SERVER] session failed: " + ex.getMessage());
        } finally {
            latch.countDown();
        }
    }

    private static Thread client(String name, int port, List<String> commands) {
        return new Thread(() -> {
            try (Socket socket = new Socket("localhost", port);
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
                System.out.println("[" + name + "] Connected");
                for (String command : commands) {
                    writer.println(command);
                    System.out.println("[" + name + "] >> " + command + " => " + reader.readLine());
                }
                System.out.println("[" + name + "] Disconnected");
            } catch (Exception ex) {
                System.out.println("[" + name + "] failed: " + ex.getMessage());
            }
        });
    }
}
