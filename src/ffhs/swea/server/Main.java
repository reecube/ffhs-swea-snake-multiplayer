package ffhs.swea.server;

import ffhs.swea.server.controller.Controller;

public class Main {
    private static final int COLS = 50;
    private static final int ROWS = 30;

    public static void main(String[] args) {
        Controller server = new Controller(12345, COLS, ROWS);

        try {
            server.start();
        } catch (Exception ex) {
            System.out.println("Unexpected server exit:");
            ex.printStackTrace();
        }
    }
}
