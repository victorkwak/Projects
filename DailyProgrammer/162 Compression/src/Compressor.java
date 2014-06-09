import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringJoiner;

/**
 * Victor Kwak
 * CS141, section 01
 * (name of assignment here)
 * May 28, 2014
 */
public class Compressor {
    private static StringJoiner stringJoiner;

    public static void readFile(File filename) {
        stringJoiner = new StringJoiner("");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            String currentline;
            while ((currentline = bufferedReader.readLine()) != null) {
                stringJoiner.add(currentline + "\n");
            }
            bufferedReader.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public static void compress() {

    }

    public static void main(String[] args) {
        readFile(new File("output.txt"));
        System.out.println(stringJoiner);
    }
}
