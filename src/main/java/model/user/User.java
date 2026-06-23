package model.user;

import java.io.Serializable;
import java.util.UUID;

public class User  implements Serializable { // userId = playerId
    private String username;
    private UUID id;
    private String passwordHash;


    public User(String username, String password, UUID playerId){
        this.id = playerId;
        this.username = username;
        this.passwordHash = password;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
