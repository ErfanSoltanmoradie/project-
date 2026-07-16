package service.map;

import javafx.scene.Node;
import model.building.Building;
import model.building.Plant;
import model.world.Coordinate;

import java.io.Serializable;

public class GameMap implements Serializable {

    private final int rows;
    private final int columns;
    private final Tile[][] tiles;

    public GameMap(int rows, int columns) {
        this.columns = columns;
        this.rows = rows;
        tiles = new Tile[rows][columns];
        this.createTiles();
    }

    private void createTiles() {
        int borderSize = 24;

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {

                tiles[i][j] = new Tile(i, j);

                if (i < borderSize || i >= this.rows - borderSize|| j < borderSize || j >= this.columns - borderSize) {

                    tiles[i][j].setType(Tile.Type.BORDER);

                    if (Math.random() < 0.20) {
                        double rand = Math.random();

                        if (rand < 0.3) {
                            tiles[i][j].setDecorateType(Tile.DecorateType.WHITE_TREE);
                        /*} else if (rand < 0.5) {
                            tiles[i][j].setDecorateType(Tile.DecorateType.LARGE_BUSH);
                        } else if (rand < 0.75) {
                            tiles[i][j].setDecorateType(Tile.DecorateType.OAK_TREE);
                        } else if (rand < 0.9) {
                            tiles[i][j].setDecorateType(Tile.DecorateType.PINE_TREE);*/
                        } else {
                            tiles[i][j].setDecorateType(Tile.DecorateType.BROWN_TREE);
                        }
                    }
                }else{
                    tiles[i][j].setType(Tile.Type.PLAYABLE);
                }
            }
        }
    }

    public boolean isInside(int row, int column){
        if((row >= 0 && row < this.rows) && (column >= 0 && column < this.columns))
            return true;
        return false;
    }

    public boolean isAreaFree(int row, int column, int width, int height) {

        if (!this.isInside(row, column))
            return false;

        if (!isInside(row + height - 1, column + width - 1))  // -1 is because that row and col starts in 0
            return false;

        int protection = 2;

        for (int i = row - protection; i < row + height + protection; i++) {
            for (int j = column - protection; j < column + width + protection; j++) {

                if (isInside(i, j)) {
                    if (tiles[i][j].getBuilding() != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    public boolean placeBuilding(Building building, int row, int column){
        if(!this.isInside(row, column))
            return false;

        if (!isInside(row + building.getHeight() - 1, column + building.getWidth() - 1))
            return false;

        if(!this.isAreaFree(row, column, building.getWidth(), building.getHeight() ))
            return false;

        for (int i = row; i < row + building.getHeight(); i++) {
            for (int j = column; j < column + building.getWidth(); j++) {
                tiles[i][j].setBuilding(building);
            }
        }

        building.setPosition(new Coordinate(row, column));

        return true;
    }

    public boolean placePlant(Plant plant, int row, int column){
        if(!this.isInside(row, column))
            return false;

        if (!isInside(row + plant.getHeight() - 1, column + plant.getWidth() - 1))
            return false;

        if(!this.isAreaFree(row, column, plant.getWidth(), plant.getHeight() ))
            return false;

        for (int i = row; i < row + plant.getHeight(); i++) {
            for (int j = column; j < column + plant.getWidth(); j++) {
                tiles[i][j].setPlant(plant);
            }
        }

        plant.setPosition(new Coordinate(row, column));

        return true;
    }

    public boolean placeGlobalTower(model.finalPart.GlobalTower tower, int row, int column){
        if(!this.isInside(row, column))
            return false;

        if (!isInside(row + model.finalPart.GlobalTower.HEIGHT - 1, column + model.finalPart.GlobalTower.WIDTH - 1))
            return false;

        if(!this.isAreaFree(row, column, model.finalPart.GlobalTower.WIDTH, model.finalPart.GlobalTower.HEIGHT))
            return false;

        for (int i = row; i < row + model.finalPart.GlobalTower.HEIGHT; i++) {
            for (int j = column; j < column + model.finalPart.GlobalTower.WIDTH; j++) {
                tiles[i][j].setObject(tower);
            }
        }

        tower.setPosition(new Coordinate(row, column));

        return true;
    }

    public void removeBuilding(Building building){
        if (building == null || building.getPosition() == null) return;

        int row = building.getPosition().getX();
        int column = building.getPosition().getY();

        for (row = building.getPosition().getX(); row < building.getPosition().getX() + building.getHeight(); row++) {
            for ( column =  building.getPosition().getY(); column < building.getPosition().getY() + building.getWidth(); column++) {
                if (tiles[row][column].getBuilding() == building) {
                    tiles[row][column].setBuilding(null);
                }
            }
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public Tile getTile(int row , int column) {
        return tiles[row][column];
    }
}

