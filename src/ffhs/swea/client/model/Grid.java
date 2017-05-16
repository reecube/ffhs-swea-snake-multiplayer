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

    public void reset(Point snake, Point food) {
        this.snake = new Snake(snake);
        this.food = new Food(food);
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

    public Food getFood() {
        return food;
    }
}
