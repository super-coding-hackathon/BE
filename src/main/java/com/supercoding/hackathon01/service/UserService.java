package com.supercoding.hackathon01.service;

import com.supercoding.hackathon01.dto.user.request.LoginRequest;
import com.supercoding.hackathon01.dto.user.request.SignupRequest;
import com.supercoding.hackathon01.dto.user.response.LoginResponse;
import com.supercoding.hackathon01.dto.user.response.MyInfoResponse;
import com.supercoding.hackathon01.entity.User;
import com.supercoding.hackathon01.error.CustomException;
import com.supercoding.hackathon01.error.domain.UserErrorCode;
import com.supercoding.hackathon01.repository.UserRepository;
import com.supercoding.hackathon01.security.AuthHolder;
import com.supercoding.hackathon01.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public LoginResponse signup(SignupRequest signupRequest) {
        checkDuplicateEmail(signupRequest.getEmail());
        checkConfirmPassword(signupRequest.getPassword(), signupRequest.getPasswordCheck());
        User user = User.from(signupRequest, passwordEncoder.encode(signupRequest.getPassword()));

        userRepository.save(user);
        String accessToken = TokenProvider.createToken(user);
        return LoginResponse.from(user, accessToken);
    }

    public MyInfoResponse getMyInfo() {
        User user = userRepository.findById(AuthHolder.getUserId()).orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));
        return MyInfoResponse.from(user);
    }


    public LoginResponse login(LoginRequest loginRequest) {
        User user = validUserByEmail(loginRequest.getEmail());
        checkPassword(loginRequest.getPassword(), user.getPassword());
        String accessToken = TokenProvider.createToken(user);

        return LoginResponse.from(user, accessToken);
    }

    private void checkConfirmPassword(String password, String passwordCheck) {
        if (!password.equals(passwordCheck)) {
            throw new CustomException(UserErrorCode.NOT_MATCH_PASSWORD_CONFIRM);
        }
    }

    private void checkDuplicateEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(UserErrorCode.DUPLICATE_USER_ID);
        }
    }

    private void checkPassword(String requestPassword, String password) {
        if (!passwordEncoder.matches(requestPassword, password)) {
            throw new CustomException(UserErrorCode.BAD_REQUEST_PASSWORD);
        }
    }

    private User validUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->new CustomException(UserErrorCode.NOT_FOUND_USER));
    }
}
