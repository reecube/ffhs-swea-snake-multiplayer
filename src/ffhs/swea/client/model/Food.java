package ffhs.swea.client.model;

public class Food {
    private Point point;

    Food(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    void setPoint(Point point) {
        this.point = point;
    }
}
