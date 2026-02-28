package my.web.app.config;

import my.web.app.service.CustomUserDetailsService;
import org.springframework.context.annotation.*;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
Основные элементы конфигурации:
userDetailsService: загружает данные пользователей из базы данных.
PasswordEncoder: отвечает за хеширование (шифрование) паролей пользователей.
AuthenticationProvider: связывет компоненты аутентификацией пользователей.
SecurityFilterChain: настраивает правила безопасности.
*/


/*
Алгоритм работы:
Пользователь вводит пароль и логин.
Затем класс CustomUserDetailsService загружает данные пользователя из базы данных.
BCryptPasswordEncoder проверяет соответствие введенного пароля и хешированного пароля в базе данных.
При успешной аутентификации пользователь получает доступ к странице /users.
Только к users нет доступа для всех.
После выхода перейдет на главную страницу
 */

@Configuration
@EnableWebSecurity //включаем настройку веб-безопасности
public class WebSecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
       // Указываем, что используется алгоритм шифрования BCript для шифрования паролей
       return new BCryptPasswordEncoder();
   }

   @Bean
   UserDetailsService userDetailsService() {
        return new CustomUserDetailsService(); // Реализация нашего сервиса
   }

   // настройка провайдера аутентификации
    @Bean
    public DaoAuthenticationProvider authenticationProvider () {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // Устанвока сервиса для загрузки пользователей
        authProvider.setUserDetailsService(userDetailsService());
        // Установка кодировщика паролей
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    // Основаная конфигурация безовасности нашего приложения
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http /* настрока авторизации запросов */
                .authorizeHttpRequests(auth ->auth
                        /* Предоставляем доступ к станице clients и list только аутентифицированному юзеру*/
                        .requestMatchers("/ord/clients").authenticated()
                        .requestMatchers("/ord/list").authenticated()
                        /* Предоставляем доступ ко всем остальным страницам */
                        .anyRequest().permitAll()
                )
                /* настройка формы входа, используем login вместо username */
                .formLogin(form -> form
                        .loginPage("/ord/login") /* переадресация на страницу login если она кастомная */
                        .usernameParameter("login") /* по умолчанию usernameParameter("username"); */
                        /*.passwordParameter("password")*/ /* занчение по умолчанию */
                        /* переадресация на страницу list после успешной авторизации */
                        .defaultSuccessUrl("/ord/list")
                        /* разрешаем всем пользователям заполнение формы */
                        .permitAll())
                /* настройка выхода */
                .logout(logout -> logout
                        //.logoutUrl("/ord/logout") /* форма logout если она кастомная*/
                        .logoutSuccessUrl("/ord")
                        .permitAll());

                
        return http.build();
    }


}
