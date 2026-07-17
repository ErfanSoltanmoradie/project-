package service.map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.building.Building;
import model.building.BuildingType;
import model.building.Plant;
import model.building.PlantType;
import model.time.BuildTask;
import model.time.TimedOperation;
import model.village.Village;
import model.world.Coordinate;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;



public class GameCanvasView extends Canvas {

    private final Village village;
    private final GameMap gameMap;

    private double tileWidth = 120;
    private double tileHeight = 60;
    private double cameraX;
    private double cameraY;

    private final double MIN_TILE_WIDTH = 10;
    private final double MAX_TILE_WIDTH = 250;

    private final Image grassTile = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/grass.png")));
    private final Image brownTree = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/tree3.png")));
    private final Image whiteTree = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/tree2.png")));
    private final Image woodMineImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/WoodMine.png")));
    private final Image laboratoryImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/researchCenter.png")));
    //private final Image customhouseImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Customhouse.png")));
    private final Image nrcPlantImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/NRC.png")));
    private final Image snrcPlantImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/SNRC.png")));
    private final Image psnrcPlantImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/PSNRC.png")));
    //private final Image buildingImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/researchCenter.png")));

    private final Map<BuildingType, BuildingGraphicProperties> buildingGraphics = new HashMap<>();
    private final Map<PlantType, PlantGraphicProperties> plantGraphics = new HashMap<>();

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
        loadPlantGraphic();

        widthProperty().addListener((obs, oldVal, newVal) -> draw());
        heightProperty().addListener((obs, oldVal, newVal) -> draw());
    }

    private void loadBuildingGraphic() {
        try {
            buildingGraphics.put(BuildingType.WOOD_MINE, new BuildingGraphicProperties(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/WoodMine.png"))),
                    1,1,1));

            buildingGraphics.put(BuildingType.IRON_MINE, new BuildingGraphicProperties(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/IronMine.png"))),
                    1,1,1));

            buildingGraphics.put(BuildingType.SOIL_PURIFIER, new BuildingGraphicProperties(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/soilPurifier.png"))),
                    1,1,1));

            buildingGraphics.put(BuildingType.GUNPOWDER_MINE, new BuildingGraphicProperties(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/GunpowderMine.png"))),
                    1,1,1));

            buildingGraphics.put(BuildingType.WATER_PURIFIER, new BuildingGraphicProperties(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/waterPurifier.png"))),
                    1,1,1));

            buildingGraphics.put(BuildingType.DIRTY_SOIL_MINE, new BuildingGraphicProperties(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/SoilMine.png"))),
                    1,1,1));

            buildingGraphics.put(BuildingType.IRON_STORAGE, new BuildingGraphicProperties(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/ironStorage.png"))),
                    1,1,1));

            buildingGraphics.put(BuildingType.MAJOR_BUILDING, new BuildingGraphicProperties(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("/major.png"))),
                1,1,1));

            buildingGraphics.put(BuildingType.RESEARCH_CENTER, new BuildingGraphicProperties(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/researchCenter.png"))),
                    1,1,1));

            buildingGraphics.put(BuildingType.LABORATORY, new BuildingGraphicProperties(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/lab.png"))),
                    1,1,1));

            buildingGraphics.put(BuildingType.BARRACKS, new BuildingGraphicProperties(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/barrack2.png"))),
                    1,1,1));

       /* buildingGraphics.put(BuildingType.CUSTOMHOUSE, new BuildingGraphicProperties(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream()))
        )) */
        } catch(NullPointerException e) {
            System.err.println("Error loading building graphics: " + e.getMessage());
        }

    }

    public void loadPlantGraphic(){
        try {
            plantGraphics.put(PlantType.NRC, new PlantGraphicProperties(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/NRC.png"))),
                    1.2,1.2,0.72
            ));

            plantGraphics.put(PlantType.SNRC, new PlantGraphicProperties(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/SNRC.png"))),
                    1.2,1.2,0.72
            ));

            plantGraphics.put(PlantType.PSNRC, new PlantGraphicProperties(
                    new Image(Objects.requireNonNull(getClass().getResourceAsStream("/PSNRC.png"))),
                    1.2,1.2,0.72
            ));
        }catch(NullPointerException e) {
            System.err.println("Error loading plant graphics: " + e.getMessage());
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

        gc.setFill(Color.web("#000415"));
        gc.fillRect(0, 0, width, height);

        gc.setImageSmoothing(false);

        double originX = width / 2 + this.cameraX;
        double originY = height / 5 + this.cameraY;

        int rows = gameMap.getRows();
        int cols = gameMap.getColumns();

        int padding = 25;

        for (int r = -padding; r < rows + padding; r++) {
            for (int c = -padding; c < cols + padding; c++) {

                double isoX = originX + (c - r) * (tileWidth / 2);  // that is how we turn 2D into 2.5 D
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

                        double renderWidth = tileWidth +6;
                        double renderHeight = tileHeight + 6;

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

        int maxDepth = (rows - 1) + (cols - 1);

        for (int depth = 0; depth <= maxDepth; depth++) {
            for (int r = 0; r <= depth; r++) {
                int c = depth - r;


                if (r >= 0 && r < rows && c >= 0 && c < cols) {

                    double currentTileWidth = tileWidth;
                    double currentTileHeight = tileHeight;

                    double isoX = originX + (c - r) * (currentTileWidth / 2.0);
                    double isoY = originY + (c + r) * (currentTileHeight / 2.0);

                    Tile tile = gameMap.getTile(r, c);


                    Tile.DecorateType deco = tile.getDecorateType();
                    if (deco != Tile.DecorateType.NONE) {
                        Image currentImg = null;
                        double wFactor = 1.0;
                        double hFactor = 1.0;

                        switch (deco) {
                            case BROWN_TREE:
                                currentImg = brownTree;
                                wFactor = 4;
                                hFactor = 8;
                                break;
                            case WHITE_TREE:
                                currentImg = whiteTree;
                                wFactor = 1.8;
                                hFactor = 3.5;
                                break;
                        }

                        if (currentImg != null) {
                            double customWidth = currentTileWidth * wFactor;
                            double customHeight = currentTileHeight * hFactor;

                            double offsetX = isoX - (customWidth - currentTileWidth) / 2;
                            double offsetY = isoY - (customHeight - currentTileHeight);

                            gc.drawImage(currentImg, Math.round(offsetX), Math.round(offsetY), Math.round(customWidth), Math.round(customHeight));
                        }
                    }


                    if (tile.getBuilding() != null) {
                        Building b = tile.getBuilding();
                        Coordinate pos = b.getPosition();

                        int bW = b.getWidth();
                        int bH = b.getHeight();

                        int bottomRow = pos.getX() + bH - 1;
                        int bottomCol = pos.getY() + bW - 1;

                        if (r == bottomRow && c == bottomCol) {
                            BuildingGraphicProperties props = buildingGraphics.get(b.getType());

                            if (props != null && props.image != null) {

                                double tileX = originX + (pos.getY() - pos.getX()) * (currentTileWidth / 2.0);
                                double tileY = originY + (pos.getY() + pos.getX()) * (currentTileHeight / 2.0);

                                double bottomX = tileX + ((bW - bH) * (currentTileWidth / 2.0));
                                double bottomY = tileY + ((bW + bH) * (currentTileHeight / 2.0));

                                double zoomFactor = currentTileWidth / 120.0;
                                double imgWidth = props.image.getWidth() * zoomFactor * props.widthScale;
                                double imgHeight = props.image.getHeight() * zoomFactor * props.heightScale;

                                double offsetX = bottomX - (imgWidth / 2.0);
                                double offsetY = bottomY - imgHeight;

                                gc.drawImage(props.image, Math.round(offsetX), Math.round(offsetY), Math.round(imgWidth), Math.round(imgHeight));
                            }
                        }
                    }if (tile.getPlant() != null) {
                        Plant p = tile.getPlant();
                        Coordinate pos = p.getPosition();

                        if (pos.getX() == r && pos.getY() == c) {
                            PlantGraphicProperties props = plantGraphics.get(p.getType());

                            if (props != null && props.image != null && !props.image.isError()) {
                                int bW = p.getWidth();
                                int bH = p.getHeight();

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
        }

        gc.setImageSmoothing(true);

        for (TimedOperation operation : village.getTimedOperation().values()) {
            Coordinate taskCoord = null;

            if (operation instanceof BuildTask bTask) {
                taskCoord = bTask.getCoordinate();
            } else if (operation instanceof model.time.UpgradeTask uTask) {
                java.util.UUID bId = uTask.getBuildingId();
                model.building.Building b = village.getBuildings().get(bId);
                if (b != null) taskCoord = b.getPosition();
            }

            if (taskCoord != null) {
                int r = taskCoord.getX();
                int c = taskCoord.getY();
                double isoX = originX + (c - r) * (tileWidth / 2);
                double isoY = originY + (c + r) * (tileHeight / 2);

                long elapsed = java.time.Duration.between(operation.getStartTime(), java.time.Instant.now()).toMillis();
                long total = java.time.Duration.between(operation.getStartTime(), operation.getFinishTime()).toMillis();

                double progress = 1.0;
                if (total > 0) progress = Math.min(1.0, (double) elapsed / total);


                gc.setFill(Color.RED);
                gc.fillRect(isoX + tileWidth * 0.1, isoY - 15, tileWidth * 0.8, 6);

                gc.setFill(Color.CHARTREUSE);
                gc.fillRect(isoX + tileWidth * 0.1, isoY - 15, (tileWidth * 0.8) * progress, 6);
            }
        }


        double radiationFactor = 0.4;
        gc.setFill(Color.web("#3e2723", radiationFactor * 0.2));
        gc.fillRect(0, 0, width, height);

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
    private static class PlantGraphicProperties {
        final Image image;
        final double heightScale;
        final double widthScale;
        final double yOffsetScale;

        public PlantGraphicProperties(Image image, double heightScale, double widthScale, double yOffsetScale) {
            this.image = image;
            this.heightScale = heightScale;
            this.widthScale = widthScale;
            this.yOffsetScale = yOffsetScale;
        }
    }
}

