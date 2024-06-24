
# IJA Project Robots

## Authors

 - Ivan Onufriienko
 - Ivan Burlustkyi

## Task

https://moodle.vut.cz/mod/folder/view.php?id=379925

## Plan (requirements?)
https://docs.google.com/spreadsheets/d/1NNP9SpfVDo6mwxlEKvGs7YZvszVIL-xYD8ANiG9ocII/edit?usp=sharing

## Build tools

- Maven
- JDK 17

## Code structure

***ija.project.model*** - declaration of all models/entities   
***ija.project.ui*** - handlers for input/output and view model  
***ija.project.observer*** - definition of observer class  
***ija.project.util*** - utils for reducing repeating code  
***ija.project.dto*** - data transfer objects  
***ija.project.generator*** - generation of long values  
***ija.project.timer*** - timer class for room animations  

## Used java dependencies

1. Lombok - fast boilerplate code generation
2. Logback - logging library
3. JUnit5 - testing framework
4. Swing  - embed GUI library
5. AWT - colliders implementation
6. Jackson - for reading and writing json files
7. JavaFX Controls - for GUI components
8. JavaFX FXML - for working with FXML in JavaFX applications.

## Build and run instructions

``` bash
mvn package
java -jar ./target/project-1.0.jar
```

## Generate documentation

``` bash
mvn javadoc:javadoc
```

## Test

Under development ....

## Notes
Version 1.0
1. Unfortunately, build requires access to the internet for installation and could not be installed
offline due to the limitations of file size for submission.
2. Output represent the movement of two robots to each other,
then their rotation on detection and moving to walls,
with subsequent rotation.
