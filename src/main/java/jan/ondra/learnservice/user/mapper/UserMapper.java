package jan.ondra.learnservice.user.mapper;

import jan.ondra.learnservice.user.controller.UserDTO;
import jan.ondra.learnservice.user.model.CreateUser;
import jan.ondra.learnservice.user.model.ModifyUser;
import jan.ondra.learnservice.user.model.User;

public class UserMapper {

    public static UserDTO toUserDTO(User user) {
        return new UserDTO(
            user.notificationEnabled(),
            user.notificationEmail(),
            user.notificationTime(),
            user.timeZone(),
            user.language()
        );
    }

    public static CreateUser toCreateUser(String authSubject, UserDTO userDTO) {
        return new CreateUser(
            authSubject,
            userDTO.notificationEnabled(),
            userDTO.notificationEmail(),
            userDTO.notificationTime(),
            userDTO.timeZone(),
            userDTO.language()
        );
    }

    public static ModifyUser toModifyUser(String authSubject, UserDTO userDTO) {
        return new ModifyUser(
            authSubject,
            userDTO.notificationEnabled(),
            userDTO.notificationEmail(),
            userDTO.notificationTime(),
            userDTO.timeZone(),
            userDTO.language()
        );
    }

}
