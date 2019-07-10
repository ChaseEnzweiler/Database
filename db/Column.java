package db;

import com.sun.tools.javac.comp.Todo;
import com.sun.xml.internal.bind.v2.TODO;

import java.util.*;

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

    /**
     * returns a duplicate of the column with a different name than the original
     * @param name String name of what you want duplicate column to be named
     * @return new Column
     */
    public Column<T> changeName(String name){

        return new Column<>(name, this.getType(), this.getValues());

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
        Can probably make a separate function to check for those exceptions in beginning
        need to add functionality if column contains "NaN" for addition and subtraction.
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
        String newType = calculateType(col1, col2);

        /*
        do arithmetic operation on the values of the two columns
         */
        List newValues = new ArrayList();

        for(int i = 0; i < col1.getSize(); i++){

            if(col2.getValue(i).equals("NaN") || col1.getValue(i).equals("NaN")) {

                newValues.add("NaN");

            } else if(newType.equals("string")){

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


    /**
     * subtracts the values of two columns and returns a new columns with the resulting values
     * only works for int and float types, string column types will throw exception
     * It will be col1 - col2
     * @param col1 Column to be subtracted
     * @param col2 Column to be subtracted by
     * @param name String name of the new Column
     * @return new Column with results of subtraction
     * @throws IllegalArgumentException throws if given string Column
     */
    public static Column subtraction(Column col1, Column col2, String name) throws IllegalArgumentException{

        /*
        check if either columns have string type and throw an exception
         */

        if(col1.getType().equals("string") || col2.getType().equals("string")){

            throw new IllegalArgumentException();
        }

        /*
        find the type of the column to return
         */
        String newType = calculateType(col1, col2);

        List newValues = new ArrayList();

        /*
        perform subtraction on the two columns, check casts, and update new values with results.
        only supports subtraction of equal length columns
         */

        for(int i = 0; i < col1.getSize(); i++){

            if(col2.getValue(i).equals("NaN") || col1.getValue(i).equals("NaN")) {

                newValues.add("NaN");

            }else if(newType.equals("int")){

                newValues.add((int) col1.getValue(i) - (int) col2.getValue(i));


            } else if(col1.getType().equals("int")){

                int x = (int) col1.getValue(i);
                float y = (float) col2.getValue(i);

                newValues.add(x - y);

            } else if(col2.getType().equals("int")){

                float x = (float) col1.getValue(i);
                int y = (int) col2.getValue(i);

                newValues.add(x - y);

            } else{

                float x = (float) col1.getValue(i);
                float y = (float) col2.getValue(i);

                newValues.add(x - y);

            }

        }

        return new Column(name, newType, newValues);

    }

    /**
     * calculates the type of a column resulting in arithmetic operation on two columns
     * @param col1 Column
     * @param col2 Column
     * @return String type("int", "float", "string")
     * */
    public static String calculateType(Column col1, Column col2){

        String newType;

        if(col1.getType().equals("float") || col2.getType().equals("float")){

            newType = "float";

        } else if(col1.getType().equals("string")){

            newType = "string";

        } else{

            newType = "int";

        }

        return newType;

    }


    /**
     * value by value division of two columns of same length, returns new column of result,
     * does not change columns used for division, value set to "NaN" if there is zero division
     * @param col1  Column to be divided by, values are numerator
     * @param col2 Column used to divide, values are denominator
     * @param name String name of new resulting column
     * @return Column with resulting values of division
     * @throws IllegalArgumentException if trying to use String Columns
     */
    public static Column division(Column col1, Column col2, String name) throws IllegalArgumentException{

        if(col1.getType().equals("string") || col2.getType().equals("string")){

            throw new IllegalArgumentException();
        }

        /*
        get type of the resulting column after division and instantiate list to add
        results of division to
         */

        String newType = calculateType(col1, col2);

        List newValues = new ArrayList();

        /*
        divide col1 by col2 value by value, check if division by zero which results in NaN.
        maybe do case switch for the changes.
         */

        for(int i = 0; i < col1.getSize(); i++){

            if(col2.getValue(i).equals("NaN") || col1.getValue(i).equals("NaN") || col2.getValue(i).equals(0)) {

                newValues.add("NaN");

            }else if(newType.equals("int")){

                newValues.add((int) col1.getValue(i) / (int) col2.getValue(i));

            } else if(col1.getType().equals("int")){

                int x = (int) col1.getValue(i);
                float y = (float) col2.getValue(i);

                newValues.add(x / y);

            } else if(col2.getType().equals("int")){

                float x = (float) col1.getValue(i);
                int y = (int) col2.getValue(i);

                newValues.add(x / y);

            } else{

                float x = (float) col1.getValue(i);
                float y = (float) col2.getValue(i);

                newValues.add(x / y);
            }
        }

        return new Column(name, newType, newValues);
    }


    /**
     * static method for multiplying columns value-wise. returns new Column with values resulting
     * in multiplying values of parameter columns
     * @param col1 Column
     * @param col2 Column
     * @param name String name of the new Column to be returned
     * @return Column new column that contains results
     * @throws IllegalArgumentException if given Column parameter with type String
     */
    public static Column multiplication(Column col1, Column col2, String name) throws IllegalArgumentException{

        /* cannot perform multiplication on string type Columns */
        if(col1.getType().equals("string") || col2.getType().equals("string")){

            throw new IllegalArgumentException();

        }

        /*
        get type of column to return and instantiate list to hold new resulting values
         */

        String newType = calculateType(col1, col2);

        List newValues = new ArrayList();


        /*
        loop through and multiply values of each column together
        need to be fixed and zero division fix taken out
         */

        for(int i = 0; i < col1.getSize(); i++){

            if(col2.getValue(i).equals("NaN") || col1.getValue(i).equals("NaN")) {

                newValues.add("NaN");


            }else if(newType.equals("int")){

                newValues.add((int) col1.getValue(i) * (int) col2.getValue(i));


            } else if(col1.getType().equals("int")){

                int x = (int) col1.getValue(i);
                float y = (float) col2.getValue(i);

                    newValues.add(x * y);


            } else if(col2.getType().equals("int")){

                float x = (float) col1.getValue(i);
                int y = (int) col2.getValue(i);

                newValues.add(x * y);

            } else{

                float x = (float) col1.getValue(i);
                float y = (float) col2.getValue(i);

                newValues.add(x * y);
                
            }

        }

        return new Column(name, newType, newValues);
    }


    /**
     * method that takes a literal and gets all row indices of that column that has value equal
     * to that literal. Literal cannot be NOVALUE or "NAN"
     * @param literal String int, float, string
     * @return Set with integer indices
     * @throws NumberFormatException thrown if literal not correct type
     */
    public Set<Integer> rowsEqualTo(String literal) throws NumberFormatException{

        /*
        need to fix so that it doesn't throw exceptions for "NaN"
        catch it and reassign new literal to a really large number, like integer or float max.
         */

        Object newLiteral;
        Set<Integer> rowsToKeep = new HashSet<>();

        switch (this.type){

            case "int":

                newLiteral = Integer.parseInt(literal);
                break;

            case "float":

                newLiteral = Float.parseFloat(literal);
                break;

            default:

                newLiteral = literal;
        }

        /*
        compare values of the column to find which are equal
        set a check for "nan", needs to check types and max values
        and probably have to set individually. can set an object
        with each max value or string.
         */
        for(int i = 0; i < this.getSize(); i++){



            if(this.getValue(i).equals(newLiteral)){

                rowsToKeep.add(i);

            } else if(this.getValue(i).equals("NaN") && !this.type.equals("string")){
                // checks if float or integer literal is equal to max number assigned to
                // "NaN" values

                if(newLiteral.equals(Float.MAX_VALUE)){

                    rowsToKeep.add(i);
                }

            }
        }

        return rowsToKeep;
    }


    /**
     * similar to method above which returns a set of indices of rows that match a criteria. This method
     * returns a set of integer indices of values in the column that do no equal the given String literal
     * @param literal String value
     * @return Set</Integer> of indices of rows to keep
     * @throws NumberFormatException thrown in literal not correct type
     */
    public Set<Integer> rowsNotEqualTo(String literal) throws NumberFormatException{



        Object newLiteral;
        Set<Integer> rowsToKeep = new HashSet<>();

        switch (this.type){

            case "int":

                newLiteral = Integer.parseInt(literal);
                break;

            case "float":

                newLiteral = Float.parseFloat(literal);
                break;

            default:

                newLiteral = literal;
        }

        /*
        checks values in the column to find row indices that contain values that
        don't equal parameter newLiteral
         */
        for(int i = 0; i < this.getSize(); i++){



            if(!this.getValue(i).equals(newLiteral)){

                rowsToKeep.add(i);

            } else if(this.getValue(i).equals("NaN") && !this.type.equals("string")){
                // checks if float or integer literal is equal to max number assigned to
                // "NaN" values

                if(!newLiteral.equals(Float.MAX_VALUE)){

                    rowsToKeep.add(i);
                }

            }
        }

        return rowsToKeep;
    }


    /**
     * method takes in a literal and resturns a set of all the indices of where a column value is
     * less than the parameter literal. indices refer to the row the value is in.
     * @param literal String value
     * @return Integer Set of indices
     * @throws NumberFormatException thrown if literal is different type than column
     */
    public Set<Integer> rowsLessThan(String literal) throws NumberFormatException{



        Object newLiteral;
        Set<Integer> rowsToKeep = new HashSet<>();

        switch (this.type){

            case "int":

                newLiteral = Integer.parseInt(literal);
                break;

            case "float":

                newLiteral = Float.parseFloat(literal);
                break;

            default:

                newLiteral = literal;
        }

        /*
        checks values in the column to find row indices that contain values that
        are less than parameter newLiteral
         */
        for(int i = 0; i < this.getSize(); i++){

            // use compare to with strings checking for NaN

            if(this.getType().equals("string")){

                if(this.getValue(i).equals("NaN")){

                    continue;

                } else if(this.getValue(i).toString().compareTo(newLiteral.toString()) < 0){

                    rowsToKeep.add(i);
                }


            } else if(this.getValue(i).equals("NaN")){

                continue;

            } else{
                /*
                compare values that are Floats or Integers
                 */

                T columnValue =  this.getValue(i);

                try{

                    if((Float) columnValue < ((Float) newLiteral)){

                        rowsToKeep.add(i);

                    }

                } catch (Exception e){

                    if((Integer) columnValue < ((Integer) newLiteral)){

                        rowsToKeep.add(i);

                    }
                }
            }
        }

        return rowsToKeep;
    }


    /**
     * method takes in a literal and returns a set of all the indices of where a column value is
     * less than or equal to the parameter literal. indices refer to the row the value is in.
     * @param literal String value
     * @return Integer Set of indices
     * @throws NumberFormatException thrown if literal is different type than column
     */
    public Set<Integer> rowsLessThanOrEqual(String literal) throws NumberFormatException{



        Object newLiteral;
        Set<Integer> rowsToKeep = new HashSet<>();

        switch (this.type){

            case "int":

                newLiteral = Integer.parseInt(literal);
                break;

            case "float":

                newLiteral = Float.parseFloat(literal);
                break;

            default:

                newLiteral = literal;
        }

        /*
        checks values in the column to find row indices that contain values that
        are less than parameter newLiteral
         */
        for(int i = 0; i < this.getSize(); i++){

            // use compare to with strings checking for NaN

            if(this.getType().equals("string")){

                if(this.getValue(i).equals("NaN")){

                    continue;

                } else if(this.getValue(i).toString().compareTo(newLiteral.toString()) <= 0){

                    rowsToKeep.add(i);
                }


            } else if(this.getValue(i).equals("NaN")){

                continue;

            } else{
                /*
                compare values that are Floats or Integers
                 */

                T columnValue =  this.getValue(i);

                try{

                    if((Float) columnValue <= ((Float) newLiteral)){

                        rowsToKeep.add(i);

                    }

                } catch (Exception e){

                    if((Integer) columnValue <= ((Integer) newLiteral)){

                        rowsToKeep.add(i);

                    }
                }
            }
        }

        return rowsToKeep;
    }



    /**
     * method takes in a literal and returns a set of all the indices of where a column value is
     * greater than or equal to the parameter literal. indices refer to the row the value is in.
     * @param literal String value
     * @return Integer Set of indices
     * @throws NumberFormatException thrown if literal is different type than column
     */
    public Set<Integer> rowsGreaterThanOrEqual(String literal) throws NumberFormatException{



        Object newLiteral;
        Set<Integer> rowsToKeep = new HashSet<>();

        switch (this.type){

            case "int":

                newLiteral = Integer.parseInt(literal);
                break;

            case "float":

                newLiteral = Float.parseFloat(literal);
                break;

            default:

                newLiteral = literal;
        }

        /*
        checks values in the column to find row indices that contain values that
        are less than parameter newLiteral
         */
        for(int i = 0; i < this.getSize(); i++){

            // use compare to with strings checking for NaN

            if(this.getType().equals("string")){

                if(this.getValue(i).equals("NaN")){

                    continue;

                } else if(this.getValue(i).toString().compareTo(newLiteral.toString()) >= 0){

                    rowsToKeep.add(i);
                }


            } else if(this.getValue(i).equals("NaN")){

                continue;

            } else{
                /*
                compare values that are Floats or Integers
                 */

                T columnValue =  this.getValue(i);

                try{

                    if((Float) columnValue >= ((Float) newLiteral)){

                        rowsToKeep.add(i);

                    }

                } catch (Exception e){

                    if((Integer) columnValue >= ((Integer) newLiteral)){

                        rowsToKeep.add(i);

                    }
                }
            }
        }

        return rowsToKeep;
    }



    /**
     * method takes in a literal and returns a set of all the indices of where a column value is
     * greater than the parameter literal. indices refer to the row the value is in.
     * @param literal String value
     * @return Integer Set of indices
     * @throws NumberFormatException thrown if literal is different type than column
     */
    public Set<Integer> rowsGreaterThan(String literal) throws NumberFormatException{



        Object newLiteral;
        Set<Integer> rowsToKeep = new HashSet<>();

        switch (this.type){

            case "int":

                newLiteral = Integer.parseInt(literal);
                break;

            case "float":

                newLiteral = Float.parseFloat(literal);
                break;

            default:

                newLiteral = literal;
        }

        /*
        checks values in the column to find row indices that contain values that
        are less than parameter newLiteral
         */
        for(int i = 0; i < this.getSize(); i++){

            // use compare to with strings checking for NaN

            if(this.getType().equals("string")){

                if(this.getValue(i).equals("NaN")){

                    continue;

                } else if(this.getValue(i).toString().compareTo(newLiteral.toString()) > 0){

                    rowsToKeep.add(i);
                }


            } else if(this.getValue(i).equals("NaN")){

                continue;

            } else{
                /*
                compare values that are Floats or Integers
                 */

                T columnValue =  this.getValue(i);

                try{

                    if((Float) columnValue > ((Float) newLiteral)){

                        rowsToKeep.add(i);

                    }

                } catch (Exception e){

                    if((Integer) columnValue > ((Integer) newLiteral)){

                        rowsToKeep.add(i);

                    }
                }
            }
        }

        return rowsToKeep;
    }
















}
