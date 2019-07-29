package db;


import org.junit.Test;
import java.util.ArrayList;
import java.util.List;


import static org.junit.Assert.assertEquals;

public class TestOperation {

    /*
    columns for left table 1
     */

    Column a1 = new Column("a", "int", new Integer[]{1,2,3,4,5});
    Column b1 = new Column("b", "string", new String[]{"1", "2", "3", "4", "5"});
    Column c1 = new Column("c", "float", new Float[]{(float) 1.1, (float) 2.2,(float) 3.3,
            (float) 4.4,(float) 5.5});
    Column d1 = new Column("d", "int", new Integer[]{1,2,3,4,5});


    /*
    columns for right table
     */

    Column a2 = new Column("a", "int", new Integer[]{3,2,1,4,5,6,1});
    Column b2 = new Column("b", "string", new String[]{"3", "13", "1", "4", "56", "6", "1"});
    Column c2 = new Column("c", "float", new Float[]{(float) 3.3, (float) 2.5,(float) 1.1,
            (float) 98.8,(float) 5.5, (float) 7.7, (float) 1.1});
    Column d2 = new Column("none", "int", new Integer[]{34, 54, 76, 87, 98, 345, 6});
    Column e2 = new Column("nomatch", "int", new Integer[]{123, 123, 543, 5456, 654, 765, 678});


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

        Column finalCol1 = new Column("a", "int", new Integer[]{1,1,3});
        Column finalCol2 = new Column("b", "string", new String[]{"1", "1", "3"});
        Column finalCol3 = new Column("c", "float", new Float[]{(float) 1.1, (float) 1.1,(float) 3.3});
        Column finalCol4 = new Column("d", "int", new Integer[]{1,1,3});
        Column finalCol5 = new Column("none", "int", new Integer[]{ 76, 6, 34});
        Column finalCol6 = new Column("nomatch", "int", new Integer[]{543, 678, 123});

        List<Column> finalColumns = new ArrayList<>();
        finalColumns.add(finalCol1);finalColumns.add(finalCol2);finalColumns.add(finalCol3);
        finalColumns.add(finalCol4);finalColumns.add(finalCol5);finalColumns.add(finalCol6);

        Table checkTable3 = new Table("table3", finalColumns);

        /*
        edit so that we get a negative test
         */

        Column finalCol11 = new Column("a", "int", new Integer[]{1,1,3});
        Column finalCol22 = new Column("b", "string", new String[]{"1", "1", "3"});
        Column finalCol33 = new Column("c", "float", new Float[]{(float) 1.1, (float) 1.1,(float) 3.3});
        Column finalCol44 = new Column("d", "int", new Integer[]{1,1,3});
        Column finalCol55 = new Column("none", "int", new Integer[]{ 76, 6, 34});
        Column finalCol66 = new Column("nmatch", "int", new Integer[]{53, 678, 123});

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
        Column a1 = new Column("String", "string", new String[]{"1", "2", "3"});
        Column a2 = new Column("Integer", "int", new Integer[]{1,2,3});
        Column a3 = new Column("Float", "float", new Float[]{(float) 1.1, (float) 2.2,
                (float) 3.3});

        /*
        columns for table 2
         */

        Column b1 = new Column("String", "string", new String[]{"10", "20", "30"});
        Column b2 = new Column("Integer", "int", new Integer[]{10,20,30});
        Column b3 = new Column("Float", "float", new Float[]{(float) 10.1, (float) 20.2,
                (float) 30.3});

        /*
        columns for table 3
         */
        Column c1 = new Column("String1", "string", new String[]{"10", "20", "30"});
        Column c2 = new Column("Integer2", "int", new Integer[]{10,20,30});
        Column c3 = new Column("Float3", "float", new Float[]{(float) 10.1, (float) 20.2,
                (float) 30.3});

        /*
        columns for table 4
         */

        Column d1 = new Column("String", "string",
                new String[]{"1","1","1","2","2","2","3","3","3"});
        Column d2 = new Column("Integer", "int", new Integer[]{1,1,1,2,2,2,3,3,3});
        Column d3 = new Column("Float", "float", new Float[]{(float) 1.1, (float) 1.1, (float) 1.1
                , (float) 2.2,(float) 2.2, (float) 2.2, (float) 3.3, (float) 3.3, (float) 3.3});
        Column d4 = new Column("String1", "string",
                new String[]{"10", "20", "30", "10", "20", "30", "10", "20", "30"});
        Column d5 = new Column("Integer2", "int",
                new Integer[]{10,20,30, 10,20,30, 10,20,30});
        Column d6 = new Column("Float3", "float",
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
        Column e1 = new Column("String", "string", new String[]{});
        Column e2 = new Column("Integer", "int", new Integer[]{});
        Column e3 = new Column("Float", "float", new Float[]{});

        empty.add(e1);empty.add(e2);empty.add(e3);

        /* test for empty table */
        assertEquals(Operation.Join(table1, table2, "tablenew"), new Table("tablenew", empty));

        /* test for cartesian join */
        assertEquals(Operation.Join(table1, table3, "table4"), table4);

    }


    @Test
    public void testSelect(){

        Column col1 = new Column("col1", "int", new Integer[]{1,2,3,4,5});
        Column col2 = new Column("col2", "string", new String[]{"1", "2", "3", "4", "5"});
        Column col3 = new Column("col3", "float", new Float[]{(float) 1.1, (float) 2.2,(float) 3.3,
                (float) 4.4,(float) 5.5});
        Column col4 = new Column("col4", "int", new Integer[]{1,2,3,4,5});
        Column col5 = new Column("col5", "string", new String[]{"1", "2", "3", "4", "5"});


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


    @Test
    public void testCondition(){


        Column col1 = new Column("col1", "int", new Integer[]{1,4,3,2,5});
        Column col2 = new Column("col2", "string", new String[]{"1", "2", "3", "4", "5"});
        Column col3 = new Column("col3", "float", new Float[]{(float) 1.1, (float) 2.2,(float) 3.3,
                (float) 4.4,(float) 5.5});
        Column col4 = new Column("col4", "int", new Object[]{1,2,"NaN",4,5});
        Column col5 = new Column("col5", "string", new String[]{"1", "2", "3", "4", "5"});


        List<Column> tableColumns = new ArrayList<>();
        tableColumns.add(col1); tableColumns.add(col2); tableColumns.add(col3);
        tableColumns.add(col4); tableColumns.add(col5);

        Table original = new Table("selector", tableColumns);


        /*
        col1 <= 2
         */

        Column col1A = new Column("col1", "int", new Integer[]{1,2});
        Column col2A = new Column("col2", "string", new String[]{"1", "4"});
        Column col3A = new Column("col3", "float", new Float[]{(float) 1.1,
                (float) 4.4});
        Column col4A = new Column("col4", "int", new Object[]{1,4});
        Column col5A = new Column("col5", "string", new String[]{"1", "4"});


        List<Column> tableColumnsA = new ArrayList<>();
        tableColumnsA.add(col1A); tableColumnsA.add(col2A); tableColumnsA.add(col3A);
        tableColumnsA.add(col4A); tableColumnsA.add(col5A);

        Table selectorA = new Table("selector", tableColumnsA);

        assertEquals(selectorA, Operation.condition(original, "col1 <= 2"));


        /*
        col4 > 2
         */
        Column col1B = new Column("col1", "int", new Integer[]{2,5});
        Column col2B = new Column("col2", "string", new String[]{"4", "5"});
        Column col3B = new Column("col3", "float", new Float[]{(float) 4.4,(float) 5.5});
        Column col4B = new Column("col4", "int", new Object[]{4,5});
        Column col5B = new Column("col5", "string", new String[]{ "4", "5"});


        List<Column> tableColumnsB = new ArrayList<>();
        tableColumnsB.add(col1B); tableColumnsB.add(col2B); tableColumnsB.add(col3B);
        tableColumnsB.add(col4B); tableColumnsB.add(col5B);

        Table selectorB = new Table("selector", tableColumnsB);

        assertEquals(selectorB, Operation.condition(original, "col4 > 2"));


        /*
        col5 == "2" and col3 != 1.1
         */

        Column col1C = new Column("col1", "int", new Integer[]{4});
        Column col2C = new Column("col2", "string", new String[]{"2"});
        Column col3C = new Column("col3", "float", new Float[]{(float) 2.2});
        Column col4C = new Column("col4", "int", new Object[]{2});
        Column col5C = new Column("col5", "string", new String[]{ "2"});


        List<Column> tableColumnsC = new ArrayList<>();
        tableColumnsC.add(col1C); tableColumnsC.add(col2C); tableColumnsC.add(col3C);
        tableColumnsC.add(col4C); tableColumnsC.add(col5C);

        Table selectorC = new Table("selector", tableColumnsC);

        assertEquals(selectorC, Operation.condition(original, "col5 == 2 and col3 != 1.1"));



        /*
        col1 < 3 and col3 > 0 and col5 == "1" and col4 > 52
         */
        Column col1D = new Column("col1", "int", new Integer[]{});
        Column col2D = new Column("col2", "string", new String[]{});
        Column col3D = new Column("col3", "float", new Float[]{});
        Column col4D = new Column("col4", "int", new Object[]{});
        Column col5D = new Column("col5", "string", new String[]{});


        List<Column> tableColumnsD = new ArrayList<>();
        tableColumnsD.add(col1D); tableColumnsD.add(col2D); tableColumnsD.add(col3D);
        tableColumnsD.add(col4D); tableColumnsD.add(col5D);

        Table selectorD = new Table("selector", tableColumnsD);

        assertEquals(selectorD, Operation.condition(original,
                "col1 > 3 and col3 > 0 and col5 == 1 and col4 > 52"));









    }



}
