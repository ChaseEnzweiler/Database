package db;

//don't import all in the future.
import java.util.*;

/**
 * This Class is used for storing all values in a single column of a Table.
 * All values must have same type unless the value is "NaN"
 */
class Column {

    /**
     * Name of the Column object.
     */
    private String name;

    /**
     * type of the Column object. Either 'string', 'int', or 'float'
     */
    private String type;

    /**
     * values that exist in the Column Object
     */
    private List<Object> values;

    /**
     * Constructor for the column class. takes in a name, type, and array of values.
     * @param name String name of column
     * @param type String type of column
     * @param values T[] of values the column contains
     */
    public Column(String name, String type, Object[] values) {

        this.name = name;
        this.type = type;

        this.values = new ArrayList<>(Arrays.asList(values));
    }

    /**
     * Overloaded constructor that takes in an arraylist for values instance variable
     * @param name String name of the column
     * @param type String type of the column
     * @param values ArrayList<T> the values of the column
     */
    public Column(String name, String type, List<Object> values) {

        this.name = name;
        this.type = type;

        this.values = values;
    }

    /**
     * Overriding equals method of object superclass
     * @param other column this is comparing itself to
     * @return boolean
     */
    @Override
    public boolean equals(Object other) {

        if(this == other){
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Column otherColumn = (Column) other;

        if(!this.name.equals(otherColumn.name)){
            return false;
        }

        if(!this.type.equals(otherColumn.type)){
            return false;
        }

        return this.values.equals(otherColumn.getValues());
    }

    /**
     * returns the size of the column i.e how many values the column has
     * @return int size of column
     */

    int getSize(){
        return values.size();
    }

    /**
     * getter method for the type of the column
     * @return String type
     */
    String getType(){
        return type;
    }

    /**
     * gets value from specified location/row of the column
     * @param index which row/ location the desired value is
     * @return String value store in the column
     */

    Object getValue(int index){
        return values.get(index);
    }

    /**
     * returns a list of all the values making up the column
     * @return List<String> of values of column
     */
    List<Object> getValues(){
        return values;
    }

    /**
     * returns the name of the column
     * @return String name
     */
    String getName(){
        return name;
    }

    /**
     * takes in a list of values by row that you want to keep and returns a new column
     * with the same name and type with only the values to keep and other values filtered out
     * The rows should be zero indexed
     * @param rowsToKeep List of integers of which rows of the values to keep. Needs to be ordered List
     * @return new Column with an updated values list.
     */
    Column filterByRow(List<Integer> rowsToKeep) {

        ArrayList<Object> filteredValues = new ArrayList<>();

        for(int index : rowsToKeep){
            filteredValues.add(this.values.get(index));
        }

        return new Column(this.name, this.type, filteredValues);
    }

    /**
     * adds value to the end of the column
     * @param literal T value
     */
    public void add(Object literal){
        this.values.add(literal);
    }

    /**
     * returns a duplicate of the column with a different name than the original
     * @param name String name of what you want duplicate column to be named
     * @return new Column
     */
    Column changeName(String name){
        return new Column(name, this.getType(), this.getValues());
    }

    /**
     * value by value addition of two columns, returns a new columns with added together values.
     * throws exception if trying to add string type to float or integer.
     * @param col1 Column object
     * @param col2 Column object
     * @param name name to new Column to return
     * @return new Column
     * @throws IllegalArgumentException
     */
    static Column addition(Column col1, Column col2, String name) throws IllegalArgumentException{

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
        List<Object> newValues = new ArrayList<>();

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
    static Column subtraction(Column col1, Column col2, String name) throws IllegalArgumentException{

        if(col1.getType().equals("string") || col2.getType().equals("string")){
            throw new IllegalArgumentException();
        }

        String newType = calculateType(col1, col2);

        List<Object> newValues = new ArrayList<>();

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
    static String calculateType(Column col1, Column col2){

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
    static Column division(Column col1, Column col2, String name) throws IllegalArgumentException{

        if(col1.getType().equals("string") || col2.getType().equals("string")){

            throw new IllegalArgumentException();
        }

        String newType = calculateType(col1, col2);

        List<Object> newValues = new ArrayList<>();

        /*
        divide col1 by col2 value by value, check if division by zero which results in NaN.
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
    static Column multiplication(Column col1, Column col2, String name) throws IllegalArgumentException{

        if(col1.getType().equals("string") || col2.getType().equals("string")){

            throw new IllegalArgumentException();
        }

        /*
        get type of column to return and instantiate list to hold new resulting values
         */
        String newType = calculateType(col1, col2);

        List<Object> newValues = new ArrayList<>();


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
    Set<Integer> rowsEqualTo(String literal) throws NumberFormatException{

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
         */
        for(int i = 0; i < this.getSize(); i++){

            if(this.getValue(i).equals(newLiteral)){

                rowsToKeep.add(i);

            } else if(this.getValue(i).equals("NaN") && !this.type.equals("string")){

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
     * @return Set<Integer> of indices of rows to keep
     * @throws NumberFormatException thrown in literal not correct type
     */
    Set<Integer> rowsNotEqualTo(String literal) throws NumberFormatException{

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
    Set<Integer> rowsLessThan(String literal) throws NumberFormatException{

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
                Object columnValue =  this.getValue(i);

                try{

                    if((Float) columnValue < ((float) newLiteral)){
                        rowsToKeep.add(i);
                    }

                } catch (Exception e){

                    if((int) columnValue < ((int) newLiteral)){
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
    Set<Integer> rowsLessThanOrEqual(String literal) throws NumberFormatException{

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
                Object columnValue =  this.getValue(i);

                try{

                    if((Float) columnValue <= ((float) newLiteral)){
                        rowsToKeep.add(i);
                    }

                } catch (Exception e){

                    if((int) columnValue <= ((int) newLiteral)){

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
    Set<Integer> rowsGreaterThanOrEqual(String literal) throws NumberFormatException{

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


            if(this.getType().equals("string")){

                if(this.getValue(i).equals("NaN")){
                    continue;

                } else if(this.getValue(i).toString().compareTo(newLiteral.toString()) >= 0){
                    rowsToKeep.add(i);
                }

            } else if(this.getValue(i).equals("NaN")){
                continue;

            } else{

                Object columnValue =  this.getValue(i);

                try{

                    if((Float) columnValue >= ((float) newLiteral)){
                        rowsToKeep.add(i);
                    }

                } catch (Exception e){

                    if((int) columnValue >= ((int) newLiteral)){
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
    Set<Integer> rowsGreaterThan(String literal) throws NumberFormatException{

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

                Object columnValue =  this.getValue(i);

                try{

                    if((Float) columnValue > ((float) newLiteral)){

                        rowsToKeep.add(i);

                    }

                } catch (Exception e){

                    if((int) columnValue > ((int) newLiteral)){

                        rowsToKeep.add(i);
                    }
                }
            }
        }
        return rowsToKeep;
    }


}
