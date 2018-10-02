package com.officemaneger.configs;
import com.officemaneger.areas.user.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    private UserService userService;

    public SpringSecurityConfig() {
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userService).passwordEncoder(getBCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/", "/register", "/bootstrap/**", "/jquery/**", "/css/**", "/js/**", "/images/**",
                        "/unauthorized", "/workdays", "/annualLeaveTypes", "/ranks", "/shiftTypes", "/businessUnit",
                        "/businessUnit/view/**", "/phoneContactTypes", "/all/**").permitAll()
                .antMatchers("/administrator/**").access("hasRole('ADMIN') OR hasRole('ADMINISTRATOR')")
                .antMatchers("/analyst/**").access("hasRole('ADMIN') OR hasRole('ANALYST')")
                .antMatchers("/boss/**").access("hasRole('ADMIN') OR hasRole('BOSS')")
                .antMatchers("/user/**").access("hasRole('ADMIN') OR hasRole('USER')")
                .antMatchers("/changePassword", "/allUsers/**").access("hasRole('ADMIN') OR hasRole('BOSS') OR hasRole('ADMINISTRATOR') OR hasRole('ANALYST') OR hasRole('USER')")
                .antMatchers("/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                .rememberMe()
                .rememberMeCookieName("RememberMeCookie")
                .rememberMeParameter("rememberMe")
                .key("SecretKey")
                .tokenValiditySeconds(1000)
                .and()
                .logout().logoutSuccessUrl("/").logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/unauthorized")
                .and()
                .csrf().csrfTokenRepository(csrfTokenRepository());
    }

    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");

        return repository;
    }

    @Bean
    public BCryptPasswordEncoder getBCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
