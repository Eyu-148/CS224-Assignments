import java.util.Scanner;
import java.util.Vector;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class Inversion_Sort_hw7 {

    // create a class to store the returned value
    static class Returned {
        // fields
        int count;
        Vector<Integer> list;

        // constructor
        public Returned(int invers_count, Vector<Integer> sorted_list) {
            this.count = invers_count;
            this.list = sorted_list;
        }
    }

    // method to read from input file and return the vector of numbers
    static Vector<Integer> read_from_file(String filename, Vector<Integer> listname) throws FileNotFoundException {
        File inputFile = new File(filename);
        Scanner reader = new Scanner(inputFile);
        while (reader.hasNextLine()) {
            String[] data = reader.nextLine().split(" ");
            for (String s : data) {
                int n = Integer.parseInt(s);
                listname.add(n);
            }
        }
        reader.close();
        return listname;
    }

    // merge-and-count step
    static Returned merge_count(Returned left, Returned right, int middle) {
        Vector<Integer> sort_list = new Vector<Integer>();

        // sort and merge the 2 sorted list
        // initialize i, j, k
        int i = 0, j = 0, count = 0;
        while (i < left.list.size() && j < right.list.size()) {
            if (left.list.get(i) <= right.list.get(j)) {
                sort_list.add(left.list.get(i));
                ++i;
            } else {
                // inversion occurs
                sort_list.add(right.list.get(j));
                count += middle - i;
                ++j;
            }
        }
        while (i < left.list.size()) {
            sort_list.add(left.list.get(i));
            ++i;
        }
        while (j < right.list.size()) {
            sort_list.add(right.list.get(j));
            ++j;
        }

        Returned returned_info = new Returned(count, sort_list);
        return returned_info;
    }

    static Returned sort_count(Vector<Integer> list, String filename) {
        if (list.size() == 1) {
            Returned returned_info = new Returned(0, list);
            try {
                FileWriter writer = new FileWriter(filename, true);
                writer.write("Inversion count: " + returned_info.count + "  Sorted list: [");
                for (int i : returned_info.list) {
                    writer.write(i + " ");
                }
                writer.write("]\n");
                writer.close();
            } catch (Exception e) {
                e.getStackTrace();
            }
            return returned_info;
        }

        // divide into 2 parts
        int mid = list.size() / 2;
        Vector<Integer> left = new Vector<Integer>();
        Vector<Integer> right = new Vector<Integer>();
        for (int i = 0; i < mid; ++i) {
            left.add(list.get(i));
        }
        for (int i = mid; i < list.size(); ++i) {
            right.add(list.get(i));
        }

        // recursive loop
        Returned left_info = sort_count(left, filename);
        Returned right_info = sort_count(right, filename);
        Returned merge_info = merge_count(left_info, right_info, mid);
        Returned info = new Returned(left_info.count + right_info.count + merge_info.count, merge_info.list);
        try {
            FileWriter writer = new FileWriter(filename, true);
            writer.write("Inversion count: " + info.count + "  Sorted list: [");
            for (int i : info.list) {
                writer.write(i + " ");
            }
            writer.write("]\n");
            writer.close();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return info;
    }

    public static void main(String[] args) {
        Vector<Integer> listOfNumbers = new Vector<Integer>();
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter file name: ");

            String filename = scanner.nextLine();
            // message to confirm the input
            System.out.println("Read data from " + filename);
            try {
                listOfNumbers = read_from_file(filename, listOfNumbers);
            } catch (FileNotFoundException e) {
                System.out.println("error: File not found.");
            }
        }
        // create the output file
        File outputfile = new File("sorted_numbers.txt");
        try {
            if (outputfile.createNewFile()) {
                System.out.println("File created: " + outputfile.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("error: File not created.");
        }
        sort_count(listOfNumbers, "sorted_numbers.txt");
    }
}
