package pl.kurs.java.test.service.doctor;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kurs.java.test.entity.Doctor;
import pl.kurs.java.test.exception.Entity.EntityNotFoundException;
import pl.kurs.java.test.model.CreateDoctorRequest;
import pl.kurs.java.test.repository.DoctorRepository;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository repository;

    @Transactional
    public Doctor findById(int id) {
        return repository.findByIdForRead(id)
                .orElseThrow(() -> new EntityNotFoundException("doctor", id));
    }

    public Doctor saveNewDoctor(CreateDoctorRequest createDoctorRequest) {
        Doctor doctor = new Doctor()
                .setName(createDoctorRequest.getName())
                .setSurname(createDoctorRequest.getSurname())
                .setMedicalSpecialization(createDoctorRequest.getMedicalSpecialization())
                .setAnimalSpecialization(createDoctorRequest.getAnimalSpecialization())
                .setRate(createDoctorRequest.getRate())
                .setNip(createDoctorRequest.getNip())
                .setHired(true);
        repository.saveAndFlush(doctor);
        return doctor;
    }

    public boolean existsById(int id) {
        return repository.existsById(id);
    }

    @Transactional
    public void dismiss(int id) {
        Doctor doctor = repository.findByIdForWrite(id).orElseThrow(() -> new EntityNotFoundException("doctor", id));
        repository.updateStatusOfDoctorHired(doctor.getId());
    }

    public Page<Doctor> findAllToPage(Pageable p) {
        return repository.findAll(p);
    }
}
