package model.user;

import model.player.Player;

public class AuthResult {
    private AuthStatus authStatus;
    private Player player;


    public AuthResult(AuthStatus authStatus, Player player) {
        this.authStatus = authStatus;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public AuthStatus getAuthStatus() {
        return authStatus;
    }

    public boolean isSuccess(){
        if(this.authStatus == AuthStatus.SUCCESS)
            return true;
        return false;
    }
}
