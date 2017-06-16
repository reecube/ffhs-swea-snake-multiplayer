package ffhs.swea.client;

import ffhs.swea.client.controller.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;


public class Main extends Application implements EventHandler<WindowEvent> {
    private Controller controller;

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Platform.setImplicitExit(false);

        primaryStage.setOnCloseRequest(this);

        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        String host = JOptionPane.showInputDialog("Server address: ", hostAddress);

        if (host == null) {
            System.exit(0);
        }

        if (host.length() <= 0) {
            host = "localhost";
        }

        try {
            boolean isReachable = Controller.isReachable(host);

            if (!isReachable) {
                throw new IOException();
            }

            this.controller = new Controller(primaryStage, host, ffhs.swea.server.Main.SERVER_PORT);
        } catch (UnknownHostException e) {
            System.err.println("Unable to lookup `" + host + "`!");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Unable to reach `" + host + "`!");
            System.exit(1);
        }
    }

    @Override
    public void handle(WindowEvent event) {
        try {
            controller.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
