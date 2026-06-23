package model.user;

public enum AuthStatus {
    SUCCESS,
    USERNAME_NOT_FOUND,
    USERNAME_ALREADY_EXIST,
    WRONG_PASSWORD,
    INVALID_PASSWORD,
    INVALID_USERNAME,
}
