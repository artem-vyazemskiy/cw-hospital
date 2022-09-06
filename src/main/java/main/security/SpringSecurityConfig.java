package main.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .authorizeRequests()
                    .antMatchers(
                            "/diagnoses",
                            "/diagnoses/add",
                            "/diagnoses/{id}",
                            "/diagnoses/{id}/delete")
                        .hasAnyRole("ADMIN", "USER")
                    .antMatchers(
                            "/diagnoses",
                            "/diagnoses/{id}")
                        .permitAll()

                    .antMatchers("/people/{id}/delete")
                        .hasAnyRole("ADMIN")
                    .antMatchers(
                            "/people",
                            "/people/add",
                            "/people/getWithDiagnosis",
                            "/people/{id}",
                            "/people/{id}/update")
                        .hasAnyRole("ADMIN", "USER")
                    .antMatchers(
                            "/people",
                            "/people/getWithDiagnosis",
                            "/people/{id}")
                        .permitAll()

                    .antMatchers(
                            "/wards/add",
                            "/wards/{id}/update",
                            "/wards/{id}/delete")
                        .hasAnyRole("ADMIN")
                    .antMatchers(
                            "/wards",
                            "/wards/getWithDiagnosis",
                            "/wards/{id}")
                        .permitAll()

                    .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin()
                    .loginPage("/auth/login")
                    .defaultSuccessUrl("/people", true)
                    .permitAll()
                .and()
                .logout()
                    .logoutUrl("/auth/logout");
        // @formatter:on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        auth.inMemoryAuthentication()
                .withUser("user").password(encoder.encode("user-password")).roles("USER")
                .and()
                .withUser("admin").password(encoder.encode("admin-password")).roles("ADMIN");
    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**", "/icon/**");
    }

}
