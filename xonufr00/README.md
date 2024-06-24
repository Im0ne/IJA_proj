
# IJA Project Robots

## Authors

 -Ivan Onufriienko  
 -Ivan Burlustkyi

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
3. Jackson - for reading and writing json files
4. JavaFX Controls - for GUI components
5. JavaFX FXML - for working with FXML in JavaFX applications.
6. JavaFX Graphics - for graphic primitives

## Build and run instructions

``` bash
mvn clean package
java -jar .\target\project-1.0-shaded.jar
```

## Generate documentation

``` bash
mvn javadoc:javadoc
```

## Implemented features

- [x] Adding robots/blocks
- [x] Moving logic for robot
- [x] Control mode for robot
- [x] Saving to json
- [x] Loading to json
- [x] Pause
- [x] Property windows (editing models)
- [x] Removing robots/blocks
- [x] Keyboard editing and movement
- [x] Auto resize on window change
- [x] Choose time to move back


## Editable properties

- Robot
  - Position
  - Robot frame sizes
  - Arc size and extent
  - Rotation sample
  - Rotation speed
  - Movement speed
- Block
  - Position
  - Size
- Room
  - Size

# Color description

1) RED - robot has detected or colliding or out of the room
2) YELLOW - default color of the arc
3) BLUE - default color of the robot frame
4) GREEN - robot is under control
5) GREY -default block color
6) PURPLE - robot is rotating

## Controls description

- During pause
 - KEY_A - Decrease arc extent
 - KEY_D - Increase arc extent
 - KEY_W - Increase arc radius
 - KEY_S - Decrease arc radius
 - KEY_LEFT - Rotate robot counterclockwise
 - KEY_RIGHT - Rotate robot clockwise
 - KEY_UP - increase robot frame radius
 - KEY_DOWN - decrease robot frame radius

- During play mode + robot controlled
 - KEY_RIGHT - rotate clockwise
 - KEY_LEFT - rotate counterclockwise
 - KEY_UP - move robot forward in his direction
 - KEY_DOWN - move robot backward 
