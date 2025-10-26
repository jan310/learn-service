package jan.ondra.learnservice.user.service;

import jan.ondra.learnservice.user.model.CreateUser;
import jan.ondra.learnservice.user.model.ModifyUser;
import jan.ondra.learnservice.user.model.User;
import jan.ondra.learnservice.user.repository.UserRepository;
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

    public void modifyUser(ModifyUser modifyUser) {
        userRepository.updateUser(modifyUser);
    }

    public void deleteUser(String authSubject) {
        userRepository.deleteUser(authSubject);
    }

}
