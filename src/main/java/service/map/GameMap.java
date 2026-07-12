package service.map;

import model.building.Building;
import model.building.Plant;
import model.world.Coordinate;

import java.io.Serializable;

public class GameMap implements Serializable {

    private final int rows;
    private final int columns;
    private final int tileSize;
    private final Tile[][] tiles;

    public GameMap(int rows, int columns, int tileSize) {
        this.columns = columns;
        this.rows = rows;
        this.tileSize = tileSize;
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

                    if (Math.random() < 0.3) {
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
                }
            }
        }
    }

    public boolean isInside(int row, int column){
        if((row >= 0 && row < this.rows) && (column >= 0 && column < this.columns))
            return true;
        return false;
    }

    public boolean isAreaFree(int row, int column, int width, int height){

        if(!this.isInside(row, column))
            return false;

        if (!isInside(row + height - 1, column + width - 1))
            return false;

        for (int i = row; i < row + height; i++) {
            for (int j = column; j < column + width; j++) {
                if(tiles[i][j].getBuilding() != null)
                    return false;
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

    public void removeBuilding(Building building){

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

    public int getTileSize() {
        return tileSize;
    }

    public Tile getTile(int row , int column) {
        return tiles[row][column];
    }
}














