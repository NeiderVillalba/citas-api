package com.fcv.citas.user.adapter.in.rest;

import com.fcv.citas.appointment.domain.InvalidAppointmentRequestException;
import com.fcv.citas.appointment.domain.InvalidAppointmentTransitionException;
import com.fcv.citas.appointment.domain.AppointmentNotFoundException;
import com.fcv.citas.appointment.domain.SlotUnavailableException;
import com.fcv.citas.user.domain.InvalidPlanException;
import com.fcv.citas.user.domain.InvalidSessionException;
import com.fcv.citas.user.domain.UserAlreadyExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(AppointmentNotFoundException.class)
    public ResponseEntity<ApiError> appointmentNotFound(AppointmentNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError("APPOINTMENT_NOT_FOUND", exception.getMessage()));
    }

    @ExceptionHandler(InvalidAppointmentTransitionException.class)
    public ResponseEntity<ApiError> invalidAppointmentTransition(InvalidAppointmentTransitionException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("INVALID_APPOINTMENT_TRANSITION", exception.getMessage()));
    }
    @ExceptionHandler(InvalidSessionException.class)
    public ResponseEntity<ApiError> invalidSession(InvalidSessionException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError("INVALID_SESSION", exception.getMessage()));
    }

    @ExceptionHandler(SlotUnavailableException.class)
    public ResponseEntity<ApiError> slotUnavailable(SlotUnavailableException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("SLOT_UNAVAILABLE", exception.getMessage()));
    }

    @ExceptionHandler(InvalidAppointmentRequestException.class)
    public ResponseEntity<ApiError> invalidAppointmentRequest(InvalidAppointmentRequestException exception) {
        return ResponseEntity.badRequest()
                .body(new ApiError("INVALID_APPOINTMENT_REQUEST", exception.getMessage()));
    }

    @ExceptionHandler(InvalidPlanException.class)
    public ResponseEntity<ApiError> invalidPlan(InvalidPlanException exception) {
        return ResponseEntity.badRequest()
                .body(new ApiError("INVALID_PLAN", exception.getMessage()));
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiError> userAlreadyExists(UserAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("USER_ALREADY_EXISTS", exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> invalidRequest(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest()
                .body(new ApiError("INVALID_REQUEST", "Revisa los datos obligatorios del formulario."));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> dataConflict(DataIntegrityViolationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("USER_ALREADY_EXISTS", "Ya existe un usuario con ese correo o documento."));
    }
}
