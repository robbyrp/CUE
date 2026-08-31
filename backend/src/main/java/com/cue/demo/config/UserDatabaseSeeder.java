package com.cue.demo.config;

import com.cue.demo.entities.User;
import com.cue.demo.enums.UserRole;
import com.cue.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class UserDatabaseSeeder implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run (final String... args) {
      if (userRepository.count() != 0) {
          User maria = User.builder()
                  .role(UserRole.ADMIN)
                  .username("maria_surubaru1")
                  .passwordHash(passwordEncoder.encode("ParolaAdmin123!"))
                  .profilePictureUrl("https://ui-avatars.com/api/?name=Maria+Surubaru")
                  .bio("Pasionata de artă și spectacole.")
                  .firstName("Maria")
                  .lastName("Surubaru")
                  .email("maria.s@example.com")
                  .build();

          User iris = User.builder()
                  .role(UserRole.USER)
                  .username("irisu")
                  .passwordHash(passwordEncoder.encode("ParolaUser123!"))
                  .profilePictureUrl("https://ui-avatars.com/api/?name=Iris+Milea")
                  .bio("Design si media digitala.")
                  .firstName("Iris")
                  .lastName("Milea")
                  .email("irism@example.com")
                  .build();


          User andrew = User.builder()
                  .role(UserRole.USER)
                  .username("andrew22")
                  .passwordHash(passwordEncoder.encode("ParolaUser123!"))
                  .profilePictureUrl("https://ui-avatars.com/api/?name=Andru")
                  .bio("Web designer talentat.")
                  .firstName("Andru")
                  .lastName("Whatever")
                  .email("andru@example.com")
                  .build();

          User cosmin = User.builder()
                  .role(UserRole.USER)
                  .username("cosmin_brn")
                  .passwordHash(passwordEncoder.encode("ParolaUser123!"))
                  .profilePictureUrl("https://ui-avatars.com/api/?name=Cosmin+Baroana")
                  .bio("Imi plac barbatii")
                  .firstName("Cosmin")
                  .lastName("Baroana")
                  .email("CosminBRN@example.com")
                  .build();

          User robert = User.builder()
                  .role(UserRole.USER)
                  .username("rpul")
                  .passwordHash(passwordEncoder.encode("ParolaUser123!"))
                  .profilePictureUrl("https://ui-avatars.com/api/?name=Robert+Pana")
                  .bio("Eat sleep gym repeat")
                  .firstName("Robert")
                  .lastName("Panda")
                  .email("robert.p@example.com")
                  .city("Constanța")
                  .build();

          User testUser = User.builder()
                  .id(10L)
                  .role(UserRole.USER)
                  .username("testUser")
                  .passwordHash(passwordEncoder.encode("ParolaUser123!"))
                  .profilePictureUrl("https://ui-avatars.com/api/?name=Test+User")
                  .bio("UNATC Student")
                  .firstName("TestUser")
                  .lastName("TestUser")
                  .email("test.user@example.com")
                  .city("Constanța")
                  .build();
//
//              Performance testPerformance = Performance.builder()
//                      .id(100L)
//                      .title("Spectacol test")
//                      .director("Director test")
//                      .location("TNB Test")
//                      .theaterName("Teatrul de stat Constanta Test")
//                      .startDateTime(LocalDateTime.parse("2027-10-10T10:10:00"))
//                      .coverImageURL("https://example.com/images/dance.jpg")
//                      .ageLimit(10)
//                      .duration(100)
//                      .fullCoverImageURL("https://example.com/images/dance-banner.jpg")
//                      .purchaseTicketLink("https://cndb.ro/bilete/sincronicitate/test")
//                      .description("Un spectacol Test impresionant din toate punctele de vedere.")
//                      .creditList(List.of())
//                      .build();
//
          userRepository.saveAll(List.of(maria, iris, andrew, robert, cosmin, testUser));

          }
    }
}
