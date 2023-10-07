package com.supercoding.hackathon01.service;

import com.supercoding.hackathon01.entity.Home;
import com.supercoding.hackathon01.entity.Transaction;
import com.supercoding.hackathon01.entity.User;
import com.supercoding.hackathon01.error.CustomException;
import com.supercoding.hackathon01.error.domain.HomeErrorCode;
import com.supercoding.hackathon01.error.domain.UserErrorCode;
import com.supercoding.hackathon01.repository.UserRepository;
import com.supercoding.hackathon01.repository.home.HomeRepository;
import com.supercoding.hackathon01.repository.transaction.TransactionRepository;
import com.supercoding.hackathon01.security.AuthHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final HomeRepository homeRepository;

    public Void requestTransaction(Long homeId) {
        User user = validUser(AuthHolder.getUserId());
        Home home = validHome(homeId);
        Transaction transaction = Transaction.of(home, user);
        transactionRepository.save(transaction);
        return null;
    }

    private User validUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new CustomException(UserErrorCode.NOT_FOUND_USER));
    }

    private Home validHome(Long homeId) {
        return homeRepository.findById(homeId).orElseThrow(() -> new CustomException(HomeErrorCode.NOT_FOUND_HOME));
    }
}
