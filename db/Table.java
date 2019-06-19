package db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Table {


    Column[] columns;

    String[] columnNames;

    String[] columnTypes;

    String tableName;

    int numRows;

    int numColumns;

    /* table constructor without data, may need to add row and column number later
    * rows are annoying so may need a columnCollector class.
    * */


    public Table(String name, String[] columnNames, String[] columnTypes, Column[] columns) {

        this.tableName = name;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
        this.columns = columns;

        this.numColumns = columns.length;

        /*
        assign variable to first column with cast to access getSize method to assign number of rows.
         */


        this.numRows = columns[0].getSize();

    }

    public Table(String name, List<Column> columns){

        this.tableName = name;

        List<String> columnNames = new ArrayList<>();
        List<String> columnTypes = new ArrayList<>();

        for(Column col: columns){

            columnNames.add(col.getName());
            columnTypes.add(col.getType());
        }

        this.columnNames = columnNames.toArray(new String[columnNames.size()]);
        this.columnTypes = columnTypes.toArray(new String[columnTypes.size()]);
        this.columns =  columns.toArray(new Column[columns.size()]);

        this.numColumns = this.columns.length;


        this.numRows = this.columns[0].getSize();


    }



    /* constructor that takes in a 2D array of table information and creates a table using columns.
    *  Data should be arranged such that the 2D array is like a table with rows and columns.
    *

    public Table(String name, String[] columnNames, String[] columnTypes, String[][] data){
    }


    all information should be parsed from a printed table representation

    this can be parsed and built from a column collection class, a class that builds and organizes columns
    by adding values to columns individually while parsing from a table.

    x int,y int
    2,5
    8,3
    13,7
    */


    @Override
    public boolean equals(Object other){


        if(this == other){

            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Table otherTable = (Table) other;

        /*
        ensure column types are equal, column names are equal, and all columns are equal
        may have to override equals method in columns also.
         */

        if(!Arrays.equals(this.columnNames, ((Table) other).columnNames)){
            /* checks the names of the columns for equality */

            return false;

        }

        return Arrays.equals(this.columns, ((Table) other).columns);

    }

    /**
     * returns the list of coulumns the table is made up from
     * @return Column[] array of columns
     */
    public Column[] getColumns(){

        return columns;

    }

    /**
     * getter for the names of the columns
     * @return String[] of column names
     */
    public String[] getColumnNames(){

        return columnNames;

    }


    /**
     * Returns true if the exact column is contained in the other table.
     * This means that the name, type and values must all be equivalent.
     * @param checkColumn Column column you want to check is in table
     * @return Boolean
     */
    public boolean containsColumn(Column checkColumn){

        for(Column col : columns){

            if(checkColumn.equals(col)){
                return true;
            }
        }

        return false;
    }

    /**
     * boolean for whether the table contains a column with the same name and type as parameter column.
     * this is useful for joins because we want to find a column with similar names and types that might
     * have different values.
     * @param checkColumn Column
     * @return boolean
     */
    public boolean hasMatchingColumn(Column checkColumn){

        for(Column col: columns){

            if(checkColumn.getName().equals(col.getName()) && checkColumn.getType().equals(col.getType())){

                return true;
            }
        }

        return false;
    }


    /**
     * gets all the columns that have the same name and type as columns in the other(param)
     * table.
     * @param other Table that you want to compare columns
     * @return a List columns this table has in common with other table
     */
    public List<Column> matchingColumns(Table other){

        List<Column> matchingColumns = new ArrayList<>();

        for(Column col : this.columns){

            if(other.hasMatchingColumn(col)){

                matchingColumns.add(col);
            }
        }

        return matchingColumns;
    }

    /**
     * returns the columns in this table as an arraylist instead of a array
     * @return List<Column></Columns>
     */
    public List<Column> getColumnsAsList(){

        List<Column> listOfColumns = new ArrayList<>();

        for(Column col: this.columns){

            listOfColumns.add(col);
        }

        return listOfColumns;
    }

    /**
     * inserts a new last row into the table, will print error if literals are not correct
     * amount or type for the table
     * @param literals Object list of values
     * @return String arbitrary, can maybe switch to a void function
     */
    public String insertInto(List<Object> literals){

        try {
            if (literals.size() != this.columns.length) {

                throw new IllegalArgumentException();
            }


            for (int i = 0; i < literals.size(); i++) {


                if((this.columns[i].getType() == "string" && literals.get(i) instanceof String)
                        || (this.columns[i].getType() == "int" && literals.get(i) instanceof Integer)
                        || (this.columns[i].getType() == "float" && literals.get(i) instanceof Float)){

                    continue;

                } else{

                    throw new IllegalArgumentException();
                }
            }

        } catch (Exception e){

            System.out.println("invalid literal input");
            return "invalid";

        }

        for(int i = 0; i < literals.size(); i++){

            this.columns[i].add(literals.get(i));
        }

        this.numRows += 1;


        System.out.println(" ");
        return "";
    }



    /**
     * print method that prints the column name, type and values
     */
    public void printTable(){

        /*
        print first line of the table
         */

        int index = 0;

        while(index < columnNames.length){

            if(index == columnNames.length - 1){

                System.out.print(columnNames[index] + " " + columnTypes[index] + "\n");

            }else{

                System.out.print(columnNames[index] + " " + columnTypes[index] + ",");
            }

            index += 1;

        }

        /*
        print the values of the table each row on a new line and each value separated by a comma.
        print across a row then print down a column
         */

        for(int rowIndex = 0; rowIndex < numRows; rowIndex++){

            for(int colIndex = 0; colIndex < numColumns; colIndex++){

                /* print respective value in each column */

                if(colIndex == numColumns - 1){

                    System.out.print(columns[colIndex].getValue(rowIndex) + "\n");

                }else{

                    System.out.print(columns[colIndex].getValue(rowIndex) + ",");
                }
            }
        }
    }




    public static void main(String[] args){

        Column<String> first = new Column<>("first", "string", new String[]{"1", "2", "4", "8", "9"});
        Column<String> second = new Column<>("second", "string", new String[]{"11", "12", "14", "18", "19"});
        Column<String> third = new Column<>("third", "string", new String[]{"21", "22", "24", "28", "29"});
        Column<String> fourth = new Column<>("fourth", "string", new String[]{"31", "32", "34", "38", "39"});

        Column[] colArray = new Column[]{first, second, third, fourth};

        String[] colNames = new String[]{"first", "second", "third", "fourth"};

        String[] colTypes = new String[]{"string", "string", "string", "string"};


        Table Table1 = new Table("Table1", colNames, colTypes, colArray);

        Table1.printTable();

        List<Object> insertList = new ArrayList<>();
        insertList.add("a");insertList.add("c");insertList.add("d");insertList.add("e");

        Table1.insertInto(insertList);

        Table1.printTable();

        

    }

}
