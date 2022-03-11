package pl.kurs.java.test.service.doctor;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kurs.java.test.dto.DoctorDto;
import pl.kurs.java.test.entity.Doctor;
import pl.kurs.java.test.exception.doctor.DoctorNotFoundException;
import pl.kurs.java.test.model.ModelDoctorToAdd;
import pl.kurs.java.test.repository.DoctorRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository repository;

    public Doctor findById(int id) {
        Optional<Doctor> obj = repository.findById(id);
        return obj.orElseThrow(() -> new DoctorNotFoundException("User Not Found with id : " + id));
    }

    public Doctor saveNewDoctor(ModelDoctorToAdd modelDoctorToAdd) {
        Doctor doctor = new Doctor()
                .setName(modelDoctorToAdd.getName())
                .setSurname(modelDoctorToAdd.getSurname())
                .setMedicalSpecialization(modelDoctorToAdd.getMedicalSpecialization())
                .setAnimalSpecialization(modelDoctorToAdd.getAnimalSpecialization())
                .setRate(modelDoctorToAdd.getRate())
                .setNip(modelDoctorToAdd.getNip())
                .setHired(true);
        repository.saveAndFlush(doctor);
        return doctor;
    }

    public boolean existsById(int id) {
        return repository.existsById(id);
    }

    public String dismiss(int id) {
        repository.updateDoctorHired(false, id);
        return "changed status of given doctor, this doctor will not be able to handle any visits";
    }

    public Page<DoctorDto> findAllToPage(Pageable p) {
        ModelMapper modelMapper = new ModelMapper();
        Page<Doctor> page = repository.findAll(p);
        return new PageImpl<DoctorDto>((page.getContent().stream()
                .map(p1 -> modelMapper.map(p1, DoctorDto.class)
                ).collect(Collectors.toList())), p, page.getTotalElements());
    }
}
