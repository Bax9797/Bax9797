package pl.kurs.java.test.cotroller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.java.test.entity.Doctor;
import pl.kurs.java.test.expection.doctor.DoctorNotFoundException;
import pl.kurs.java.test.model.ModelDoctorToAdd;
import pl.kurs.java.test.repository.DoctorRepository;
import pl.kurs.java.test.service.doctor.DoctorService;

import javax.print.Doc;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private final DoctorRepository repository;


    @Operation(summary = "Add new doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add doctor successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Doctor.class))}),
            @ApiResponse(responseCode = "400", description = "salary cannot be negative or" +
                    "duplicate tax identification number.There is a person with the given" +
                    " tax identification number in the database",
                    content = @Content)})
    @PostMapping("/add")
    public ResponseEntity AddDoctor(@RequestBody ModelDoctorToAdd modelDoctorToAdd) {
        return new ResponseEntity(doctorService.validationOfTheEnteredParameterData(modelDoctorToAdd), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a doctor by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the Doctor",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Doctor.class))}),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") int id) {
        return new ResponseEntity(doctorService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Soft fire a doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "changed status of given doctor," +
                    " this doctor will not be able to handle any visits"),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content)})
    @PutMapping("/fire/{id}")
    public ResponseEntity dismissTheEmployee(@PathVariable("id") int id) {
        if (!doctorService.existsById(id)) {
            throw new DoctorNotFoundException("User Not Found with id : " + id);
        } else {
            return new ResponseEntity(doctorService.dismiss(id), HttpStatus.OK);
        }
    }
    @Operation(summary = "Page wit Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "look on page with doctor")})
    @GetMapping("/page")
    public ResponseEntity pageOfDoctor(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "3") int size) {
        Page<Doctor> response = repository.findAll(PageRequest.of(page, size));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
/*
*API dla lekarzy
root path: /doctor
@POST - should add doctor
example body: {"name": "xx", "surname": "xx", "type": "xxx", "animalType": "yyy", "salary": 000, "nip": "xxx"}
response: 201 Created
response: 400 BAD request. Error handling: duplicated nip, all fields must be not empty, salary cannot be negative.

@GET /{id}
response: 200 ok. body: {"name": "xx", "surname": "xx", "type": "xxx", "animalType": "yyy", "salary": 000, "nip": "xxx"}
response: 404 not found - if given id not exists in db.

@GET
request parameters: page/size - optional, with default values. Default spring boot pagination expected.
response: 200 ok: body: page with doctors body content. (nie chce mi sie pisac tutaj:D)

@PUT /fire/{id}
no body
response: 200 OK. changed status of given doctor, this doctor will not be able to handle any visits.
response: 404 NOT FOUND - if given id not exists in db.
* */