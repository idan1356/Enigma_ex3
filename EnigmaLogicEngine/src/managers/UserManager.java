package managers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserManager {
    private final HashSet<String> userManager;

    public UserManager(){
        this.userManager = new HashSet<>();
    }

    public synchronized void addUser(String username) {
        userManager.add(username);
    }

    public synchronized void removeUser(String username) {
        userManager.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(userManager);
    }
    public boolean isUserExists(String username) {
        return userManager.contains(username);
    }
}
