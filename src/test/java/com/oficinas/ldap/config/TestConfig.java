package com.oficinas.ldap.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.test.unboundid.EmbeddedLdapServerFactoryBean;
import org.springframework.ldap.test.unboundid.LdifPopulator;

import com.oficinas.ldap.client.LdapClient;

@Configuration
public class TestConfig {
    @Autowired
    private Environment env;

    @Autowired
    private ResourceLoader resourceLoader;

    /** Configuración del servidor ApacheDS */
    @Bean("embeddedLdapServer")
    public EmbeddedLdapServerFactoryBean embeddedLdapServer() {
        EmbeddedLdapServerFactoryBean contextSource = new EmbeddedLdapServerFactoryBean();
        contextSource.setPartitionSuffix(env.getRequiredProperty("ldap.partitionSuffix"));
        contextSource.setPartitionName(env.getRequiredProperty("ldap.defaultPartitionName"));
        contextSource.setPort(Integer.valueOf(env.getRequiredProperty("ldap.port")));
        return contextSource;
    }

    /** Carga de datos en el servidor ApacheDS */
    @Bean
    @DependsOn({ "embeddedLdapServer" })
    public LdifPopulator ldifPopulator() {

        LdifPopulator contextSource = new LdifPopulator();
        contextSource.setContextSource(contextSource());
        contextSource.setResource(resourceLoader.getResource(env.getRequiredProperty("ldap.ldiffile")));
        contextSource.setBase(env.getRequiredProperty("ldap.partitionSuffix"));
        contextSource.setClean(true);
        contextSource.setDefaultBase(env.getRequiredProperty("ldap.partitionSuffix"));
        return contextSource;
    }

    /** Configuración del cliente contra ApacheDS */
    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(env.getRequiredProperty("ldap.url"));
        contextSource.setBase(env.getRequiredProperty("ldap.partitionSuffix"));
        contextSource.setUserDn(env.getRequiredProperty("ldap.principal"));
        contextSource.setPassword(env.getRequiredProperty("ldap.password"));
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }

    @Bean
    public LdapClient ldapClient() {
        return new LdapClient();
    }
}
