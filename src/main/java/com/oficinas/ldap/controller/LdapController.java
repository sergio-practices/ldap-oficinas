package com.oficinas.ldap.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerMapping;

import com.oficinas.ldap.client.service.LdapService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;

@RestController
@RequestMapping(value = "/oficinas")
public class LdapController {

    Logger logger = LoggerFactory.getLogger(LdapController.class);

    private LdapService ldapService;

    public LdapController(LdapService ldapService) {
        this.ldapService = ldapService;
    }

    /**
     * Repondemos 200 si es un token válido, 204 si no es válido 400 se por ejemplo no nos pasan algún parámetro, 
     * el 500 por ejemplo si el directorio activo falla.
     */
    @Operation(summary = "Validación de usuario activo", description = "Valida si un usuario se encuentra en un grupo en el directorio activo")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", description = "Successfully created"),
            @ApiResponse(responseCode = "204", description = "No content"), 
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error") })
    @GetMapping(value = "/{idGrupo}/{idUsuario}")
    public ResponseEntity<Void> isValidLdapUserGroup(
    		@PathVariable("idGrupo") @NotEmpty String idGrupo, 
    		@PathVariable("idUsuario") @NotEmpty String idUsuario) {
    	
        boolean result = ldapService.isValidUserGroup(idGrupo, idUsuario.toLowerCase());

        return (result) ? new ResponseEntity<>(HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Control de errores para LDAPController
     * 
     * @param ex Error
     * @param request Petición de entrada
     */
    @ExceptionHandler(RuntimeException.class)
    public void runtimeException(RuntimeException ex, HttpServletRequest request) {
        Map<String, String> pathVariables = (Map) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        logger.error("Error interno del servidor con el usuario {} y grupo {}.", pathVariables.get("idUsuario"), pathVariables.get("idGrupo"));
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
