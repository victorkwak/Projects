import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.text.DefaultCaret;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Victor Kwak
 * Description: Grabs hosts files from multiples sources and compiles into one list while removing duplicate entries.
 *              Currently, the program can automatically update the system's hosts file automatically only on OS X systems.
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
        setSize(350, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel instructions = new JLabel("      Press \"Start\" to begin.");
        add(instructions);

        start = new JButton("Start");
        start.addActionListener(this);
        add(start);

        jProgressBar = new JProgressBar(0, 100);
        jProgressBar.setValue(0);
        add(jProgressBar);

        currentTask = new JTextArea(5, 25);
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
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            Path currentPath = Paths.get("");
            String absolutePath = currentPath.toAbsolutePath().toString();

            //Credit to the sources
            final String SOURCES_CREDIT =
                    "# ==================================================================================\n" +
                            "# The following list was built from the these sources on " +
                            simpleDateFormat.format(Calendar.getInstance().getTime()) + ":\n\n" +
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
                for (String aCompiledList : compiledList) {
                    bufferedWriter.write(aCompiledList + "\n");
                }
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                //If OS X machine
                if (System.getProperty("os.name").contains("Mac")) {
                    Path hostsPathMac = Paths.get(absolutePath + "/hosts");
                    Path privateEtc = Paths.get("/private/etc");
                    if (Files.isReadable(hostsPathMac)) {
                        System.out.println("Copying hosts file to the System...");
                        currentTask.append("Copying hosts file to the System...\n");
                        Runtime.getRuntime().exec("cp " + hostsPathMac + " " + privateEtc);
                        Runtime.getRuntime().exec("rm " + hostsPathMac);
                        System.out.println("Flushing DNS cache");
                        currentTask.append("Flushing DNS cache\n");
                        Runtime.getRuntime().exec("sudo killall -HUP mDNSResponder");
                    }
                }
                //If Windows machine
//                else if (System.getProperty("os.name").contains("Windows")) {
//                    Path hostsPathWindows = Paths.get(absolutePath + "\\hosts");
//                    Path windowsHosts = Paths.get("C:\\Windows\\System32\\Drivers\\etc\\hosts");
//                    if (Files.isReadable(hostsPathWindows)) {
//                        Runtime.getRuntime().exec("cmd.exe /c Copy /y \"" + hostsPathWindows + "\" \"" + windowsHosts + "\"");
//                        System.out.println("Copying hosts file to " + windowsHosts);
//                        currentTask.append("Copying hosts file to " + windowsHosts + "\n");
//                    }
//                }
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
     *
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
     * Used for updating progress bar and statements.
     *
     * @param evt changes in getHosts object
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
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(HostsGrabber::new);
    }
}