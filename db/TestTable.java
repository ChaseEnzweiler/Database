package db;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TestTable {


    Column<String> first = new Column<>("first", "string", new String[]{"1", "2", "4", "8", "9"});
    Column<String> second = new Column<>("second", "string", new String[]{"11", "12", "14", "18", "19"});
    Column<String> third = new Column<>("third", "string", new String[]{"21", "22", "24", "28", "29"});
    Column<String> fourth = new Column<>("fourth", "string", new String[]{"31", "32", "34", "38", "39"});
    Column<String> fifth = new Column<>("fifth", "string", new String[]{"41", "42", "44", "48", "49"});


    @Test
    public void testEquals(){

        Column<String> first = new Column<>("first", "string", new String[]{"1", "2", "4", "8", "9"});
        Column<String> second = new Column<>("second", "string", new String[]{"11", "12", "14", "18", "19"});
        Column<String> third = new Column<>("third", "string", new String[]{"21", "22", "24", "28", "29"});
        Column<String> fourth = new Column<>("fourth", "string", new String[]{"31", "32", "34", "38", "39"});
        Column<String> fifth = new Column<>("fifth", "string", new String[]{"41", "42", "44", "48", "49"});


        Column[] colArray = new Column[]{first, second, third, fourth, fifth};

        String[] colNames = new String[]{"first", "second", "third", "fourth", "fifth"};

        String[] colTypes = new String[]{"string", "string", "string", "string", "string"};

        Table table1 = new Table("table1", colNames, colTypes, colArray);



        /* Same as table1 but one name of column is off*/

        String[] colNames2 = new String[]{"first", "stupid", "third", "fourth", "fifth"};

        Table table2 = new Table("table2", colNames2, colTypes, colArray);

        /* same as table1 but a value in the table is off */

        Column<String> fifthWrong = new Column<>("fifth", "string", new String[]{"1", "42", "44", "48", "49"});

        Column[] colArrayWrong = new Column[]{first, second, third, fourth, fifthWrong};

        Table table3 = new Table("table3", colNames, colTypes, colArrayWrong);


        /* Same as Table1*/

        Table table0 = new Table("table0", colNames, colTypes, colArray);

        /*
        use other constructor
         */

        List<Column> otherConstruct = new ArrayList<>();
        otherConstruct.add(first); otherConstruct.add(second); otherConstruct.add(third);
        otherConstruct.add(fourth); otherConstruct.add(fifth);

        Table otherConstructor = new Table("table1", otherConstruct);


        /*
        Assert Equals statements
         */

        boolean table1_table0 = table1.equals(table0);
        assertEquals(table1_table0, true);

        boolean table1_table2 = table1.equals(table2);
        assertEquals(table1_table2, false);

        boolean table1_table3 = table1.equals(table3);
        assertEquals(table1_table3, false);

        boolean table1_null = table1.equals(null);
        assertEquals(table1_null, false);

        boolean table1_col = table1.equals(first);
        assertEquals(table1_col, false);

        assertEquals(table1.equals(otherConstructor), true);

    }

    @Test
    public void testContainsColumn(){

        Column<String> first = new Column<>("first", "string", new String[]{"1", "2", "4", "8", "9"});
        Column<String> second = new Column<>("second", "string", new String[]{"11", "12", "14", "18", "19"});
        Column<String> third = new Column<>("third", "string", new String[]{"21", "22", "24", "28", "29"});
        Column<String> fourth = new Column<>("fourth", "string", new String[]{"31", "32", "34", "38", "39"});
        Column<String> fifth = new Column<>("fifth", "string", new String[]{"41", "42", "44", "48", "49"});


        Column[] colArray = new Column[]{first, second, third, fourth, fifth};

        String[] colNames = new String[]{"first", "second", "third", "fourth", "fifth"};

        String[] colTypes = new String[]{"String", "String", "String", "String", "String"};

        Table table1 = new Table("table1", colNames, colTypes, colArray);

        Column<String> checkTrue = new Column<>("fourth", "string", new String[]{"31", "32", "34", "38", "39"});

        Column<String> checkFalse = new Column<>("fourth", "string", new String[]{"31", "78", "34", "38", "39"});

        assertEquals(table1.containsColumn(checkTrue), true);
        assertEquals(table1.containsColumn(checkFalse), false);

    }

    @Test
    public void testMatchingColumns(){

        Column[] colArray1 = new Column[]{first, second, third, fourth, fifth};
        String[] colNames1 = new String[]{"first", "second", "third", "fourth", "fifth"};
        String[] colTypes1 = new String[]{"String", "String", "String", "String", "String"};
        Table table1 = new Table("table1", colNames1, colTypes1, colArray1);

        Column[] colArray2 = new Column[]{second, first, fourth, third, fifth};
        String[] colNames2 = new String[]{"second", "first", "fourth", "third", "fifth"};
        String[] colTypes2 = new String[]{"String", "String", "String", "String", "String"};
        Table table2 = new Table("table2", colNames2, colTypes2, colArray2);

        Column<String> noMatch1 = new Column<>("stupid", "String", new String[]{"1", "2", "4", "8", "9"});
        Column<String> noMatch2 = new Column<>("second", "incorrect", new String[]{"11", "12", "14", "18", "19"});
        Column[] colArray3 = new Column[]{noMatch1, noMatch2, first};
        String[] colNames3 = new String[]{"noMatch1", "noMatch2", "first"};
        String[] colTypes3 = new String[]{"String", "incorrect", "String"};
        Table table3 = new Table("table3", colNames3, colTypes3, colArray3);


        Column[] colArray4 = new Column[]{noMatch1, noMatch2};
        String[] colNames4 = new String[]{"noMatch1", "noMatch2"};
        String[] colTypes4 = new String[]{"String", "incorrect"};

        Table table4 = new Table("table4", colNames4, colTypes4, colArray4);



        Column<String> DiffValues = new Column<>("third", "string", new String[]{"21", "29"});

        Column[] colArray5 = new Column[]{DiffValues};
        String[] colNames5 = new String[]{"third"};
        String[] colTypes5 = new String[]{"String"};

        Table table5 = new Table("table5", colNames5, colTypes5, colArray5);



        /* method calls */

        List<Column> call1 = table1.matchingColumns(table2);
        List<Column> check1 = new ArrayList<>();
        check1.add(first); check1.add(second); check1.add(third); check1.add(fourth);
        check1.add(fifth);

        assertEquals(call1, check1);

        List<Column> call2 = table1.matchingColumns(table3);
        List<Column> check2 = new ArrayList<>();
        check2.add(first);

        assertEquals(call2, check2);

        List<Column> call3 = table1.matchingColumns(table4);
        List<Column> check3 = new ArrayList<>();

        assertEquals(call3, check3);

        List<Column> call5 = table1.matchingColumns(table5);
        List<Column> check5 = new ArrayList<>();
        check5.add(third);

        assertEquals(call5, check5);

    }

    @Test
    public void testConstructor(){

        Column[] colArray = new Column[]{first, second, third, fourth, fifth};

        String[] colNames = new String[]{"first", "second", "third", "fourth", "fifth"};

        String[] colTypes = new String[]{"string", "string", "string", "string", "string"};

        Table table1 = new Table("table1", colNames, colTypes, colArray);

        List<Column> checkCols = new ArrayList<>();
        checkCols.add(first);checkCols.add(second);
        checkCols.add(third);checkCols.add(fourth);checkCols.add(fifth);

        Table table2 = new Table("table1", checkCols);

        assertEquals(table1, table2);

    }

    @Test
    public void testInsertInto(){

        Column<Integer> a1 = new Column<>("a", "int", new Integer[]{1,2,3,4,5});
        Column<String> b1 = new Column<>("b", "string", new String[]{"1", "2", "3", "4", "5"});
        Column<Float> c1 = new Column<>("c", "float", new Float[]{(float) 1.1, (float) 2.2,(float) 3.3,
                (float) 4.4,(float) 5.5});
        Column<Integer> d1 = new Column<>("d", "int", new Integer[]{1,2,3,4,5});

        List<Column> table1List = new ArrayList<>();
        table1List.add(a1);table1List.add(b1);table1List.add(c1);table1List.add(d1);

        Table table1 = new Table("table1", table1List);

        /*
        verification table
         */
        Column<Integer> v1 = new Column<>("a", "int", new Integer[]{1,2,3,4,5, 1});
        Column<String> v2 = new Column<>("b", "string", new String[]{"1", "2", "3", "4", "5", "1"});
        Column<Float> v3 = new Column<>("c", "float", new Float[]{(float) 1.1, (float) 2.2,(float) 3.3,
                (float) 4.4,(float) 5.5, (float) 1.1});
        Column<Integer> v4 = new Column<>("d", "int", new Integer[]{1,2,3,4,5, 1});

        List<Column> tablevList = new ArrayList<>();
        tablevList.add(v1);tablevList.add(v2);tablevList.add(v3);tablevList.add(v4);

        Table tableVerify = new Table("verification", tablevList);

        /*
        list to insert to table 1
         */
        List<Object> insertList = new ArrayList<>();
        insertList.add(1); insertList.add("1");insertList.add((float) 1.1);insertList.add(1);

        List<Object> badInsertList = new ArrayList<>();
        badInsertList.add(1); badInsertList.add("1");badInsertList.add((float) 1.1);

        /*
        assert
         */

        table1.insertInto(badInsertList);

        assertEquals(table1.equals(tableVerify), false);

        assertEquals(table1.numRows == tableVerify.numRows, false);

        table1.insertInto(insertList);

        assertEquals(table1, tableVerify);

        assertEquals(table1.numRows, tableVerify.numRows);




    }








}
