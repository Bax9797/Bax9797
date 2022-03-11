package pl.kurs.java.test.service.patient;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pl.kurs.java.test.dto.PatientDto;
import pl.kurs.java.test.entity.Patient;
import pl.kurs.java.test.exception.patient.PatientNotFoundException;
import pl.kurs.java.test.model.ModelPatientToAdd;
import pl.kurs.java.test.repository.PatientRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository repository;

    public Patient findById(int id) {
        Optional<Patient> obj = repository.findById(id);
        return obj.orElseThrow(() -> new PatientNotFoundException("User Not Found with id : " + id));
    }

    public Patient saveNewPatient(ModelPatientToAdd modelPatientToAdd) {
        Patient patient = new Patient()
                .setAnimalName(modelPatientToAdd.getAnimalName())
                .setAnimalSpecies(modelPatientToAdd.getAnimalSpecies())
                .setAnimalBreed(modelPatientToAdd.getAnimalBreed())
                .setAge(modelPatientToAdd.getAge())
                .setOwnerName(modelPatientToAdd.getOwnerName())
                .setOwnerSurname(modelPatientToAdd.getOwnerSurname())
                .setEmail(modelPatientToAdd.getEmail())
                .setCurrentCustomer(true);
        repository.saveAndFlush(patient);
        return patient;
    }

    public boolean existsById(int id) {
        return repository.existsById(id);
    }

    public String removePatient(int id) {
        repository.updatePatientCurrentCustomer(false, id);
        return "changed status of given patient, this client is no longer our patient";
    }

    public Page<PatientDto> findAllToPage(Pageable p) {
        ModelMapper modelMapper = new ModelMapper();
        Page<Patient> page = repository.findAll(p);
        return new PageImpl<PatientDto>((page.getContent().stream()
                .map(p1 -> modelMapper.map(p1, PatientDto.class)
                ).collect(Collectors.toList())), p, page.getTotalElements());
    }
}