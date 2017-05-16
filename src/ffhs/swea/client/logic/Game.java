package ffhs.swea.client.logic;

import ffhs.swea.client.model.*;

import java.util.Random;

public class Game {
    private Grid grid;

    public Game(int cols, int rows) {
        this.grid = new Grid(cols, rows);
    }

    public Grid getGrid() {
        return grid;
    }

    public void reset() {
        grid.reset(new Point(grid.getCols() / 2, grid.getRows() / 2), getRandomPoint());
    }

    private Point wrap(Point point) {
        int x = point.getX();
        int y = point.getY();
        if (x >= grid.getCols()) x = 0;
        if (y >= grid.getRows()) y = 0;
        if (x < 0) x = grid.getCols() - 1;
        if (y < 0) y = grid.getRows() - 1;
        return new Point(x, y);
    }

    private Point getRandomPoint() {
        Random random = new Random();
        Point point;
        do {
            point = new Point(random.nextInt(grid.getCols()), random.nextInt(grid.getRows()));
        } while (grid.getSnake().getPoints().contains(point));
        return point;
    }

    public void update() {
        if (grid.getFood().getPoint().equals(grid.getSnake().getHead())) {
            extendSnake();
            grid.getFood().setPoint(getRandomPoint());
        } else {
            moveSnake();
        }
    }

    private void moveSnake() {
        Snake snake = grid.getSnake();

        if (!snake.isStill()) {
            snake.addPoint(wrap(snake.translateHead()));
            snake.removeOldestPoint();
        }
    }

    private void extendSnake() {
        Snake snake = grid.getSnake();

        if (!snake.isStill()) {
            snake.addPoint(wrap(snake.translateHead()));
        }
    }
}
