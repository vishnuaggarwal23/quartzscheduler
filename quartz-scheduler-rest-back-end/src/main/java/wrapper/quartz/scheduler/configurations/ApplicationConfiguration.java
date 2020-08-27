package wrapper.quartz.scheduler.configurations;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import wrapper.quartz.scheduler.entity.jpa.User;
import wrapper.quartz.scheduler.service.UserService;
import wrapper.quartz.scheduler.util.LoggerUtility;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

/**
 * The type Application configuration.
 */
@Configuration
@Slf4j
public class ApplicationConfiguration extends WebMvcConfigurationSupport {

    private final UserService userService;

    /**
     * Instantiates a new Application configuration.
     *
     * @param userService the user service
     */
    public ApplicationConfiguration(UserService userService) {
        super();
        this.userService = userService;
    }

    /**
     * B crypt password encoder b crypt password encoder.
     *
     * @return the b crypt password encoder
     */
    @Bean
    @Primary
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(5);
    }

    /**
     * Account status user details check user details checker.
     *
     * @return the user details checker
     */
    @Bean
    @Primary
    UserDetailsChecker accountStatusUserDetailsCheck() {
        return new AccountStatusUserDetailsChecker();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(mappingJackson2HttpMessageConverter(converters));
        addDefaultHttpMessageConverters(converters);
        super.configureMessageConverters(converters);
    }

    private MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = (MappingJackson2HttpMessageConverter) converters.stream()
                .filter(it -> MappingJackson2HttpMessageConverter.class.isAssignableFrom(it.getClass()))
                .findAny()
                .orElse(new MappingJackson2HttpMessageConverter());
        mappingJackson2HttpMessageConverter
                .getObjectMapper()
                .setTimeZone(TimeZone.getTimeZone(ZoneId.of("UTC")))
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mappingJackson2HttpMessageConverter;
    }

    /**
     * Auditor aware auditor aware.
     *
     * @return the auditor aware
     */
    @Bean
    @Primary
    public AuditorAware<User> auditorAware() {
        return () -> {
            User user;
            try {
                user = userService.getCurrentLoggedIn();
            } catch (UsernameNotFoundException | AccountStatusException e) {
                LoggerUtility.error(log, e.getMessage());
                user = null;
            }
            return Optional.ofNullable(user);
        };
    }
}