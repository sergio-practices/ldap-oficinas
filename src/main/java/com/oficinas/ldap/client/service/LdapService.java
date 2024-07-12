package com.oficinas.ldap.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.oficinas.ldap.client.LdapClient;

@Service
public class LdapService {

    Logger logger = LoggerFactory.getLogger(LdapService.class);

    private LdapClient ldapClient;

    LdapService(LdapClient ldapClient) {
        this.ldapClient = ldapClient;
    }

    /**
     * Checks to see if the user is in a group in active directory
     */
    public boolean isValidUserGroup(String idGrupo, String idUsuario) {
        boolean encontrado = ldapClient.isValidUserGroup(idGrupo, idUsuario);

        // If it is not found, it is written to the log
        if (!encontrado) {
            logger.error("El usuario {} no se encuentra en el grupo {} de LDAP.", idUsuario, idGrupo);
        }
        return encontrado;
    }

}
