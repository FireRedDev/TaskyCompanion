# JavaFX Daily-Organizer TaskyCompanion


## Beschreibung

In diesem Projekt wurde ein Aufgabenplaner als Desktopapp implementiert. 
Mit diesem Programm („TaskyCompanion“) kann man Aufgaben verwalten, via Drag & Drop abhaken, Color-Coden, kategorisieren via Tags und Projekten, filtern, Zeittracking betreiben, diese Daten sichern und importieren und Statistiken ansehen.

Das Projekt baut auf dem JavaFX Maven Archetype aus. Benutzte Technologien sind hauptsächlich Java und JavaFX. Das Projekt ist in die MainApp Klasse, eine Launcher Klasse und drei Packages gegliedert: 
Dataclasses – beinhaltet Datenobjekte die repräsentiert werden
Util – Diverse Hilfsklassen wie Enums & Dateparser
Controller – beinhaltet die Steuernde Logik, die Controller
Im Ressources Ordner finden sich die Views (fxml) und die App-Settings als Property-File, wie auch Styling-Related Content.


![Screenshot](https://github.com/Jiraci/TaskyCompanion/blob/master/screenshot.PNG)

Die App ist ein Soloprojekt von mir, das ich urprünglich innerhalb der Uni als Gruppenarbeit gemacht hätte, aber dann wie es der Zufall so wollte alleine gemacht hab.

## Installationsanleitung

Benötigt: Maven, Java, je nach Ausführmethode OpenJFX oder ein JDK mit OpenJFX inkludiert, z.B. Liberica JDK


Via IDE: Rechtsklick auf Launcher.java, Ausführen


Via Maven:
mvn clean javafx:run


Via JLINK/RunScript:
Mvn javafx:jlink


Im Projektdirectoryroot in der Shell:
target/image/bin/java -m at.gusenbauer.taskycompanion/at.gusenbauer.taskycompanion.Launcher

In der aktuellsten Projektversion wird auch ein .bat Script generiert, welches automatisch die App ausführt. 
Dieses generierte Image ist Plattformabhängig und muss je OS neu generiert werden, im Github Repository inkludiert sind die Windows Dateien.

## Ressourcen
Als Hilfestellung wurden sehr viele verschiedene Websiten und Guides benützt, z.B.:
Stackoverflow, JavaFX CSS Reference Documentation, Marco Jacobs JavaFX Blog Guides und ein Base Project, Baldung, Oracle Documentation, Github Wikis, draeger-it Blogs, IntelliJ Doku, diverse Subreddits wie /r/java /r/javahelp und /r/programming

