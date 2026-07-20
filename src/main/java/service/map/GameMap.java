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

        //this.setMapDesign();
    }

    private void createTiles() {
        int borderSize = 25;


        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {

                tiles[i][j] = new Tile(i, j);

                if (i < borderSize || i >= this.rows - borderSize|| j < borderSize || j >= this.columns - borderSize) {

                    tiles[i][j].setType(Tile.Type.BORDER);
                    tiles[i][j].setGroundType(Tile.GroundType.DIRT);

                    if (Math.random() < 0.30){
                        if (i < borderSize -1 || i >= this.rows - borderSize -1 || j < borderSize -1 || j >= this.columns - borderSize -1)
                            tiles[i][j].setDecorateType(Tile.DecorateType.ROCK);
                    }

                    if (Math.random() < 0.270){
                        if (i < borderSize -2 || i >= this.rows - borderSize -2 || j < borderSize -2 || j >= this.columns - borderSize -2)
                            tiles[i][j].setDecorateType(Tile.DecorateType.KAJ_TREE);
                    }
                    if (Math.random() < 0.01){
                        if (i < borderSize -2 || i >= this.rows - borderSize -2 || j < borderSize -2 || j >= this.columns - borderSize -2)
                            tiles[i][j].setDecorateType(Tile.DecorateType.RADIO_TREE);
                    }

                }else{
                    tiles[i][j].setType(Tile.Type.PLAYABLE);
                }
            }
        }
    }

    /*private void setMapDesign(){
        /*this.setDecorateRange(0, 5, 0, 150, Tile.DecorateType.BROWN_TREE);
        this.setDecorateRange(145, 150, 0, 150, Tile.DecorateType.BROWN_TREE);
        this.setDecorateRange(0, 150, 0, 5, Tile.DecorateType.BROWN_TREE);
        this.setDecorateRange(0, 150, 145, 150, Tile.DecorateType.BROWN_TREE);

        //setGroundRange(24, 28, 0, 149, Tile.GroundType.WATER);
        //setGroundRange(125, 127, 0, 149, Tile.GroundType.WATER);
        //setGroundRange(24, 128, 24, 26, Tile.GroundType.WATER);
        //setGroundRange(24, 125, 125, 127, Tile
        // .GroundType.WATER);
        //setGroundRange(125, 149, 0, 149, Tile.GroundType.WATER);

        this.setGroundRange(34, 122, 31, 124, Tile.GroundType.GRASS);
    }*/

    /*private void setGroundRange(int startRow, int endRow, int startCol, int endCol, Tile.GroundType type){
        for (int i = startRow; i <=endRow ; i++) {
            for (int j = startCol; j <= endCol; j++) {
                if(isInside(i, j)){
                    tiles[i][j].setGroundType(type);
                }
            }
        }
    }

    private void setDecorateRange(int startRow, int endRow, int startCol, int endCol, Tile.DecorateType type){
        for (int i = startRow; i <=endRow ; i++) {
            for (int j = startCol; j <= endCol; j++) {
                if(isInside(i, j)){
                    tiles[i][j].setDecorateType(type);
                }
            }
        }
    }*/

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
                    if (tiles[i][j].getBuilding() != null || tiles[i][j].getType() != Tile.Type.PLAYABLE) {
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

    public boolean removeGlobalTower(model.finalPart.GlobalTower tower) {
        if (tower == null || tower.getPosition() == null) return false;

        int startRow = tower.getPosition().getX();
        int startCol = tower.getPosition().getY();

        for (int i = startRow; i < startRow + model.finalPart.GlobalTower.HEIGHT; i++) {
            for (int j = startCol; j < startCol + model.finalPart.GlobalTower.WIDTH; j++) {
                if (isInside(i, j) && tiles[i][j].getObject() == tower) {
                    tiles[i][j].setObject(null);
                }
            }
        }
        return true;
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

