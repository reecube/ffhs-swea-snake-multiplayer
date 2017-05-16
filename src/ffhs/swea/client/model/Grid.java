package ffhs.swea.client.model;

public class Grid {
    private final int rows;
    private final int cols;

    private Snake snake;
    private Food food;

    public Grid(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Snake getSnake() {
        return snake;
    }

    public void setSnake(Snake snake) {
        this.snake = snake;
    }

    public Food getFood() {
        return food;
    }

    public void setFood(Food food) {
        this.food = food;
    }
}
