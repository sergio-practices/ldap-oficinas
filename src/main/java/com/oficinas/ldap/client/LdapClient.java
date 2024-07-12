package com.oficinas.ldap.client;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.naming.directory.Attribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Component;

@Component
public class LdapClient {

    @Autowired
    private LdapTemplate ldapTemplate;
    private static final String MEMBER_FILTER = "member";

    /**
     * Searches a group for all members and loops through them to see if the user is found
     */
    public boolean isValidUserGroup(final String idGrupo, final String username) {
        AtomicBoolean isUserInGroup = new AtomicBoolean(false);

        String groupName = "cn=" + idGrupo;
        String[] att = new String[] { MEMBER_FILTER };

        // Filters all attributes of type member. Then they are collected although only those come.
        ldapTemplate.lookup(groupName, att, (AttributesMapper<Void>) attrs -> {
            Attribute memberAttribute = attrs.get(MEMBER_FILTER);
            for (int i = 0; i < memberAttribute.size(); i++) {
                String groupMember = (String) memberAttribute.get(i);
                if (groupMember.toLowerCase().contains("cn=" + username)) {
                    isUserInGroup.set(true);
                    break;
                }
            }
            return null;
        });

        return isUserInGroup.get();
    }

}
