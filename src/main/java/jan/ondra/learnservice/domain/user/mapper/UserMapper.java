package jan.ondra.learnservice.domain.user.mapper;

import jan.ondra.learnservice.domain.user.api.UserDTO;
import jan.ondra.learnservice.domain.user.model.CreateUser;
import jan.ondra.learnservice.domain.user.model.UpdateUser;
import jan.ondra.learnservice.domain.user.model.User;

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

    public static UpdateUser toUpdateUser(String authSubject, UserDTO userDTO) {
        return new UpdateUser(
            authSubject,
            userDTO.notificationEnabled(),
            userDTO.notificationEmail(),
            userDTO.notificationTime(),
            userDTO.timeZone(),
            userDTO.language()
        );
    }

}
