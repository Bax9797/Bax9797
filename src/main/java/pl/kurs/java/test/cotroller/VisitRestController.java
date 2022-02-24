package pl.kurs.java.test.cotroller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kurs.java.test.model.DoctorVisitModel;
import pl.kurs.java.test.model.ModelToAddVisit;
import pl.kurs.java.test.model.ModelToFindNearestVisit;
import pl.kurs.java.test.service.visit.VisitService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/visit")
public class VisitRestController {

    private final VisitService service;


    @Operation(summary = "Booked visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booked visit successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Integer.class))}),
            @ApiResponse(responseCode = "400", description = "400 bad request, potential errors:" +
                    " doctor id not found, patient id not found, or doctor/patient already has visit that date.",
                    content = @Content)})
    @PostMapping("/booked")
    public ResponseEntity postToBooked(@RequestBody ModelToAddVisit modelToAddVisit) {
        return new ResponseEntity(service.validationOfTheEnteredParameterData(modelToAddVisit), HttpStatus.OK);
    }

    @Operation(summary = "Confirm visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Confirm visit successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "400 bad request, potential errors:" +
                    "  wrong token or visit confirmed too late",
                    content = @Content)})
    @GetMapping("/{token}/confirm")
    public ResponseEntity confirmVisit(@PathVariable("token") String token) {
        return new ResponseEntity(service.checkingTokenToConfirmVisit(token), HttpStatus.OK);
    }

    @Operation(summary = "Cancel visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cancel visit successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "400 bad request, potential errors:" +
                    "  wrong token or visit confirmed too late",
                    content = @Content)})
    @GetMapping("/{token}/cancel")
    public ResponseEntity cancelVisit(@PathVariable("token") String token) {
        return new ResponseEntity(service.checkingTokenToCanceledVisit(token), HttpStatus.OK);
    }

    @Operation(summary = "find visit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cancel visit successful",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = DoctorVisitModel.class))}),
            @ApiResponse(responseCode = "400", description = " 400badRequest: no type, no animal support" +
                    " or invalid dates",
                    content = @Content)})
    @PostMapping("/find")
    public ResponseEntity findTopNearestVisits(@RequestBody ModelToFindNearestVisit modelToFindNearestVisit) {
        return new ResponseEntity(service.findNearestVisits(modelToFindNearestVisit), HttpStatus.OK);
    }
//
//    @POST
///check
//    should find top few (configurable parameter) nearest visits for given parameters
//    example body: { "type": "kardiolog", "animal": "pies", "from": "date_from", "to": "date_to" }
//    response: 200OK body: [{"doctor":{"id": 3, "name": "Jan Kowalski"} "date": "2021-05-01 15:00:00"}, ...]
//    response: 400badRequest, no type, no animal support, invalid dates etc.*/

}
/*API dla wizyt:
root path: /visit
@POST
example body: {"doctorId": xxx, "patientId": yyy, "date": "2021-01-05 15:00:00"}
response: 201 created. body: { "id": xxx }
response: 400 bad request, potential errors: doctor id not found, patient id not found, or doctor/patient already has visit that date.

Przy tworzeniu wizyty musisz wyslac maila do pacjenta (stworz sobie jakies tmp konto na gmailu ktorego uzyjesz jako smtp)
z unikalnym one-time-tokenem przypisanym do wizyty.

@GET
/confirm/{token}
response: 200ok body: {"message": "Wizyta potwierdzona!"}
response: 400bad request: {"message": "za pozno aby potwierdzic/zly token/inne bledy? wymysl"}

@GET
/cancel/{token}
response: 200ok body: {"message": "Wizyta Anulowana"} - usuwa permanentnie wizyte z systemu.
response: 400bad request: {"message": "zly token/inne bledy? wymysl"}

@POST
/check
should find top few (configurable parameter) nearest visits for given parameters
example body: { "type": "kardiolog", "animal": "pies", "from": "date_from", "to": "date_to" }
response: 200OK body: [{"doctor":{"id": 3, "name": "Jan Kowalski"} "date": "2021-05-01 15:00:00"}, ...]
response: 400badRequest, no type, no animal support, invalid dates etc.*/