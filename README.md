# Beschreibung

Diese Website ist eine Cloud Stroage Website. Mit dieser kann man Dateien hochladen und herunterladen.  
Dieses Projekt soll mir hierbei bei der Bewerbung helfen.

## Projekt Starten

1. Repository klonen
2. Backend starten (Spring Boot). Dies kann man entweder durch die IDE, wo man die Application klasse startet
   oder im Terminal wo man im Projektordner Cloudservice "./gradlew bootrun" aufruft.
3. Browser öffnen: (Lokal) http://localhost:8080 oder auch die IPv4-Adresse mit :8080.
4. Account anlegen/registrieren. Hierbei muss man bei key "test" eingeben.
5. Einloggen

Die Anwendung verwendet eine eingebettete H2-Datenbank,
daher ist keine Datenbankinstallation nötig.

Das Projekt ist so angelegt, dass es im lokalen wlan getestet werden kann. Möchte man
dies nicht, so sollte man bei application.properties "server.address=0.0.0.0" und "server.port=8080" löschen und man kann dann das projekt nur lokal testen.

## Funktionen

Momentan kann man nur Dateien hochladen und herunterladen. Zudem kann man diese auch auf seinen Account löschen.
