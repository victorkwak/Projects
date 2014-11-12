import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Victor Kwak - 2014
 * <p>
 * Grabs hosts files from multiples sources and compiles into one list while removing duplicate entries.
 * Currently, the program can automatically update the system's hosts file automatically only on OS X systems.
 */
public class HostsGrabber extends JFrame implements ActionListener, PropertyChangeListener {
    private JProgressBar jProgressBar;
    private JTextArea currentTask;
    private JButton start;
    private int progress = 0;
    private String os;

    //Maintained hosts file lists
    private final String[] HOSTS_SOURCES = {
            "https://adaway.org/hosts.txt",
            "http://winhelp2002.mvps.org/hosts.txt",
            "http://hosts-file.net/ad_servers.asp",
            "http://pgl.yoyo.org/adservers/serverlist.php?hostformat=hosts&showintro=0&mimetype=plaintext",
            "http://someonewhocares.org/hosts/hosts",
            "http://www.malwaredomainlist.com/hostslist/hosts.txt"};

    //Lists used in Firefox's Adblock extension. These aren't in hosts file format.
    private final String[] ADBLOCK_SOURCES = {
            "http://www.fanboy.co.nz/fanboy-korean.txt",
            "https://easylist-downloads.adblockplus.org/easylist_noelemhide.txt"};

    /**
     * Determines OS and builds GUI
     */
    public HostsGrabber() {
        os = System.getProperty("os.name");
        if (System.getProperty("os.name").contains("Mac")) {
            os = "Mac";
        } else if (System.getProperty("os.name").contains("Windows")) {
            os = "Windows";
        }

        setTitle("HostsGrabber - " + os);
        setLayout(new FlowLayout());
        setSize(400, 250);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel instructions = new JLabel("      Press \"Start\" to begin.");
        add(instructions);

        start = new JButton("Start");
        start.addActionListener(this);
        add(start);

        jProgressBar = new JProgressBar(0, 100);
        Dimension progressBarSize = jProgressBar.getPreferredSize();
        progressBarSize.width = 250;
        jProgressBar.setPreferredSize(progressBarSize);
        jProgressBar.setValue(0);
        add(jProgressBar);

        currentTask = new JTextArea(8, 30);
        currentTask.setMargin(new Insets(5, 5, 5, 5));
        currentTask.setEditable(false);
        DefaultCaret defaultCaret = (DefaultCaret) currentTask.getCaret();
        defaultCaret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        add(new JScrollPane(currentTask));

        setVisible(true);

        JRootPane jRootPane = this.getRootPane();
        jRootPane.setDefaultButton(start);
    }

    /**
     * Nested class provides main function of the program. Will run in a background thread.
     */
    class GetHosts extends SwingWorker<Void, Void> {

        public Set<String> generateList() {
            Set<String> list = new LinkedHashSet<>();

            int numberOfSources = HOSTS_SOURCES.length + ADBLOCK_SOURCES.length;

            hostsList(list, HOSTS_SOURCES, numberOfSources);
            adBlockList(list, ADBLOCK_SOURCES, numberOfSources);
            setProgress(99);
            return list;
        }

        /**
         * @param list            HashSet. Same one used for overall list.
         * @param SOURCES         These sources are already in hosts file format and can be used directly.
         * @param numberOfSources The sum of all regular hosts and AdBlock sources. Used for progressbar incrementation.
         */
        public void hostsList(Set<String> list, String[] SOURCES, int numberOfSources) {
            System.out.println("Getting hosts files...");
            currentTask.append("Getting hosts files...\n");
            try {
                for (String e : SOURCES) {
                    System.out.print("    " + e + "...");
                    currentTask.append("    " + e + "...");
                    URL sourceURL = new URL(e);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sourceURL.openStream()));
                    String currentLine;
                    while ((currentLine = bufferedReader.readLine()) != null) {
                        list.add(currentLine);
                    }
                    System.out.print(" Done\n");
                    currentTask.append(" Done\n");
                    progress += (100 / numberOfSources) - 1;
                    setProgress(progress);
                    bufferedReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Lists made for AdBlock extensions use lots of regular expressions, something that hosts files
         * don't support. They still contain information usable by a hosts file (ad-server addresses) but
         * they must be filtered out and changed into proper format. The lists also lack uniformity and
         * so separate rules for different lists must be considered.
         *
         * @param list            HashSet. Same one used for overall list.
         * @param SOURCES         Array of AdBlock sources
         * @param numberOfSources The sum of all regular hosts and AdBlock sources. Used for progressbar incrementation.
         */
        public void adBlockList(Set<String> list, String[] SOURCES, int numberOfSources) {
            System.out.println("Getting AdBlock lists...");
            currentTask.append("Getting AdBlock lists...\n");
            try {
                for (String e : SOURCES) {
                    boolean useSection = false;
                    System.out.print("    " + e + "...");
                    currentTask.append("    " + e + "...");
                    list.add("# " + e);
                    URL sourceURL = new URL(e);
                    // Server would not accept a non-browser connection.
                    // Must use addRequestProperty from HttpURLConnection.
                    HttpURLConnection httpSource = (HttpURLConnection) sourceURL.openConnection();
                    httpSource.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
                    httpSource.connect();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpSource.getInputStream()));
                    String currentLine;
                    Pattern pattern = Pattern.compile("\\d");
                    Matcher matcher;
                    while ((currentLine = bufferedReader.readLine()) != null) {
                        if (currentLine.startsWith("!")) {
                            //test merge/compare
                            if (currentLine.contains("License") ||
                                    currentLine.contains("Licence") ||
                                    currentLine.contains("Title") ||
                                    currentLine.contains("Updated")) {
                                list.add("# " + currentLine.substring(2));
                            } else if (currentLine.contains("General blocking rules") ||
                                    currentLine.contains("3rd party blocking rules") ||
                                    currentLine.contains("Third-party advertisers")) {
                                list.add("# " + currentLine.substring(1));
                                useSection = true;
                            } else if (currentLine.contains("1st party") ||
                                    currentLine.contains("Third-party advert") ||
                                    currentLine.contains("Korean Trackers")) {
                                useSection = false;
                            }
                        }

                        if (useSection && currentLine.startsWith("||")) {

                            if (currentLine.contains("^")) {
                                String temp = currentLine.substring(2, currentLine.indexOf("^"));
                                if (temp.contains("*")) {
                                    temp = temp.substring(0, temp.indexOf("*"));
                                    if (temp.contains("/")) {
                                        temp = temp.substring(0, temp.indexOf("/"));
                                    }
                                } else if (temp.contains("/")) {
                                    temp = temp.substring(0, temp.indexOf("/"));
                                    if (temp.contains("*")) {
                                        temp = temp.substring(0, temp.indexOf("*"));
                                    }
                                }
                                if (temp.contains(".")) {
                                    list.add("127.0.0.1 " + temp);
                                }
                            } else if (currentLine.contains("*")) {
                                String temp = currentLine.substring(2, currentLine.indexOf("*"));
                                if (temp.contains("^")) {
                                    temp = temp.substring(0, temp.indexOf("^"));
                                    if (temp.contains("/")) {
                                        temp = temp.substring(0, temp.indexOf("/"));
                                    }
                                } else if (temp.contains("/")) {
                                    temp = temp.substring(0, temp.indexOf("/"));
                                    if (temp.contains("^")) {
                                        temp = temp.substring(0, temp.indexOf("^"));
                                    }
                                }
                                if (temp.contains(".")) {
                                    list.add("127.0.0.1 " + temp);
                                }
                            } else if (currentLine.contains("/")) {
                                String temp = currentLine.substring(2, currentLine.indexOf("/"));
                                if (temp.contains("*")) {
                                    temp = temp.substring(0, temp.indexOf("*"));
                                    if (temp.contains("^")) {
                                        temp = temp.substring(0, temp.indexOf("^"));
                                    }
                                } else if (temp.contains("^")) {
                                    temp = temp.substring(0, temp.indexOf("^"));
                                    if (temp.contains("*")) {
                                        temp = temp.substring(0, temp.indexOf("*"));
                                    }
                                }
                                if (temp.contains(".")) {
                                    list.add("127.0.0.1 " + temp);
                                }
                            } else {
                                list.add("127.0.0.1 " + currentLine.substring(2));
                            }
                        }
                    }
                    System.out.print(" Done\n");
                    currentTask.append(" Done\n");
                    progress += (100 / numberOfSources) - 1;
                    setProgress(progress);
                    httpSource.disconnect();
                    bufferedReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public String generateComments() {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            String comments = "# ==================================================================================\n" +
                    "# The following list was built from the these sources on " +
                    simpleDateFormat.format(Calendar.getInstance().getTime()) + ":\n\n";
            for (String e : HOSTS_SOURCES) {
                comments += "# " + e + "\n";
            }
            for (String e : ADBLOCK_SOURCES) {
                comments += "# " + e + "\n";
            }
            comments += "\n" +
                    "# ==================================================================================\n\n";
            return comments;
        }

        public void writeHostsFile(Set<String> compiledList) {
            Path currentPath = Paths.get("");
            String absolutePath = currentPath.toAbsolutePath().toString();

            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("hosts"));
                bufferedWriter.write(generateComments());
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
//                        Runtime.getRuntime().exec("rm " + hostsPathMac);
                        System.out.println("Flushing DNS cache");
                        currentTask.append("Flushing DNS cache\n");
                        Runtime.getRuntime().exec("sudo killall -HUP mDNSResponder");
                        Runtime.getRuntime().exec("sudo dscacheutil -flushcache");
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