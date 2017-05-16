package ffhs.swea.server.model;

import java.util.ArrayList;
import java.util.HashMap;

public class Grid {
    private final int rows;
    private final int cols;

    private Snake playerSnake;
    private HashMap<Integer, Snake> snakes;
    private ArrayList<Food> foods;

    public Grid(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;

        this.snakes = new HashMap<>();
        this.playerSnake = null;
        this.foods = new ArrayList<>();
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Snake getPlayerSnake() {
        return playerSnake;
    }

    public void setPlayerSnake(Snake playerSnake) {
        this.playerSnake = playerSnake;
    }

    public HashMap<Integer, Snake> getSnakes() {
        return snakes;
    }

    public void addSnake(Integer key, Snake snake) {
        snakes.put(key, snake);
    }

    public void removeSnake(Integer key) {
        snakes.remove(key);
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public void addFood(Food food) {
        foods.add(food);
    }

    public void removeFood() {
        foods.remove(0);
    }
}
