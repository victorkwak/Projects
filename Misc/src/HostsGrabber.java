import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Victor Kwak
 * May 19, 2014
 * <p>
 * Description: Grabs hosts files from multiples sources and compiles into one list while removing duplicate entries.
 */
public class HostsGrabber {
    //sources
    private static final String ADAWAY = "https://adaway.org/hosts.txt";
    private static final String MVPS = "http://winhelp2002.mvps.org/hosts.txt";
    private static final String HPHOSTS = "http://hosts-file.net/ad_servers.asp";
    private static final String YOYOS = "http://pgl.yoyo.org/adservers/serverlist.php?hostformat=hosts&showintro=0&mimetype=plaintext";

    public static void getHostFiles(String sourceName, ArrayList<String> sourceArray) throws Exception {
        URL sourceURL = new URL(sourceName);
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sourceURL.openStream()));
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                sourceArray.add(currentLine);
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeHostsFile(ArrayList<String> compiledList) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("hosts"));
            for (String aCompiledList : compiledList) {
                bufferedWriter.write(aCompiledList + "\n");
            }
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ArrayList<String> source1 = new ArrayList<>();
        ArrayList<String> source2 = new ArrayList<>();
        ArrayList<String> source3 = new ArrayList<>();
        ArrayList<String> source4 = new ArrayList<>();

        try {
            getHostFiles(ADAWAY, source1);
            getHostFiles(MVPS, source2);
            getHostFiles(HPHOSTS, source3);
            getHostFiles(YOYOS, source4);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Set<String> hashedList = new LinkedHashSet<>();
        hashedList.addAll(source1);
        hashedList.addAll(source2);
        hashedList.addAll(source3);
        hashedList.addAll(source4);
        ArrayList<String> compiledList = new ArrayList<>();
        compiledList.addAll(hashedList);
        writeHostsFile(compiledList);
    }
}