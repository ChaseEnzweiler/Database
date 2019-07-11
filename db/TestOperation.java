package db;

import edu.princeton.cs.introcs.In;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TestOperation {

    /*
    columns for left table 1
     */

    Column<Integer> a1 = new Column<>("a", "int", new Integer[]{1,2,3,4,5});
    Column<String> b1 = new Column<>("b", "string", new String[]{"1", "2", "3", "4", "5"});
    Column<Float> c1 = new Column<>("c", "float", new Float[]{(float) 1.1, (float) 2.2,(float) 3.3,
            (float) 4.4,(float) 5.5});
    Column<Integer> d1 = new Column<>("d", "int", new Integer[]{1,2,3,4,5});


    /*
    columns for right table
     */

    Column<Integer> a2 = new Column<>("a", "int", new Integer[]{3,2,1,4,5,6,1});
    Column<String> b2 = new Column<>("b", "string", new String[]{"3", "13", "1", "4", "56", "6", "1"});
    Column<Float> c2 = new Column<>("c", "float", new Float[]{(float) 3.3, (float) 2.5,(float) 1.1,
            (float) 98.8,(float) 5.5, (float) 7.7, (float) 1.1});
    Column<Integer> d2 = new Column<>("none", "int", new Integer[]{34, 54, 76, 87, 98, 345, 6});
    Column<Integer> e2 = new Column<>("nomatch", "int", new Integer[]{123, 123, 543, 5456, 654, 765, 678});


    @Test
    public void testFilterAndConcatenate(){

        List<Column> table1List = new ArrayList<>();
        table1List.add(a1);table1List.add(b1);table1List.add(c1);table1List.add(d1);


        List<Column> table2List = new ArrayList<>();
        table2List.add(a2);table2List.add(b2);table2List.add(c2);table2List.add(d2);table2List.add(e2);

        /*
        Create left and right tables
         */

        Table left = new Table("left", table1List);
        Table right = new Table("right", table2List);

        List<Column> leftMatching = left.matchingColumns(right);

        List<Column> rightMatching = right.matchingColumns(left);

        /*
        create a row collection and get indices from there
         */

        RowCollection leftRowCollection = new RowCollection(leftMatching);
        RowCollection rightRowCollection = new RowCollection(rightMatching);

        Indices index = leftRowCollection.matchingRowIndex(rightRowCollection);

        /*
        call filter and concatenate
         */

        Table table3 = Operation.filterAndConcatenate(left, right, index, "table3");

        /*
        create what table 3 should equal
         */

        Column<Integer> finalCol1 = new Column<>("a", "int", new Integer[]{1,1,3});
        Column<String> finalCol2 = new Column<>("b", "string", new String[]{"1", "1", "3"});
        Column<Float> finalCol3 = new Column<>("c", "float", new Float[]{(float) 1.1, (float) 1.1,(float) 3.3});
        Column<Integer> finalCol4 = new Column<>("d", "int", new Integer[]{1,1,3});
        Column<Integer> finalCol5 = new Column<>("none", "int", new Integer[]{ 76, 6, 34});
        Column<Integer> finalCol6 = new Column<>("nomatch", "int", new Integer[]{543, 678, 123});

        List<Column> finalColumns = new ArrayList<>();
        finalColumns.add(finalCol1);finalColumns.add(finalCol2);finalColumns.add(finalCol3);
        finalColumns.add(finalCol4);finalColumns.add(finalCol5);finalColumns.add(finalCol6);

        Table checkTable3 = new Table("table3", finalColumns);

        /*
        edit so that we get a negative test
         */

        Column<Integer> finalCol11 = new Column<>("a", "int", new Integer[]{1,1,3});
        Column<String> finalCol22 = new Column<>("b", "string", new String[]{"1", "1", "3"});
        Column<Float> finalCol33 = new Column<>("c", "float", new Float[]{(float) 1.1, (float) 1.1,(float) 3.3});
        Column<Integer> finalCol44 = new Column<>("d", "int", new Integer[]{1,1,3});
        Column<Integer> finalCol55 = new Column<>("none", "int", new Integer[]{ 76, 6, 34});
        Column<Integer> finalCol66 = new Column<>("nmatch", "int", new Integer[]{53, 678, 123});

        List<Column> finalColumns1 = new ArrayList<>();
        finalColumns1.add(finalCol11);finalColumns1.add(finalCol22);finalColumns1.add(finalCol33);
        finalColumns1.add(finalCol44);finalColumns1.add(finalCol55);finalColumns1.add(finalCol66);

        Table checkTable33 = new Table("table3", finalColumns1);

        /*
        assert calls
         */


        assertEquals(table3, checkTable3);

        assertEquals(table3.equals(checkTable33), false);

    }

    @Test
    public void testCartesianJoin(){

        /*
        columns for table 1
         */
        Column<String> a1 = new Column<>("String", "string", new String[]{"1", "2", "3"});
        Column<Integer> a2 = new Column<>("Integer", "int", new Integer[]{1,2,3});
        Column<Float> a3 = new Column<>("Float", "float", new Float[]{(float) 1.1, (float) 2.2,
                (float) 3.3});

        /*
        columns for table 2
         */

        Column<String> b1 = new Column<>("String", "string", new String[]{"10", "20", "30"});
        Column<Integer> b2 = new Column<>("Integer", "int", new Integer[]{10,20,30});
        Column<Float> b3 = new Column<>("Float", "float", new Float[]{(float) 10.1, (float) 20.2,
                (float) 30.3});

        /*
        columns for table 3
         */
        Column<String> c1 = new Column<>("String1", "string", new String[]{"10", "20", "30"});
        Column<Integer> c2 = new Column<>("Integer2", "int", new Integer[]{10,20,30});
        Column<Float> c3 = new Column<>("Float3", "float", new Float[]{(float) 10.1, (float) 20.2,
                (float) 30.3});

        /*
        columns for table 4
         */

        Column<String> d1 = new Column<>("String", "string",
                new String[]{"1","1","1","2","2","2","3","3","3"});
        Column<Integer> d2 = new Column<>("Integer", "int", new Integer[]{1,1,1,2,2,2,3,3,3});
        Column<Float> d3 = new Column<>("Float", "float", new Float[]{(float) 1.1, (float) 1.1, (float) 1.1
                , (float) 2.2,(float) 2.2, (float) 2.2, (float) 3.3, (float) 3.3, (float) 3.3});
        Column<String> d4 = new Column<>("String1", "string",
                new String[]{"10", "20", "30", "10", "20", "30", "10", "20", "30"});
        Column<Integer> d5 = new Column<>("Integer2", "int",
                new Integer[]{10,20,30, 10,20,30, 10,20,30});
        Column<Float> d6 = new Column<>("Float3", "float",
                new Float[]{(float) 10.1, (float) 20.2, (float) 30.3, (float) 10.1, (float) 20.2, (float) 30.3,
                        (float) 10.1, (float) 20.2, (float) 30.3});

        /*
        lists for each table
         */

        List<Column> tableList1 = new ArrayList<>();
        List<Column> tableList2 = new ArrayList<>();
        List<Column> tableList3 = new ArrayList<>();
        List<Column> tableList4 = new ArrayList<>();

        tableList1.add(a1);tableList1.add(a2);tableList1.add(a3);

        tableList2.add(b1);tableList2.add(b2);tableList2.add(b3);

        tableList3.add(c1);tableList3.add(c2);tableList3.add(c3);

        tableList4.add(d1);tableList4.add(d2);tableList4.add(d3);
        tableList4.add(d4);tableList4.add(d5);tableList4.add(d6);

        /*
        initialize tables
         */

        Table table1 = new Table("table1", tableList1);
        Table table2 = new Table("table2", tableList2);
        Table table3 = new Table("table3", tableList3);
        Table table4 = new Table("table4", tableList4);

        /*
        asserts
         */

        List<Column> empty = new ArrayList<>();
        Column<String> e1 = new Column<>("String", "string", new String[]{});
        Column<Integer> e2 = new Column<>("Integer", "int", new Integer[]{});
        Column<Float> e3 = new Column<>("Float", "float", new Float[]{});

        empty.add(e1);empty.add(e2);empty.add(e3);

        /* test for empty table */
        assertEquals(Operation.Join(table1, table2, "tablenew"), new Table("tablenew", empty));

        /* test for cartesian join */
        assertEquals(Operation.Join(table1, table3, "table4"), table4);

    }


    @Test
    public void testSelect(){

        Column<Integer> col1 = new Column<>("col1", "int", new Integer[]{1,2,3,4,5});
        Column<String> col2 = new Column<>("col2", "string", new String[]{"1", "2", "3", "4", "5"});
        Column<Float> col3 = new Column<>("col3", "float", new Float[]{(float) 1.1, (float) 2.2,(float) 3.3,
                (float) 4.4,(float) 5.5});
        Column<Integer> col4 = new Column<>("col4", "int", new Integer[]{1,2,3,4,5});
        Column<String> col5 = new Column<>("col5", "string", new String[]{"1", "2", "3", "4", "5"});


        List<Column> tableColumns = new ArrayList<>();
        tableColumns.add(col1); tableColumns.add(col2); tableColumns.add(col3);
        tableColumns.add(col4); tableColumns.add(col5);

        Table selector = new Table("selector", tableColumns);



        /*
        first select
         */

        List<String> firstSelectCol = new ArrayList<>();
        firstSelectCol.add("col5"); firstSelectCol.add("col2");

        List<String> firstSelectNames = new ArrayList<>();
        firstSelectNames.add("col5"); firstSelectNames.add("chase");

        List<Column> firstSelectResult = new ArrayList<>();
        firstSelectResult.add(col5); firstSelectResult.add(col2.changeName("chase"));

        Table firstSelectTable = new Table("firstSelect", firstSelectResult);

        assertEquals(Operation.select(firstSelectCol, firstSelectNames, selector, "firstSelect"), firstSelectTable);

        /*
        second select
         */

        List<String> secondSelectCol = new ArrayList<>();
        secondSelectCol.add("col5 + col2"); secondSelectCol.add("col3 - col4");

        List<String> secondSelectNames = new ArrayList<>();
        secondSelectNames.add("fivePlusTwo"); secondSelectNames.add("threeMinusFour");

        List<Column> secondSelectResult = new ArrayList<>();
        secondSelectResult.add(Column.addition(col5, col2, "fivePlusTwo"));
        secondSelectResult.add(Column.subtraction(col3, col4, "threeMinusFour"));

        Table secondSelectTable = new Table("secondSelect", secondSelectResult);

        assertEquals(Operation.select(secondSelectCol, secondSelectNames, selector, "secondSelect"),
                secondSelectTable);

        /*
        third select
         */

        List<String> thirdSelectCol = new ArrayList<>();
        thirdSelectCol.add("col3 / col1"); thirdSelectCol.add("col3 * col4");
        thirdSelectCol.add("col1");

        List<String> thirdSelectNames = new ArrayList<>();
        thirdSelectNames.add("ratio"); thirdSelectNames.add("combo"); thirdSelectNames.add("col1");

        List<Column> thirdSelectResult = new ArrayList<>();
        thirdSelectResult.add(Column.division(col3, col1, "ratio"));
        thirdSelectResult.add(Column.multiplication(col3, col4, "combo"));
        thirdSelectResult.add(col1);

        Table thirdSelectTable = new Table("thirdSelect", thirdSelectResult);

        assertEquals(Operation.select(thirdSelectCol, thirdSelectNames, selector, "thirdSelect"), thirdSelectTable);






    }



}
