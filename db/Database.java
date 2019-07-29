package db;

import java.util.*;
import java.io.*;



public class Database {

    /*
    TODO: refactor to fix generics <Object> issue with columns, add '' to outside of strings to match tbl files
    TODO: List<Column<Object>> refactor over many classes.
     */


    private Map<String, Table> database;



    public Database() {

        database = new HashMap<>();
    }


    /**
     * returns table from database specified by String name
     * @param name String
     * @return Table
     */
    Table getTable(String name){

        return this.database.get(name);

    }

    /**
     * adds parameter table into the database
     * @param table Table you want added to the database
     */
    public void add(Table table){

        database.put(table.getTableName(), table);

    }

    /**
     * method to see if a table is in the database
     * @param name String name of table
     * @return True if table exists
     */
    boolean containsTable(String name){

        return database.containsKey(name);
    }


    /**
     * removes table from the database specified by string name and prints empty string on success,
     * if the specified table does not exist prints error
     * @param name String name of table to drop
     */
    void dropTable(String name){

        if (database.containsKey(name)){

            database.remove(name);

        } else {

            System.out.println("Error: Table '" + name + "' does not exist in database");
        }
    }



    /**
     * loads a Table into the database map from a tbl file stored in project examples folder. If loading a
     * table into database that already exists in database it will overwrite.
     * @param tableName String name of the table you want to load
     */
    void load(String tableName){

        File file = new File("/Users/Cenzwe/Desktop/proj2/examples/" + tableName + ".tbl");


        BufferedReader reader;

        try{

            reader = new BufferedReader(new FileReader(file));

        } catch (Exception e){

            System.out.println("Error: File " + tableName + ".tbl not found");
            return;
        }

        String line;

        List<Column> columnsToStore = new ArrayList<>();

        /*
        parse the first line of the tbl file that contains names and types
         */

        try {

            line = reader.readLine();

            String[] nameAndType = line.split(",");

            for (String info : nameAndType) {

                String[] nameTypeSplit = info.split(" ");

                Column col = new Column(nameTypeSplit[0], nameTypeSplit[1], new ArrayList<>());

                columnsToStore.add(col);

            }

        /*
        now parse by commas for each line and add to a list of columns making sure that columns are objects
        and parsing to int, string, or float. adding values to columns
         */

            while ((line = reader.readLine()) != null) {

                String[] values = line.split(",");

                int count = 0;

                for (String value : values) {

                /*
                check type of each column and add values to each column
                 */

                    if (value.equals("NaN")) {

                        columnsToStore.get(count).add("NaN");

                    } else if (columnsToStore.get(count).getType().equals("int")) {

                        columnsToStore.get(count).add(Integer.parseInt(value));

                    } else if (columnsToStore.get(count).getType().equals("float")) {

                        columnsToStore.get(count).add(Float.parseFloat(value));

                    } else {

                        columnsToStore.get(count).add(value);
                    }

                    count += 1;

                }
            }

        } catch(Exception e){

            System.out.println("Error: Could not read line from file " + tableName + ".tbl");
            return;
        }
        /*
        create the table and put it into the database.
         */

        Table tableToLoad = new Table(tableName, columnsToStore);

        database.put(tableName, tableToLoad);

    }


    /** // verified
     * method takes a table in the database and writes it to a tbl file stored in examples folder,
     * if table already exists in tbl folder this method should overwrite the file.
     * @param name String
     */
    void storeTable(String name){

        Table toStore;

        File file = new File("/Users/Cenzwe/Desktop/proj2/examples/" + name + ".tbl");

        BufferedWriter writer;

        if(!database.containsKey(name)){

            System.out.println("Error: Table named " + name + " does not exist in database");
            return;
        }

        try {

            writer = new BufferedWriter(new FileWriter(file, false));

        } catch(Exception e){

            System.out.println("Error: Could not write contents of file");
            return;
        }

        toStore = database.get(name);

        List<String> linesToWrite = toStore.tableToStringSegments();

        try {

            for (String line : linesToWrite) {

                writer.write(line);
                writer.newLine();

            }

            writer.close();

        } catch (Exception e){

            System.out.println("Error: Could not write table rows to file");

        }

    }


    public String transact(String query) {

         CommandLineParser.eval(query, this);
         return "";


    }
}
