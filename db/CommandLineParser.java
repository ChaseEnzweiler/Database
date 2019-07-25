package db;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import java.util.StringJoiner;

/* TODO: adding strings has too many apostrophe*/
public class CommandLineParser {

    // Various common constructs, simplifies parsing.
    private static final String REST  = "\\s*(.*)\\s*",
            COMMA = "\\s*,\\s*",
            AND   = "\\s+and\\s+";

    // Stage 1 syntax, contains the command name.
    private static final Pattern CREATE_CMD = Pattern.compile("create table " + REST),
            LOAD_CMD   = Pattern.compile("load " + REST),
            STORE_CMD  = Pattern.compile("store " + REST),
            DROP_CMD   = Pattern.compile("drop table " + REST),
            INSERT_CMD = Pattern.compile("insert into " + REST),
            PRINT_CMD  = Pattern.compile("print " + REST),
            SELECT_CMD = Pattern.compile("select " + REST);

    // Stage 2 syntax, contains the clauses of commands.
    private static final Pattern CREATE_NEW  = Pattern.compile("(\\S+)\\s+\\(\\s*(\\S+\\s+\\S+\\s*" +
            "(?:,\\s*\\S+\\s+\\S+\\s*)*)\\)"),
            SELECT_CLS  = Pattern.compile("([^,]+?(?:,[^,]+?)*)\\s+from\\s+" +
                    "(\\S+\\s*(?:,\\s*\\S+\\s*)*)(?:\\s+where\\s+" +
                    "([\\w\\s+\\-*/'<>=!.]+?(?:\\s+and\\s+" +
                    "[\\w\\s+\\-*/'<>=!.]+?)*))?"),
            CREATE_SEL  = Pattern.compile("(\\S+)\\s+as select\\s+" +
                    SELECT_CLS.pattern()),
            INSERT_CLS  = Pattern.compile("(\\S+)\\s+values\\s+(.+?" +
                    "\\s*(?:,\\s*.+?\\s*)*)");



    public static void eval(String query, Database db) {
        Matcher m;
        if ((m = CREATE_CMD.matcher(query)).matches()) {
            createTable(m.group(1), db);
        } else if ((m = LOAD_CMD.matcher(query)).matches()) {
            loadTable(m.group(1), db);
        } else if ((m = STORE_CMD.matcher(query)).matches()) {
            storeTable(m.group(1), db);
        } else if ((m = DROP_CMD.matcher(query)).matches()) {
            dropTable(m.group(1), db);
        } else if ((m = INSERT_CMD.matcher(query)).matches()) {
            insertRow(m.group(1), db);
        } else if ((m = PRINT_CMD.matcher(query)).matches()) {
            printTable(m.group(1), db);
        } else if ((m = SELECT_CMD.matcher(query)).matches()) {
            select(m.group(1), db);
        } else {
            System.err.printf("Malformed query: %s\n", query);
        }

    }

    private static void createTable(String expr, Database db) {
        Matcher m;
        if ((m = CREATE_NEW.matcher(expr)).matches()) {
            createNewTable(m.group(1), m.group(2).split(COMMA));
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4));
        } else {
            System.err.printf("Malformed create: %s\n", expr);
        }

    }

    private static void createNewTable(String name, String[] cols) {
        StringJoiner joiner = new StringJoiner(", ");
        for (int i = 0; i < cols.length-1; i++) {
            joiner.add(cols[i]);
        }

        String colSentence = joiner.toString() + " and " + cols[cols.length-1];
        System.out.printf("You are trying to create a table named %s with the columns %s\n", name, colSentence);


    }

    private static void createSelectedTable(String name, String exprs, String tables, String conds) {
        System.out.printf("You are trying to create a table named %s by selecting these expressions:" +
                " '%s' from the join of these tables: '%s', filtered by these conditions: '%s'\n", name, exprs, tables, conds);


    }

    private static void loadTable(String name, Database db) {

        db.load(name);
    }

    private static void storeTable(String name, Database db) {

        db.storeTable(name);
    }

    private static void dropTable(String name, Database db) {

        db.dropTable(name);

    }

    private static void insertRow(String expr, Database db) {
        Matcher m = INSERT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("Malformed insert: %s\n", expr);

        }
        /*
        first check if the lengths match up, then convert strings into proper types for each column,
        need to pass into method as a list<Object>
         */

        String tableName = m.group(1);

        if(!db.containsTable(tableName)){

            System.err.println("Error: Table "+ tableName + " does not exist.");
            return;
        }

        Table table = db.getTable(tableName);

        String cleanedLiterals =
                m.group(2).replaceAll("\\s+", "").replaceAll("'", "");

        String[] values = cleanedLiterals.split(",");

        List<Column> columns = table.getColumns();

        /*
        check to make sure correct amount of literals are provided
         */
        if(columns.size() != values.length){

            System.err.println("Error: did not provide correct amount of literals for this table.");
            return;
        }

        /*
        check to make sure the literals are the right types and add to an object list
         */
        List<Object> literals = new ArrayList<>();

        int counter = 0;

        try {

            for (Column col : columns) {

                if (col.getType().equals("int")) {

                    literals.add(Integer.parseInt(values[counter]));

                } else if (col.getType().equals("float")) {

                    literals.add(Float.parseFloat(values[counter]));

                } else {

                    literals.add("'" + values[counter] + "'");
                }

                counter += 1;
            }

            table.insertInto(literals);

        } catch(Exception e){

            System.err.println("Error: wrong type literal " + values[counter] + " given.");
            return;
        }

        //System.out.printf("You are trying to insert the row \"%s\" into the table %s\n", m.group(2), m.group(1));
    }

    private static void printTable(String name, Database db) {

        if(!db.containsTable(name)){

            System.err.println("Error: Table " + name + " does not exist in database.");
            return;
        }

        db.getTable(name).printTable();

    }

    private static void select(String expr, Database db) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("Malformed select: %s\n", expr);

        }

        select(m.group(1), m.group(2), m.group(3), db);

    }

    /* can prob edit to make work with create selected table */
    private static void select(String exprs, String tables, String conds, Database db) {

        Table afterJoin;

        String[] tablesToJoin = tables.replaceAll("\\s+", "").split(",");

        /*
        check to make sure all tables exist in the database before executing join
         */
        for(String table : tablesToJoin){

            if(!db.containsTable(table)){

                System.err.println("Error: Table " + table + " does not exist in the database.");
                return;
            }
        }

        /*
        join tables
         */
        if(tablesToJoin.length > 1){
            // do join

            afterJoin = db.getTable(tablesToJoin[0]);

            int counter = 1;

            while(counter < tablesToJoin.length){

                afterJoin = Operation.Join(afterJoin, db.getTable(tablesToJoin[counter]), "");

                counter += 1;

            }

        } else{

            afterJoin = db.getTable(tablesToJoin[0]);
        }

        /*
        now evaluate column expressions on the joined table, check if the column should be named as something
        else using the "as" keyword. then select those columns from afterJoin Table. need columns and column names
        in String lists to pass into select
         */

        String[] columns = exprs.split(",");

        List<String> columnSelect = new ArrayList<>();

        List<String> columnNames = new ArrayList<>();

        for(String col : columns){

            if(col.contains(" as ")){

                String[] nameAs = col.split(" as ");

                columnSelect.add(nameAs[0]);
                columnNames.add(nameAs[1]);

            } else{

                columnSelect.add(col);
                columnNames.add(col);
            }
        }

        try {

            afterJoin = Operation.select(columnSelect, columnNames, afterJoin, "");
        } catch (Exception e){

            System.err.println("Error: Illegal Column Expression.");
            return;
        }

        /*
        lastly evaluate  conditional statement if a conditional statement exists
         */
        if(conds != null){

            try {
                afterJoin = Operation.condition(afterJoin, conds);

            } catch(IllegalArgumentException i){

                System.err.println("Error: There is no operator in the conditional statement");
                return;
            }

        }

        afterJoin.printTable();
    }
}





