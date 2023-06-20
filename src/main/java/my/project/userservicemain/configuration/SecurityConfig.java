package my.project.userservicemain.configuration;

import lombok.RequiredArgsConstructor;
import my.project.userservicemain.configuration.jwt.AuthTokenFilter;
import my.project.userservicemain.configuration.jwt.UnauthHandler;
import my.project.userservicemain.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static org.springframework.http.HttpMethod.*;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthTokenFilter authTokenFilter;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new UnauthHandler())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/test/**").permitAll()
                .antMatchers(GET, "/**").authenticated()
                .antMatchers("/**").hasRole("ADMIN")
                .anyRequest().authenticated();

        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
