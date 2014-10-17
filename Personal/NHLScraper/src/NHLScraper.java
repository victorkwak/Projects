import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Victor Kwak
 * <p>
 * While taking intro Microeconomics, the professor asked a favor of the class: he needs data for a paper he is working
 * on and needs data from the NHL website on specific playerNames. In exchange, he will give students that participate
 * extra credit on the midterm. He will provide a list of names, and we are to copy and paste those playerNames'
 * information to an Excel spreadsheet. I thought I would try and write a program to do this, essentially replacing
 * like a hundred people in this data-gathering process while doing a better job.
 * <p>
 * October 12, 2014
 */

public class NHLScraper {
    private static InputStream inputStream;
    private static DirectoryEntry root;
    private static POIFSFileSystem fs;
    private static ArrayList<String> playerNames = new ArrayList<>();
    private static ArrayList<String> playerStatsURLs = new ArrayList<>();
    private static ArrayList<String> playersToFind = new ArrayList<>();

    // Apparently, the NHL.com's big-assed list of all players doesn't contain the players in the extra credit list. WTF?
    // These links are essentially useless...
    private static final String mainStatsPage = "http://www.nhl.com/ice/playerstats.htm";
    private static final String iStatsPage = "http://www.nhl.com/ice/playerstats.htm?fetchKey=20152ALLSASAll&viewName=summary&sort=points&pg=";

    public static void main(String[] args) {
        readTextFile("players");
        playersToFind.forEach(NHLScraper::searchForPlayer);
    }

    public static void searchForPlayer(String name) {
        String[] namesSplit = name.split(" ");
        String url = "http://www.nhl.com/ice/search.htm?q=";
        for (int i = 0; i < namesSplit.length; i++) {
            if (i < namesSplit.length - 1) {
                url += namesSplit[i];
                url += "+";
            } else {
                url += namesSplit[i];
            }
        }
        url += "&tab=players";
        try {
            Document document = Jsoup.connect(url).get();
            Elements position = document.select("span[style=\"color: #666;\"");
            Pattern posPattern = Pattern.compile(";\">\\w+\\s?\\w*");
            String[] g1Split = position.toString().split("\n");
            System.out.println(name);
            for (String e : g1Split) {
                System.out.println(e);
            }
            System.out.println();
//            for (String e : g1Split) {
//                Matcher posMatcher = posPattern.matcher(e);
//                if (posMatcher.find()) {
//                    System.out.println(name + " : " + posMatcher.group().substring(3));
//                }
//            }
        } catch (IOException e) {
            System.out.println("Cant connect");
        }
    }

    /**
     * Reads a given file for names to search for.
     *
     * @param filename file to read
     */
    public static void readTextFile(String filename) {
        playersToFind = new ArrayList<>();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
            String currentLine;
            while ((currentLine = bufferedReader.readLine()) != null) {
                playersToFind.add(currentLine.trim());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Turns out this method isn't needed since the main stats page doesn't contain all searchable players.
     *
     * @param url Main stats page
     * @return Max value of index (for iteration)
     */
    public static int getNumOfURL(String url) {
        int num = 0;
        try {
            Document document = Jsoup.connect(url).get();
            Elements index = document.select("a[href*=pg=");
            String[] indexArr = index.toString().split("\n");
            Pattern last = Pattern.compile("pg=\\d+.+Last");
            Pattern number = Pattern.compile("\\d+");
            for (String e : indexArr) {
                Matcher matcher = last.matcher(e);
                if (matcher.find()) {
                    matcher = number.matcher(matcher.group());
                    if (matcher.find()) {
                        return Integer.parseInt(matcher.group());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Cannot connect");
        }
        return num;
    }

    /**
     * Not need because main stats page doesn't contain all searchable players.
     *
     * @param url
     */
    public static void getPlayerID(String url) {
        try {
            Document document = Jsoup.connect(url).get();
            Elements html = document.select("a[href*=/ice/player.htm]");
            String[] data = html.toString().split("\n");
            for (String e : data) {
                Pattern playerID = Pattern.compile("\\d+");
                Matcher mPlayerID = playerID.matcher(e);
                Pattern playerName = Pattern.compile("\\w+[\\w\\.'-]+\\s\\w+[\\w\\.'-]+");
                Matcher mPlayerName = playerName.matcher(e);
                if (mPlayerID.find() && mPlayerName.find()) {
                    playerNames.add(mPlayerName.group());
                    playerStatsURLs.add(mPlayerID.group());
                }
            }
        } catch (IOException e) {
            System.out.println("Could not get " + url);
        }
    }

    /**
     * Reads from an Excel spreadsheet
     *
     * @param fileName
     */
    public static void readFile(File fileName) {
        try {
            inputStream = new FileInputStream("NHLsample22c.xlsx");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        fs = null;
        try {
            fs = new POIFSFileSystem(inputStream);
        } catch (IOException e) {
            System.out.println("WTF?");
        }

        if (fs != null) {
            root = fs.getRoot();
        }
    }

    /**
     * Sample code from Apache's website to illustrate how to use API
     */
    public void writeNew() {
        // create a new file
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("workbook.xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // create a new workbook
        Workbook wb = new HSSFWorkbook();
        // create a new sheet
        Sheet s = wb.createSheet();
        // declare a row object reference
        Row r = null;
        // declare a cell object reference
        Cell c = null;
        // create 3 cell styles
        CellStyle cs = wb.createCellStyle();
        CellStyle cs2 = wb.createCellStyle();
        CellStyle cs3 = wb.createCellStyle();
        DataFormat df = wb.createDataFormat();
        // create 2 fonts objects
        Font f = wb.createFont();
        Font f2 = wb.createFont();

        //set font 1 to 12 point type
        f.setFontHeightInPoints((short) 12);
        //make it blue
        f.setColor((short) 0xc);
        // make it bold
        //arial is the default font
        f.setBoldweight(Font.BOLDWEIGHT_BOLD);

        //set font 2 to 10 point type
        f2.setFontHeightInPoints((short) 10);
        //make it red
        f2.setColor((short) Font.COLOR_RED);
        //make it bold
        f2.setBoldweight(Font.BOLDWEIGHT_BOLD);

        f2.setStrikeout(true);

        //set cell stlye
        cs.setFont(f);
        //set the cell format
        cs.setDataFormat(df.getFormat("#,##0.0"));

        //set a thin border
        cs2.setBorderBottom(cs2.BORDER_THIN);
        //fill w fg fill color
        cs2.setFillPattern((short) CellStyle.SOLID_FOREGROUND);
        //set the cell format to text see DataFormat for a full list
        cs2.setDataFormat(HSSFDataFormat.getBuiltinFormat("text"));

        // set the font
        cs2.setFont(f2);

        // set the sheet name in Unicode
        wb.setSheetName(0, "\u0422\u0435\u0441\u0442\u043E\u0432\u0430\u044F " +
                "\u0421\u0442\u0440\u0430\u043D\u0438\u0447\u043A\u0430");
        // in case of plain ascii
        // wb.setSheetName(0, "HSSF Test");
        // create a sheet with 30 rows (0-29)
        int rownum;
        for (rownum = (short) 0; rownum < 30; rownum++) {
            // create a row
            r = s.createRow(rownum);
            // on every other row
            if ((rownum % 2) == 0) {
                // make the row height bigger  (in twips - 1/20 of a point)
                r.setHeight((short) 0x249);
            }

            //r.setRowNum(( short ) rownum);
            // create 10 cells (0-9) (the += 2 becomes apparent later
            for (short cellnum = (short) 0; cellnum < 10; cellnum += 2) {
                // create a numeric cell
                c = r.createCell(cellnum);
                // do some goofy math to demonstrate decimals
                c.setCellValue(rownum * 10000 + cellnum
                        + (((double) rownum / 1000)
                        + ((double) cellnum / 10000)));

                String cellValue;

                // create a string cell (see why += 2 in the
                c = r.createCell((short) (cellnum + 1));

                // on every other row
                if ((rownum % 2) == 0) {
                    // set this cell to the first cell style we defined
                    c.setCellStyle(cs);
                    // set the cell's string value to "Test"
                    c.setCellValue("Test");
                } else {
                    c.setCellStyle(cs2);
                    // set the cell's string value to "\u0422\u0435\u0441\u0442"
                    c.setCellValue("\u0422\u0435\u0441\u0442");
                }


                // make this column a bit wider
                s.setColumnWidth((short) (cellnum + 1), (short) ((50 * 8) / ((double) 1 / 20)));
            }
        }

        //draw a thick black border on the row at the bottom using BLANKS
        // advance 2 rows
        rownum++;
        rownum++;

        r = s.createRow(rownum);

        // define the third style to be the default
        // except with a thick black border at the bottom
        cs3.setBorderBottom(CellStyle.BORDER_THICK);

        //create 50 cells
        for (short cellnum = (short) 0; cellnum < 50; cellnum++) {
            //create a blank type cell (no value)
            c = r.createCell(cellnum);
            // set it to the thick black border style
            c.setCellStyle(cs3);
        }

        //end draw thick black border


        // demonstrate adding/naming and deleting a sheet
        // create a sheet, set its title then delete it
        s = wb.createSheet();
        wb.setSheetName(1, "DeletedSheet");
        wb.removeSheetAt(1);
        //end deleted sheet

        // write the workbook to the output stream
        // close our file (don't blow out our file handles
        try {
            wb.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
