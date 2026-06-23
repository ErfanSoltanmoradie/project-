package model.user;

import java.util.HashMap;
import java.util.Map;

public class UserManager {

    private Map<String, User> users;

    public UserManager(){
        users = new HashMap<>();
    }

    public boolean existsUser(String username){
        return users.containsKey(username);
    }

    public User findUser(String username){
        return users.get(username);
    }

    public void addUser(User user){
        users.put(user.getUsername(), user);
    }

}
