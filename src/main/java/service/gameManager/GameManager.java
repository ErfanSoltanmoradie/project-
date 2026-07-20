package service.gameManager;

import model.finalPart.GlobalTower;
import model.player.Player;
import model.repository.PlayerRepository;
import model.repository.UserRepository;
import model.village.Village;
import model.world.WorldMap;
import service.filehandeling.GameState;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private static final Duration PHASE_ONE_DURATION = Duration.ofDays(7);
    private static final Duration PHASE_TWO_DURATION = Duration.ofDays(7);

    public static void checkGameWinner(List<Village> allVillages) {
        Village winnerVillage = null;

        for (Village village : allVillages) {
            GlobalTower tower = village.getGlobalTower();

            if (tower != null && tower.isActive()) {
                if (winnerVillage == null) {
                    winnerVillage = village;
                } else {
                    GlobalTower winnerTower = winnerVillage.getGlobalTower();

                    if (tower.getHp() > winnerTower.getHp()) {
                        winnerVillage = village;
                    } else if (tower.getHp() == winnerTower.getHp()) {
                        if (tower.getConstructionCompleteTime().isBefore(winnerTower.getConstructionCompleteTime())) {
                            winnerVillage = village;
                        }
                    }
                }
            }
        }

        if (winnerVillage != null) {
            System.out.println("Finnal winner: " + winnerVillage.getUserName());
        } else {
            System.out.println("The game doesn't have any winner");
        }
    }

    public static List<String> checkAndEnforcePhaseOneEnd(GameState gameState,
                                                          PlayerRepository playerRepository,
                                                          UserRepository userRepository,
                                                          WorldMap worldMap) {

        List<String> eliminatedUsernames = new ArrayList<>();

        if (gameState.isPhaseOneEnforced()) {
            return eliminatedUsernames;
        }

        Instant startTime = gameState.getGameStartTime();
        if (startTime == null) {
            return eliminatedUsernames;
        }

        if (Duration.between(startTime, Instant.now()).compareTo(PHASE_ONE_DURATION) < 0) {
            return eliminatedUsernames;
        }

        List<Player> snapshot = new ArrayList<>(playerRepository.getAllPlayers().values());

        for (Player player : snapshot) {
            player.getLock().writeLock().lock();
            try {
                Village village = player.getVillage();
                if (village == null) continue;

                boolean survived;
                village.getLock().readLock().lock();
                try {
                    survived = village.getCloud().isFullyNeutralized();
                } finally {
                    village.getLock().readLock().unlock();
                }

                if (!survived) {
                    player.setEliminationReason("Your colony failed to neutralize the cloud in time and was eliminated at the end of Phase 1.");
                    playerRepository.getAllPlayers().remove(player.getPlayerId());
                    gameState.getEliminatedUsernames().add(player.getUsername());
                    worldMap.releaseCoordinate(village.getCoordinate());
                    eliminatedUsernames.add(player.getUsername());
                }

            } finally {
                player.getLock().writeLock().unlock();
            }
        }
        dissolveAllAlliances(playerRepository);
        if (gameState.getPhaseTwoStartTime() == null) {
            gameState.setPhaseTwoStartTime(Instant.now());
        }

        gameState.setPhaseOneEnforced(true);

        System.out.println("Phase two start = " + gameState.getPhaseTwoStartTime());

        return eliminatedUsernames;
    }

    private static void dissolveAllAlliances(PlayerRepository playerRepository) {
        for (Player player : playerRepository.getAllPlayers().values()) {
            if (player.getAlliance() != null) {
                player.setAlliance(null);
            }
        }
    }

    public static String checkAndEnforcePhaseTwoEnd(GameState gameState,
                                                    PlayerRepository playerRepository,
                                                    UserRepository userRepository,
                                                    WorldMap worldMap) {

        if (!gameState.isPhaseOneEnforced()) {
            return null;
        }

        if (gameState.isPhaseTwoEnforced()) {
            return gameState.getGameWinner();
        }

        Instant phaseTwoStart = gameState.getPhaseTwoStartTime();

        if (phaseTwoStart == null) {
            return null;
        }


        long elapsed = Duration.between(phaseTwoStart, Instant.now()).getSeconds();

        if (Duration.between(phaseTwoStart, Instant.now())
                .compareTo(PHASE_TWO_DURATION) < 0) {
            return null;
        }

        List<Player> players =
                new ArrayList<>(playerRepository.getAllPlayers().values());

        Player winnerPlayer = null;
        GlobalTower winnerTower = null;

        boolean anyActiveTower = false;

        for (Player player : players) {

            Village village = player.getVillage();

            if (village == null)
                continue;

            GlobalTower tower = village.getGlobalTower();

            if (tower == null)
                continue;

            if (!tower.isActive())
                continue;

            anyActiveTower = true;

            if (winnerTower == null) {
                winnerTower = tower;
                winnerPlayer = player;
                continue;
            }

            if (tower.getHp() > winnerTower.getHp()) {
                winnerTower = tower;
                winnerPlayer = player;
            }
            else if (tower.getHp() == winnerTower.getHp()
                    && tower.getConstructionCompleteTime()
                    .isBefore(winnerTower.getConstructionCompleteTime())) {

                winnerTower = tower;
                winnerPlayer = player;
            }
        }

        String winner = null;

        if (anyActiveTower) {

            winner = winnerPlayer.getUsername();

            System.out.println("Winner = " + winner);

        } else {

        System.out.println("No active tower survived.");
        }

        gameState.setGameWinner(winner);
        gameState.setPhaseTwoEnforced(true);

        return winner;
    }

    public static boolean isPhaseTwoStarted(GameState gameState) {
        return gameState.getPhaseTwoStartTime() != null;
    }

    public static List<String> checkAndEnforceHealthElimination(GameState gameState,
                                                                PlayerRepository playerRepository,
                                                                UserRepository userRepository,
                                                                WorldMap worldMap) {

        List<String> eliminatedUsernames = new ArrayList<>();

        List<Player> snapshot = new ArrayList<>(playerRepository.getAllPlayers().values());

        for (Player player : snapshot) {
            player.getLock().writeLock().lock();
            try {
                Village village = player.getVillage();
                if (village == null) continue;

                boolean shouldEliminate;
                village.getLock().readLock().lock();
                try {
                    shouldEliminate = village.getHealth() <= 0;
                } finally {
                    village.getLock().readLock().unlock();
                }

                if (shouldEliminate) {
                    player.setEliminationReason("Your village's health reached zero and was destroyed.");
                    playerRepository.getAllPlayers().remove(player.getPlayerId());
                    gameState.getEliminatedUsernames().add(player.getUsername());
                    worldMap.releaseCoordinate(village.getCoordinate());
                    eliminatedUsernames.add(player.getUsername());
                }

            } finally {
                player.getLock().writeLock().unlock();
            }
        }

        return eliminatedUsernames;
    }

}