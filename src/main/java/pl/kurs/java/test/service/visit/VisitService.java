package pl.kurs.java.test.service.visit;

import freemarker.template.TemplateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kurs.java.test.dto.DoctorVisitDto;
import pl.kurs.java.test.entity.*;
import pl.kurs.java.test.exception.Entity.EntityNotFoundException;
import pl.kurs.java.test.exception.date.ValidDateException;
import pl.kurs.java.test.exception.token.TimeResponseException;
import pl.kurs.java.test.exception.token.TokenNotFoundException;
import pl.kurs.java.test.exception.visit.NotFoundDoctorWithTheGivenParametersOfSpecializationsException;
import pl.kurs.java.test.exception.visit.NotFoundFreeVisitAtGivenTimeException;
import pl.kurs.java.test.exception.visit.VisitNotFoundException;
import pl.kurs.java.test.mail.EmailService;
import pl.kurs.java.test.model.FindVisitsRequest;
import pl.kurs.java.test.model.NearestVisitResponse;
import pl.kurs.java.test.model.VisitToAddRequest;
import pl.kurs.java.test.repository.DoctorRepository;
import pl.kurs.java.test.repository.PatientRepository;
import pl.kurs.java.test.repository.TokenRepository;
import pl.kurs.java.test.repository.VisitRepository;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository repository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenRepository tokenGeneratorRepository;
    private final EmailService emailService;

    @Transactional
    public Visit saveVisit(VisitToAddRequest model) throws TemplateException, IOException, MessagingException {
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
        Patient patient = patientRepository.findById(model.getPatientId()).
                orElseThrow(() -> new EntityNotFoundException("Patient", model.getPatientId()));

        emailService.sendMessage(patient.getEmail(), emailService.messageContent(token));
        return visit;
    }

    @Transactional
    public int validationOfTheEnteredParameterData(VisitToAddRequest visitToAddRequest) throws TemplateException, IOException, MessagingException {
        //lock jest wykonywany w Repo
        Doctor doctor = doctorRepository.findByIdForWrite(visitToAddRequest.getDoctorId())
                .orElseThrow(() -> new EntityNotFoundException("doctor", visitToAddRequest.getDoctorId()));
        Patient patient = patientRepository.findByIdForWrite(visitToAddRequest.getPatientId())
                .orElseThrow(() -> new EntityNotFoundException("patient", visitToAddRequest.getPatientId()));

        if (!checkIfDoctorIsFreeOnThisDate(doctor.getId(), visitToAddRequest.getDate()))
            throw new ValidDateException("Doctor");
        if (!checkIfPatientIsFreeOnThisDate(patient.getId(), visitToAddRequest.getDate()))
            throw new ValidDateException("Patient");
        return saveVisit(visitToAddRequest).getId();
    }

    @Transactional
    public boolean checkIfPatientIsFreeOnThisDate(int patientId, LocalDateTime dateTime) {
        LocalDateTime end = dateTime.plusHours(1L);
        List<Visit> list = repository.findByPatientId(patientId);
        for (Visit visit : list) {
            if (visit.getStartVisit().isEqual(dateTime) && visit.getEndVisit().isEqual(end)) return false;
            if ((dateTime.isAfter(visit.getStartVisit()) && dateTime.isBefore(visit.getEndVisit()))
                    || (end.isAfter(visit.getStartVisit()) && end.isBefore(visit.getEndVisit()))) {
                return false;
            }
        }
        return true;
    }

    @Transactional
    public Boolean checkIfDoctorIsFreeOnThisDate(int doctorId, LocalDateTime dateTime) {
        LocalDateTime end = dateTime.plusHours(1L);
        List<Visit> list = repository.findByDoctorId(doctorId);
        for (Visit visit : list) {
            if (visit.getStartVisit().isEqual(dateTime) && visit.getEndVisit().isEqual(end)) return false;
            if ((dateTime.isAfter(visit.getStartVisit()) && dateTime.isBefore(visit.getEndVisit()))
                    || (end.isAfter(visit.getStartVisit()) && end.isBefore(visit.getEndVisit()))) {
                return false;
            }
        }
        return true;
    }

    public boolean checkingTokenToConfirmVisit(String code) {
        Token token = tokenGeneratorRepository.getByCode(code)
                .orElseThrow(TokenNotFoundException::new);
        if (!token.getExpireDate().isAfter(LocalDateTime.now()))
            throw new TimeResponseException();
        Visit visitToConfirm = repository.findByTokenId(token.getId()).
                orElseThrow(() -> new VisitNotFoundException("Token", token.getId()));
        repository.updateVisitStatus(Status.CONFIRMED, visitToConfirm.getId());
        return repository.existsById(visitToConfirm.getId());
    }

    public boolean checkingTokenToCanceledVisit(String code) {
        Token token = tokenGeneratorRepository.getByCode(code)
                .orElseThrow(TokenNotFoundException::new);
        if (!token.getExpireDate().isAfter(LocalDateTime.now()))
            throw new TimeResponseException();
        Visit visitToCancel = repository.findByTokenId(token.getId()).
                orElseThrow(() -> new VisitNotFoundException("Token", token.getId()));
        repository.delete(visitToCancel);
        return repository.existsById(visitToCancel.getId());
    }

    @Transactional
    public List<NearestVisitResponse> findNearestVisits(FindVisitsRequest findVisitsRequest) {
        List<Doctor> list = doctorRepository.findAllByMedicalSpecializationAndAnimalSpecialization(
                findVisitsRequest.getMedicalSpecialization().toLowerCase(),
                findVisitsRequest.getAnimalSpecialization().toLowerCase());
        if (list.isEmpty())
            throw new NotFoundDoctorWithTheGivenParametersOfSpecializationsException(findVisitsRequest.getMedicalSpecialization(),
                    findVisitsRequest.getAnimalSpecialization());

        List<NearestVisitResponse> nearestVisitList = new ArrayList<>();
        LocalDateTime time = findVisitsRequest.getDateFrom();
        for (Doctor doctor : list) {
            LocalDateTime visitTime = time;
            while (!visitTime.isAfter(findVisitsRequest.getDateTo())) {
                if (checkIfDoctorIsFreeOnThisDate(doctor.getId(), visitTime)) {
                    nearestVisitList.add(new NearestVisitResponse
                            (new DoctorVisitDto(doctor.getId(), (doctor.getName() + " " + doctor.getSurname())), visitTime));
                }
                visitTime = visitTime.plusHours(1L);
            }
        }
        if (nearestVisitList.isEmpty())
            throw new NotFoundFreeVisitAtGivenTimeException(findVisitsRequest.getDateFrom(), findVisitsRequest.getDateTo());
        return nearestVisitList;
    }

    public Token getTokenById(int id) {
        return tokenGeneratorRepository.findById(id).orElseThrow(TokenNotFoundException::new);
    }

    public Visit getVisitById(int id) {
        return repository.findById(id).orElseThrow(() -> new VisitNotFoundException("Visit", id));
    }
}