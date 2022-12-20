import java.util.*;
import java.io.*;

public class Knapsack_hw9 {
    // create knapsack class
    static class knapsack {
        // fields
        int itemNum;
        int itemVal;
        int itemWeight;
        boolean optimal;

        // constructor
        public knapsack(int n, int v, int w) {
            this.itemNum = n;
            this.itemVal = v;
            this.itemWeight = w;
            this.optimal = false;
        }
    }

    static class cell {
        // fields
        int cellVal;
        boolean cellBool;

        // constructor
        public cell(int v) {
            this.cellVal = v;
            this.cellBool = false;
        }
    }

    static int read_max_weight(String filename) throws FileNotFoundException {
        File inFile = new File(filename);
        Scanner reader = new Scanner(inFile);
        String header = reader.nextLine();
        int num = Integer.parseInt(header);
        reader.close();
        return num;
    }

    static Vector<knapsack> read_items(String filename) throws FileNotFoundException {
        // open the file and read the first line
        File inFile = new File(filename);
        Scanner reader = new Scanner(inFile);
        Vector<knapsack> objs = new Vector<knapsack>();
        String header = reader.nextLine();
        // ignore the header

        // read the items
        while (reader.hasNextLine()) {
            String[] data = reader.nextLine().split(" ");
            int n = Integer.parseInt(data[0]);
            int v = Integer.parseInt(data[1]);
            int w = Integer.parseInt(data[2]);
            knapsack obj = new knapsack(n, v, w);
            objs.add(obj);
        }
        reader.close();
        return objs;
    }

    // method to retrieve the whole table and wrtie it into file
    static void print_table(String filename, cell[][] table, int colTotal, int rowTotal, int currentRow)
            throws IOException {
        FileWriter writer = new FileWriter(filename, true);
        writer.write("\nMemoization table, Row " + currentRow + " completed\nWeights-->\n");
        // format a table
        String colLabel = "";
        for (int i = 0; i < colTotal; ++i) {
            if (i == 0) {
                colLabel = String.format("%28d", i);
            } else {
                colLabel += String.format("%8d", i);
            }
        }
        writer.write(colLabel + "\n");
        writer.write("-----------------------------------------------------------------------------------\n");

        // format by row
        for (int r = 1; r <= rowTotal; ++r) {
            // form row label
            String rowLabel = "{";
            for (int l = 1; l < r; ++l) {
                if (r == 1) {
                    rowLabel = "";
                } else {
                    rowLabel += (l + " ");
                }
            }
            rowLabel += "}";
            String row = String.format("%20.20s", rowLabel);
            // content in one row
            for (int f = 1; f <= colTotal; ++f) {
                row += String.format("%8d", table[r - 1][f - 1].cellVal);
            }
            // write into file
            writer.write(row + "\n");
            row = "";
            rowLabel = "";
        }

        writer.close();
    }

    // knapsack implementation & write into file
    static void knapsack_writer(int maxWeight, cell[][] Mtable, Vector<knapsack> items) throws IOException {
        // begin with the header
        FileWriter writer = new FileWriter("test_output.txt", true);
        writer.write("Solving knapsack weight capacity " + maxWeight + ", with " + items.size() + " items\n");
        writer.write("-----------------------------------------------------------------------------------\n");
        writer.close();
        // fill the first row i=0 & write into file
        for (int w = 0; w <= maxWeight; ++w) {
            Mtable[0][w].cellVal = 0;
        }
        print_table("test_output.txt", Mtable, maxWeight + 1, items.size() + 1, 0);

        // now we have items...
        for (int i = 1; i <= items.size(); ++i) {
            for (int w = 0; w <= maxWeight; ++w) {
                if (items.get(i - 1).itemWeight <= w) {
                    Mtable[i][w].cellVal = Math.max(Mtable[i - 1][w].cellVal,
                            items.get(i - 1).itemVal + Mtable[i - 1][w - items.get(i - 1).itemWeight].cellVal);
                    if (Mtable[i][w].cellVal != Mtable[i - 1][w].cellVal) {
                        Mtable[i][w].cellBool = true;
                    }
                } else {
                    Mtable[i][w].cellVal = Mtable[i - 1][w].cellVal;
                }
            }
            print_table("test_output.txt", Mtable, maxWeight + 1, items.size() + 1, i);
        }

        FileWriter writer2 = new FileWriter("test_output.txt", true);
        writer2.write("\nKnapsack Knapsack with weight capacity " + maxWeight + " has an optimal solution "
                + Mtable[items.size()][maxWeight].cellVal + "\n");

        // find the items in optimal set
        int currentWeight = maxWeight;
        writer2.write("_____Knapsack Contains_____\n");
        for (int j = items.size(); j >= 0; --j) {
            if (Mtable[j][maxWeight].cellBool == true && currentWeight >= items.get(j - 1).itemWeight) {
                writer2.write(
                        "Item " + j + "(Value=" + items.get(j - 1).itemVal + ", Weight=" + items.get(j - 1).itemWeight
                                + ")\n");
                currentWeight -= items.get(j - 1).itemWeight;
            }
        }
        writer2.close();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        String inFile = "sample_input.txt";
        int maxWeight = read_max_weight(inFile);
        Vector<knapsack> items = read_items(inFile);
        // now to create a memorized table
        cell[][] table = new cell[items.size() + 1][maxWeight + 1];
        for (int i = 0; i < items.size() + 1; ++i) {
            for (int j = 0; j < maxWeight + 1; ++j) {
                table[i][j] = new cell(-1);
            }
        }

        // knapsack algorithm
        knapsack_writer(maxWeight, table, items);
    }
}
