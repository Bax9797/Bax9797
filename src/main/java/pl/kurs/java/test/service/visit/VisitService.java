package pl.kurs.java.test.service.visit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.java.test.dto.NearestVisitDto;
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
import pl.kurs.java.test.model.ModelToFindNearestVisit;
import pl.kurs.java.test.model.ModelVisitToAdd;
import pl.kurs.java.test.repository.DoctorRepository;
import pl.kurs.java.test.repository.PatientRepository;
import pl.kurs.java.test.repository.TokenRepository;
import pl.kurs.java.test.repository.VisitRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository repository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenRepository tokenGeneratorRepository;
    private final EmailService emailService;

    private Visit saveVisit(ModelVisitToAdd model) {
        Token token = new Token()
                .setCode(UUID.randomUUID().toString())
                .setExpireDate(LocalDateTime.now().plusHours(1L));
        tokenGeneratorRepository.saveAndFlush(token);

        Visit visit = new Visit()
                .setDoctor(doctorRepository.getById(model.getDoctorId()))
                .setPatient(patientRepository.getById(model.getPatientId()))
                .setStartVisit(model.getDate())
                .setEndVisit(model.getDate().plusHours(1L))
                .setTokenId(tokenGeneratorRepository.findIdByCode(token.getCode()))
                .setStatus(Status.BOOKED);
        repository.saveAndFlush(visit);
        Optional<Patient> patient = patientRepository.findById(model.getPatientId());
        emailService.sendMessage(patient.get().getEmail(), emailService.messageContent(token));
        return visit;
    }

    public int validationOfTheEnteredParameterData(ModelVisitToAdd modelToAddVisit) {
        if (!doctorRepository.existsById(modelToAddVisit.getDoctorId()))
            throw new DoctorNotFoundException("Doctor Not Found with id : " + modelToAddVisit.getDoctorId());
        if (!doctorRepository.getStatusHiredById(modelToAddVisit.getDoctorId()))
            throw new DoctorNotFoundException("Doctor is not available  with id : " + modelToAddVisit.getDoctorId());
        if (!patientRepository.existsById(modelToAddVisit.getPatientId()))
            throw new PatientNotFoundException("Patient Not Found with id : " + modelToAddVisit.getPatientId());
        if (!checkIfDoctorIsFreeOnThisDate(modelToAddVisit.getDoctorId(), modelToAddVisit.getDate())
                || !checkIfPatientIsFreeOnThisDate(modelToAddVisit.getPatientId(), modelToAddVisit.getDate()))
            throw new ValidDateException("Doctor or Patient have already visit in this time");
        return saveVisit(modelToAddVisit).getId();
    }

    private boolean checkIfPatientIsFreeOnThisDate(int patientId, LocalDateTime dateTime) {
        LocalDateTime start = dateTime;
        LocalDateTime end = dateTime.plusHours(1L);
        List<Visit> list = repository.findByPatientId(patientId);
        for (Visit visit : list) {
            if (visit.getStartVisit().isEqual(start) && visit.getEndVisit().isEqual(end)) return false;
            if ((start.isAfter(visit.getStartVisit()) && start.isBefore(visit.getEndVisit()))
                    || (end.isAfter(visit.getStartVisit()) && end.isBefore(visit.getEndVisit()))) {
                return false;
            }
        }
        return true;
    }

    private Boolean checkIfDoctorIsFreeOnThisDate(int doctorId, LocalDateTime dateTime) {
        LocalDateTime start = dateTime;
        LocalDateTime end = dateTime.plusHours(1L);
        List<Visit> list = repository.findByDoctorId(doctorId);
        for (Visit visit : list) {
            if (visit.getStartVisit().isEqual(start) && visit.getEndVisit().isEqual(end)) return false;
            if ((start.isAfter(visit.getStartVisit()) && start.isBefore(visit.getEndVisit()))
                    || (end.isAfter(visit.getStartVisit()) && end.isBefore(visit.getEndVisit()))) {
                return false;
            }
        }
        return true;
    }

    public String checkingTokenToConfirmVisit(String code) {
        if (!tokenIsPresent(code)) throw new TokenNotFoundException("There is no Token with the given parameters");
        Token token = tokenGeneratorRepository.getByCode(code);
        if (!token.getExpireDate().isAfter(LocalDateTime.now()))
            throw new TimeResponseException("time to confirm the visit has passed");
        Visit visitToConfirm = repository.getByTokenId(token.getId());
        repository.updateVisitStatus(Status.CONFIRMED, visitToConfirm.getId());
        return "visit confirmed!";
    }

    public boolean tokenIsPresent(String code) {
        return tokenGeneratorRepository.existsByCode(code);
    }

    public boolean visitIsPresent(int tokenId) {
        return repository.existsByTokenId(tokenId);
    }

    public String checkingTokenToCanceledVisit(String code) {
        Token token;
        if (!tokenIsPresent(code)) throw new TokenNotFoundException("There is no Token with the given parameters");
        token = tokenGeneratorRepository.getByCode(code);
        if (!visitIsPresent(token.getId()))
            throw new VisitNotFoundException("There is no assigned visit with the given parameters");
        Visit visit = repository.getByTokenId(token.getId());
        repository.delete(visit);
        return "the visit has been canceled";
    }

    public List<NearestVisitDto> findNearestVisits(ModelToFindNearestVisit modelToFindNearestVisit) {
        List<Doctor> list = doctorRepository.findAllByMedicalSpecializationAndAnimalSpecializationAndHired(
                modelToFindNearestVisit.getMedicalSpecialization().toLowerCase(),
                modelToFindNearestVisit.getAnimalSpecialization().toLowerCase(), true);
        if (list.isEmpty())
            throw new NotFoundDoctorWithTheGivenParametersOfSpecializationsException("There is no doctor with the given parameters of Specialization ");

        List<NearestVisitDto> nearestVisitList1 = new ArrayList<>();
        LocalDateTime time = modelToFindNearestVisit.getDateFrom();
        for (Doctor doctor : list) {
            LocalDateTime visitTime = time;
            while (!visitTime.isAfter(modelToFindNearestVisit.getDateTo())) {
                if (checkIfDoctorIsFreeOnThisDate(doctor.getId(), visitTime)) {
                    nearestVisitList1.add(new NearestVisitDto
                            (new DoctorVisitModel(doctor.getId(), (doctor.getName() + " " + doctor.getSurname())), visitTime));
                }
                visitTime = visitTime.plusHours(1L);
            }
        }
        if (nearestVisitList1.isEmpty())
            throw new NotFoundFreeVisitAtGivenTimeException("There is no free visit at given time, please change the time slot for the meeting");
        return nearestVisitList1;
    }
}
