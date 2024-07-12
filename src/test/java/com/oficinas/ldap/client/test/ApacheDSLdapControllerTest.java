package com.oficinas.ldap.client.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

@Disabled("not by now")
@DirtiesContext
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class ApacheDSLdapControllerTest {

    static Logger logger = LoggerFactory.getLogger(ApacheDSLdapControllerTest.class);

    private static String WRONG = "wrong";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getUserHasFootprintOK() {
        ResponseEntity<Void> responseOK = restTemplate.getForEntity("/oficinas/GRP1/EEXP0293", Void.class);
        assertEquals(HttpStatus.OK, responseOK.getStatusCode());

        // El footprint enviado no se corresponde con el que tiene
        ResponseEntity<Void> responseKO = restTemplate.getForEntity("/oficinas/GRP1/" + WRONG, Void.class);
        assertEquals(HttpStatus.NO_CONTENT, responseKO.getStatusCode());

    }

}
