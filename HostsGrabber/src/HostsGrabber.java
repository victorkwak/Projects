import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Victor Kwak
 * Description: Grabs hosts files from multiples sources and compiles into one list while removing duplicate entries.
 * Currently, the program can automatically update the system's hosts file automatically only on OS X systems.
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
    }

    /**
     * Nested class provides main function of the program. Will run in a background thread.
     */
    class GetHosts extends SwingWorker<Void, Void> {

        public Set<String> generateList() {

            final String[] SOURCES = {"https://adaway.org/hosts.txt",
                    "http://winhelp2002.mvps.org/hosts.txt",
                    "http://hosts-file.net/ad_servers.asp",
                    "http://pgl.yoyo.org/adservers/serverlist.php?hostformat=hosts&showintro=0&mimetype=plaintext",
                    "http://someonewhocares.org/hosts/hosts",
                    "http://www.malwaredomainlist.com/hostslist/hosts.txt"};

            Set<String> compiledList = new LinkedHashSet<>();

            System.out.println("Compiling from sources...");
            currentTask.append("Compiling from sources...\n");
            int progress = 0;
            try {
                for (String e : SOURCES) {
                    System.out.print("    " + e + "...");
                    currentTask.append("    " + e + "...");
                    URL sourceURL = new URL(e);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(sourceURL.openStream()));
                    String currentLine;
                    while ((currentLine = bufferedReader.readLine()) != null) {
                        compiledList.add(currentLine);
                    }
                    System.out.print(" Done\n");
                    currentTask.append(" Done\n");
                    progress += (100 / SOURCES.length) - 1;
                    setProgress(progress);
                    bufferedReader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            setProgress(99);
            return compiledList;
        }

        public void writeHostsFile(Set<String> compiledList) {
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