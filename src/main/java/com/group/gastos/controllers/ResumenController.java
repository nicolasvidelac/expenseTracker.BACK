package com.group.gastos.controllers;

import com.group.gastos.models.dtos.ResumenDTO;
import com.group.gastos.others.jwt.JwtToken;
import com.group.gastos.servicesDTO.ResumenServiceDTO;
import lombok.AllArgsConstructor;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/resumen")
@CrossOrigin
public class ResumenController {

    private final ResumenServiceDTO _resumenServiceDTO;
    private final JwtToken jwtToken;

    @GetMapping("/active")
    public ResponseEntity<Object> findActiveResumen(@RequestHeader(name = "Authorization") String token) {

        try {
            String username = getUsername(token);
            return ResponseEntity.ok(_resumenServiceDTO.findActiveResumen(username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getCause()));
        }
    }

    @GetMapping("/{year}/{month}")
    public ResponseEntity<Object> findResumenByDate(@RequestHeader(name = "Authorization") String token,
                                                    @PathVariable("year") Integer year,
                                                    @PathVariable("month") Integer month
    ) {
        try {
            String username = getUsername(token);
            LocalDate fecha = LocalDate.now().withYear(year).withMonth(month);
            return ResponseEntity.ok(_resumenServiceDTO.findByFechaInicio(username, fecha));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getCause()));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllResumenes(@RequestHeader(name = "Authorization") String token) {

        try {
            String username = getUsername(token);
            return ResponseEntity.ok(_resumenServiceDTO.findAllResumenes(username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getCause()));
        }
    }

    @PatchMapping("/{year}/{month}")
    public ResponseEntity<Object> updateSueldo(@RequestHeader(name = "Authorization") String token,
                                                @PathVariable("year") Integer year,
                                                @PathVariable("month") Integer month,
                                                @RequestBody ResumenDTO resumenDTO
                                                ) {
        try {
            String username = getUsername(token);
            LocalDate fecha = LocalDate.now().withYear(year).withMonth(month);
            return ResponseEntity.ok(_resumenServiceDTO.updateResumen(username, fecha, resumenDTO.getSueldoActual()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorMessage(e.getMessage(), e.getCause()));
        }
    }

    private String getUsername(String token){
        return jwtToken.getUsername(token.substring(7));
    }
}
