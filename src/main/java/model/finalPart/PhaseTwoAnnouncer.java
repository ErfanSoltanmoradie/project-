package model.finalPart;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PhaseTwoAnnouncer {

    private static final CopyOnWriteArrayList<String> announcements = new CopyOnWriteArrayList<>();

    private static final String PHASE_TWO_MESSAGE =
            "Congratulations! Your colony has survived Phase 1 and entered Phase 2.\n\n" +
                    "The radioactive clouds hovering over the colonies were only a small fragment " +
                    "of a massive mother cloud floating in the upper layers of Earth's atmosphere.\n\n" +
                    "This mother cloud is steadily expanding, and if it is not neutralized, " +
                    "it will destroy humanity's fledgling civilization forever.\n\n" +
                    "The only way to survive is to build the \"Global Neutralizer Tower.\" " +
                    "Using advanced technology, this tower can absorb and destroy the mother cloud's radiation.\n\n" +
                    "From this moment, all alliances are automatically dissolved and the final stage of the game begins. " +
                    "From now on, only military strength, resource management, and the ability to defend your colony " +
                    "will determine the fate of the world.";

    public static void announcePhaseTwoStarted() {
        announcements.add(PHASE_TWO_MESSAGE);
    }

    public static List<String> getAnnouncements() {
        return announcements;
    }

    public static int getAnnouncementCount() {
        return announcements.size();
    }
}