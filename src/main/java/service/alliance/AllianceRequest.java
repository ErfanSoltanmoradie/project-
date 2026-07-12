package service.alliance;

import model.player.Player;

import java.time.Instant;
import java.util.UUID;

public class AllianceRequest {

    private AllianceStatus allianceStatus;
    private UUID id;
    private Player sender;
    private Player receiver;
    private Instant requestTime;

    public AllianceRequest(Player sender, Player receiver) {
        this.id = UUID.randomUUID();
        this.sender = sender;
        this.receiver = receiver;
        this.allianceStatus = AllianceStatus.PENDING;
        this.requestTime = Instant.now();
    }

    public AllianceStatus getAllianceStatus() {
        return allianceStatus;
    }

    public void setAllianceStatus(AllianceStatus allianceStatus) {
        this.allianceStatus = allianceStatus;
    }

    public UUID getId() {
        return id;
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
}