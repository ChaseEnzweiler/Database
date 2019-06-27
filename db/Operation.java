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


    /**
     * evaluates a select statement and returns a Table of the resulting select.
     * @param columnSelect List<String> list of column names to select
     * @param columnNames List<String> list of names to rename selected columns to, needs
     *                    to be same size as columnSelect List
     * @param table Table we are selecting columns from
     * @param name String name of new table if slect is used for create table, if not just enter empty string.
     * @return Table resulting select
     * @throws IllegalArgumentException if column name does not exist
     */
    public static Table select(List<String> columnSelect, List<String> columnNames, Table table, String name)
        throws IllegalArgumentException{


        List<Column> tableToReturnColumns = new ArrayList<>();

        int columnNameCounter = 0;

        for(String nameMatch : columnSelect){

            /*
            get the column and put into new list, if name does not exist in table(throws exception) we catch
            that exception and see if an arithmetic operator is involved, if not throw exception.
             */

            try{

                Column toAdd = table.getColumnByName(nameMatch);

                toAdd = toAdd.changeName(columnNames.get(columnNameCounter));

                tableToReturnColumns.add(toAdd);

            } catch (IllegalArgumentException e){

                /* parse string to see if arithmetic, split by an expression, do not include spaces */

                tableToReturnColumns.add(applyArithmetic(nameMatch, columnNames.get(columnNameCounter), table));

            }

            columnNameCounter += 1;

        }

        /* create table and return it */
        return new Table(name, tableToReturnColumns);
    }


    /**
     * method that takes in column names with an arithmetic operator all as one expression, then applies
     * arithmetic to column and returns new resulting column
     * @param expression String of two column names and one operator
     * @param name String name of resulting column after arithmetic
     * @param table Table table the columns exist in
     * @return Column resulting Column of arithmetic
     * @throws IllegalArgumentException if column does not exist in table or expression does not
     * have operator
     */
    public static Column applyArithmetic(String expression, String name, Table table)
            throws IllegalArgumentException{

        /*
        remove the white space so cn split by arithmetic operator
         */

        String cleanedExpression = expression.replaceAll("\\s+", "");

        /*
        search for arithmetic operator and split the string by that operator,
        perform column arithmetic and return new column
         */

        if(cleanedExpression.contains("+")){

            String[] parts = cleanedExpression.split("[+]", 2);

            return Column.addition(table.getColumnByName(parts[0]), table.getColumnByName(parts[1]), name);


        } else if(cleanedExpression.contains("-")){

            String[] parts = cleanedExpression.split("-", 2);

            return Column.subtraction(table.getColumnByName(parts[0]), table.getColumnByName(parts[1]), name);


        } else if(cleanedExpression.contains("/")){

            String[] parts = cleanedExpression.split("/", 2);

            return Column.division(table.getColumnByName(parts[0]), table.getColumnByName(parts[1]), name);


        }else if(cleanedExpression.contains("*")){

            String[] parts = cleanedExpression.split("[*]", 2);

            return Column.multiplication(table.getColumnByName(parts[0]), table.getColumnByName(parts[1]), name);


        } else{

            throw new IllegalArgumentException("Column name does not exist.");
        }

    }





















}
