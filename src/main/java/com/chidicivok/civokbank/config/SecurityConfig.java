package com.chidicivok.civokbank.config;

import com.chidicivok.civokbank.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /*
     * SecurityFilterChain is interface at core of Spring Security that contains list of HttpServlet filters
     * Every incoming Http request passes through this chain with each filter performing a specific task
     * The filters include
     * 1. DisableEncodeUrlFilter - prevent sessions IDs in URLs
     * 2. WebAsyncManagerIntegrationFilter - propagates SecurityContext to async threads
     * 3. SecurityContextHolderFilter - loads and saves SecurityCOntext (contains currently authenticated principal)
     * 4. HeaderWriterFilter - Adds security Http response headers to processed requests by Spring application
     * 5. CorsFilter - handles CORS (Cross Origin Resource Sharing), which allows backend to handle request from different origins such as when front end and backend are hosted differently
     * 6. CsrfFilter - validates CSRF(Cross Site Request Forgery) tokens
     * 7. LogoutFilter - processes Logout
     * 8. UsernamePasswordAuthenticationFilter - handles form login
     * 9. DefaultLogoutPageGeneratingFilter - auto generates logout pages
     * 10.DefaultLogInPageGeneratingFilter - auto generates Login page
     * 11. SessionManagementFilter - manages Session fixation and concurrency control
     *
     * HttpSecurity is a configuration class that allows the configuration of web based security for specific Http requests
     *
     * -----------------------------------------------------------------------------------------
     * CSRF - Cross Site Forgery Request - is a security risk that exploits the application's trust on a user browser
     *
     * An attacker can create a hidden form on a website that makes use of the browser's active token to perform request the user did not authorize
     *
     * e.g. A user signs in to the bank, and then in another tab tries to visit a site with hidden form that embeds a transfer url and submit to transfer money to the attacker
     * The browser automatically adds the cookie to authenticate that hidden request and the bank thinking it is still you performs the transaction with user's consent
     *
     * The issue is that browsers are designed to automatically store and add your session IDs to requests made on your browser
     *
     * With CSRF Token - we add a secondary value required (almost like a secondary username and password with session id and token value)
     * Browsers do not automatically forward your tokens to site requests, and thus we prevent that exploitation
     *
     * disable() - temporary to ease testing process on postman. if not you will have to fetch token values as well
     *--------------------------------------------------------------------------------------------
     * authorizeHttpRequests() - used to configure authorization rules
     *
     * requestMatchers(String URL) - checks if the request matches a specified URL or URL Patter(/**)
     *
     * permitAll() - allows for no authentication to a specific URL or URL pattern
     *
     * hasRole(String Role) - requires that the authenticated user has a certain role to access a specific URL
     *
     * hasAnyRole(List<String> Roles) - specifies all roles permitted on a specific URL or URL patterns
     *
     * anyRequest() - refers to any other requests to the application
     *
     * authenticated() - ensures that user must be authenticated
     *----------------------------------------------------------------------------------------------
     * httpBasic() - Spring Security basic http request for Username and Password
     *
     * Customizer - Spring functional interface used to apply custom configurations to components and objects - here in the case of httpBasic
     *
     * withDefaults() - method of the Customizer interface that defaults the components
     *
     * formLogin()- default form page
     *
     * build()
     * */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // disable csrf token
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Authorize URLs
                .authorizeHttpRequests(
                        // check for the customer create request6 and allow everyone to use it
                        auth -> auth.requestMatchers(HttpMethod.POST, "/api/customers/register").permitAll()
                                .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                                // grant the special admin its access alone
                                .requestMatchers(HttpMethod.POST, "/api/admin/create").hasRole("AUTHORIZED_ADMIN")
                                // now admins of both types using the .hasAnyRole()
                                .requestMatchers("/api/admin/**").hasAnyRole("AUTHORIZED_ADMIN", "ADMIN")
                                // admin exchange rate access
                                .requestMatchers(HttpMethod.PUT, "/api/exchange-rates").hasAnyRole("ADMIN", "AUTHORIZED_ADMIN")
                                // remaining admin pages access
                                .requestMatchers("/api/admin/**").hasAnyRole("ADMIN", "AUTHORIZED_ADMIN")
                                // every other customer URL to be authenticated by user with CUSTOMER role
                                .requestMatchers("/api/customers/**").hasRole("CUSTOMER")
                                .requestMatchers("/api/accounts/**").hasRole("CUSTOMER")
                                .requestMatchers("/api/transfers/**").hasRole("CUSTOMER")
                                .requestMatchers("/api/otp/**").hasRole("CUSTOMER")
                                .requestMatchers("/api/notifications/**").hasRole("CUSTOMER")
                                .requestMatchers("/api/vaults/**").hasRole("CUSTOMER")
                                .requestMatchers("/api/external-banks/**").hasRole("CUSTOMER")

                                .requestMatchers(HttpMethod.GET, "/api/exchange-rates").hasAnyRole("CUSTOMER", "AUTHORIZED_ADMIN", "ADMIN")

                                // audit logs


                                .anyRequest()
                                .authenticated()
                ).httpBasic(Customizer.withDefaults())
                .formLogin(form -> form.permitAll());

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}

