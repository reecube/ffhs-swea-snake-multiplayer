package ffhs.swea.client.controller;

import ffhs.swea.client.logic.GameLoop;
import ffhs.swea.client.logic.Grid;
import ffhs.swea.client.logic.Snake;
import ffhs.swea.client.view.View;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Controller implements EventHandler<KeyEvent>, Runnable {
    private View view;

    private GameLoop loop;
    private Grid grid;

    public Controller(int cols, int rows) {
        this.loop = new GameLoop(this);
        this.grid = new Grid(cols, rows);
    }

    public void start(Stage primaryStage) {
        this.view = new View(primaryStage);

        view.initializeStage(grid.getCols(), grid.getRows());
        view.registerKeyboardEvents(this);

        reset();

        view.showStage();

        (new Thread(loop)).start();
    }

    private void reset() {
        grid.reset();
        view.resetStage(grid);
    }

    @Override
    public void handle(KeyEvent event) {
        Snake snake = grid.getSnake();
        switch (event.getCode()) {
            case UP:
                snake.setUp();
                break;
            case DOWN:
                snake.setDown();
                break;
            case LEFT:
                snake.setLeft();
                break;
            case RIGHT:
                snake.setRight();
                break;
            case ENTER:
                if (loop.isPaused()) {
                    reset();
                    loop.resume();
                }
        }
    }


    @Override
    public void run() {
        grid.update();
        view.paint(grid);

        if (!grid.getSnake().isSafe()) {
            loop.pause();
            view.paintResetMessage();
        }
    }
}
