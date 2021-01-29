package com.example.sshomework.config;

import com.example.sshomework.entity.User;
import com.example.sshomework.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * @author Aleksey Romodin
 *
 * Отслеживание и обработка удачных/неудачных попыток аутентификации
 */

@Component
public class AuthenticationTracker {
    private static final int MAX_ATTEMPT_INPUT = 3;         //Максимальное количество попыток входа с неверным паролем
    private static final int MIN_DELAY_MINUTES_BLOCKED = 5; //Минимальное количество минут блокировки, после окончания попыток входа с неверным паролем

    private final UserRepository userRepository;

    @Autowired
    public AuthenticationTracker(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Отслеживание событий
     * @param event Событие
     */
    @EventListener
    public void authenticationTracking(ApplicationEvent event) {
        //Удачная попытка
        if (event instanceof InteractiveAuthenticationSuccessEvent) {
            processingEvent(((InteractiveAuthenticationSuccessEvent) event).getAuthentication().getName(), false);
        }
        //Неудачная попытка
        if (event instanceof AuthenticationFailureBadCredentialsEvent) {
            processingEvent(((AuthenticationFailureBadCredentialsEvent) event).getAuthentication().getName(), true);
        }
    }

    /**
     * Обработка попыток аутентификации
     * @param username Логин пользователя
     * @param event false->удачная попытка, true->неудачная попытка
     */
    private void processingEvent(String username, boolean event) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            User account = user.get();
            if (event) {
                long delayBetweenAttempts = ChronoUnit.MINUTES.between(account.getDateTimeInput(), LocalDateTime.now());
                int attempt = account.getLoginAttemptInput();

                if ((attempt < MAX_ATTEMPT_INPUT) && (delayBetweenAttempts < MIN_DELAY_MINUTES_BLOCKED)) {
                    attempt++;
                    account.setLoginAttemptInput(attempt);
                }
            } else {
                account.setLoginAttemptInput(0);
            }

            account.setDateTimeInput(LocalDateTime.now());
            userRepository.save(account);
        }
    }

    /**
     * Получение статуса для аккаунта
     * @param account аккаунт пользователя
     * @return блокировка true/false
     */
    public boolean getStatusLockedAccount(User account) {
        long delayBetweenAttempts = ChronoUnit.MINUTES.between(account.getDateTimeInput(), LocalDateTime.now());
        int attempt = account.getLoginAttemptInput();
        if ((attempt == MAX_ATTEMPT_INPUT) && (delayBetweenAttempts < MIN_DELAY_MINUTES_BLOCKED)) {
            return true;
        }
        if (delayBetweenAttempts > MIN_DELAY_MINUTES_BLOCKED) {
            account.setDateTimeInput(LocalDateTime.now());
            account.setLoginAttemptInput(0);
            userRepository.save(account);
        }
        return false;
    }

}
