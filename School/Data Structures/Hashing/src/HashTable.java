import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * Victor Kwak
 * CS240
 * Hashing
 * The program will read files from a directory and organize the data into a hash table.
 * The table is searchable by name (athlete) and will return their average score.
 * July 14, 2014
 */
public class HashTable {
    private static Entry[] scores = new Entry[50];
    private static int tableSize = 0;
    private static int collisions = 0;

    /**
     * Entries to put into the hash table
     */
    static class Entry {
        Entry next;
        String key;
        float value;
        int occurrence = 1;

        Entry(String key, float value) {
            next = null;
            this.key = key;
            this.value = value;
        }

        /**
         * @return average score for the entry.
         */
        float averageScore() {
            return value / occurrence;
        }

        /**
         * @return a string containing the average score and the number of times a score for this entry was inputted.
         */
        public String toString() {
            DecimalFormat decimalFormat = new DecimalFormat("##0.000");
            return "\t" + key + ": " + "Avg: " + String.valueOf(decimalFormat.format(averageScore()))
                    + "\t   # Scores: " + occurrence;
        }
    }

    /**
     * Takes a filename and and calls the "put" method for each line in the file.
     *
     * @param filename name of file to be read
     */
    public static void readFile(File filename) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                if (!currentLine.isEmpty()) {
                    put(currentLine);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes a string and parses the key (name) and value (score) from the line.
     *
     * @param currentLine string to be parsed
     */
    public static void put(String currentLine) {
        String key = "";
        float value;

        String[] format = currentLine.split("\\s+"); // Split current line by whitespace
        // Parsing information in the array
        if (format[format.length - 1].matches("\\d+([.]\\d*)?")) { // Checking if last word of the line is a number
            for (int i = 0; i < format.length - 1; i++) {
                key += format[i];
                if (i != format.length - 2) {
                    key += " ";
                }
            }
            value = Float.parseFloat(format[format.length - 1]);
        } else {
            return;
        }

        // Inputting into hash table
        int hashValue = hash(key);
        if (scores[hashValue] == null) { // nothing in location
            scores[hashValue] = new Entry(key, value);
            tableSize++;
        } else {
            Entry current = scores[hashValue];
            while (current != null) {
                if (current.key.equals(key)) { // same value inside entry
                    current.value = current.value + value;
                    current.occurrence++;
                    return;
                } else {
                    if (current.next == null) { // different value inside entry
                        current.next = new Entry(key, value);
                        tableSize++;
                        collisions++;
                        return;
                    }
                }
                current = current.next;
            }
        }
    }

    /**
     * @param key String to be hashed
     * @return hash value for the string argument
     */
    public static int hash(String key) {
        int hashValue = compression(hashCode(key));
        if (hashValue < 0) {
            hashValue = -hashValue;
        }
        return hashValue;
    }

    /**
     * @param s String to generate hash code using polynomial method
     * @return hash code
     */
    public static int hashCode(String s) {
        int hash = 0;
        for (int i = 0; i < s.length(); i++) {
            hash = 31 * hash + s.charAt(i);
        }
        return hash;
    }

    /**
     *
     * @param hashCode integer to be changed so that it can act as an index value in the hash table
     * @return hash value for the entry
     */
    public static int compression(int hashCode) {
        return hashCode % (scores.length - 1);
    }

    /**
     * Prints out the minimum and maximum averages for the data set
     */
    public static void averages() {
        float minAverage = 200;
        float maxAverage = 0;
        String minNames = "";
        String maxNames = "";
        for (Entry e : scores) {
            if (e != null) {
                if (e.next == null) {
                    if (e.averageScore() < minAverage) {
                        minAverage = e.averageScore();
                        minNames = "\t" + e.key;
                    } else if (e.averageScore() > maxAverage) {
                        maxAverage = e.averageScore();
                        maxNames = "\t" + e.key;
                    } else if (e.averageScore() == minAverage) {
                        minNames = minNames + "\n\t" + e.key;
                    } else if (e.averageScore() == maxAverage) {
                        maxNames = maxNames + "\n\t" + e.key;
                    }
                } else {
                    Entry current = e.next;
                    while (current != null) {
                        if (current.averageScore() < minAverage) {
                            minAverage = current.averageScore();
                            minNames = "\t" + current.key;
                        } else if (current.averageScore() > maxAverage) {
                            maxAverage = current.averageScore();
                            maxNames = "\t" + current.key;
                        } else if (current.averageScore() == minAverage) {
                            minNames = minNames + "\n\t" + current.key;
                        } else if (current.averageScore() == maxAverage) {
                            maxNames = maxNames + "\n\t" + current.key;
                        }
                        current = current.next;
                    }
                }
            }
        }
        DecimalFormat decimalFormat = new DecimalFormat("##0.000");
        System.out.println("Minimum average:    " + decimalFormat.format(minAverage));
        System.out.println(minNames);
        System.out.println("Maximum average:    " + decimalFormat.format(maxAverage));
        System.out.println(maxNames);
    }

    /**
     * Used when user searches for a string. Prints out the contents of an entry if there is a match.
     * @param name string to search for
     */
    public static void get(String name) {
        Entry entry = scores[hash(name)];
        if (entry == null) {
            System.out.println(name + " not found.");
            return;
        } else {
            while (entry != null) {
                if (entry.key.equals(name)) {
                    System.out.println(entry);
                    return;
                }
                entry = entry.next;
            }
        }
        System.out.println(name + " not found");
    }

    /**
     * Displays information about the current hash table and takes the user's input. User can either enter what they
     * want to search for or input "exit" to exit the program.
     */
    public static void menu() {
        System.out.println("# of collisions:    " + collisions);
        System.out.println("Size of table:      " + tableSize);
        System.out.println("");
        System.out.println("# of names:         " + tableSize);
        averages();
        System.out.println("");
        Scanner scanner = new Scanner(System.in);
        String name = "";
        while (!name.equalsIgnoreCase("exit")) {
            System.out.print("Name: ");
            name = scanner.nextLine().trim();
            if (!name.equalsIgnoreCase("exit")) {
                get(name);
            }
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Directory isn't there.");
            System.exit(1);
        }
        File directory = new File(args[0]);
        File[] files = directory.listFiles();
        for (File e : files) {
            readFile(e);
        }
        menu();
    }
}
