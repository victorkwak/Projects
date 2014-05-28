import java.io.*;
import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Victor Kwak
 * <p>
 * The basic idea of this compression scheme will be the dictionary system. Words used in the data will be put
 * into a dictionary, so instead of repeating phrases over and over again, you can just repeat a number instead,
 * thus saving space. Also, because this system is based around written text, it will be specifically designed to
 * handle sentences and punctuation, and will not be geared to handle binary data.
 * <p>
 * May 22, 2014
 */
public class Decompressor {
    private String[] dictionary;
    private ArrayList<String> chunks = new ArrayList<>();
    private StringJoiner stringJoiner = new StringJoiner("");

    public void readFile(File fileName) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

            dictionary = new String[Integer.parseInt(bufferedReader.readLine())];

            for (int i = 0; i < dictionary.length; i++) {
                dictionary[i] = bufferedReader.readLine();
            }

            String chunk;
            while ((chunk = bufferedReader.readLine()) != null) {
                chunks.add(chunk);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(File fileName) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
            System.out.println("Writing file...");
            bufferedWriter.write(stringJoiner.toString());
            System.out.println("File written to " + fileName);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void decompress() {
        String[][] chunksSplit = new String[chunks.size()][];
        Pattern word;
        Pattern nonWord;
        Matcher matcher;
        int entry;

        for (int i = 0; i < chunksSplit.length; i++) {
            chunksSplit[i] = chunks.get(i).split(" ");

            for (int j = 0; j < chunksSplit[i].length; j++) {
                word = Pattern.compile("\\d+");
                matcher = word.matcher(chunksSplit[i][j]);

                if (matcher.find()) {
                    entry = Integer.parseInt(matcher.group());
                    nonWord = Pattern.compile("\\D");
                    matcher = nonWord.matcher(chunksSplit[i][j]);

                    if (matcher.find()) {
                        switch (matcher.group()) {
                            case "^":
                                if (stringJoiner.toString().isEmpty() || stringJoiner.toString().endsWith("\n")
                                        || stringJoiner.toString().endsWith("-")) {
                                    stringJoiner.add(dictionary[entry].substring(0, 1).toUpperCase()
                                            + dictionary[entry].substring(1));
                                } else {
                                    stringJoiner.add(" " + dictionary[entry].substring(0, 1).toUpperCase()
                                            + dictionary[entry].substring(1));
                                }
                                break;
                            case "!":
                                if (stringJoiner.toString().isEmpty() || stringJoiner.toString().endsWith("\n")
                                        || stringJoiner.toString().endsWith("-")) {
                                    stringJoiner.add(dictionary[entry].toUpperCase());
                                } else {
                                    stringJoiner.add(" " + dictionary[entry].toUpperCase());
                                }
                                break;
                        }
                    } else {
                        if (stringJoiner.toString().isEmpty() || stringJoiner.toString().endsWith("\n")
                                || stringJoiner.toString().endsWith("-")) {
                            stringJoiner.add(dictionary[entry]);
                        } else {
                            stringJoiner.add(" " + dictionary[entry]);
                        }
                    }
                } else {
                    nonWord = Pattern.compile("\\D");
                    matcher = nonWord.matcher(chunksSplit[i][j]);

                    if (matcher.find()) {
                        switch (matcher.group()) {
                            case "-":
                                stringJoiner.add("-");
                                break;
                            case "r":
                            case "R":
                                stringJoiner.add("\n");
                                break;
                            case "E":
                            case "e":
                                System.out.println("Finished. Final output: \n");
                                System.out.println(stringJoiner.toString());
                                return;
                            default:
                                stringJoiner.add(matcher.group());
                                break;
                        }
                    }
                }
            }
        }
    }
}
