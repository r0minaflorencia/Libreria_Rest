package com.libreria.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.libreria.services.UsuarioServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        @Autowired
        private UsuarioServiceImpl usuarioService; 

        @Bean
        public AuthenticationManager authenticationManager(
                        AuthenticationConfiguration authenticationConfiguration) throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Bean
        public AuthenticationProvider authenticationProvider() {
                DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
                authProvider.setUserDetailsService(usuarioService);
                authProvider.setPasswordEncoder(passwordEncoder);
                return authProvider;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

                http
                                .csrf(csrfConfig -> csrfConfig.disable())
                                // Deshabilitar CSRF solo para desarrollo (en producción considera habilitarlo)
                                .csrf(csrfConfig -> csrfConfig.disable())

                                // CAMBIO IMPORTANTE: Usar sesiones en lugar de STATELESS
                                .sessionManagement(sessionManagementConfig -> sessionManagementConfig
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                                .maximumSessions(1)
                                                .maxSessionsPreventsLogin(false))

                                .authorizeHttpRequests(authConfig -> {

                                        // Recursos estáticos públicos
                                        authConfig.requestMatchers("/img/**", "/css/**", "/js/**", "/favicon.ico",
                                                        "/webjars/**")
                                                        .permitAll();

                                        // Endpoints públicos
                                        authConfig.requestMatchers(HttpMethod.GET, "/").permitAll();
                                        authConfig.requestMatchers(HttpMethod.GET, "/error").permitAll();
                                        authConfig.requestMatchers(HttpMethod.GET, "/*/lista").permitAll();
                                        authConfig.requestMatchers(HttpMethod.GET, "/*/busqueda").permitAll();
                                        authConfig.requestMatchers(HttpMethod.GET, "/*/info/*").permitAll();

                                        // Endpoints de autenticación (deben ser públicos)
                                        authConfig.requestMatchers(HttpMethod.GET, "/usuario/register").permitAll();
                                        authConfig.requestMatchers(HttpMethod.POST, "/usuario/signup").permitAll();
                                        authConfig.requestMatchers(HttpMethod.GET, "/usuario/login").permitAll();

                                        // Endpoints que requieren autenticación
                                        authConfig.requestMatchers(HttpMethod.GET, "/usuario/logout").authenticated();
                                        authConfig.requestMatchers(HttpMethod.GET, "/usuario/me").authenticated();
                                        authConfig.requestMatchers(HttpMethod.GET, "/usuario/panel").authenticated();
                                        authConfig.requestMatchers(HttpMethod.POST, "/usuario/become-admin")
                                                        .authenticated();

                                        // Endpoints de administración
                                        authConfig.requestMatchers(HttpMethod.GET, "/*/registrar").hasRole("ADMIN");
                                        authConfig.requestMatchers(HttpMethod.POST, "/*/registro").hasRole("ADMIN");
                                        authConfig.requestMatchers(HttpMethod.GET, "/*/eliminar").hasRole("ADMIN");
                                        authConfig.requestMatchers(HttpMethod.GET, "/*/modificar").hasRole("ADMIN");
                                        authConfig.requestMatchers(HttpMethod.POST, "/*/modificar").hasRole("ADMIN");

                                        // Cualquier otra request requiere autenticación
                                        authConfig.anyRequest().authenticated();
                                })

                                // Configuración de login form
                                .formLogin(formLogin -> formLogin
                                                .loginPage("/usuario/login")
                                                .loginProcessingUrl("/usuario/login")
                                                .usernameParameter("email")
                                                .passwordParameter("password")
                                                .defaultSuccessUrl("/", true)
                                                .failureUrl("/usuario/login?error=true")
                                                .permitAll())

                                // Configuración de logout
                                .logout(logout -> logout
                                                .logoutUrl("/usuario/logout")
                                                .logoutSuccessUrl("/usuario/login?logout=true")
                                                .invalidateHttpSession(true)
                                                .deleteCookies("JSESSIONID")
                                                .permitAll());
                return http.build();

        }

}
