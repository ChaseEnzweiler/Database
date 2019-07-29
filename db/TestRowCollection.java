package db;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class TestRowCollection {


    @Test
    public void testMatchingRowIndex(){

        /*
        first row collection
         */

        String colName1 = "col1";
        String colType1 = "int";
        ArrayList<Object> colValues1 = new ArrayList<>();

        colValues1.add(1);
        colValues1.add(2);
        colValues1.add(3);
        colValues1.add(4);
        colValues1.add(5);

        String colName2 = "col2";
        String colType2 = "String";
        ArrayList<Object> colValues2 = new ArrayList<>();

        colValues2.add("a");
        colValues2.add("b");
        colValues2.add("c");
        colValues2.add("d");
        colValues2.add("e");

        String colName3 = "col3";
        String colType3 = "float";
        ArrayList<Object> colValues3 = new ArrayList<>();

        colValues3.add((float) 1.1);
        colValues3.add((float) 2.2);
        colValues3.add((float) 3.3);
        colValues3.add((float) 4.4);
        colValues3.add((float) 5.5);


        Column col1 = new Column(colName1, colType1, colValues1);
        Column col2 = new Column(colName2, colType2, colValues2);
        Column col3 = new Column(colName3, colType3, colValues3);

        List<Column> colList1 = new ArrayList<>();
        colList1.add(col1);
        colList1.add(col2);
        colList1.add(col3);

        RowCollection RC1 = new RowCollection(colList1);

        /*
        create row collection 2
         */

        String colNameA = "colA";
        String colTypeA = "int";
        ArrayList<Object> colValuesA = new ArrayList<>();

        colValuesA.add(1);
        colValuesA.add(67);
        colValuesA.add(87);
        colValuesA.add(5);
        colValuesA.add(3);
        colValuesA.add(1);

        String colNameB = "colB";
        String colTypeB = "String";
        ArrayList<Object> colValuesB = new ArrayList<>();

        colValuesB.add("a");
        colValuesB.add("chase");
        colValuesB.add("sase");
        colValuesB.add("e");
        colValuesB.add("c");
        colValuesB.add("a");

        String colNameC = "colC";
        String colTypeC = "float";
        ArrayList<Object> colValuesC = new ArrayList<>();

        colValuesC.add((float) 1.1);
        colValuesC.add((float) 11.1);
        colValuesC.add((float) 12.2);
        colValuesC.add((float) 5.5);
        colValuesC.add((float) 3.3);
        colValuesC.add((float) 1.1);


        Column colA = new Column(colNameA, colTypeA, colValuesA);
        Column colB = new Column(colNameB, colTypeB, colValuesB);
        Column colC = new Column(colNameC, colTypeC, colValuesC);

        List<Column> colListA = new ArrayList<>();
        colListA.add(colA);
        colListA.add(colB);
        colListA.add(colC);

        RowCollection RCA = new RowCollection(colListA);

        /*
        call to the method
         */

        Indices rowsToKeep = RC1.matchingRowIndex(RCA);

        List<Integer> shouldBe1 = new ArrayList<>();
        shouldBe1.add(0);
        shouldBe1.add(0);
        shouldBe1.add(2);
        shouldBe1.add(4);


        List<Integer> shouldBe2 = new ArrayList<>();
        shouldBe2.add(0);
        shouldBe2.add(5);
        shouldBe2.add(4);
        shouldBe2.add(3);

        Indices check = new Indices(shouldBe1, shouldBe2);





        /*
        assert statements
         */
        Row row = new Row();
        row.add(1);
        row.add("a");
        row.add((float) 1.1);

        assertEquals(RC1.contains(row), true);

        assertEquals(rowsToKeep, check);




    }










}
