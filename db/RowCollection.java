package db;

import java.util.*;



public class RowCollection {

    /*
    stores rows derived from columns as a list<object> and stores them in order in a list

    create a row class and then implement this class.

    needs to create and order rows, have a contain function for other row collection objects,
    have a similarity function that returns indices of similar rows.

     */

    private List<Row> rows = new ArrayList<>();

    private int rowCount;





    public RowCollection(List<Column> columns){

            rowCount = columns.get(0).getSize();

        for(int i = 0; i < rowCount; i++){

            Row rowToAdd = new Row();

            for(Column col : columns){

                rowToAdd.add(col.getValue(i));

            }

            rows.add(rowToAdd);



        }
    }


    /**
     * returns true if the row collection contains specified row
     * @param row Row to check if RowCollection contains
     * @return boolean
     */
    public boolean contains(Row row){

        return this.rows.contains(row);

    }

    /*
    method that returns list of integers of rows to keep
    either make it an method here or make a static method
    in the operations class
     */

    /**
     * returns a list of integer indices of rows in this RowCollection that match
     * 1 or more rows in another RowCollection Object
     * @param other RowCollection
     * @return list of integer indices
     */
    public Indices matchingRowIndex(RowCollection other){

        /*
        Edit need to change to get ordered indices from the right table may need
        copy's of certain rows that match multiple right table rows.

        returns left indices to keep and right indices to keep
         */

        List<Integer> leftIndices = new ArrayList<>();

        List<Integer> rightIndices = new ArrayList<>();


        for(int i = 0; i < rows.size(); i++){

            for(int j = 0; j < other.rows.size(); j++){

                if(this.rows.get(i).equals(other.rows.get(j))){

                    leftIndices.add(i);
                    rightIndices.add(j);

                }
            }

        }


        return new Indices(leftIndices, rightIndices);

    }


    public Indices cartesianRowIndex(RowCollection other){

        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();

        for(int i = 0; i < rowCount; i++){

            for(int j = 0; j < other.rowCount; j++){

                left.add(i);
                right.add(j);
            }
        }

        return new Indices(left, right);

    }


}
