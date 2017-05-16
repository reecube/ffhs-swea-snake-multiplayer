package ffhs.swea.global.model;

import ffhs.swea.server.model.Grid;

public class UpdateObject {
    private int hashCode;
    private Grid grid;

    public UpdateObject(int hashCode, Grid grid) {
        this.hashCode = hashCode;
        this.grid = grid;
    }

    public int getHashCode() {
        return hashCode;
    }

    public Grid getGrid() {
        return grid;
    }
}
