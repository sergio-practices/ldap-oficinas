package com.oficinas.ldap.client.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerMapping;

import com.oficinas.ldap.client.service.LdapService;
import com.oficinas.ldap.controller.LdapController;

@WebMvcTest(LdapController.class)
class LdapControllerTest {

    @MockBean
    private LdapService ldapService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getUserFootprintOK() throws Exception {
        given(ldapService.isValidUserGroup(anyString(), anyString())).willReturn(true);
        mockMvc.perform(get("/oficinas/MOCKEDUSER/MOCKEDFOOTPRINT")).andExpect(status().isOk()).andReturn();
    }

    @Test
    void getUserFootprintKO() throws Exception {
        given(ldapService.isValidUserGroup(anyString(), anyString())).willReturn(false);
        mockMvc.perform(get("/oficinas/MOCKEDUSER/MOCKEDFOOTPRINT")).andExpect(status().isNotFound()).andReturn();
    }

    /**
     * Estos errores se lanzan cuando salta una excepción no controlada por ejemplo si la base de datos está caída
     */
    @Test
    void exceptionHandler() {
        LdapController controller = new LdapController(ldapService);
        try {
            Map<String, String> test = new HashMap<>();
            test.put("igGrupo", "MOCKEDUSER");
            test.put("igUsuario", "MOCKEDFOOTPRINT");

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, test);

            controller.runtimeException(new RuntimeException(), request);
        } catch (ResponseStatusException ex) {
            assertNotNull(ex);
        }
    }

}
