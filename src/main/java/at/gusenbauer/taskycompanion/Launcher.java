/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.gusenbauer.taskycompanion;


import javafx.application.Application;

/**
 * @author chris
 */
public class Launcher {

    /**
     * Wrapper Main Class to launch the JavaFX App for Compatability Reasons
     *
     * @param args
     */
    public static void main(String[] args) {
        Application.launch(MainApp.class);
    }
}
//I'd like to share my findings regarding launching a javafx 11 app. I'm talking about an existing legacy javafx app originally developed without modules (e.g. w/ Java 8).
//
//The most interesting way IMO is Method #2b below, which requires the minimum of changes, i.e. it doesn't need -module-path argument. I mention that I'm using maven, so cf. previous answers the first step is to add a dependency in your pom.xml towards javafx. E.g.:
//
//<dependency>
//  <groupId>org.openjfx</groupId>
//  <artifactId>javafx-controls</artifactId>
//  <version>11.0.2</version>
//</dependency>
//In order to launch, I found several options:
//
//1a) Launch using maven from command line
//Configure in your pom.xml a section like:
//
//    <build>
//        <plugins>
//            ...
//            <plugin>
//                <groupId>org.codehaus.mojo</groupId>
//                <artifactId>exec-maven-plugin</artifactId>
//                <version>1.6.0</version>
//                <executions>
//                    <execution>
//                        <goals>
//                            <goal>java</goal>
//                        </goals>
//                    </execution>
//                </executions>
//                <configuration>
//                    <mainClass>org.openjfx.hellofx.MainApp</mainClass>
//                </configuration>
//            </plugin>
//        </plugins>
//    </build>
//After this, from a console window you can run mvn exec:java, and this should launch the application. Maven will take care of adding all the entries from classpath to module path. I remind the CTRL + ALT + T shortcut in Eclipse, that opens a terminal window directly in the IDE.
//
//1b) Launch using maven, but as an Eclipse launch config
//Right click on the project > Run As > Maven Build.... Then enter exec:java in the Goals text box. Advantage over the previous method: a bit more integrated to Eclipse. And easy debugging. You only need to relaunch the launch config in Debug mode and that's it. May I remind that the Eclipse launch configs can be stored as .launch files directly in the project dir (repo), thus shareable/reusable by colleagues. Use the last tab, Common, in the Edit Configuration window.
//
//2a) Launch using Eclipse and specifying -module-path using a variable
//Right click on the main class > Run As > Java Application. The first time you click a new launch configuration is created; probably it won't work, so you need to edit it (e.g. **Run button (w/ Play icon) in the toolbar > Run configurations > select the one corresponding to your class. Or CTRL + click on it after clicking on Play).
//
//Then add this in Arguments > VM arguments:
//
//--module-path ${project_classpath:REPLACE_ME_WITH_YOUR_PROJECT_NAME} --add-modules javafx.controls,javafx.fxml
//So the trick here was to use the ${project_classpath} variable. Because otherwise you should have needed to write exactly the path towards the javafx jar, that are somewhere in your .m2 repo. Doing this would make the launch config not easily reusable by colleagues.
//
//2b) Launch using Eclipse WITHOUT specifying -module-path
//This is la piece de résistence, which I found by mistake, after about 5h of Java 11 & modules "fun". Your application can work out of the box, without touching -module-path. The javafx libs will of course still need to be in your classpath (but this is handled by mvn). The trick is (cf. this) that your main app SHOULD NOT extend Application (e.g. MyApplication below). If this is your case, then make a new class with a main() function that does e.g.:
//
//Application.launch(MyApplication.class);
