package model.repository;

import model.user.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserRepository {

    private Map<String, User> userRepository;

    public UserRepository(Map<String, User> userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user){
        this.userRepository.put(user.getUsername(), user);
    }

    public boolean exists(String username) {
        return this.userRepository.containsKey(username);
    }

    public User findUserByUsername(String username){
        if(this.exists(username))
            return this.userRepository.get(username);
        return null;
    }

    public Map<String, User> getAllUsers(){
        return this.userRepository;
    }
}
