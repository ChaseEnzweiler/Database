package db;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;



class CommandLineParser {

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


    /**
     * this method takes in a string, parses it, and passes the parsed String into the correct method
     * that should be called
     * @param query String user query
     * @param db Database
     */
    static void eval(String query, Database db) {
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
            createNewTable(m.group(1), m.group(2).split(COMMA), db);
        } else if ((m = CREATE_SEL.matcher(expr)).matches()) {
            createSelectedTable(m.group(1), m.group(2), m.group(3), m.group(4), db);
        } else {
            System.err.printf("Malformed create: %s\n", expr);
        }

    }

    /**
     * Create a new empty Table and add it to the Database.
     * @param name String name of the new table
     * @param cols String columns to be in the new Table
     * @param db Database for the table to be added to
     */
    private static void createNewTable(String name, String[] cols, Database db) {

        List<String> columnNames = new ArrayList<>();
        List<String> columnType = new ArrayList<>();

        for(String col : cols){

            String[] colAndType = col.trim().split("\\s+");

            String colName = colAndType[0].trim();

            if(columnNames.contains(colName)){

                System.err.println("Error: cannot create table with duplicate columns " + colName);
                return;
            }

            columnNames.add(colName);

            String type = colAndType[1].trim();

            if(!(type.equals("string") || type.equals("int") || type.equals("float"))){

                System.err.println("Error: incorrect type given " + type);
                return;
            }
            columnType.add(colAndType[1]);

        }

        Table table = new Table(name, columnNames, columnType);

        db.add(table);

    }

    /**
     * creates a new table from the results of a select statement and where clause and adds the table to
     * the Databse
     * @param name String name of the new Table
     * @param exprs String select statement
     * @param tables String Tables to be operated on
     * @param conds String conditional statement
     * @param db Database to have table added to
     */
    private static void createSelectedTable(String name, String exprs, String tables, String conds, Database db) {

        Table table = selectHelper(exprs, tables, conds, db, name);

        if(table == null){
            return;
        }

        if(table.numColumns == 0){
            System.err.println("Error: cannot create a table with 0 columns");
            return;
        }

        if(db.containsTable(table.getTableName())){

            System.err.println("Error: Table " + table.getTableName() + " already exists");
            return;
        }

        db.add(table);

    }

    private static void loadTable(String name, Database db) {

        name = name.trim();

        db.load(name);
    }

    private static void storeTable(String name, Database db) {

        name = name.trim();

        db.storeTable(name);
    }

    private static void dropTable(String name, Database db) {

        name = name.trim();

        db.dropTable(name);

    }

    /**
     * inserts a new row at the end of the specified Table in the expression String
     * @param expr String specifies values to be inserted and Table
     * @param db Database
     */
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

        }

    }


    private static void printTable(String name, Database db) {

        if(!db.containsTable(name)){

            System.err.println("Error: Table " + name + " does not exist in database.");
            return;
        }

        db.getTable(name).printTable();

    }

    /**
     * parses expression given and calls the Select method to print corresponding Table
     * @param expr String select clause
     * @param db Database
     */
    private static void select(String expr, Database db) {
        Matcher m = SELECT_CLS.matcher(expr);
        if (!m.matches()) {
            System.err.printf("Malformed select: %s\n", expr);

        }

        select(m.group(1), m.group(2), m.group(3), db, "");

    }

    /**
     * Prints resulting table from the operating on the given tables and clauses
     * according to inputted clauses
     * @param exprs Select clause columns
     * @param tables Tables to be operated on
     * @param conds where clause conditions
     * @param db Database
     * @param tableName String name of table to be printed
     */
    private static void select(String exprs, String tables, String conds, Database db, String tableName){

        Table tableToPrint = selectHelper(exprs, tables, conds, db, tableName);

        if(tableToPrint == null){
            return;
        }

        tableToPrint.printTable();

    }

    /**
     * method returns a new Table created from operated on the Columns and Tables
     * specified in the clauses
     * @param exprs String of columns to be selected from tables
     * @param tables String of tables to be used
     * @param conds String of conditions with regards to where clause
     * @param db Database
     * @param tableName String name of new table to be created
     * @return Table after all clauses are evaluated
     */
    private static Table selectHelper(String exprs, String tables, String conds, Database db, String tableName) {

        Table afterJoin;

        String[] tablesToJoin = tables.replaceAll("\\s+", "").split(",");

        /*
        check to make sure all tables exist in the database before executing join
         */
        for(String table : tablesToJoin){

            if(!db.containsTable(table)){

                System.err.println("Error: Table " + table + " does not exist in the database.");
                return null;
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

                afterJoin = Operation.Join(afterJoin, db.getTable(tablesToJoin[counter]), tableName);

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

                columnSelect.add(nameAs[0].trim());
                columnNames.add(nameAs[1].trim());

            } else{

                columnSelect.add(col.trim());
                columnNames.add(col.trim());
            }
        }

        try {

            afterJoin = Operation.select(columnSelect, columnNames, afterJoin, tableName);
        } catch (Exception e){

            System.err.println("Error: Illegal Column Expression.");
            return null;
        }

        /*
        lastly evaluate  conditional statement if a conditional statement exists
         */
        if(conds != null){

            try {
                afterJoin = Operation.condition(afterJoin, conds);

            } catch(IllegalArgumentException i){

                System.err.println("Error: There is no operator in the conditional statement");
                return null;
            }

        }
        return afterJoin;

    }
}





