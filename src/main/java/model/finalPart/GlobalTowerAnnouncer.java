package model.finalPart;

import java.util.concurrent.CopyOnWriteArrayList;

public class GlobalTowerAnnouncer {

    private static final CopyOnWriteArrayList<String> announcements = new CopyOnWriteArrayList<>();

    public static void announceTowerBuilt(String villageUsername) {
        announcements.add(villageUsername + " has completed the Global Tower! The world now depends on their defense.");
    }

    public static java.util.List<String> getAnnouncements() {
        return announcements;
    }

    public static int getAnnouncementCount() {
        return announcements.size();
    }
}