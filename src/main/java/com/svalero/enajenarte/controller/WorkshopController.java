package com.svalero.enajenarte.controller;

import com.svalero.enajenarte.dto.WorkshopInDto;
import com.svalero.enajenarte.dto.WorkshopOutDto;
import com.svalero.enajenarte.dto.WorkshopOutDtoV2;
import com.svalero.enajenarte.exception.DuplicateWorkshopException;
import com.svalero.enajenarte.exception.ErrorResponse;
import com.svalero.enajenarte.service.WorkshopService;
import com.svalero.enajenarte.exception.SpeakerNotFoundException;
import com.svalero.enajenarte.exception.WorkshopNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WorkshopController {

    @Autowired
    private WorkshopService workshopService;

    // GET
    @GetMapping("/workshops")
    public ResponseEntity<List<WorkshopOutDto>> getAll(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "isOnline", defaultValue = "") String isOnline,
            @RequestParam(value = "speakerId", defaultValue = "") String speakerId) throws SpeakerNotFoundException {

        List<WorkshopOutDto> workshopOutDto = workshopService.findAll(name, isOnline, speakerId);
        // Si la lista está vacía, devuelve 204 No Content
        if( workshopOutDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        // Si hay resultados, devuelve 200 Ok con la lista
        return ResponseEntity.ok(workshopOutDto);
    }

    // GET V1
    @GetMapping("/api/v1/workshops")
    public ResponseEntity<List<WorkshopOutDto>> getAllV1(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "isOnline", defaultValue = "") String isOnline,
            @RequestParam(value = "speakerId", defaultValue = "") String speakerId) {

        List<WorkshopOutDto> workshopOutDto = workshopService.findAll(name, isOnline, speakerId);

        // Si la lista está vacía, devuelve 204 No Content
        if (workshopOutDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Si hay resultados, devuelve 200 Ok con la lista
        return ResponseEntity.ok(workshopOutDto);
    }

    // GET V2
    @GetMapping("/api/v2/workshops")
    public ResponseEntity<List<WorkshopOutDtoV2>> getAllV2(
            @RequestParam(value = "name", defaultValue = "") String name,
            @RequestParam(value = "isOnline", defaultValue = "") String isOnline,
            @RequestParam(value = "speakerId", defaultValue = "") String speakerId) {

        List<WorkshopOutDtoV2> workshopOutDto = workshopService.findAllV2(name, isOnline, speakerId);

        // Si la lista está vacía, devuelve 204 No Content
        if (workshopOutDto.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // Si hay resultados, devuelve 200 Ok con la lista
        return ResponseEntity.ok(workshopOutDto);
    }

    // GET BY ID
    @GetMapping("/workshops/{id}")
    public ResponseEntity<WorkshopOutDto> get(@PathVariable long id) throws WorkshopNotFoundException {
        WorkshopOutDto workshopOutDto = workshopService.findById(id);
        return ResponseEntity.ok(workshopOutDto);
    }

    //POST
    @PostMapping("/workshops")
    public ResponseEntity<WorkshopOutDto> addWorkshop(@Valid @RequestBody WorkshopInDto workshopInDto) throws SpeakerNotFoundException {
        WorkshopOutDto newWorkshop = workshopService.add(workshopInDto);
        return new ResponseEntity<>(newWorkshop, HttpStatus.CREATED);
    }

    // POST V1
    @PostMapping("/api/v1/workshops")
    public ResponseEntity<WorkshopOutDto> addWorkshopV1(@Valid @RequestBody WorkshopInDto workshopInDto)
            throws SpeakerNotFoundException {
        WorkshopOutDto newWorkshop = workshopService.add(workshopInDto);
        return new ResponseEntity<>(newWorkshop, HttpStatus.CREATED);
    }

    // POST V2
    @PostMapping("/api/v2/workshops")
    public ResponseEntity<WorkshopOutDto> addWorkshopV2(@Valid @RequestBody WorkshopInDto workshopInDto)
            throws SpeakerNotFoundException, DuplicateWorkshopException {
        WorkshopOutDto newWorkshop = workshopService.addV2(workshopInDto);
        return new ResponseEntity<>(newWorkshop, HttpStatus.CREATED);
    }

    // PUT
    @PutMapping("/workshops/{id}")
    public ResponseEntity<WorkshopOutDto> modifyWorkshop(@PathVariable long id, @Valid @RequestBody WorkshopInDto workshopInDto)
        throws SpeakerNotFoundException, WorkshopNotFoundException {
        WorkshopOutDto updateWorkshop = workshopService.modify(id, workshopInDto);
        return ResponseEntity.ok(updateWorkshop);
    }

    // DELETE
    @DeleteMapping("/workshops/{id}")
    public ResponseEntity<Void> deleteWorkshop(@PathVariable long id) throws WorkshopNotFoundException {
        workshopService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //404 - Workshop
    @ExceptionHandler(WorkshopNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(WorkshopNotFoundException wnfe) {
        ErrorResponse errorResponse = ErrorResponse.notFound("The workshop does not exist");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // 404 - Speaker (relación)
    @ExceptionHandler(SpeakerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(SpeakerNotFoundException snfe) {
        ErrorResponse errorResponse = ErrorResponse.notFound("The speaker does not exist");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // 409 - Workshop duplicado
    @ExceptionHandler(DuplicateWorkshopException.class)
    public ResponseEntity<ErrorResponse> handleException(DuplicateWorkshopException dwe) {
        ErrorResponse errorResponse = ErrorResponse.generalError(409, "conflict", "The workshop already exists");
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    // 400 - Validaciones
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException manve) {
        Map<String, String> errors = new HashMap<>();
        manve.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        ErrorResponse errorResponse = ErrorResponse.validationError(errors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}