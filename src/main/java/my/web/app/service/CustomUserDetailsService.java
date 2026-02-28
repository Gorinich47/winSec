package my.web.app.service;

import my.web.app.models.User;
import my.web.app.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Данный класс активируется при аутентификации пользователя
// Ищем в системе пользователя с email, который он ввел.
// Если пользователь найден, то возвращаем объект класса CustomUserDetails

@Service
public class CustomUserDetailsService  implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Реализация единстверрного метода интерфейса UserDetailsService
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Поиск пользователя по email
        User user = userRepository.findByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return new CustomUserDetails(user);// Возвращаем CustomUserDetails c данными пользователя
    }
}
