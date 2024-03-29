package db;


import java.util.ArrayList;
import java.util.List;
import java.util.*;

/**
 * class holds static methods for table operations such as join.
 */
class Operation {

    /**
     * this method performs a join of two tables
     * @param leftTable left Table to be performed on
     * @param rightTable right Table to be performed on
     * @param name String name of the new table to be created
     * @return Table new table result of join
     */
    static Table Join(Table leftTable, Table rightTable, String name){

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

            return filterAndConcatenate(leftTable, rightTable, indicesToReturn, name);
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
    static Table filterAndConcatenate(Table left, Table right, Indices rowIndices, String name){

        List<Column> filteredColumns = new ArrayList<>();

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
     * @param name String name of new table if select is used for create table, if not just enter empty string.
     * @return Table resulting select
     * @throws IllegalArgumentException if column name does not exist
     */
    static Table select(List<String> columnSelect, List<String> columnNames, Table table, String name)
            throws IllegalArgumentException{

        /* if an '*' is passed in return all the columns in a new table */
        if(columnSelect.size() == 1 && columnSelect.get(0).equals("*")){
            return new Table(name, table.getColumns());
        }


        List<Column> tableToReturnColumns = new ArrayList<>();

        int columnNameCounter = 0;

        for(String nameMatch : columnSelect){

            nameMatch = nameMatch.trim();

            try{
                Column toAdd = table.getColumnByName(nameMatch);
                toAdd = toAdd.changeName(columnNames.get(columnNameCounter));
                tableToReturnColumns.add(toAdd);

            } catch (IllegalArgumentException e){

                tableToReturnColumns.add(applyArithmetic(nameMatch,
                        columnNames.get(columnNameCounter).replaceAll("\\s+", ""), table));
            }

            columnNameCounter += 1;
        }

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
    static Column applyArithmetic(String expression, String name, Table table)
            throws IllegalArgumentException{

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


    /**
     * this method provides conditional filtering passed through by the where clause. Takes in a table
     * and a conditional string statement and returns a new table that has been filtered according to the
     * where clause condition.
     * @param select Table to be filtered
     * @param condition String of where clause
     * @return new Table with rows not meeting where clause filtered out.
     */
    static Table condition(Table select, String condition) throws IllegalArgumentException{

        /* split up conditions by 'and' */
        String[] parts = condition.split(" and ");

        /*
        used for when to assign a set and when to take the intersection of a set
         */
        int count = 0;

        /*
        set is used to store all indices of each column that should be kept, at then end of each loop
        the it will be set to the previous set intersected by the current set. whatever indices are left
        in the final set are the ones of the rows that should be kept. since we need an initial set
        of indices we set a count to determine when we should take the intersection
         */
        Set<Integer> indexIntersection = new HashSet<>(); //edit originally Set<Intersection> and now assigned to empty set

        for(String part : parts){

            String cleanPart = part.trim();

            Set<Integer> currentSet;

            if(cleanPart.contains("==")){
                String[] colAndLiteral = cleanPart.split("==", 2);
                Column col = select.getColumnByName(colAndLiteral[0].trim());
                currentSet = col.rowsEqualTo(colAndLiteral[1].trim());

            } else if(cleanPart.contains("!=")){
                String[] colAndLiteral = cleanPart.split("!=", 2);
                Column col = select.getColumnByName(colAndLiteral[0].trim());
                currentSet = col.rowsNotEqualTo(colAndLiteral[1].trim());

            }else if(cleanPart.contains("<=")){
                String[] colAndLiteral = cleanPart.split("<=", 2);
                Column col = select.getColumnByName(colAndLiteral[0].trim());
                currentSet = col.rowsLessThanOrEqual(colAndLiteral[1].trim());

            }else if(cleanPart.contains(">=")){
                String[] colAndLiteral = cleanPart.split(">=", 2);
                Column col = select.getColumnByName(colAndLiteral[0].trim());
                currentSet = col.rowsGreaterThanOrEqual(colAndLiteral[1].trim());

            }else if(cleanPart.contains(">")){
                String[] colAndLiteral = cleanPart.split(">", 2);
                Column col = select.getColumnByName(colAndLiteral[0].trim());
                currentSet = col.rowsGreaterThan(colAndLiteral[1].trim());

            }else if(cleanPart.contains("<")){
                String[] colAndLiteral = cleanPart.split("<", 2);
                Column col = select.getColumnByName(colAndLiteral[0].trim());
                currentSet = col.rowsLessThan(colAndLiteral[1].trim());

            } else{
                throw new IllegalArgumentException("no comparison operator");
            }
            /*
            check count of how many loops and either assign current set to indexIntersection,
            or calculate the intersection of two sets
             */

            if(count == 0){
                indexIntersection = currentSet;

            } else{
                 indexIntersection.retainAll(currentSet);

            }

            count += 1;
        }
        /*
        now that we have a set of all the indices we need, we filter out the rows in the columns
        we don't want then can return a new table with the specified columns.
         */
        List<Column> columns = select.getColumns();

        List<Integer> rowsToKeep = new ArrayList<>(indexIntersection);

        Collections.sort(rowsToKeep);

        /*
        loop through all the columns and take out values that should not belong
        Needs to be an ordered list.
         */

        List<Column> columnsToReturn = new ArrayList<>();

        for(Column clmn : columns){
            columnsToReturn.add(clmn.filterByRow(rowsToKeep));

        }

        return new Table(select.getTableName(), columnsToReturn);
    }
}
