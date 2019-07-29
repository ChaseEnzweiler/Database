package db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Table {
    
    List<Column> columns;

    String[] columnNames;

    String[] columnTypes;

    String tableName;

    int numRows;

    int numColumns;


    Table(String name, String[] columnNames, String[] columnTypes, Column[] columns) {

        this.tableName = name;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
        this.columns = new ArrayList<>(Arrays.asList(columns));

        this.numColumns = columns.length;

        /*
        assign variable to first column with cast to access getSize method to assign number of rows.
         */


        this.numRows = columns[0].getSize();

    }

    Table(String name, List<Column> columns){

        this.tableName = name;

        List<String> columnNames = new ArrayList<>();
        List<String> columnTypes = new ArrayList<>();

        for(Column col: columns){

            columnNames.add(col.getName());
            columnTypes.add(col.getType());
        }

        this.columnNames = columnNames.toArray(new String[columnNames.size()]);
        this.columnTypes = columnTypes.toArray(new String[columnTypes.size()]);
        this.columns =  columns;

        this.numColumns = this.columns.size();


        this.numRows = this.columns.get(0).getSize();


    }

    /*
    constructor that only takes in column names and types. input parameters are a String[] name, String[] type
    used in create table parse
     */

    Table(String tableName, List<String> columnNames, List<String> columnTypes){

        this.tableName = tableName;
        this.columnNames = columnNames.toArray(new String[columnNames.size()]);
        this.columnTypes = columnTypes.toArray(new String[columnTypes.size()]);;
        this.numRows = 0;
        this.numColumns = columnNames.size();

        List<Column> columns = new ArrayList<>();

        for(int i = 0; i < columnNames.size(); i++){

            Column toAdd = new Column(columnNames.get(i), columnTypes.get(i), new Object[]{});

            columns.add(toAdd);
        }

        this.columns = columns;
    }


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

        return this.columns.equals(((Table) other).columns);

    }

    /**
     * returns the name of the Table
     * @return String tableName
     */
    String getTableName(){

        return tableName;
    }

    /**
     * returns the list of columns the table is made up from
     * @return Column[] array of columns
     */
    List<Column> getColumns(){

        return columns;

    }

    /**
     * getter for the names of the columns
     * @return String[] of column names
     */
    String[] getColumnNames(){

        return columnNames;

    }


    /**
     * Returns true if the exact column is contained in the other table.
     * This means that the name, type and values must all be equivalent.
     * @param checkColumn Column column you want to check is in table
     * @return Boolean
     */
    boolean containsColumn(Column checkColumn){

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
    boolean hasMatchingColumn(Column checkColumn){

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
    List<Column> matchingColumns(Table other){

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
    List<Column> getColumnsAsList(){

        return this.columns;
    }


    /**
     * returns proper column from table by name, if column with nameToGet does not exists
     * throws a illegal argument exception
     * @param nameToGet String of the name of the Column you want returned
     * @return Column
     * @throws IllegalArgumentException throws if Column name does not exist
     */
    Column getColumnByName(String nameToGet) throws IllegalArgumentException{

        List<Column> columns = this.getColumnsAsList();

        /*
        look through columns for matching name, if not found throw Illegal Argument Exception
         */

        for(Column col : columns){

            if(col.getName().equals(nameToGet)){

                return col;
            }
        }

        throw new IllegalArgumentException();
    }


    /**
     * inserts a new last row into the table, will print error if literals are not correct
     * amount or type for the table
     * @param literals Object list of values
     * @return String arbitrary, can maybe switch to a void function
     */
    String insertInto(List<Object> literals){

        try {
            if (literals.size() != this.columns.size()) {

                throw new IllegalArgumentException();
            }


            for (int i = 0; i < literals.size(); i++) {


                if((this.columns.get(i).getType().equals("string") && literals.get(i) instanceof String)
                        || (this.columns.get(i).getType().equals("int") && literals.get(i) instanceof Integer)
                        || (this.columns.get(i).getType().equals("float") && literals.get(i) instanceof Float)){

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

            this.columns.get(i).add(literals.get(i));
        }

        this.numRows += 1;


        System.out.println(" ");
        return "";
    }



    /**
     * print method that prints the column name, type and values
     */
    void printTable(){

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

                    System.out.print(columns.get(colIndex).getValue(rowIndex) + "\n");

                }else{

                    System.out.print(columns.get(colIndex).getValue(rowIndex) + ",");
                }
            }
        }
    }


    /**
     * Method that returns a list of each line of the table as strings. first element will be the name and types,
     * then the rest each row of values, formatted like print table. (verified) w/ prints
     * @return List of Strings of each line of table
     */
    List<String> tableToStringSegments(){

        String firstLine = "";

        StringBuffer sb = new StringBuffer(firstLine);

        List<String> stringSegments = new ArrayList<>();

        int count = 0;

        /*
        loop to add first line of names and types
         */

        while(count < columnNames.length){


            if(count == columnNames.length - 1){

                String toAppend = columnNames[count] + " " + columnTypes[count];

                sb.append(toAppend);

            }else{

                String toAppend = columnNames[count] + " " + columnTypes[count] + ",";

                sb.append(toAppend);
            }

            count += 1;
        }

        stringSegments.add(sb.toString());

        int valueCount = 0;

        while(valueCount < numRows){

            String valueSegments = "";

            StringBuffer valueBuffer = new StringBuffer(valueSegments);

            int colCounter = 0; // used to check which column is last to stop using commas

            for(Column col : columns){

                if(colCounter == numColumns - 1){

                    String toAppend = "" + col.getValue(valueCount) + "";

                    valueBuffer.append(toAppend);

                }else{

                    String toAppend = "" + col.getValue(valueCount) + ",";

                    valueBuffer.append(toAppend);
                }

                colCounter += 1;
            }

            stringSegments.add(valueBuffer.toString());

            valueCount += 1;

        }

        return stringSegments;

    }




    public static void main(String[] args){

        Column first = new Column("first", "string", new String[]{"1", "2", "4", "8", "9"});
        Column second = new Column("second", "string", new String[]{"11", "12", "14", "18", "19"});
        Column third = new Column("third", "string", new String[]{"21", "22", "24", "28", "29"});
        Column fourth = new Column("fourth", "string", new String[]{"31", "32", "34", "38", "39"});

        Column[] colArray = new Column[]{first, second, third, fourth};

        String[] colNames = new String[]{"first", "second", "third", "fourth"};

        String[] colTypes = new String[]{"string", "string", "string", "string"};


        Table Table1 = new Table("Table1", colNames, colTypes, colArray);

        Table1.printTable();

        List<Object> insertList = new ArrayList<>();
        insertList.add("a");insertList.add("c");insertList.add("d");insertList.add("e");

        Table1.insertInto(insertList);

        Table1.printTable();

        System.out.println(Table1.tableToStringSegments());

        

    }

}
