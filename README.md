<h3>LDAP project using Bitnami as a real server and ApacheDS for tests</h3>

<p>Create the Bitnami server</p>


```
docker run --detach --hostname ldapexample --name ldapexample ^
  --env LDAP_ROOT=dc=oficinas,dc=com ^
  --env LDAP_ADMIN_USERNAME=admin1 ^
  --env LDAP_ADMIN_PASSWORD=adminpassword1 ^
  --env LDAP_ADMIN_DN=cn=admin1,dc=oficinas,dc=com ^
  --env LDAP_CUSTOM_LDIF_DIR=/ldifs ^
  -v vexample:/bitnami/openldap ^
  -v ldifs:/ldifs ^
  -p 1389:1389 -p 1636:1636 ^
  bitnami/openldap:latest
```

Execute sh to run commands in Docker

```
docker exec -it -u root ldapexample /bin/bash
```

<p>Install nano and update the ldif file to create the LDAP structure</p>
<p>Copy values from local project src/test/resources/all.ldif using nano editor</p>


```
apt-get update
apt-get install nano
touch /ldifs/all.ldif
nano /ldifs/all.ldif
```

<p>Update the LDAP tree with the file all.ldif</p>

```
ldapadd -x -H ldap://localhost:1389 -D "cn=admin1,dc=oficinas,dc=com" -W -f /ldifs/all.ldif
```
<p>Remember admin password: adminpassword1</p>

<p>You can check the LDAP tree using commands or programs like Apache Directory Stucio</p>
<p>Now that your LDAP server is running you can run SpringBootOficinasApplication.java at make calls to search users in groups using http calls to the controller<p>

```
http://localhost:8080/oficinas/GRP1/EEXP0293
```

Or check tests using an inmemory LDAP server (ApacheDS)
