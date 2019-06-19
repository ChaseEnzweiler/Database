package db;

import com.sun.xml.internal.bind.v2.TODO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * class holds static methods for table operations such as join.
 */
public class Operation {


    /**
     * this method performs a join of two tables
     * @param leftTable left Table to be performed on
     * @param rightTable right Table to be performed on
     * @param name String name of the new table to be created
     * @return Table new table result of join
     */
    public static Table Join(Table leftTable, Table rightTable, String name){

        /*
        calculate the columns that both tables have in common regarding name and type
         */
        List<Column> leftMatchingColumns = leftTable.matchingColumns(rightTable);
        List<Column> rightMatchingColumns = rightTable.matchingColumns(leftTable);

        /*
        create RowCollection objects with these columns and calculate Indices object
        that creates lists of what row indices need to be kept for both tables
         */


        if(leftMatchingColumns.size() > 0){
            /*
            if there are matching columns we perform a natural inner join
             */

            RowCollection leftRowCollection = new RowCollection(leftMatchingColumns);
            RowCollection rightRowCollection = new RowCollection(rightMatchingColumns);

            Indices indicesToReturn = leftRowCollection.matchingRowIndex(rightRowCollection);

            return filterAndConcatenate(leftTable, rightTable, indicesToReturn , name);

        }else{
            /*
            if there are no matching columns we perform a cartesian join
            create row collection inside control statement and use a list of all columns
            instead of matching columns
             */
            List<Column> leftColumns = leftTable.getColumnsAsList();
            List<Column> rightColumns = rightTable.getColumnsAsList();

            RowCollection leftRowCollection = new RowCollection(leftColumns);
            RowCollection rightRowCollection = new RowCollection(rightColumns);


            Indices indicesToReturn = leftRowCollection.cartesianRowIndex(rightRowCollection);

            return filterAndConcatenate(leftTable, rightTable, indicesToReturn , name);

        }

    }

    /**
     * used for natural inner joins, takes in two tables performs A natural inner join on the tables.
     * filters out the rows of the two tables that don't match and then concatenates the columns
     * to form one new table
     * @param left Table
     * @param right Table
     * @param rowIndices contains which rows of each table should be kept
     * @return new table
     */
    public static Table filterAndConcatenate(Table left, Table right, Indices rowIndices, String name){

        List<Column> filteredColumns = new ArrayList<>();

        /* the matching columns need to go at the beginning of the newly created table*/

        List<Column> matchingColumns = left.matchingColumns(right);

        for(Column matches : matchingColumns){

            filteredColumns.add(matches.filterByRow(rowIndices.getLeftIndices()));

        }

        /*
        now add in columns that aren't matching columns but come from the left table
         */

        for(Column leftCol: left.getColumns()){

            if(!right.hasMatchingColumn(leftCol)){

                filteredColumns.add(leftCol.filterByRow(rowIndices.getLeftIndices()));
            }
        }

        /*
        now add in the columns in the right table that don't match any in the left
         */

        for(Column rightCol: right.getColumns()){

            if(!left.hasMatchingColumn(rightCol)){

                filteredColumns.add(rightCol.filterByRow(rowIndices.getRightIndices()));

            }
        }

        return  new Table(name, filteredColumns);


    }


























}
