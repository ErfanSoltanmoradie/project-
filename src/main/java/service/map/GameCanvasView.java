package service.map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.building.Building;
import model.village.Village;
import model.world.Coordinate;
import java.util.Objects;

public class GameCanvasView extends Canvas {

    private final Village village;
    private final GameMap gameMap;

    private double tileWidth = 40;
    private double tileHeight = 20;
    private double cameraX;
    private double cameraY;

    private final double MIN_TILE_WIDTH = 15;
    private final double MAX_TILE_WIDTH = 50;

    private final Image grassTile = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/grass.png")));


    public Village getVillage() {
        return village;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public double getTileWidth() {
        return tileWidth;
    }

    public double getTileHeight() {
        return tileHeight;
    }

    public GameCanvasView(Village village) {
        this.village = village;
        this.gameMap = village.getGameMap();

        widthProperty().addListener((obs, oldVal, newVal) -> draw());
        heightProperty().addListener((obs, oldVal, newVal) -> draw());
    }

    public void moveCamera(double dx, double dy) {
        this.cameraX += dx;
        this.cameraY += dy;
        draw();
    }

    public void draw() {
        double width = getWidth();
        double height = getHeight();

        if (width <= 0 || height <= 0) return;

        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);


        gc.setFill(Color.web("#4c7828"));
        gc.fillRect(0, 0, width, height);


        gc.setImageSmoothing(false);

        double originX = width / 2 + this.cameraX;
        double originY = height / 5 + this.cameraY;

        int rows = gameMap.getRows();
        int cols = gameMap.getColumns();

        int padding = 25;


        for (int r = -padding; r < rows + padding; r++) {
            for (int c = -padding; c < cols + padding; c++) {

                double isoX = originX + (c - r) * (tileWidth / 2);
                double isoY = originY + (c + r) * (tileHeight / 2);

                if (isoX < -tileWidth || isoX > width || isoY < -tileHeight || isoY > height) {
                    continue;
                }

                boolean isInsideMap = (r >= 0 && r < rows && c >= 0 && c < cols);

                if (isInsideMap) {
                    if (grassTile != null && !grassTile.isError()) {
                        gc.drawImage(grassTile, isoX, isoY, tileWidth + 1.0, tileHeight + 1.0);
                    } else {
                        double[] xPoints = {isoX + tileWidth / 2, isoX + tileWidth, isoX + tileWidth / 2, isoX};
                        double[] yPoints = {isoY, isoY + tileHeight / 2, isoY + tileHeight, isoY + tileHeight / 2};
                        gc.setFill(Color.web("#5c8e32"));
                        gc.fillPolygon(xPoints, yPoints, 4);
                    }
                } else {
                    double[] xPoints = {isoX + tileWidth / 2, isoX + tileWidth, isoX + tileWidth / 2, isoX};
                    double[] yPoints = {isoY, isoY + tileHeight / 2, isoY + tileHeight, isoY + tileHeight / 2};
                    if ((r + c) % 2 == 0) {
                        gc.setFill(Color.web("#2e471a"));
                    } else {
                        gc.setFill(Color.web("#263b15"));
                    }
                    gc.fillPolygon(xPoints, yPoints, 4);
                }
            }
        }

        gc.setImageSmoothing(true);


        for (Building building : village.getBuildings().values()) {
            Coordinate pos = building.getPosition();
            int r = pos.getX();
            int c = pos.getY();

            if (building.getBuildingStatus() == model.building.BuildingStatus.ACTIVE) {
                int bW = building.getWidth();
                int bH = building.getHeight();


                double baseTopX = originX + (c - r) * (tileWidth / 2) + tileWidth / 2;
                double baseTopY = originY + (c + r) * (tileHeight / 2);

                double baseLeftX = originX + (c - (r + bW - 1)) * (tileWidth / 2);
                double baseLeftY = originY + (c + (r + bW - 1)) * (tileHeight / 2) + tileHeight / 2;

                double baseRightX = originX + ((c + bH - 1) - r) * (tileWidth / 2) + tileWidth;
                double baseRightY = originY + ((c + bH - 1) + r) * (tileHeight / 2) + tileHeight / 2;

                double baseBottomX = originX + ((c + bH - 1) - (r + bW - 1)) * (tileWidth / 2) + tileWidth / 2;
                double baseBottomY = originY + ((c + bH - 1) + (r + bW - 1)) * (tileHeight / 2) + tileHeight;


                double bHeight = tileHeight * 1.5;


                double roofTopX = baseTopX;       double roofTopY = baseTopY - bHeight;
                double roofLeftX = baseLeftX;     double roofLeftY = baseLeftY - bHeight;
                double roofRightX = baseRightX;   double roofRightY = baseRightY - bHeight;
                double roofBottomX = baseBottomX; double roofBottomY = baseBottomY - bHeight;

                gc.setFill(Color.web("#5d4037"));
                gc.fillPolygon(
                        new double[]{baseLeftX, baseBottomX, roofBottomX, roofLeftX},
                        new double[]{baseLeftY, baseBottomY, roofBottomY, roofLeftY},
                        4
                );


                gc.setFill(Color.web("#8d6e63"));
                gc.fillPolygon(
                        new double[]{baseRightX, baseBottomX, roofBottomX, roofRightX},
                        new double[]{baseRightY, baseBottomY, roofBottomY, roofRightY},
                        4
                );


                gc.setFill(Color.web("#a1887f"));
                gc.fillPolygon(
                        new double[]{roofTopX, roofRightX, roofBottomX, roofLeftX},
                        new double[]{roofTopY, roofRightY, roofBottomY, roofLeftY},
                        4
                );


                gc.setStroke(Color.web("#3e2723", 0.7));
                gc.setLineWidth(1.2);

                gc.strokePolygon(
                        new double[]{roofTopX, roofRightX, roofBottomX, roofLeftX},
                        new double[]{roofTopY, roofRightY, roofBottomY, roofLeftY},
                        4
                );

                gc.strokeLine(baseBottomX, baseBottomY, roofBottomX, roofBottomY);
            }
        }
    }

    public void zoom(double factor, double mouseX, double mouseY) {
        double newWidth = tileWidth * factor;

        if (newWidth < MIN_TILE_WIDTH || newWidth > MAX_TILE_WIDTH) {
            return;
        }

        double width = getWidth();
        double height = getHeight();
        double originX = width / 2 + cameraX;
        double originY = height / 5 + cameraY;

        double relX = mouseX - originX;
        double relY = mouseY - originY;

        tileWidth = newWidth;
        tileHeight = tileWidth / 2;

        cameraX = mouseX - width / 2 - (relX * factor);
        cameraY = mouseY - height / 5 - (relY * factor);


        draw();
    }

    public Coordinate getCoordinateFromPixels(double pixelX, double pixelY) {
        double width = getWidth();
        double originX = width / 2 + cameraX;
        double originY = getHeight() / 5 + cameraY;

        double dx = pixelX - originX;
        double dy = pixelY - originY;

        int col = (int) Math.floor((dx / (tileWidth / 2) + dy / (tileHeight / 2)) / 2);
        int row = (int) Math.floor((dy / (tileHeight / 2) - dx / (tileWidth / 2)) / 2);

        return new Coordinate(row, col);
    }
}