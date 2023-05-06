package github.com.itstoony.product.controller;

import github.com.itstoony.product.dto.AuthenticationData;
import github.com.itstoony.product.dto.UserDTO;
import github.com.itstoony.product.model.Product.User;
import github.com.itstoony.product.security.jwt.DataTokenJWT;
import github.com.itstoony.product.security.jwt.TokenService;
import github.com.itstoony.product.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationManager manager;
    private final TokenService tokenService;
    private final UserService userService;
    private final ModelMapper modelMapper;


    @PostMapping("/login")
    public ResponseEntity<DataTokenJWT> login(@RequestBody @Valid AuthenticationData data) {
        log.info("Login with user: {}", data.login());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        Authentication authentication = manager.authenticate(authenticationToken);

        String tokenJWT = tokenService.generateToken((User) authentication.getPrincipal());

        return ResponseEntity.ok(new DataTokenJWT(tokenJWT));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid UserDTO dto) {
        log.info("Registering user: {}", dto.getLogin());
        User user = modelMapper.map(dto, User.class);

        User savedUser = userService.register(user);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedUser.getId()).toUri();

        return ResponseEntity.created(uri).build();
    }

}
