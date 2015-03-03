/**
 * Name:        Kwak, Victor
 * Description: Implementation of the Windows version of Notepad.
 */

import javax.swing.*;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Notepad extends JFrame implements ActionListener {
    private JTextArea textArea;
    private JCheckBoxMenuItem wordWrap;
    private String filename;
    private File file;
    private JPopupMenu popupMenu;
    private int newDocNum = 1;
    private JFileChooser fileChooser = new JFileChooser();
    private FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
    private JMenuItem cut;
    private JMenuItem copy;
    private JMenuItem delete;
    private JMenuItem popupCut;
    private JMenuItem popupCopy;
    private JMenuItem find;
    private JMenuItem findNext;
    private JMenuItem statusBar;
    private JDialog findDialog;
    private JTextField findTextField;
    private JButton findButton;
    private int findIndex;
    private JDialog font;

    JLabel sample;
    String[] allFonts;
    JList fontList;

    Notepad() {
        makeFrame();
        makeTextArea();
        makeMenu();
        makeFindDialog();
    }

    /**
     * Generates the frame for the program. The close operation is to check for any changes, and ask
     * the user of he wants to save if changes are detected.
     */
    private void makeFrame() {
        setLayout(new BorderLayout());
        setTitle("Untitled" + String.valueOf(newDocNum));
        setSize(800, 600);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (filename != null) {
                    if (checkChanges()) {
                        saveConfirmDialog("close");
                    }
                } else if (!textArea.getText().equals("")) {
                    saveConfirmDialog("close");
                } else {
                    System.exit(0);
                }
            }
        });
    }

    /**
     * Generates the menu for the program. Cut, Copy, and Delete are disabled until text is highlighted.
     * Find and Find Next are disabled if editor is empty.
     */
    private void makeMenu() {
        JMenuBar menuBar = new JMenuBar();

        //======= File ======
        JMenu file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        menuBar.add(file);

        JMenuItem newf = new JMenuItem("New", KeyEvent.VK_N);
        newf.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK + InputEvent.ALT_DOWN_MASK));
        newf.addActionListener(this);
        file.add(newf);

        JMenuItem open = new JMenuItem("Open...");
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        open.addActionListener(this);
        file.add(open);

        JMenuItem save = new JMenuItem("Save");
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        save.addActionListener(this);
        file.add(save);

        JMenuItem saveAs = new JMenuItem("Save As...");
        saveAs.addActionListener(this);
        file.add(saveAs);

        file.addSeparator();

        JMenuItem pageSetup = new JMenuItem("Page Setup...", KeyEvent.VK_U);
        pageSetup.addActionListener(this);
        file.add(pageSetup);

        JMenuItem print = new JMenuItem("Print...");
        print.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
        print.addActionListener(this);
        file.add(print);

        file.addSeparator();

        JMenuItem exit = new JMenuItem("Exit", KeyEvent.VK_E);
        exit.setMnemonic(KeyEvent.VK_X);
        exit.addActionListener(this);
        file.add(exit);

        //====== Edit ======
        JMenu edit = new JMenu("Edit");
        edit.setMnemonic(KeyEvent.VK_E);
        menuBar.add(edit);

//		JMenuItem undo = new JMenuItem("Undo");
//		undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK));
//		undo.setEnabled(false);
//		undo.addActionListener(this);
//		edit.add(undo);
//
//		edit.addSeparator();

        cut = new JMenuItem("Cut");
        cut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        cut.setEnabled(false);
        cut.addActionListener(this);
        edit.add(cut);

        copy = new JMenuItem("Copy");
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        copy.setEnabled(false);
        copy.addActionListener(this);
        edit.add(copy);


        JMenuItem paste = new JMenuItem("Paste");
        paste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        paste.addActionListener(this);
        edit.add(paste);

        delete = new JMenuItem("Delete");
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        delete.setEnabled(false);
        delete.addActionListener(this);
        edit.add(delete);

        edit.addSeparator();

        find = new JMenuItem("Find...");
        find.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
        find.setEnabled(false);
        find.addActionListener(this);
        edit.add(find);

        findNext = new JMenuItem("Find Next");
        findNext.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        findNext.setEnabled(false);
        findNext.addActionListener(this);
        edit.add(findNext);

//		JMenuItem replace = new JMenuItem("Replace...");
//		replace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
//		replace.addActionListener(this);
//		edit.add(replace);

//		JMenuItem goTo = new JMenuItem("Go To...");
//		goTo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK));
//		goTo.addActionListener(this);
//		edit.add(goTo);

        edit.addSeparator();

        JMenuItem selectAll = new JMenuItem("Select All");
        selectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_MASK));
        selectAll.addActionListener(this);
        edit.add(selectAll);

//		JMenuItem timeDate = new JMenuItem("Time/Date");
//		timeDate.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
//		timeDate.addActionListener(this);
//		edit.add(timeDate);

        //====== Format ======
        JMenu format = new JMenu("Format");
        format.setMnemonic(KeyEvent.VK_O);
        menuBar.add(format);


        wordWrap = new JCheckBoxMenuItem("Word Wrap", false);
        wordWrap.setMnemonic(KeyEvent.VK_W);
        wordWrap.addActionListener(this);
        format.add(wordWrap);

        JMenuItem font = new JMenuItem("Font", KeyEvent.VK_F);
        font.addActionListener(this);
        format.add(font);

        //====== View ======
        JMenu view = new JMenu("View");
        view.setMnemonic(KeyEvent.VK_V);
        menuBar.add(view);

        statusBar = new JMenuItem("Status Bar", KeyEvent.VK_S);
        statusBar.addActionListener(this);
        view.add(statusBar);

        //====== Help ======
        JMenu help = new JMenu("Help");
        help.setMnemonic(KeyEvent.VK_H);
        menuBar.add(help);

//		JMenuItem viewHelp = new JMenuItem("View Help", KeyEvent.VK_H);
//		viewHelp.addActionListener(this);
//		help.add(viewHelp);
//
//		help.addSeparator();

        JMenuItem aboutNotePad = new JMenuItem("About Notepad");
        aboutNotePad.addActionListener(this);
        help.add(aboutNotePad);

        setJMenuBar(menuBar);

        //====== Pop-up Menu ======
        popupMenu = new JPopupMenu();

        popupCut = new JMenuItem("Cut");
        popupCut.addActionListener(this);
        popupMenu.add(popupCut);

        popupCopy = new JMenuItem("Copy");
        popupCopy.addActionListener(this);
        popupMenu.add(popupCopy);

        JMenuItem popupPaste = new JMenuItem("Paste");
        popupPaste.addActionListener(this);
        popupMenu.add(popupPaste);

        PopupListener popupListener = new PopupListener();
        textArea.addMouseListener(popupListener);
    }

    /**
     * Generates the editor portion of the program.
     */
    private void makeTextArea() {
        textArea = new JTextArea();
        textArea.setLineWrap(false);
        textArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        textArea.addCaretListener(e -> {
            int dot = e.getDot();
            int mark = e.getMark();

            findIndex = textArea.getCaretPosition();

            if (dot == mark) {
                cut.setEnabled(false);
                popupCut.setEnabled(false);
                copy.setEnabled(false);
                popupCopy.setEnabled(false);
                delete.setEnabled(false);
            } else {
                cut.setEnabled(true);
                popupCut.setEnabled(true);
                copy.setEnabled(true);
                popupCopy.setEnabled(true);
                delete.setEnabled(true);
            }

            if (!textArea.getText().equals("")) {
                find.setEnabled(true);
                findNext.setEnabled(true);
            } else {
                find.setEnabled(false);
                findNext.setEnabled(false);
            }
        });
    }

    private void makeFindDialog() {
        findDialog = new JDialog(this, "Find", Dialog.ModalityType.MODELESS);
        findDialog.setLayout(new FlowLayout());
        findDialog.setSize(450, 150);
        findDialog.setDefaultCloseOperation(HIDE_ON_CLOSE);

        JPanel find1 = new JPanel();
        find1.setSize(400, 75);
        JLabel findLabel = new JLabel("Find what:    ");
        find1.add(findLabel);

        findTextField = new JTextField(20);
        find1.add(findTextField);
        findDialog.add(find1);

        JPanel find2 = new JPanel();
        find2.setLayout(new GridLayout(2, 1));
        find2.setSize(50, 150);

        findButton = new JButton("Find Next");
        findButton.setEnabled(false);
        find2.add(findButton);
        findButton.addActionListener(this);

        JButton cancelButton = new JButton("Cancel");
        find2.add(cancelButton);
        cancelButton.addActionListener(this);
        findDialog.add(find2);

        JPanel find3 = new JPanel();
        find3.setSize(400, 75);
        find3.setLayout(new BorderLayout());
        JCheckBox matchCase = new JCheckBox("Match Case");
        find3.add(matchCase, BorderLayout.WEST);
        find3.add(new JLabel("                        "), BorderLayout.CENTER);

        findDialog.add(find3);

        JPanel find4 = new JPanel();
        find4.setBorder(new TitledBorder(BorderFactory.createTitledBorder("Direction")));
        ButtonGroup directionGroup = new ButtonGroup();
        JRadioButton up = new JRadioButton("Up");
        JRadioButton down = new JRadioButton("Down");
        directionGroup.add(up);
        directionGroup.add(down);
        down.setSelected(true);
        find4.add(up);
        find4.add(down);
        find3.add(find4, BorderLayout.EAST);

        findTextField.addCaretListener(e -> {
            if (findTextField.getText().equals("")) {
                findButton.setEnabled(false);
            } else {
                findButton.setEnabled(true);
            }
        });

    }

    private void find(int start) {
        String currentText = textArea.getText();
        String textToFind = findTextField.getText();

        int index = currentText.indexOf(textToFind, start);

        if (index > -1) {
            textArea.setCaretPosition(index);
            findIndex = index;
        }

        textArea.requestFocusInWindow();

    }

    /**
     * Checks to see whether or not to append the filename with .txt
     *
     * @param name the filename to be checked.
     */
    private void checkFileName(String name) {
        if (name.contains(".txt")) {
            filename = name;
        } else {
            filename = name + ".txt";
        }
    }

    /**
     * Checks to see if any changes were made to the document.
     *
     * @return
     */
    private boolean checkChanges() {
        try {
            file = new File(filename);
            FileReader fileReader = new FileReader(file);
            JTextArea test = new JTextArea();
            test.read(fileReader, null);
            fileReader.close();
            return !textArea.getText().equals(test.getText());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Shows the user the option to save or not.
     *
     * @param option "new" or "open" or "close" for the appropriate actions.
     */
    private void saveConfirmDialog(String option) {
        int response = JOptionPane.showConfirmDialog(this, "Do you want to save " + getTitle() + "?");
        switch (response) {
            case JOptionPane.YES_OPTION:
                if (filename == null) {
                    saveAs();
                } else {
                    save();
                }
                switch (option) {
                    case "new":
                        newFile();
                        break;
                    case "open":
                        openFile();
                        break;
                    case "close":
                        System.exit(0);
                }
                break;
            case JOptionPane.NO_OPTION:
                switch (option) {
                    case "new":
                        newFile();
                        break;
                    case "open":
                        openFile();
                        break;
                    case "close":
                        System.exit(0);
                }
                break;
            case JOptionPane.CANCEL_OPTION:
                break;
            case JOptionPane.CLOSED_OPTION:
                break;
        }
    }

    /**
     * Creates a new file, keeping track of the number of new documents created. If there are
     * any differences between the saved file and the current text in the editor, it will
     * ask the user if they want to save.
     */
    private void newFile() {
        textArea.setText("");
        newDocNum++;
        setTitle("Untitled" + String.valueOf(newDocNum));
        filename = null;
    }

    /**
     * Opens a text file.
     */
    private void openFile() {
        int returnVal = fileChooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            checkFileName(String.valueOf(fileChooser.getSelectedFile()));
            try {
                textArea.setText("");
                file = new File(filename);
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String currentLine;
                while ((currentLine = bufferedReader.readLine()) != null) {
                    textArea.append(currentLine);
                }
                fileReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setTitle(filename);
    }

    /**
     * Saves the document to the current directory/filename.
     */
    private void save() {
        try {
            FileWriter save = new FileWriter(file);
            save.write(textArea.getText());
            save.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the user the Save As screen, and saves the file to the specified directory/filename.
     */
    private void saveAs() {
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showSaveDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            checkFileName(fileChooser.getSelectedFile().getName());
            filename = String.valueOf(fileChooser.getCurrentDirectory()) + "/" + filename;
            try {
                file = new File(filename);
                FileWriter save = new FileWriter(file);
                save.write(textArea.getText());
                save.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setTitle(filename);
        }
    }

    /**
     * Handles the events within the program.
     *
     * @param e the action being performed.
     */
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            //====== File ======
            case "New":
                if (filename != null) {
                    if (checkChanges()) {
                        saveConfirmDialog("new");
                    }
                } else if (!textArea.getText().equals("")) {
                    saveConfirmDialog("new");
                } else {
                    newFile();
                }
                break;
            case "Open...":
                if (filename != null) {
                    if (checkChanges()) {
                        saveConfirmDialog("open");
                    }
                } else if (!textArea.getText().equals("")) {
                    saveConfirmDialog("open");
                } else {
                    openFile();
                }
                break;
            case "Save":
                if (filename == null) {
                    saveAs();
                } else {
                    save();
                }
                break;
            case "Save As...":
                saveAs();
                break;
            case "Page Setup...":
                break;
            case "Print...":
                break;
            case "Exit":
                System.exit(0);
                break;

            //====== Edit ======
//			case "Undo":
//				break;
            case "Cut":
                textArea.cut();
                break;
            case "Copy":
                textArea.copy();
                break;
            case "Paste":
                textArea.paste();
                break;
            case "Delete":
                textArea.replaceSelection("");
                break;
            case "Find...":
                findDialog.setVisible(true);
                break;
            case "Find Next":
                find(findIndex + 1);
                break;
//			case "Replace...":
//				break;
//			case "Go To...":
//				break;
            case "Select All":
                textArea.selectAll();
                break;
//			case "Time/Date":
//				break;

            //====== Format ======
            case "Word Wrap":
                if (wordWrap.isSelected()) {
                    textArea.setLineWrap(true);
                    statusBar.setEnabled(false);
                } else {
                    textArea.setLineWrap(false);
                    statusBar.setEnabled(true);
                }
                break;
            case "Font...":
                fontWindow();
                break;

            //====== View ======
            case "Status Bar":

                break;

            //====== Help ======
            case "View Help":
                break;
            case "About Notepad":
                aboutWindow();
                break;

            //====== Find ======
            case "Cancel":
                findDialog.setVisible(false);
        }
    }

    private void fontWindow() {
        font = new JDialog(this, "Fonts");
        font.setSize(100, 100);
        font.setDefaultCloseOperation(HIDE_ON_CLOSE);
        font.setEnabled(true);
        allFonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        fontList = new JList(allFonts);

        fontList.addListSelectionListener(le -> {
            int idx = fontList.getSelectedIndex();
            if (idx != 1) {
                sample.setFont(new Font(allFonts[idx], Font.PLAIN, 24));
            }
        });
        font.setVisible(true);
        font.add(new JScrollPane(fontList));
    }

    private void aboutWindow() {
        JDialog about = new JDialog(this, "About");
        about.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        about.setSize(150, 100);
        about.setEnabled(true);
        about.setVisible(true);
        JLabel aboutLabel = new JLabel("<html>Notepad<br>" +
                "(c) Victor Kwak");
        about.add(aboutLabel);
        add(about);
    }

    /**
     * Handles pop up menu for the cut, copy, and paste functions.
     */
    private class PopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                popupMenu.show(e.getComponent(),
                        e.getX(), e.getY());
            }
        }
    }

    /**
     * Main function. Uses the Nimbus Look and Feel.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
        }
        SwingUtilities.invokeLater(Notepad::new);
    }
}
