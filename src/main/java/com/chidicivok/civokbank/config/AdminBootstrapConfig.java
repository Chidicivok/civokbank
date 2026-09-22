package com.chidicivok.civokbank.config;

import com.chidicivok.civokbank.entities.Admin;
import com.chidicivok.civokbank.enums.UserRole;
import com.chidicivok.civokbank.repositories.AdminRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
/*
* Create the first Admin to store in database
* @Configuration - Class level annotation used to tell Spring that the class can be used by Spring IoC for Bean definitions and creations
* */
@Configuration
public class AdminBootstrapConfig {

    /*
    * CommandLineRunner interface - allows execution of code after application context is initialized but before application starts serving requests
    * Methods using CommandLineRunner must return args
    * Classes implementing CommandLineRunner must override the run(String... args)
    * Multiple CommandLineRunner classes and methods may be created and then ordered using the @Order(n) where n can be numerical value for the order of execution
    * Bean is an Object that is instantiated and managed by SpringBoot Inversion of Control container
    * It makes use of the @Bean annotation - a Method Level annotation
    * Inversion of Control - ???
    * ----------------------------------------------------------------------------------------------
    * @SpringBootApplication
    * public static void main(String[] args) {
    *       SpringApplication.run(DemoApplication.class, args);
    *       System.out.println("This is the main application method");
    * }
    *
    * @Order(3)
    * @Bean
    * public CommandLineRunner doStuff() {
    *       return args -> {
    *               System.out.println("Command Line runner method created first but given an order of 3");
    *       };
    * }
    *
    *
    * @Order(1)
    * @Bean
    * public CommandLineRunner doStuffAgain() {
    *       return args -> {
    *               System.out.println("Command Line runner method created second but given an order of 1");
    *       };
    * }
    *
    * }
    *
    * =============================OUTPUT====================================
    * Command Line runner method created second but given an order of 1
    * Command Line runner method created first but given an order of 3
    * This is the main application method
    * ========================================================================
    *
    *---------------------------------------------------------------------------------------------
    * */

    // declare cred
    @Value("${super.admin.firstname}")
    private String firstName;

    @Value("${super.admin.lastname}")
    private String lastName;

    @Value("${super.admin.email}")
    private String email;

    @Value("${super.admin.password}")
    private String password;


    @Bean
    public CommandLineRunner createAuthorizedAdmin(AdminRepository adminRepository, PasswordEncoder passwordEncoder){

        return args -> {
            if(adminRepository.findByEmail(email).isEmpty()) {

                Admin admin = new Admin();

                admin.setFirstName(firstName);
                admin.setLastName(lastName);
                admin.setEmail(email);
                admin.setPassword(passwordEncoder.encode(password));
                admin.setUserRole(UserRole.AUTHORIZED_ADMIN);
                admin.setActive(true);

                adminRepository.save(admin);

            }
        };

    }
}
