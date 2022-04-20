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
import pl.kurs.java.test.dto.PatientDto;
import pl.kurs.java.test.dto.ResponseMessageDto;
import pl.kurs.java.test.model.CreatePatientRequest;
import pl.kurs.java.test.service.patient.PatientService;

import javax.validation.Valid;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientRestController {
    private final PatientService patientService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Add new patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "response message: add patient successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreatePatientRequest.class))}),
            @ApiResponse(responseCode = "400", description = "potential error: all fields must be not empty or null. "
                    + "Animal age can not be less than 0." +
                    " Email has to be unique or should be a valid",
                    content = @Content)})
    @PostMapping
    public ResponseEntity addPatient(@Valid @RequestBody CreatePatientRequest createPatientRequest) {
        PatientDto response = modelMapper
                .map(patientService.saveNewPatient(createPatientRequest), PatientDto.class);
        return new ResponseEntity(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a patient by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "response message: found the patient",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PatientDto.class))}),
            @ApiResponse(responseCode = "404", description = "potential error: Patient not found",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") int id) {
        PatientDto response = modelMapper.map(patientService.findById(id), PatientDto.class);
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Operation(summary = "Remove Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "response message: changed status of given patient," +
                    " this client is no longer our patient"),
            @ApiResponse(responseCode = "404", description = "potential error: Patient not found",
                    content = @Content)})
    @PutMapping("/{id}/remove")
    public ResponseEntity removeFromTheListOfCurrentPatients(@PathVariable("id") int id) {
        patientService.removePatient(id);
        ResponseMessageDto response = new ResponseMessageDto("delete");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    @Operation(summary = "Page wit Patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "response message: look on page with Patient")})
    @GetMapping
    public ResponseEntity pageOfPatient(@PageableDefault Pageable pageable) {
        Page<PatientDto> response = patientService.findAllToPage(pageable)
                .map(patient -> modelMapper.map(patient, PatientDto.class));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}