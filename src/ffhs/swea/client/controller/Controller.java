package ffhs.swea.client.controller;

import ffhs.swea.client.logic.GameLoop;
import ffhs.swea.client.logic.Game;
import ffhs.swea.client.model.Snake;
import ffhs.swea.client.view.View;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Controller implements EventHandler<KeyEvent>, Runnable {
    private View view;

    private GameLoop loop;
    private Game game;

    public Controller(int cols, int rows) {
        this.loop = new GameLoop(this);
        this.game = new Game(cols, rows);
    }

    public void start(Stage primaryStage) {
        this.view = new View(primaryStage);

        view.initializeStage(game.getGrid().getCols(), game.getGrid().getRows());
        view.registerKeyboardEvents(this);

        reset();

        view.showStage();

        (new Thread(loop)).start();
    }

    private void reset() {
        game.reset();
        view.resetStage(game.getGrid());
    }

    @Override
    public void handle(KeyEvent event) {
        Snake snake = game.getGrid().getSnake();

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
        game.update();
        view.paint(game.getGrid());

        if (!game.getGrid().getSnake().isSafe()) {
            loop.pause();
            view.paintResetMessage();
        }
    }
}
