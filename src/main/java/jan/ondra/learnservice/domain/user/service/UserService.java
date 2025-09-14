package jan.ondra.learnservice.domain.user.service;

import jan.ondra.learnservice.domain.user.model.User;
import jan.ondra.learnservice.domain.user.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        userRepository.persistUser(user);
    }

    public UUID getUserIdByAuthId(String authId) {
        return userRepository.getUserIdByAuthId(authId);
    }

    public User getUser(String authId) {
        return userRepository.getUser(authId);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public void deleteUser(String authId) {
        userRepository.deleteUser(authId);
    }

}
