public class test {
    public static void main(String[] args) {
        int[][] table = new int[3][7];
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 7; ++j) {
                table[i][j] = -1;
            }
        }

        String colLabel = String.format("%28d", 1);
        for (int i = 2; i <= 7; ++i) {
            colLabel += String.format("%8d", i);
        }
        System.out.println(colLabel);

        // format by row
        for (int r = 1; r <= 3; ++r) {
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
            for (int f = 1; f <= 7; ++f) {
                row += String.format("%8d", table[r - 1][f - 1]);
            }
            System.out.println(row);
            row = "";
            rowLabel = "";
        }
    }
}
