package db;

import java.util.*;

/**
 * this class stores all values of a row across a table. Each value my be a different type.
 * Used in RowCollection when performing a join operation on two tables.
 */
class Row {

    /**
     * Object values that are stored in a row
     */
    private List<Object> values;


    Row(){ values = new ArrayList<>(); }

    /**
     * method adds an object to the back of the row
     * @param item object
     */
    public void add(Object item){
        values.add(item);
    }

    /**
     * returns the object at the specified index
     * @param index integer where you want object from values list
     * @return object at location index
     */
    public Object getValue(int index){
        return values.get(index);
    }

    @Override
    public boolean equals(Object other){

        if(this == other){

            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        Row otherRow = (Row) other;

        return this.values.equals(otherRow.values);
    }
}
