import java.util.ArrayList;
import java.util.Scanner;

/**
 * Victor Kwak
 * Djikstra's Algorithm
 */
public class Djikstra {
    private static Digraph digraph = new Digraph();
    private static double[][] graph = digraph.getGraph();
    private static double[] distance;
    private static ArrayList<Integer> queue;

    public static void main(String[] args) {
        boolean exit = false;
        while (!exit) {
            exit = menu();
        }
    }

    public static boolean menu() {
        System.out.print("Command? ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().toUpperCase();
        if (input.matches("\\s*[QDIRHE]\\s*")) {
            char command = input.trim().charAt(0);
            switch (command) {
                case 'Q':
                    System.out.print("City code: ");
                    String cityCode = scanner.nextLine().toUpperCase();
                    if (cityCode.matches("\\s*[A-Z]{2}\\s*")) {
                        cityInfo(cityCode);
                    } else {
                        System.out.println("Invalid input.");
                    }
                    break;
                case 'D':
                    System.out.print("City codes: ");
                    String cityCodes = scanner.nextLine().toUpperCase();
                    if (cityCodes.matches("\\s*[A-Z]{2}\\s+[A-Z]{2}\\s*")) {
                        String[] codes = cityCodes.split("\\s+");
                        dijkstra(codes[0], codes[1]);
                    } else {
                        System.out.println("Invalid input.");
                    }
                    break;
                case 'I':
                    System.out.print("City codes and distance: ");
                    String codesDistance = scanner.nextLine().toUpperCase();
                    if (codesDistance.matches("\\s*[A-Z]{2}\\s+[A-Z]{2}\\s+\\d+\\s*")) {
                        String[] split = codesDistance.split("\\s+");
                        insert(split[0], split[1], split[2]);
                    } else {
                        System.out.println("Invalid input");
                    }
                    break;
                case 'R':
                    System.out.print("City codes: ");
                    cityCodes = scanner.nextLine().toUpperCase();
                    if (cityCodes.matches("\\s*[A-Z]{2}\\s+[A-Z]{2}\\s*")) {
                        String[] codes = cityCodes.split("\\s+");
                        remove(codes[0], codes[1]);
                    } else {
                        System.out.println("Invalid input.");
                    }
                    break;
                case 'H':
                    System.out.println("  Q\tQuery the city information by entering the city code");
                    System.out.println("  D\tFind the minimum distance between two cities");
                    System.out.println("  I\tInsert a road by entering two city codes and distance");
                    System.out.println("  R\tRemove an existing road by entering two city codes");
                    System.out.println("  H\tDisplay this message");
                    break;
                case 'E':
                    System.out.println("Exiting...");
                    return true;
            }
        } else {
            System.out.println("Invalid command. Try again.");
            return false;
        }
        return false;
    }

    public static void cityInfo(String cityCode) {
        int i = digraph.getIndex(cityCode);
        if (i == -1) {
            System.out.println("Invalid city code.");
        } else {
            System.out.println(digraph.getNum(i) + " "
                    + digraph.getCityCode(i) + " "
                    + digraph.getCity(i) + " "
                    + digraph.getPopulation(i) + " "
                    + digraph.getElevation(i));
        }
    }

    public static void dijkstra(String source, String target) {
        int sourceIndex = digraph.getIndex(source);
        int targetIndex = digraph.getIndex(target);
        if (sourceIndex == -1 || targetIndex == -1) {
            System.out.println("Invalid city codes.");
            return;
        }

        queue = new ArrayList<Integer>();
        int[] previous = new int[graph.length];
        distance = new double[graph.length];

        //Initializations
        for (int i = 0; i < graph.length; i++) {
            distance[i] = Double.POSITIVE_INFINITY;
            previous[i] = -1;
            queue.add(i);
        }
        distance[sourceIndex] = 0;

        while (!queue.isEmpty()) {
            int u = getMin();
            if (distance[u] == Double.POSITIVE_INFINITY) {
                break;
            }
            queue.remove(queue.indexOf(u));
            for (int i = 0; i < graph[u].length; i++) {
                double alt = distance[u] + graph[u][i];
                if (alt < distance[i]) {
                    distance[i] = alt;
                    previous[i] = u;
                }
            }
        }

        System.out.print("The minimum distance between "
                + digraph.getCity(sourceIndex) + " and " + digraph.getCity(targetIndex) + " is "
                + distance[targetIndex] + " through the route: ");
        String route = target;
        int preIndex = targetIndex;
        while (true) {
            route = digraph.getCityCode(previous[preIndex]) + ", " + route;
            preIndex = previous[preIndex];
            if (preIndex == sourceIndex) {
                break;
            }
        }
        System.out.print(route + "\n");
    }

    public static int getMin() {
        double minimum = Double.POSITIVE_INFINITY;
        int minIndex = 0;
        for (Integer aQueue : queue) {
            if (distance[aQueue] < minimum) {
                minimum = distance[aQueue];
                minIndex = aQueue;
            }
        }
        return minIndex;
    }

    public static void insert(String code1, String code2, String dist) {
        int index1 = digraph.getIndex(code1);
        int index2 = digraph.getIndex(code2);
        if (graph[index1][index2] == Double.POSITIVE_INFINITY) {
            graph[index1][index2] = Double.parseDouble(dist);
            System.out.println("You have inserted a road from "
                    + digraph.getCity(index1) + " to " + digraph.getCity(index2) + " with a distance of " + dist);
        } else {
            System.out.println("There is already a road between "
                    + digraph.getCity(index1) + " and " + digraph.getCity(index2));
        }
    }

    public static void remove(String code1, String code2) {
        int index1 = digraph.getIndex(code1);
        int index2 = digraph.getIndex(code2);
        if (graph[index1][index2] == Double.POSITIVE_INFINITY) {
            System.out.println("The road from "
                    + digraph.getCity(index1) + " and " + digraph.getCity(index2) + " doesn't exit.");
        } else {
            graph[index1][index2] = Double.POSITIVE_INFINITY;
            System.out.println("You have removed the road from "
                    + digraph.getCity(index1) + " to " + digraph.getCity(index2));
        }
    }
}