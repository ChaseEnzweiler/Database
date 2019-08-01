# Database
This is a small version of a relational database management system. A declarative programming language similar to SQL was also implemented 
for the user to interact with the database. All code I wrote is contained in the db package and all tables are loaded from and stored to 
the examples file.

## Commands 
Here are some commands supported by the database. Arbitrary amounts of whitespace is allowed inside commands. 
Run Main.java to use the database

* Load tables into the database
```
load fans
```
* Store tables as .tbl files in examples file.
```
store fans
```
* Create table as a new table or create table from a select statement
```
create table Trees (Name string, Count int, AvgHeight float)

create table Trees as select * from MyTrees, YourTrees where Height > 5.5
```
* Drop tables from the database
```
drop table fans
```
* Insert a new row into a table
```
insert into Trees values 'oak', 5, 40.1
```
* Print table 
```
print fans
```
* Select Statements
```
select Amount / PeopleCount as Average from Party
select Wins, Season from fans, records where Season > 2014 and Wins < 100
select Wins - Losses as Difference, TeamName from fans, records
```
