# drone-project
Interdisziplinäres Projekt

_Members_ Lucas Gersch, Tim Bensberg, Luisa Voigt, André Voiß, Simon Beck

## Ordnerstruktur
_src_ .java Sourcedateien

_license_ Lizenzdateien (z.B. Apache)

## Install

### GlassFish Server _HTTP Server_

Install GlassFish from [https://javaee.github.io/glassfish/download](https://javaee.github.io/glassfish/download)

IntelliJConfig:
File->Settings->Build,Execution,Deployment->Application Servers

Point to Valid GlassFish path

### Maven _dependency management_

Install Maven from [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)



## REST API
Verschiedene APIS
### Authentifizierung 
Die Authentifizierung erfolgt über Basic Auth. Ein Benutzername sowie ein passwort werden Base64 verschlüsselt an den Server im Header übergeben
Für User test PW test wäre das bspw. `Basic dGVzdDp0ZXN0`

Test der Authentifizierung über eigene Methode
https://pphvs03.reekind.de/rest/users/authenticate
Übergabe als x-www-form-urlencoded

    POST /rest/users/authenticate HTTP/1.1
    Host: pphvs03.reekind.de
    Accept: application/json
    Authorization: Basic dGVzdDp0ZXN0
    Content-Type: application/x-www-form-urlencoded
    Cache-Control: no-cache
    
    username=test&password=test

### URLS

URL https://pphvs03.reekind.de/rest/drones

URL https://pphvs03.reekind.de/rest/orders

URL https://pphvs03.reekind.de/rest/users (Nur lesend)

#### GET - Lade alle Items

    GET /rest/orders/ HTTP/1.1
    Host:pphvs03.reekind.de
    Accept: application/json
    Authorization: Basic dGVzdDp0ZXN0
    Cache-Control: no-cache
 
 *Benutze `/rest/orders/1` zum Lesen einer einzelnen Drohne*
 #### POST - Neues Item anlegen
 
     POST /rest/orders/ HTTP/1.1
     Host: pphvs03.reekind.de
     Content-Type: application/json
     Authorization: Basic dGVzdDp0ZXN0
     Cache-Control: no-cache
     
         {
             "orderTime": "2017-01-20T01:01:00",
             "weight": 500,
             "orderStatus": "EINGEGANGEN",
             "droneId": -1,
             "location": {
                 "latitude": 50.28,
                 "longitude": -3.8
             }
         }
 #### PUT - Bearbeiten eines vorhandenen Items anhand der Id
 
     PUT /rest/orders/ HTTP/1.1
     Host: pphvs03.reekind.de
     Content-Type: application/json
     Authorization: Basic dGVzdDp0ZXN0
     
         {
            "orderId": "1",
             "orderTime": "2017-01-20T01:01:00",
             "weight": 900,
             "orderStatus": "EINGEGANGEN",
             "droneId": -1,
             "location": {
                 "latitude": 50.28,
                 "longitude": -3.8
             }
         }
