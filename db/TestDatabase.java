package db;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestDatabase {

    @Test
    public void testLoad(){

        Database example = new Database();


        example.load("records");




        Table result = example.getTable("records");

        /*
        create matching table
         */

        Column<String> col1 =  new Column<>("TeamName", "string",
                new String[]{"'Golden Bears'", "'Golden Bears'", "'Golden Bears'", "'Steelers'", "'Steelers'",
                        "'Steelers'", "'Mets'", "'Mets'", "'Mets'", "'Patriots'", "'Patriots'", "'Patriots'"});

        Column<Integer> col2 = new Column<>("Season", "int", new Integer[]{2016, 2015, 2014,
        2015, 2014, 2013, 2015, 2014, 2013, 2015, 2014, 2013});

        Column<Integer> col3 = new Column<>("Wins", "int", new Integer[]{5, 8, 5, 10, 11,
        8, 90, 79, 74, 12, 12, 12});

        Column<Integer> col4 = new Column<>("Losses", "int", new Integer[]{7, 5, 7, 6, 5, 8, 72, 83, 88,
         4, 4, 4});

        Column<Integer> col5 = new Column<>("Ties", "int", new Integer[]{0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0});

        List<Column> allColumns = new ArrayList<>();
        allColumns.add(col1); allColumns.add(col2); allColumns.add(col3); allColumns.add(col4); allColumns.add(col5);

        Table actual = new Table("records", allColumns);

        assertEquals(result, actual);

    }









}
