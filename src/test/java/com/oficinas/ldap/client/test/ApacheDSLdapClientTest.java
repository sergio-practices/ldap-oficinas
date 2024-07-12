package com.oficinas.ldap.client.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.test.annotation.DirtiesContext;

import com.oficinas.ldap.client.LdapClient;

@DirtiesContext
@SpringBootTest
class ApacheDSLdapClientTest {

    private static final String SEARCH_GROUP1 = "GRP1";
    private static final String SEARCH_GROUP2 = "GRP2";
    private static final String SEARCH_GROUP_WRONG = "WRONG";

    private static final String SEARCH_USER_GROUP1 = "USU01";
    private static final String SEARCH_USER_GROUP2 = "USU04";
    private static final String SEARCH_USER_WRONG = "wrong";

    @Autowired
    private LdapClient ldapClient;

    @Test
    void isValidUserGroup1() {
        Boolean result = ldapClient.isValidUserGroup(SEARCH_GROUP1, SEARCH_USER_GROUP1);
        assertTrue(result);

        Boolean resultFalse = ldapClient.isValidUserGroup(SEARCH_GROUP1, SEARCH_USER_WRONG);
        assertFalse(resultFalse);

        try {
            ldapClient.isValidUserGroup(SEARCH_GROUP_WRONG, SEARCH_USER_GROUP1);
        } catch (RuntimeException ex) {
            assertEquals(NameNotFoundException.class, ex.getClass());
        }
    }

    @Test
    void isValidUserGroup2() {
        Boolean result = ldapClient.isValidUserGroup(SEARCH_GROUP2, SEARCH_USER_GROUP2);
        assertTrue(result);
    }

}
