package model.finalPart;

import model.world.Coordinate;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

public class GlobalTower implements Serializable {
    private static final int MAX_HP = 1200;
    public static final int WIDTH = 2;
    public static final int HEIGHT = 2;

    private static final Duration PROTECTION_DURATION = Duration.ofHours(24);

    private int hp;
    private boolean isActive;
    private LocalDateTime constructionCompleteTime;
    private String builderVillageUsername;
    private Coordinate position;

    public GlobalTower() {
        this.hp = MAX_HP;
        this.isActive = false;
    }

    public void active() {
        this.isActive = true;
        constructionCompleteTime = LocalDateTime.now();
    }

    public boolean damageTower(int dmg) {
        if(!isActive) return false;

        this.hp -= dmg;
        if(this.hp <= 0) {
            this.hp = 0;
            this.isActive = false;
            return true;
        }
        return false;
    }

    public boolean isUnderProtection() {
        if (constructionCompleteTime == null) return false;
        Duration elapsed = Duration.between(constructionCompleteTime, LocalDateTime.now());
        return elapsed.compareTo(PROTECTION_DURATION) < 0;
    }

    // مدت زمان باقی‌مانده از بازه‌ی حفاظت (برای نمایش در رابط کاربری)
    public Duration getRemainingProtection() {
        if (constructionCompleteTime == null) return Duration.ZERO;
        Duration elapsed = Duration.between(constructionCompleteTime, LocalDateTime.now());
        Duration remaining = PROTECTION_DURATION.minus(elapsed);
        return remaining.isNegative() ? Duration.ZERO : remaining;
    }

    public int getHp() {return hp;}
    public int getMaxHp() {return MAX_HP;}
    public boolean isActive() {return isActive;}
    public LocalDateTime getConstructionCompleteTime() {return constructionCompleteTime;}

    public String getBuilderVillageUsername() {return builderVillageUsername;}
    public void setBuilderVillageUsername(String builderVillageUsername) {this.builderVillageUsername = builderVillageUsername;}

    public Coordinate getPosition() {return position;}
    public void setPosition(Coordinate position) {this.position = position;}
}