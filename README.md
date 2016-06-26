# ch-data-pipeline-test
Miniature example of a data pipeline according to [problem spec](file_parser.md).

## Required dependencies
Java JDK: 1.8.0_45 or later
MySql: 5.6.25 or later

All other dependencies will be downloaded from Maven Central when building the project.

## How to run jar
Jar should be available here: [clover-1.0-SNAPSHOT.jar](clover-1.0-SNAPSHOT.jar)
Execute the following command:
```
java -jar <path_to_clover_jar> <database name> <spec file path> <data file path>
```
A command that should work with a clone of this repository:
```
java -jar clover-1.0-SNAPSHOT.jar NewDatabase specs/ data/
```