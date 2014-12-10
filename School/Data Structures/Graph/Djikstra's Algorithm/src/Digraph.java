import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Victor Kwak
 * December 01, 2014
 * Graph for use with Djikstra's algorithm project. Reads "city.dat" and "road.dat" and generates a graph.
 */
public class Digraph {
    private int[] nums;
    private String[] cities;
    private String[] cityCodes;
    private int[] populations;
    private int[] elevations;
    private double[][] graph;

    Digraph() {
        readData();
    }

    public void readData() {
        try {
            String readCity = "";
            String readRoad = "";
            String current;

            // city.dat - Number, City_Code(2 letters), Full_City_Name, Population, Elevation. Sorted by the city_dat code.
            BufferedReader bufferedReader = new BufferedReader(new FileReader("city.dat"));
            while ((current = bufferedReader.readLine()) != null && !current.equals("")) {
                readCity += current.trim() + "\n";
            }
            readCity = readCity.trim();
            String[] cityDat = readCity.substring(0, readCity.length()).split("\n");

            // road.dat - From_City, To_City, Distance. City is numbered in the order given in city_dat.dat file.
            bufferedReader = new BufferedReader(new FileReader("road.dat"));
            while ((current = bufferedReader.readLine()) != null && !current.equals("")) {
                readRoad += current.trim() + "\n";
            }
            readRoad = readRoad.trim();
            String[] roadDat = readRoad.substring(0, readRoad.length()).split("\n");

            bufferedReader.close();

            nums = new int[cityDat.length];
            cities = new String[cityDat.length];
            cityCodes = new String[cityDat.length];
            populations = new int[cityDat.length];
            elevations = new int[cityDat.length];

            for (int i = 0; i < cityDat.length; i++) {
                String[] temp = cityDat[i].split("\\s{2,}");
                nums[i] = Integer.parseInt(temp[0]);
                cityCodes[i] = temp[1];
                cities[i] = temp[2];
                populations[i] = Integer.parseInt(temp[3]);
                elevations[i] = Integer.parseInt(temp[4]);
            }

            graph = new double[cityDat.length][cityDat.length];
            for (int i = 0; i < graph.length; i++) {
                for (int j = 0; j < graph[i].length; j++) {
                    if (i == j) {
                        graph[i][j] = 0;
                    } else {
                        graph[i][j] = Double.POSITIVE_INFINITY;
                    }
                }
            }

            for (String aRoadDat : roadDat) {
                String[] temp = aRoadDat.split("\\s+");
                graph[Integer.parseInt(temp[0]) - 1][Integer.parseInt(temp[1]) - 1] = Integer.parseInt(temp[2]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double[][] getGraph() {
        return graph;
    }

    public int getNum(int i) {
        return nums[i];
    }

    public String getCity(int i) {
        return cities[i];
    }

    public String getCityCode(int i) {
        return cityCodes[i];
    }

    public int getPopulation(int i) {
        return populations[i];
    }

    public int getElevation(int i) {
        return elevations[i];
    }

    public int getIndex(String cityCode) {
        for (int i = 0; i < cityCodes.length; i++) {
            if (cityCodes[i].equals(cityCode)) {
                return i;
            }
        }
        return -1;
    }

    public void printGraph() {
        DecimalFormat decimalFormat = new DecimalFormat("0");
        System.out.print("\t");
        for (String e : cityCodes) {
            System.out.print(e + "\t");
        }
        System.out.println();
        for (int i = 0; i < graph.length; i++) {
            System.out.print(cityCodes[i] + "\t");
            for (int j = 0; j < graph[i].length; j++) {
                if (graph[i][j] == Double.POSITIVE_INFINITY) {
                    System.out.print("Inf\t");
                } else {
                    System.out.print(decimalFormat.format(graph[i][j]) + "\t");
                }
            }
            System.out.println();
        }
    }
}