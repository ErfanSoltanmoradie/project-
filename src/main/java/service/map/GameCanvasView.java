package service.map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.building.Building;
import model.building.BuildingStatus;
import model.building.BuildingType;
import model.time.BuildTask;
import model.time.TimedOperation;
import model.village.Village;
import model.world.Coordinate;

import java.time.Duration;
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
    private final Image laboratoryImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Laboratory.jpg")));
    private final Image customhouseImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/Customhouse.png")));
    private final Image nrcPlantImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/NRC.jpg")));
    private final Image snrcPlantImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/SNRC.png")));
    private final Image psnrcPlantImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/PSNRC.png")));

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


        // ۱. رسم تمام ساختمان‌ها
        for (Building building : village.getBuildings().values()) {
            Coordinate pos = building.getPosition();
            if (pos == null) continue;
            int r = pos.getX();
            int c = pos.getY();

            if (building.getBuildingStatus() == BuildingStatus.ACTIVE) {
                int bW = building.getWidth();
                int bH = building.getHeight();
                double isoX = originX + (c - r) * (tileWidth / 2);
                double isoY = originY + (c + r) * (tileHeight / 2);

                // تشخیص اتوماتیک تصویر مناسب بر اساس نوع ساختمان
                Image currentBuildingImg = null;
                switch (building.getType()) {
                    case LABORATORY -> currentBuildingImg = laboratoryImage;
                    case CUSTOMHOUSE -> currentBuildingImg = customhouseImage;
                }

                // الف) اگر عکس موجود بود، عکس واقعی ساختمان بدون پس‌زمینه رسم می‌شود
                if (currentBuildingImg != null && !currentBuildingImg.isError()) {
                    double imgWidth = tileWidth * bH;
                    double imgHeight = tileHeight * 2.5; // ارتفاع بلندتر برای ایجاد افکت سه بعدی (ایزومتریک)
                    gc.drawImage(currentBuildingImg, isoX, isoY - (imgHeight - tileHeight), imgWidth, imgHeight);
                }
                // ب) اگر عکس لود نشد، به عنوان زاپاس مکعب رنگی رسم کن
                else {
                    double baseTopX = originX + (c - r) * (tileWidth / 2) + tileWidth / 2;
                    double baseTopY = originY + (c + r) * (tileHeight / 2);
                    double baseLeftX = originX + (c - (r + bW - 1)) * (tileWidth / 2);
                    double baseLeftY = originY + (c + (r + bW - 1)) * (tileHeight / 2) + tileHeight / 2;
                    double baseRightX = originX + ((c + bH - 1) - r) * (tileWidth / 2) + tileWidth;
                    double baseRightY = originY + ((c + bH - 1) + r) * (tileHeight / 2) + tileHeight / 2;
                    double baseBottomX = originX + ((c + bH - 1) - (r + bW - 1)) * (tileWidth / 2) + tileWidth / 2;
                    double baseBottomY = originY + ((c + bH - 1) + (r + bW - 1)) * (tileHeight / 2) + tileHeight;

                    double bHeight = tileHeight * 1.5;
                    double roofTopX = baseTopX;
                    double roofTopY = baseTopY - bHeight;
                    double roofLeftX = baseLeftX;
                    double roofLeftY = baseLeftY - bHeight;
                    double roofRightX = baseRightX;
                    double roofRightY = baseRightY - bHeight;
                    double roofBottomX = baseBottomX;
                    double roofBottomY = baseBottomY - bHeight;

                    String leftWallColor = "#5d4037";
                    String rightWallColor = "#8d6e63";
                    String roofColor = "#a1887f";
                    if (building.getType() == BuildingType.LABORATORY) {
                        leftWallColor = "#311b92";
                        rightWallColor = "#4527a0";
                        roofColor = "#b388ff";
                    }

                    gc.setFill(Color.web(leftWallColor));
                    gc.fillPolygon(new double[]{baseLeftX, baseBottomX, roofBottomX, roofLeftX}, new double[]{baseLeftY, baseBottomY, roofBottomY, roofLeftY}, 4);
                    gc.setFill(Color.web(rightWallColor));
                    gc.fillPolygon(new double[]{baseRightX, baseBottomX, roofBottomX, roofRightX}, new double[]{baseRightY, baseBottomY, roofBottomY, roofRightY}, 4);
                    gc.setFill(Color.web(roofColor));
                    gc.fillPolygon(new double[]{roofTopX, roofRightX, roofBottomX, roofLeftX}, new double[]{roofTopY, roofRightY, roofBottomY, roofLeftY}, 4);
                }
            }
        }

// ۲. رسم تمام گیاهان (Plants)
        for (model.building.Plant plant : village.getPlants().values()) {
            Coordinate pos = plant.getPosition();
            if (pos == null) continue;
            int r = pos.getX();
            int c = pos.getY();

            double isoX = originX + (c - r) * (tileWidth / 2);
            double isoY = originY + (c + r) * (tileHeight / 2);

            Image currentPlantImg = null;
            if (plant.getType() == model.building.PlantType.NRC) {
                currentPlantImg = nrcPlantImage;
            } else if (plant.getType() == model.building.PlantType.SNRC) {
                currentPlantImg = snrcPlantImage;
            } else if (plant.getType() == model.building.PlantType.PSNRC) {
                currentPlantImg = psnrcPlantImage;
            }

            // الف) رسم عکس واقعی گیاه بدون پس‌زمینه
            if (currentPlantImg != null && !currentPlantImg.isError()) {
                double pImgWidth = tileWidth;
                double pImgHeight = tileHeight * 1.8;
                gc.drawImage(currentPlantImg, isoX, isoY - (pImgHeight - tileHeight), pImgWidth, pImgHeight);
            }
            // ب) اگر عکس نبود، رسم هندسی زاپاس سبز رنگ
            else {
                double plantHeight = tileHeight * 0.8;
                double pTopX = isoX + tileWidth / 2;
                double pTopY = isoY + tileHeight / 2 - plantHeight;
                double pLeftX = isoX + tileWidth * 0.25;
                double pLeftY = isoY + tileHeight * 0.5;
                double pRightX = isoX + tileWidth * 0.75;
                double pRightY = isoY + tileHeight * 0.5;
                double pBottomX = isoX + tileWidth / 2;
                double pBottomY = isoY + tileHeight * 0.75;

                gc.setFill(Color.web("#00c853"));
                gc.fillPolygon(new double[]{pLeftX, pBottomX, pRightX, pTopX}, new double[]{pLeftY, pBottomY, pRightY, pTopY}, 4);
                gc.setFill(Color.web("#00e676"));
                gc.fillOval(isoX + tileWidth * 0.4, isoY + tileHeight * 0.3, tileWidth * 0.2, tileHeight * 0.3);
            }
        }

// ۳. رسم نوار پیشرفت پروژه‌های در حال ساخت/آپگرید (به بیرون از حلقه گیاه منتقل شد تا باگ گرافیکی ندهد)
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
        gc.setFill(Color.web("#3e2723", radiationFactor * 0.2)); // مقدار شفافیت کاهش یافت تا عکس‌ها واضح دیده شوند
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
}