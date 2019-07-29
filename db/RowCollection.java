package db;

import java.util.*;


class RowCollection {

    /**
     * all the rows that are in the RowCollection object
     */
    private List<Row> rows = new ArrayList<>();

    /**
     * the Integer number of rows that exist in the RowCollection object
     */
    private int rowCount;


    RowCollection(List<Column> columns){

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


    /**
     * returns a list of integer indices of rows in this RowCollection that match
     * 1 or more rows in another RowCollection Object
     * @param other RowCollection
     * @return list of integer indices
     */
    Indices matchingRowIndex(RowCollection other){


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

    /**
     * adds indices used for a cartesian join and returns an Indices object
     * @param other RowCollection
     * @return Indicies containing indices needed  for a cartesian join
     */
    Indices cartesianRowIndex(RowCollection other){

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
