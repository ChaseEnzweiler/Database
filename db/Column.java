package db;

import com.sun.tools.javac.comp.Todo;
import com.sun.xml.internal.bind.v2.TODO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Column<T> {


    String name;

    String type;

    /* set column as an array of strings, as functionality begins to work
    expand to include other object types
     */

    private List<T> values;


    public Column(String name, String type, T[] values){

        this.name = name;
        this.type = type;

        // Beware of line below if anything breaks

        this.values = new ArrayList<>(Arrays.asList(values));


    }

    /**
     * Overloaded constructor that takes in an arraylist for values instance variable
     * @param name String name of the column
     * @param type String type of the column
     * @param values ArrayList<T> the values of the column
     */
    public Column(String name, String type, List<T> values){ //changed ArrayList<T> to List<T>

        this.name = name;
        this.type = type;

        // Beware of line below if anything breaks

        this.values = values;


    }



    /**
     * Overriding equals method of object superclass
     * @param other column this is comparing itself to
     * @return boolean
     */
    @Override
    public boolean equals(Object other){

        if(this == other){

            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Column otherColumn = (Column) other;

        /*
        ensure column types are equal, column names are equal, and all columns are equal
        may have to override equals method in columns also.
         */

        if(!this.name.equals(otherColumn.name)){
            /* differing names */
            return false;
        }

        if(!this.type.equals(otherColumn.type)){
            /* checks for differing types this may not be needed
            b/c will be handled when comparing values
             */
            return false;
        }

        return this.values.equals(otherColumn.getValues());

    }


    /**
     * returns the size of the column i.e how many values the column has
     * @return int size of column
     */

    public int getSize(){

        return values.size();

    }

    /**
     * getter method for the type of the column
     * @return String type
     */
    public String getType(){

        return type;
    }

    /**
     * gets value from specified location/row of the column
     * @param index which row/ location the desired value is
     * @return String value store in the column
     */

    public T getValue(int index){

        return values.get(index);

    }

    /**
     * returns a list of all the values making up the column
     * @return List</String> of values of column
     */
    public List<T> getValues(){

        return values;
    }


    /**
     * returns the name of the column
     * @return String name
     */
    public String getName(){

        return name;

    }


    /**
     * takes in a list of values by row that you want to keep and returns a new column
     * with the same name and type with only the values to keep and other values filtered out
     * The rows should be zero indexed
     * @param rowsToKeep List of integers of which rows of the values to keep
     * @return new Column with an updated values list.
     */
    public Column<T> filterByRow(List<Integer> rowsToKeep){


        ArrayList<T> filteredValues = new ArrayList<>();

        for(int index : rowsToKeep){

            filteredValues.add(this.values.get(index));
        }

        return new Column<>(this.name, this.type, filteredValues);

    }

    /**
     * adds value to the end of the column
     * @param literal T value
     */
    public void add(T literal){

        this.values.add(literal);

    }

    /*
    need static arithmetic operations that also check types and throwing exceptions. For no value and not a number
    there will be no changes to further operations on nan, but for division by zero it will throw an arithmetic
    exception which we will catch and set to a specified number where we will then print NAN in the print
    function of a table
     */


    /**
     * value by value addition of two columns, returns a new columns with added together values.
     * throws exception if trying to add string type to float or integer.
     * @param col1 Column object
     * @param col2 Column object
     * @param name name to new Column to return
     * @return new Column
     * @throws IllegalArgumentException
     */
    public static Column addition(Column col1, Column col2, String name) throws IllegalArgumentException{

        /*
        check to make sure the types of the columns are correct or else throw exception
        string types cannot be added to column types of float or int
        if one column has string types the other columns cannot!
         */
        if(col1.getType().equals("string") || col2.getType().equals("string")) {

            if (!col2.getType().equals("string")) {

                throw new IllegalArgumentException();

            } else if(!col1.getType().equals("string")) {

                throw new IllegalArgumentException();
            }
        }

        /*
        find the appropriate type for the new Column
         */
        String newType;

        if(col1.getType().equals("float") || col2.getType().equals("float")){

            newType = "float";

        } else if(col1.getType().equals("string")){

            newType = "string";

        } else{

            newType = "int";

        }
        /*
        do arithmetic operation on the values of the two columns
         */
        List newValues = new ArrayList();

        for(int i = 0; i < col1.getSize(); i++){

            if(newType.equals("string")){

                newValues.add((String) col1.getValue(i) + (String) col2.getValue(i));

            } else if(newType.equals("int")){

                newValues.add((int) col1.getValue(i) + (int) col2.getValue(i));

            } else{

                if(col1.getType().equals("int")){

                    int x = (int) col1.getValue(i);
                    float y = (float) col2.getValue(i);
                    newValues.add(x + y);


                } else if(col2.getType().equals("int")){

                    float x = (float) col1.getValue(i);
                    int y = (int) col2.getValue(i);
                    newValues.add(x + y);

                } else{

                    float x = (float) col1.getValue(i);
                    float y = (float) col2.getValue(i);
                    newValues.add(x + y);

                }
            }
        }

        return new Column(name, newType, newValues);

    }
















}
