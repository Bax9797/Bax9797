package pl.kurs.java.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.java.test.dto.DoctorDto;
import pl.kurs.java.test.entity.Doctor;
import pl.kurs.java.test.exception.doctor.DoctorNotFoundException;
import pl.kurs.java.test.model.ModelDoctorToAdd;
import pl.kurs.java.test.service.doctor.DoctorService;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorRestController {

    private final DoctorService doctorService;
    private final ModelMapper modelMapper;


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
    public ResponseEntity addDoctor(@RequestBody ModelDoctorToAdd modelDoctorToAdd) {
        DoctorDto response = modelMapper
                .map(doctorService.validationOfTheEnteredParameterData(modelDoctorToAdd), DoctorDto.class);
        return new ResponseEntity(response, HttpStatus.CREATED);
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
        DoctorDto response = modelMapper.map(doctorService.findById(id), DoctorDto.class);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Operation(summary = "Soft fire a doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "changed status of given doctor," +
                    " this doctor will not be able to handle any visits"),
            @ApiResponse(responseCode = "404", description = "Doctor not found",
                    content = @Content)})
    @PutMapping("/{id}/fire")
    public ResponseEntity dismissTheEmployee(@PathVariable("id") int id) {
        if (!doctorService.existsById(id)) {
            throw new DoctorNotFoundException("User Not Found with id : " + id);
        } else {
            String response = doctorService.dismiss(id);
            return new ResponseEntity(response, HttpStatus.OK);
        }
    }

    @Operation(summary = "Page wit Doctor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "look on page with doctor")})
    @GetMapping("/page")
    public ResponseEntity pageOfDoctor(@PageableDefault(value = 2, page = 0) Pageable pageable) {
        Page<DoctorDto> response = doctorService.findAllToPage(pageable);
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