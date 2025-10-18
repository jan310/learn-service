package jan.ondra.learnservice.domain.user.service;

import jan.ondra.learnservice.domain.user.model.CreateUser;
import jan.ondra.learnservice.domain.user.model.UpdateUser;
import jan.ondra.learnservice.domain.user.model.User;
import jan.ondra.learnservice.domain.user.persistence.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(CreateUser createUser) {
        userRepository.persistUser(createUser);
    }

    public User getUser(String authSubject) {
        return userRepository.getUser(authSubject);
    }

    public void updateUser(UpdateUser updateUser) {
        userRepository.updateUser(updateUser);
    }

    public void deleteUser(String authSubject) {
        userRepository.deleteUser(authSubject);
    }

}
