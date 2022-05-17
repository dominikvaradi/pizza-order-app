package hu.dominikvaradi.pizzaorderapp.config;

import hu.dominikvaradi.pizzaorderapp.security.jwt.JwtAuthenticationEntryPoint;
import hu.dominikvaradi.pizzaorderapp.security.jwt.JwtTokenAuthenticationFilter;
import hu.dominikvaradi.pizzaorderapp.security.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsService;

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    private final JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, JwtAuthenticationEntryPoint authenticationEntryPoint, JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter) {
        this.jwtTokenAuthenticationFilter = jwtTokenAuthenticationFilter;
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    private static final String[] AUTH_POST_WHITELIST = {
            // Authorization
            "/api/auth/login",
            "/api/auth/refresh-token",
            "/api/users",
            // Swagger-UI
            "/v3/api-docs/**",
            "/swagger-ui/**"
    };

    private static final String[] AUTH_GET_WHITELIST = {
            // Swagger-UI
            "/v3/api-docs/**",
            "/swagger-ui/**",
            // Pizza
            "/api/pizzas",
            "/api/pizzas/*",
            "/api/pizzas/*/image",
            // TODO teszt
            "/api/test"
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
            .and()
            .csrf().disable()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
            .and()
            .addFilterBefore(jwtTokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, AUTH_POST_WHITELIST).permitAll()
                .antMatchers(HttpMethod.GET, AUTH_GET_WHITELIST).permitAll()
                .anyRequest().authenticated();
    }
}
