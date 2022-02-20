package pl.kurs.java.test;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import pl.kurs.java.test.entity.*;
import pl.kurs.java.test.repository.DoctorRepository;
import pl.kurs.java.test.repository.PatientRepository;
import pl.kurs.java.test.repository.TokenRepository;
import pl.kurs.java.test.repository.VisitRepository;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
@ActiveProfiles("dev")
class DataLoader implements CommandLineRunner {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final TokenRepository tokenRepository;
    private final VisitRepository visitRepository;

    @Override
    public void run(String... args) throws Exception {
        LoadUsers();
    }

    private void LoadUsers() {
        patientRepository.saveAll(Arrays.asList(new Patient("Kapsel", "Pies", "Pitbull",
                        4, "Michał", "Piec", "mkrolak7@gmail.com", true),
                new Patient("Kamyk", "Kot", "Kanapowiec", 2,
                        "Tomek", "Kot", "mado@gmail.com", true),
                new Patient("Dog", "Wąż", "Pyton", 1, "Karolina",
                        "Bachor", "Kacep@gmail.com", true)));

        doctorRepository.saveAll(Arrays.asList(new Doctor("Michał", "Gocek", "Kardiolog",
                        "Pies", 100.00, "864232112", true),
                new Doctor("Michał", "Golec", "Chirurg", "Kot",
                        200.00, "3246547542", true), new Doctor("Walery", "Mich",
                        "Weterynarz", "Wąż", 50.00, "32235231", true)));

        visitRepository.saveAll(Arrays.asList(new Visit(1, 1, LocalDateTime.now().plusHours(5L), 1, Status.BOOKED),
                new Visit(2, 2, LocalDateTime.now().plusHours(2L), 2, Status.BOOKED)));

        tokenRepository.saveAll(Arrays.asList(new Token("token12345", LocalDateTime.now().plusHours(2L))
                , new Token("token1", LocalDateTime.now())));
    }
}