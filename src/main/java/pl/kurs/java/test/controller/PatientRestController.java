package pl.kurs.java.test.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.java.test.dto.PatientDto;
import pl.kurs.java.test.entity.Doctor;
import pl.kurs.java.test.entity.Patient;
import pl.kurs.java.test.exception.patient.PatientNotFoundException;
import pl.kurs.java.test.model.ModelPatientToAdd;
import pl.kurs.java.test.service.patient.PatientService;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientRestController {

    private final PatientService patientService;

    @Operation(summary = "Add new Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add patient successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Patient.class))}),
            @ApiResponse(responseCode = "400", description = "potential error: " +
                    "animal age can not be less than 0" +
                    "Email has to be unique. There is a person with the given email in the database" +
                    "all fields must be not empty",
                    content = @Content)})
    @PostMapping("/add")
    public ResponseEntity AddPatient(@RequestBody ModelPatientToAdd modelPatientToAdd) {
        return new ResponseEntity(patientService.validationOfTheEnteredParameterData(modelPatientToAdd), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a patient by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the patient",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Doctor.class))}),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") int id) {
        return new ResponseEntity(patientService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "Remove Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "changed status of given patient," +
                    " this client is no longer our patient"),
            @ApiResponse(responseCode = "404", description = "Patient not found",
                    content = @Content)})
    @PutMapping("/{id}/remove")
    public ResponseEntity removeFromTheListOfCurrentPatients(@PathVariable("id") int id) {
        if (!patientService.existsById(id)) {
            throw new PatientNotFoundException("User Not Found with id : " + id);
        } else {
            return new ResponseEntity(patientService.removePatient(id), HttpStatus.OK);
        }
    }

    @Operation(summary = "Page wit Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "look on page with Patient")})
    @GetMapping("/page")
    public ResponseEntity pageOfPatient(@PageableDefault(value = 2, page = 0) Pageable pageable) {
        Page<PatientDto> all = patientService.findAllToPage(pageable);
        return new ResponseEntity<>(all, HttpStatus.OK);
    }
}
