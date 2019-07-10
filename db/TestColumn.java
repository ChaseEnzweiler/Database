package db;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

import static org.junit.Assert.assertEquals;

public class TestColumn {



    @Test
    public void testEquals(){

        Column<String> col1 = new Column<>("fourth", "string", new String[]{"31", "78", "34", "38", "39"});

        // different than col1
        Column<String> col2 = new Column<>("first", "string", new String[]{"1", "2", "4", "8", "9"});


        Column<String> col3 = new Column<>("second", "string", new String[]{"11", "12", "14", "18", "19"});

        // different name than col2
        Column<String> col4 = new Column<>("stupid", "string", new String[]{"1", "2", "4", "8", "9"});

        // different type than col3
        Column<String> col5 = new Column<>("second", "dumb", new String[]{"11", "12", "14", "18", "19"});

        // different value than col2
        Column<String> col6 = new Column<>("first", "string", new String[]{"100", "2", "4", "8", "9"});

        //same as col1
        Column<String> col7 = new Column<>("fourth", "string", new String[]{"31", "78", "34", "38", "39"});


        assertEquals(col1.equals(col2), false);
        assertEquals(col2.equals(col4), false);
        assertEquals(col5.equals(col3), false);
        assertEquals(col6.equals(col2), false);
        assertEquals(col1.equals(col7), true);

    }



    @Test
    public void testFilterByRow(){

        Column<String> col1 = new Column<>("first", "string", new String[]{"1", "2", "4", "8", "9"});

        List<Integer> list1 = new ArrayList<>();
        list1.add(0);
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);

        Column<String> colList1 = new Column<>("first", "string", new String[]{"1", "2", "4", "8", "9"});


        List<Integer> list2 = new ArrayList<>();
        list2.add(0);
        list2.add(2);
        list2.add(4);

        Column<String> colList2 = new Column<>("first", "string", new String[]{"1", "4", "9"});

        List<Integer> list3 = new ArrayList<>();
        list3.add(3);

        Column<String> colList3 = new Column<>("first", "string", new String[]{"8"});

        List<Integer> emptyList = new ArrayList<>();

        Column<String> colEmptyList = new Column<>("first", "string", new String[]{});



        /* calls */
        Column<String> filterCol1 = col1.filterByRow(list1);

        Column<String> filterCol2 = col1.filterByRow(list2);

        Column<String> filterCol3 = col1.filterByRow(list3);

        Column<String> filterColEmpty = col1.filterByRow(emptyList);

        /* assert statements */

        assertEquals(colList1, filterCol1);
        assertEquals(colList2, filterCol2);
        assertEquals(colList3, filterCol3);
        assertEquals(filterColEmpty, colEmptyList);

        /*
        test new row collection functionality for filter with duplicate indices
         */

        List<Integer> duplicateList = new ArrayList<>();
        duplicateList.add(0);
        duplicateList.add(0);
        duplicateList.add(4);
        duplicateList.add(2);
        duplicateList.add(0);
        duplicateList.add(4);

        Column<String> col1Duplicate =
                new Column<>("first", "string", new String[]{"1", "1", "9", "4", "1", "9"});

        assertEquals(col1.filterByRow(duplicateList), col1Duplicate);

    }



    /*
    quick test to check the equals method for row class
     */
    @Test
    public void rowEquals(){

        Row first = new Row();

        first.add("string");
        first.add("chase");
        first.add("day");
        first.add(56);
        first.add(90.000000);

        Row second = new Row();

        second.add("string");
        second.add("chase");
        second.add("day");
        second.add(56);
        second.add(90.000000);

        Row third = new Row();

        third.add("string");
        third.add("chse");
        third.add("day");
        third.add(56);
        third.add(90.000000);

        Row fourth = new Row();

        fourth.add("string");
        fourth.add("chase");
        fourth.add("day");
        fourth.add(56);


        assertEquals(first.equals(second), true);
        assertEquals(first.equals(third), false);
        assertEquals(first.equals(fourth), false);
        assertEquals(third.equals(fourth), false);
        assertEquals(first.equals(first), true);


    }

    @Test
    public void testAdd(){

        Column<String> col1 = new Column<>("fourth", "string", new String[]{"31", "78", "34", "38", "39"});

        Column<String> col2 = new Column<>("fourth", "string",
                new String[]{"31", "78", "34", "38", "39", "4"});


        col1.add("4");

        assertEquals(col1, col2);

    }



    @Test
    public void testAddition(){

        /*
        float plus integer
         */

        Column<Float> col1 = new Column<>("fourth", "float", new Float[]{(float) 31
                ,(float) 78,(float) 34,(float) 38,(float) 39});


        Column<Integer> col2 = new Column<>("first", "int", new Integer[]{1, 2, 4, 8, 9});

        List<Float> result = new ArrayList<>();
        result.add((float) 32);
        result.add((float) 80);
        result.add((float) 38);
        result.add((float) 46);
        result.add((float) 48);

        Column<Float> resultCol = new Column<>("result", "float", result);

        assertEquals(Column.addition(col1, col2, "result"), resultCol);


        /*
        String plus String
         */

        Column<String> col3 = new Column<>("fourth", "string", new String[]{"31", "78", "34", "38", "39"});


        Column<String> col4 = new Column<>("first", "string", new String[]{"1", "2", "4", "8", "9"});

        List<String> result2 = new ArrayList<>();
        result2.add("311");
        result2.add("782");
        result2.add("344");
        result2.add("388");
        result2.add("399");

        Column<String> result2Col = new Column<String>("result2", "string", result2);

        assertEquals(Column.addition(col3, col4, "result2"), result2Col);

        /*
        Integer addition
         */

        Column<Integer> col5 = new Column<>("first", "int", new Integer[]{1, 2, 4, 8, 9});

        Column<Integer> col6 = new Column<>("first", "int", new Integer[]{1, 2, 4, 8, 9});

        Column<Integer> resultCol3 = new Column<>("result3", "int", new Integer[]{2, 4, 8, 16, 18});

        assertEquals(Column.addition(col5, col6, "result3"), resultCol3);

    }

    @Test
    public void testSubtraction(){

        Column<Float> col1 = new Column<>("fourth", "float", new Float[]{(float) 31
                ,(float) 78,(float) 34,(float) 38,(float) 39});

        Column<Integer> col2 = new Column<>("first", "int", new Integer[]{1, 2, 4, 8, 9});

        Column<Integer> col3 = new Column<>("first", "int", new Integer[]{1, 2, 4, 8, 9});

        Column<String> col5 = new Column<>("fourth", "string", new String[]{"31", "78", "34", "38", "39"});


        List<Float> col1minus2 = new ArrayList<>();
        col1minus2.add((float) 30);col1minus2.add((float) 76);col1minus2.add((float) 30);
        col1minus2.add((float) 30);col1minus2.add((float) 30);

        Column<Float> result1minus2 = new Column<>("result", "float", col1minus2);


        List<Integer> col2minus3 = new ArrayList<>();
        col2minus3.add(0);col2minus3.add(0);col2minus3.add(0);col2minus3.add(0);
        col2minus3.add(0);

        Column<Integer> result2minus3 = new Column<>("result", "int", col2minus3);

        assertEquals(Column.subtraction(col1, col2, "result"), result1minus2);

        assertEquals(Column.subtraction(col2, col3, "result"), result2minus3);


    }


    @Test
    public void testDivision(){

        Column<Float> col1 = new Column<>("fourth", "float", new Float[]{(float) 31
                ,(float) 78,(float) 34,(float) 38,(float) 49});

        Column<Integer> col2 = new Column<>("first", "int", new Integer[]{0, 2, 2, 2, 7});


        //result
        List result = new ArrayList();
        result.add("NaN"); result.add((float) 39); result.add((float) 17);
        result.add((float) 19); result.add((float) 7);

        Column resultCol = new Column("result", "float", result);

        assertEquals(Column.division(col1, col2, "result"), resultCol);

    }

    @Test
    public void testMultiplication(){

        Column<Float> col1 = new Column<>("fourth", "float", new Float[]{(float) 31
                ,(float) 78,(float) 34,(float) 38,(float) 49});

        Column<Integer> col2 = new Column<>("first", "int", new Integer[]{0, 2, 2, 2, 7});

        Column col3 = new Column<>("third", "int", new Object[]{2, 3, "NaN", 5, 8});

        /*
        create resulting columns
         */

        List col1Times2 = new ArrayList();
        col1Times2.add((float) 0); col1Times2.add((float) 156); col1Times2.add((float) 68);
        col1Times2.add((float) 76); col1Times2.add((float) 343);

        Column result1 = new Column("result", "float", col1Times2);

        List col2Times3 = new ArrayList();
        col2Times3.add(0); col2Times3.add(6);col2Times3.add("NaN");
        col2Times3.add(10); col2Times3.add(56);

        Column result2 = new Column("result", "int", col2Times3);


        assertEquals(Column.multiplication(col1, col2, "result"), result1);

        assertEquals(Column.multiplication(col2, col3, "result"), result2);

    }



    @Test
    public void testRowEqualsTo(){

        Column<Float> col1 = new Column<>("fourth", "float", new Float[]{(float) 31
                ,(float) 78,(float) 34,(float) 31,(float) 39});

        Column col3 = new Column<>("third", "int", new Object[]{5, 3, "NaN", 5, 8});

        Column<String> col4 = new Column<>("fourth", "string", new String[]{"31", "78", "31", "38", "31"});


        Set<Integer> set1 = new HashSet<>();
        set1.add(0); set1.add(3);

        Set<Integer> set2 = new HashSet<>();
        set2.add(0); set2.add(3);

        Set<Integer> set4 = new HashSet<>();
        set4.add(0); set4.add(2);set4.add(4);


        assertEquals(col1.rowsEqualTo("31"), set1);
        assertEquals(col3.rowsEqualTo("5"), set2);
        assertEquals(col4.rowsEqualTo("31"), set4);


    }

    @Test
    public void testRowLessThan(){

        /*
        finish this
         */
        Column<Float> col1 = new Column<>("first", "float", new Float[]{(float) 31
                ,(float) 78,(float) 34,(float) 31,(float) 39});

        Column col2 = new Column<>("second", "int", new Object[]{5, 3, "NaN", 5, 8});

        Column<String> col3 = new Column<>("third", "string", new String[]{"31", "78", "31", "38", "31"});

        Set<Integer> set1 = new HashSet<>(); //34
        set1.add(0); set1.add(3);

        Set<Integer> set2 = new HashSet<>(); //8
        set2.add(0); set2.add(1); set2.add(3);

        Set<Integer> set3 = new HashSet<>(); //78
        set3.add(0); set3.add(2); set3.add(3); set3.add(4);

        assertEquals(col1.rowsLessThan("34"), set1);
        assertEquals(col2.rowsLessThan("8"), set2);
        assertEquals(col3.rowsLessThan("78"), set3);


    }





    @Test
    public void testComparisonMethods(){

        /*
        quickly testing the rest of the comparison column methods
         */

        Column<Float> col1 = new Column<>("first", "float", new Float[]{(float) 31
                ,(float) 78,(float) 34,(float) 31,(float) 39});

        Column col2 = new Column<>("second", "int", new Object[]{5, 3, "NaN", 5, 8});

        Column<String> col3 = new Column<>("third", "string", new String[]{"abc", "smith", "dog", "a", "pop"});

        Set<Integer> set1 = new HashSet<>(); //not equal 31
        set1.add(1); set1.add(2); set1.add(4);

        Set<Integer> set2 = new HashSet<>(); // greater than 4
        set2.add(0); set2.add(3); set2.add(4);

        Set<Integer> set3 = new HashSet<>(); // less than or equal smit
        set3.add(0); set3.add(2); set3.add(3); set3.add(4);

        Set<Integer> set4 = new HashSet<>(); //redo 2, greater than equal 5
        set4.add(0); set4.add(3); set4.add(4);



        assertEquals(col1.rowsNotEqualTo("31"), set1);

        assertEquals(col2.rowsGreaterThan("4"), set2);

        assertEquals(col3.rowsLessThanOrEqual("smit"), set3);

        assertEquals(col2.rowsGreaterThanOrEqual("5"), set4);


    }



}
