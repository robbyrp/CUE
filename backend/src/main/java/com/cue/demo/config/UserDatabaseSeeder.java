package com.cue.demo.config;

import com.cue.demo.entities.User;
import com.cue.demo.enums.UserRole;
import com.cue.demo.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserDatabaseSeeder {
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
          if (userRepository.count() == 0) {
              User maria = User.builder()
                      .role(UserRole.ADMIN)
                      .username("maria_surubaru1")
                      .phoneNumber("0740000001")
                      .profilePictureUrl("https://ui-avatars.com/api/?name=Maria+Surubaru")
                      .bio("Pasionata de artă și spectacole.")
                      .firstName("Maria")
                      .lastName("Surubaru")
                      .email("maria.s@example.com")
                      .build();

              User iris = User.builder()
                      .role(UserRole.USER)
                      .username("irisu")
                      .phoneNumber("0740000002")
                      .profilePictureUrl("https://ui-avatars.com/api/?name=Iris+Milea")
                      .bio("Design si media digitala.")
                      .firstName("Iris")
                      .lastName("Milea")
                      .email("irism@example.com")
                      .build();


              User andru = User.builder()
                      .role(UserRole.USER)
                      .username("andru22")
                      .phoneNumber("0740000003")
                      .profilePictureUrl("https://ui-avatars.com/api/?name=Andru")
                      .bio("Web designer talentat.")
                      .firstName("Andru")
                      .lastName("Whatever")
                      .email("andru@example.com")
                      .build();

              User cosmin = User.builder()
                      .role(UserRole.USER)
                      .username("cosmin_brn")
                      .phoneNumber("0740000004")
                      .profilePictureUrl("https://ui-avatars.com/api/?name=Cosmin+Baroana")
                      .bio("Imi plac barbatii")
                      .firstName("Cosmin")
                      .lastName("Baroana")
                      .email("CosminBRN@example.com")
                      .build();

              User robert = User.builder()
                      .role(UserRole.USER)
                      .username("rp-ul")
                      .phoneNumber("0740000005")
                      .profilePictureUrl("https://ui-avatars.com/api/?name=Maria+Surubaru")
                      .bio("Eat sleep gymr repeat")
                      .firstName("Robert")
                      .lastName("Panda")
                      .email("robert.p@example.com")
                      .city("Constanța")
                      .build();


              userRepository.saveAll(List.of(maria, iris, andru, robert, cosmin));
          }
        };
    }
}
