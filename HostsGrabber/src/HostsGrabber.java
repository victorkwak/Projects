import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Victor Kwak
 * Description: Grabs hosts files from multiples sources and compiles into one list while removing duplicate entries.
 */
public class HostsGrabber extends JFrame implements ActionListener, PropertyChangeListener {
    private static JProgressBar jProgressBar;
    private static JTextArea currentTask;
    private static JButton start;

    /**
     * Constructor builds GUI
     */
    public HostsGrabber() {
        //GUI code
        setTitle("HostsGrabber");
        setLayout(new FlowLayout());
        setSize(250, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel instructions = new JLabel("Press \"Start\" to begin.");
        add(instructions);

        start = new JButton("Start");
        start.addActionListener(this);
        add(start);

        jProgressBar = new JProgressBar(0, 100);
        jProgressBar.setValue(0);
        add(jProgressBar);

        currentTask = new JTextArea(5, 15);
        currentTask.setMargin(new Insets(5, 5, 5, 5));
        currentTask.setEditable(false);
        DefaultCaret defaultCaret = (DefaultCaret) currentTask.getCaret();
        defaultCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        add(new JScrollPane(currentTask));

        setVisible(true);
    }

    /**
     * Nested class provides main function of the program. Will run in a background thread.
     */
    class GetHosts extends SwingWorker<Void, Void> {
        public ArrayList<String> generateList() {
            // Sources
            final String ADAWAY = "https://adaway.org/hosts.txt";
            final String MVPS = "http://winhelp2002.mvps.org/hosts.txt";
            final String HPHOSTS = "http://hosts-file.net/ad_servers.asp";
            final String YOYOS = "http://pgl.yoyo.org/adservers/serverlist.php?hostformat=hosts&showintro=0&mimetype=plaintext";
            final String SOMEONEWHOCARES = "http://someonewhocares.org/hosts/hosts";
            final String MALWARE = "http://www.malwaredomainlist.com/hostslist/hosts.txt";

            ArrayList<String> source1 = new ArrayList<>();
            ArrayList<String> source2 = new ArrayList<>();
            ArrayList<String> source3 = new ArrayList<>();
            ArrayList<String> source4 = new ArrayList<>();
            ArrayList<String> source5 = new ArrayList<>();
            ArrayList<String> source6 = new ArrayList<>();

            System.out.println("Pulling from sources...");
            currentTask.append("Pulling from sources...\n");
            try {
                System.out.println("    Adaway...");
                currentTask.append("    Adaway...\n");
                getHostFiles(ADAWAY, source1);
                setProgress(16);

                System.out.println("    MVPS...");
                currentTask.append("    MVPS...\n");
                getHostFiles(MVPS, source2);
                setProgress(32);

                System.out.println("    hphosts...");
                currentTask.append("    hphosts...\n");
                getHostFiles(HPHOSTS, source3);
                setProgress(50);

                System.out.println("    yoyos...");
                currentTask.append("    yoyos...\n");
                getHostFiles(YOYOS, source4);
                setProgress(66);

                System.out.println("    someonewhocares...");
                currentTask.append("    someonewhocares...\n");
                getHostFiles(SOMEONEWHOCARES, source5);
                setProgress(82);

                System.out.println("    Malware...");
                currentTask.append("    Malware...\n");
                getHostFiles(MALWARE, source6);
                setProgress(98);
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Compiling List...");
            currentTask.append("Compiling List...\n");
            //Add ArrayLists to LinkedHashSet to remove duplicate entries while maintaining list order.
            Set<String> hashedList = new LinkedHashSet<>();
            hashedList.addAll(source1);
            hashedList.addAll(source2);
            hashedList.addAll(source3);
            hashedList.addAll(source4);
            hashedList.addAll(source5);
            hashedList.addAll(source6);

            //Put back list without duplicates into final compiled list.
            ArrayList<String> compiledList = new ArrayList<>();
            compiledList.addAll(hashedList);
            setProgress(99);
            return compiledList;
        }

        public void getHostFiles(String sourceName, ArrayList<String> sourceArray) {
            try {
                URL sourceURL = new URL(sourceName);
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

        public void writeHostsFile(ArrayList<String> compiledList) {
            //Credit to the sources
            final String SOURCES_CREDIT =
                    "# ==================================================================================\n" +
                            "# The following list was built from the these sources:\n\n" +
                            "# https://adaway.org/hosts.txt\n" +
                            "# http://winhelp2002.mvps.org/hosts.txt\n" +
                            "# http://hosts-file.net/ad_servers.asp\n" +
                            "# http://pgl.yoyo.org/adservers/serverlist.php?hostformat=hosts&showintro=0&mimetype=plaintext\n" +
                            "# http://someonewhocares.org/hosts/hosts\n" +
                            "# http://www.malwaredomainlist.com/hostslist/hosts.txt\n\n" +
                            "# All original comments are maintained. Duplicate entries are removed. \n\n" +
                            "# ==================================================================================\n\n";

            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("hosts"));
                bufferedWriter.write(SOURCES_CREDIT);
                System.out.println("Writing...");
                currentTask.append("Writing...\n");
                for (String aCompiledList : compiledList) {
                    bufferedWriter.write(aCompiledList + "\n");
                }
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        @Override
        public Void doInBackground() throws Exception {
            setProgress(0);
            writeHostsFile(generateList());
            return null;
        }

        @Override
        public void done() {
            start.setEnabled(true);
            setProgress(100);
            System.out.println("Done!");
            currentTask.append("Done!\n");
        }
    }

    /**
     * Listens for "Start" button to be pushed and starts the program.
     * @param ae actioncommand always "Start"
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        start.setEnabled(false);
        GetHosts getHosts = new GetHosts();
        getHosts.addPropertyChangeListener(this);
        getHosts.execute();
    }

    /**
     * Listens for changes in the progressbar
     * @param evt
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("progress")) {
            jProgressBar.setValue((Integer) evt.getNewValue());
        }
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }
        SwingUtilities.invokeLater(HostsGrabber::new);
    }
}