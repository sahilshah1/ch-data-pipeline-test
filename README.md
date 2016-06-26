# ch-data-pipeline-test
Miniature example of a data pipeline according to [problem spec](file_parser.md).

## Assumptions 
Some assumptions were made to solve the problem. I should've asked questions,
but this is what I get for putting this off till the weekend.
I've tried to write the code to account for changes in these assumptions.

* MySql database server running at localhost:3306 (see [client class](src/main/java/com/ch/persistence/MySqlClient.java)) for more details)
* Spec files and data files are present at invocation time, files are **not** continuously dropped
* each spec file corresponds to 1 table in the database. so `test1.csv` will prompt creation
of a `test1` table and `test1_2016-06-23.txt` will prompt insertion of rows into that table
* Only 3 unique column types in spec: `"column name"`, `width`, and `datatype`.
the actual values and order of those columns can be anything. also assumes the first line of each spec is something like below (the exact names matter, order doesn't):
```
valid: "column name",width,datatype
valid: "column name",datatype,width
invalid: "colName",width,datatype
```

## Required dependencies
* Java JDK: 1.8.0_45 or later
* MySql: 5.6.25 or later

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
