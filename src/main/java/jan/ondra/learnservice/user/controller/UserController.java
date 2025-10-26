package jan.ondra.learnservice.user.controller;

import jakarta.validation.Valid;
import jan.ondra.learnservice.user.mapper.UserMapper;
import jan.ondra.learnservice.user.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public void createUser(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid UserDTO userDTO) {
        userService.createUser(UserMapper.toCreateUser(jwt.getSubject(), userDTO));
    }

    @GetMapping
    @ResponseStatus(OK)
    public UserDTO getUser(@AuthenticationPrincipal Jwt jwt) {
        return UserMapper.toUserDTO(userService.getUser(jwt.getSubject()));
    }

    @PutMapping
    @ResponseStatus(NO_CONTENT)
    public void modifyUser(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid UserDTO userDTO) {
        userService.modifyUser(UserMapper.toModifyUser(jwt.getSubject(), userDTO));
    }

    @DeleteMapping
    @ResponseStatus(NO_CONTENT)
    public void deleteUser(@AuthenticationPrincipal Jwt jwt) {
        userService.deleteUser(jwt.getSubject());
    }

}
