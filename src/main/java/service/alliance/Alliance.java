package service.alliance;

import model.player.Player;

import java.io.Serializable;
import java.util.UUID;

public class Alliance implements Serializable {
    private UUID allianceId;
    private Player sender;
    private Player receiver;

    private boolean scienceBonusGiven;

    public Alliance(Player sender, Player receiver) {
        this.allianceId = UUID.randomUUID();
        this.sender = sender;
        this.receiver = receiver;
        this.scienceBonusGiven = false;
    }

    public UUID getAllianceId() {
        return allianceId;
    }

    public Player getSender() {
        return sender;
    }

    public void setSender(Player sender) {
        this.sender = sender;
    }

    public Player getReceiver() {
        return receiver;
    }

    public void setReceiver(Player receiver) {
        this.receiver = receiver;
    }

    public boolean isScienceBonusGiven() {
        return scienceBonusGiven;
    }

    public void setScienceBonusGiven(boolean scienceBonusGiven) {
        this.scienceBonusGiven = scienceBonusGiven;
    }
}
