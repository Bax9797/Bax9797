package pl.kurs.java.test.service.visit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.java.test.entity.*;
import pl.kurs.java.test.exception.date.ValidDateException;
import pl.kurs.java.test.exception.doctor.DoctorNotFoundException;
import pl.kurs.java.test.exception.patient.PatientNotFoundException;
import pl.kurs.java.test.exception.token.TimeResponseException;
import pl.kurs.java.test.exception.token.TokenNotFoundException;
import pl.kurs.java.test.exception.visit.NotFoundDoctorWithTheGivenParametersOfSpecializationsException;
import pl.kurs.java.test.exception.visit.NotFoundFreeVisitAtGivenTimeException;
import pl.kurs.java.test.exception.visit.VisitNotFoundException;
import pl.kurs.java.test.mail.EmailService;
import pl.kurs.java.test.model.DoctorVisitModel;
import pl.kurs.java.test.model.ModelToAddVisit;
import pl.kurs.java.test.model.ModelToFindNearestVisit;
import pl.kurs.java.test.model.NearestVisit;
import pl.kurs.java.test.repository.DoctorRepository;
import pl.kurs.java.test.repository.PatientRepository;
import pl.kurs.java.test.repository.TokenRepository;
import pl.kurs.java.test.repository.VisitRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository repository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenRepository tokenGeneratorRepository;
    private final EmailService emailService;

    private Visit saveVisit(ModelToAddVisit model) {
        Token token = new Token()
                .setCode(UUID.randomUUID().toString())
                .setExpireDate(LocalDateTime.now().plusHours(1L));
        tokenGeneratorRepository.saveAndFlush(token);

        Visit visit = new Visit()
                .setDoctor(doctorRepository.getById(model.getDoctorId()))
                .setPatient(patientRepository.getById(model.getPatientId()))
                .setMeetingDate(model.getDate())
                .setTokenId(tokenGeneratorRepository.findIdByCode(token.getCode()))
                .setStatus(Status.BOOKED);
        repository.saveAndFlush(visit);

        Optional<Patient> patient = patientRepository.findById(model.getPatientId());
        emailService.sendSimpleMessage(patient.get().getEmail(), emailService.messageContent(token));
        return visit;
    }

    public int validationOfTheEnteredParameterData(ModelToAddVisit modelToAddVisit) {
        if (!doctorRepository.existsById(modelToAddVisit.getDoctorId())) {
            throw new DoctorNotFoundException("User Not Found with id : " + modelToAddVisit.getDoctorId());
        } else if (!patientRepository.existsById(modelToAddVisit.getPatientId())) {
            throw new PatientNotFoundException("User Not Found with id : " + modelToAddVisit.getPatientId());
        } else if (!checkIfDoctorIsFreeOnThisDate(modelToAddVisit.getDoctorId(), modelToAddVisit.getDate())
                || !checkIfPatientIsFreeOnThisDate(modelToAddVisit.getPatientId(), modelToAddVisit.getDate())) {
            throw new ValidDateException("Doctor or Patient have already visit in this time");
        } else {
            return saveVisit(modelToAddVisit).getId();
        }
    }

    private boolean checkIfPatientIsFreeOnThisDate(int patientId, LocalDateTime dateTime) {
        List<LocalDateTime> localDateTimeList = repository.getDateByPatientId(patientId);
        System.out.println(localDateTimeList);
        List<LocalDateTime> localDateTime = localDateTimeList.stream().filter(t -> t.isEqual(dateTime))
                .collect(Collectors.toList());
        if (localDateTime.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private Boolean checkIfDoctorIsFreeOnThisDate(int doctorId, LocalDateTime dateTime) {
        List<LocalDateTime> localDateTimeList = repository.getDateByDoctorId(doctorId);
        System.out.println(localDateTimeList);
        List<LocalDateTime> localDateTime = localDateTimeList.stream().filter(t -> t.isEqual(dateTime))
                .collect(Collectors.toList());
        if (localDateTime.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public String checkingTokenToConfirmVisit(String code) {
        Token token;
        if (!tokenIsPresent(code)) {
            throw new TokenNotFoundException("There is no Token with the given parameters");
        } else {
            token = tokenGeneratorRepository.getByCode(code);
        }
        if (!token.getExpireDate().isAfter(LocalDateTime.now())) {
            throw new TimeResponseException("time to confirm the visit has passed");
        } else {
            Visit visitToConfirm = repository.getByTokenId(token.getId());
            repository.updateVisitStatus(Status.CONFIRMED, visitToConfirm.getId());
            return "visit confirmed!";
        }
    }

    public String checkingTokenToCanceledVisit(String code) {
        Token token;
        if (!tokenIsPresent(code)) {
            throw new TokenNotFoundException("There is no Token with the given parameters");
        } else {
            token = tokenGeneratorRepository.getByCode(code);
        }
        if (!visitIsPresent(token.getId())) {
            throw new VisitNotFoundException("There is no assigned visit with the given parameters");
        } else {
            Visit visit = repository.getByTokenId(token.getId());
            repository.delete(visit);
            return "the visit has been canceled";
        }
    }

    public boolean visitIsPresent(int tokenId) {
        if (repository.existsByTokenId(tokenId)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean tokenIsPresent(String code) {
        if (tokenGeneratorRepository.existsByCode(code)) {
            return true;
        } else {
            return false;
        }
    }

    public List<NearestVisit> findNearestVisits(ModelToFindNearestVisit modelToFindNearestVisit) {
        List<Doctor> list = doctorRepository.findAllByMedicalSpecializationAndAnimalSpecialization(
                modelToFindNearestVisit.getMedicalSpecialization(), modelToFindNearestVisit.getAnimalSpecialization());
        if (list.isEmpty()) {
            throw new NotFoundDoctorWithTheGivenParametersOfSpecializationsException("There is no doctor with the given parameters of Specialization ");
        }

        List<NearestVisit> nearestVisitList = new ArrayList<>();
        LocalDateTime time = modelToFindNearestVisit.getDateFrom().plusHours(1l);
        LocalDateTime nearestVisitTime = modelToFindNearestVisit.getDateFrom();
        LocalDateTime farthestVisitTime = modelToFindNearestVisit.getDateTo();

        for (Doctor doctor : list) {
            LocalDateTime visitTime = time;
            while (visitTime.isBefore(farthestVisitTime)) {
                if (visitTime.isAfter(nearestVisitTime) && checkIfDoctorIsFreeOnThisDate(doctor.getId(), visitTime)) {
                    nearestVisitList.add(new NearestVisit(new DoctorVisitModel(doctor.getId(),
                            (doctor.getName() + " " + doctor.getSurname())), visitTime));
                }
                visitTime = visitTime.plusHours(1l);
            }
        }
        if (nearestVisitList.isEmpty()) {
            throw new NotFoundFreeVisitAtGivenTimeException("There is no free visit at given time, please change the time slot for the meeting");
        } else {
            return nearestVisitList;
        }
    }
}
