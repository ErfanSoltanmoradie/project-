package service.map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.building.Building;
import model.building.BuildingType;
import model.village.Village;
import model.world.Coordinate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



public class GameCanvasView extends Canvas {

    private final Village village;
    private final GameMap gameMap;

    private double tileWidth = 50;
    private double tileHeight = 25;
    private double cameraX;
    private double cameraY;

    private final double MIN_TILE_WIDTH = 15;
    private final double MAX_TILE_WIDTH = 50;

    private final Image grassTile = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/grass.png")));
    private final Image brownTree = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/tree3.png")));
    private final Image whiteTree = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/tree2.png")));
    //private final Image buildingImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/research_center.png")));

    private final Map<BuildingType, BuildingGraphicProperties> buildingGraphics = new HashMap<>();

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

        loadBuildingGraphic();

        widthProperty().addListener((obs, oldVal, newVal) -> draw());
        heightProperty().addListener((obs, oldVal, newVal) -> draw());
    }

    private void loadBuildingGraphic() {
        try {
        /*buildingGraphics.put(BuildingType.WOOD_MINE, new BuildingGraphicProperties(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/wood_mine.png"))),
                1.4, 0.75
        ));

        buildingGraphics.put(BuildingType.IRON_MINE, new BuildingGraphicProperties(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/iron_mine.png"))),

        ));*/

        buildingGraphics.put(BuildingType.MAJOR_BUILDING, new BuildingGraphicProperties(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/major.png"))),
                0.8,1.1, 0.72
        ));

        buildingGraphics.put(BuildingType.RESEARCH_CENTER, new BuildingGraphicProperties(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/research_center.png"))),
                1.2, 1.2, 0.72
        ));
        } catch(NullPointerException e) {
            System.err.println("Error loading building graphics: " + e.getMessage());
        }
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
                    Tile tile = gameMap.getTile(r, c);

                    if (grassTile != null && !grassTile.isError()) {

                        double renderX = Math.floor(isoX);
                        double renderY = Math.floor(isoY);

                        double renderWidth = tileWidth +12;
                        double renderHeight = tileHeight + 12;

                        gc.drawImage(grassTile, renderX, renderY, renderWidth, renderHeight);
                    }

                    if (tile.getType() == Tile.Type.BORDER) {
                        double[] xPoints = {isoX + tileWidth / 2, isoX + tileWidth, isoX + tileWidth / 2, isoX};
                        double[] yPoints = {isoY, isoY + tileHeight / 2, isoY + tileHeight, isoY + tileHeight / 2};

                        gc.setFill(Color.rgb(15, 15, 15, 0.35));
                        gc.fillPolygon(xPoints, yPoints, 4);
                    }
                }
            }
        }

        for (int r = 0; r < rows; r++) {
            for (int c = cols - 1; c >= 0; c--) {

                double isoX = originX + (c - r) * (tileWidth / 2);
                double isoY = originY + (c + r) * (tileHeight / 2);

                Tile tile = gameMap.getTile(r, c);
                Tile.DecorateType deco = tile.getDecorateType();

                if (deco != Tile.DecorateType.NONE) {
                    Image currentImg = null;
                    double wFactor = 1.0;
                    double hFactor = 1.0;

                    switch (deco) {
                        case BROWN_TREE:
                            currentImg = brownTree;
                            wFactor = 2.0;
                            hFactor = 4.0;
                            break;
                        case WHITE_TREE:
                            currentImg = whiteTree;
                            wFactor = 1.8;
                            hFactor = 3.5;
                            break;
                    }

                    if (currentImg != null) {
                        double customWidth = tileWidth * wFactor;
                        double customHeight = tileHeight * hFactor;

                        double offsetX = isoX - (customWidth - tileWidth) / 2;
                        double offsetY = isoY - (customHeight - tileHeight);

                        gc.drawImage(currentImg, offsetX, offsetY, customWidth, customHeight);
                    }
                }


                if (tile.getBuilding() != null) {
                    Building b = tile.getBuilding();
                    Coordinate pos = b.getPosition();

                    if (pos.getX() == r && pos.getY() == c) {
                        BuildingGraphicProperties props = buildingGraphics.get(b.getType());

                        if (props != null && props.image != null && !props.image.isError()) {
                            int bW = b.getWidth();
                            int bH = b.getHeight();

                            int bottomRow = r + bH - 1;
                            int bottomCol = c + bW - 1;

                            double baseBottomX = originX + (bottomCol - bottomRow) * (tileWidth / 2);
                            double baseBottomY = originY + (bottomCol + bottomRow) * (tileHeight / 2) + tileHeight;

                            double customWidth = tileWidth * Math.max(bW, bH) * 1.0 * props.widthScale;

                            double imageRatio = props.image.getHeight() / props.image.getWidth();
                            double customHeight = customWidth * imageRatio * props.heightScale;

                            double offsetX = baseBottomX - (customWidth / 2);

                            double offsetY = baseBottomY - (customHeight * props.yOffsetScale);

                            gc.drawImage(props.image, offsetX, offsetY, customWidth, customHeight);
                        }
                    }
                }
            }
        }

        gc.setImageSmoothing(true);
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


    private static class BuildingGraphicProperties {
        final Image image;
        final double heightScale;
        final double widthScale;
        final double yOffsetScale;

        public BuildingGraphicProperties(Image image, double heightScale, double widthScale, double yOffsetScale) {
            this.image = image;
            this.heightScale = heightScale;
            this.widthScale = widthScale;
            this.yOffsetScale = yOffsetScale;
        }
    }
}

