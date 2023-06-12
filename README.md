# LightEmAll

A puzzle game - try to light all the pieces!

## Getting Started

### Prerequisites

Java version between 8 and 19.

### Installing and running

Download the repository and unzip it.

Run the project

```
./gradlew run
```

The game runs with a default of an 7x7. To change this, run the project with command line arguments.

```
./gradlew run --args="-r 5 -c 6"
```

or

```
./gradlew run --args="--rows 5 --cols 6"
```

## Mechanics

Click a piece to rotate it. Left click rotates counterclockwise, right click rotates clockwise. Use keyboard arrow keys to move the light source. The goal is to have all the pieces connected and lit up.

## Built With

* [Amazon Coretto 17](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html) - distribution of OpenJDK used
* [Gradle 8](https://gradle.org/) - build automation tool