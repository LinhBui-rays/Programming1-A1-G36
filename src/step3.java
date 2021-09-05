// step3.java
import java.io.*;
import java.text.ParseException;
import java.util.*;

public class step3 {
    public static void main(String[] args) throws IOException, ParseException {
    }

    public static void feature3(String starting_date, String end_date) throws IOException, ParseException {
        Scanner sc = new Scanner(System.in);

        ArrayList<String> list = returnGroupsFromFileCSV3();

        long[] arr = new long[list.size()];

        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
            arr[i] = Long.parseLong((list.get(i).split(",", -1))[3]);
        }

        System.out.printf("How would you like to display the data?\n");
        System.out.printf("          1. Tabular Display\n");
        System.out.printf("          2. Chart Display\n");
        System.out.printf("Your choice: ");
        int check = sc.nextInt();
        sc.nextLine(); // Workaround

        System.out.println();


        if (check == 1) {
            Display d = createTabularDisplay(list);
            d.display();
        } else {
            Display d = createChartDisplay(list);
            d.display();
        }

        System.out.println();

        BufferedReader br = new BufferedReader(new FileReader("covid-data3.csv"));
        String line = br.readLine();
        String[] parts = line.split(",");
        String metric = "";
        if (Integer.parseInt(parts[0]) == 1) {
            metric = "New Cases";
        } else if (Integer.parseInt(parts[0]) == 2) {
            metric = "New Deaths";
        } else if (Integer.parseInt(parts[0]) == 3) {
            metric = "New People Vaccinated";
        }
        String calc_type = parts[1];

        line = br.readLine();
        parts = line.split(",");

        br.close();

        System.out.printf("Overview:\n");
        System.out.printf("- Region: %s\n", parts[3]);
        System.out.printf("- Starting Date: %s\n", starting_date);
        System.out.printf("- End Date: %s\n", end_date);
        System.out.printf("- Chosen Metric: %s\n", metric);
        System.out.printf("- Chosen Calculation type: %s\n", calc_type);

        System.out.println("________________________________________________________________________________\n");

        String choice;
        do {
            if (check == 1) {
                System.out.printf("Would you like to view in Chart format?\n");
            } else {
                System.out.printf("Would you like to view in Tabular format?\n");
            }

            System.out.printf("Your choice (Y/N): ");
            choice = sc.nextLine().toUpperCase(Locale.ROOT);

            System.out.println();

            if (choice.equals("Y")) {

                if (check == 1) {
                    Display d = createChartDisplay(list);
                    d.display();
                    check = 2;
                } else {
                    Display d = createTabularDisplay(list);
                    d.display();
                    check = 1;
                }

                System.out.println();

                br = new BufferedReader(new FileReader("covid-data3.csv"));
                line = br.readLine();
                parts = line.split(",");
                metric = "";
                if (Integer.parseInt(parts[0]) == 1) {
                    metric = "New Cases";
                } else if (Integer.parseInt(parts[0]) == 2) {
                    metric = "New Deaths";
                } else if (Integer.parseInt(parts[0]) == 3) {
                    metric = "New People Vaccinated";
                }
                calc_type = parts[1];

                line = br.readLine();
                parts = line.split(",");

                br.close();

                System.out.printf("Overview:\n");
                System.out.printf("- Region: %s\n", parts[3]);
                System.out.printf("- Starting Date: %s\n", starting_date);
                System.out.printf("- End Date: %s\n", end_date);
                System.out.printf("- Chosen Metric: %s\n", metric);
                System.out.printf("- Chosen Calculation type: %s\n", calc_type);
            }
            System.out.println("________________________________________________________________________________\n");
        } while (choice.equals("Y"));
    }

    public static Display createTabularDisplay(ArrayList<String> list) throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader("covid-data3.csv"));
        String line;

        line = br.readLine();
        int type = 0;

        if (Objects.equals(line.split(",")[1], "new_total")) {
            type = 1;
        } else if (Objects.equals(line.split(",")[1], "up_to")) {
            type = 2;
        }

        br.close();

        ArrayList<Group> g = new ArrayList<Group>();

        for (String a : list) {
            String[] parts = a.split(",");
            g.add(new Group(Integer.parseInt(parts[0]), parts[1], parts[2], Long.parseLong(parts[3])));
        }



        return new TabularDisplay(g, type);
    }

    public static Display createChartDisplay(ArrayList<String> list) throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader("covid-data3.csv"));
        String line;

        line = br.readLine();
        int type = 0;

        if (Objects.equals(line.split(",")[1], "new_total")) {
            type = 1;
        } else if (Objects.equals(line.split(",")[1], "up_to")) {
            type = 2;
        }

        br.close();

        ArrayList<Group> g = new ArrayList<Group>();

        for (String a : list) {
            String[] parts = a.split(",");
            g.add(new Group(Integer.parseInt(parts[0]), parts[1], parts[2], Long.parseLong(parts[3])));
        }

        return new ChartDisplay(g, type);
    }

    public static ArrayList<String> returnGroupsFromFileCSV3() throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader("covid-data3.csv"));
        int value_type = 0;
        String line;

        line = br.readLine();

        if (Objects.equals(line.split(",")[1], "new_total")) {
            value_type = 1;
        } else if (Objects.equals(line.split(",")[1], "up_to")) {
            value_type = 2;
        }

        String line2;
        line2 = br.readLine();
        String[] tmp = line2.split(",");

        String starting_date = tmp[4];
        int group = Integer.parseInt(tmp[0]);
        String previous_new_total = tmp[9];
        String previous_up_to = tmp[10];
        String last_date = null;
        last_date = starting_date;

        String previous_starting_date = null, previous_end_date = null;
        ArrayList<String> list = new ArrayList<String>();



        while ((line2 = br.readLine()) != null) {
            String[] parts = line2.split(",");
            if (Integer.parseInt(parts[0]) != group) {
//                System.out.println(line);
                group = Integer.parseInt(parts[0]);
                previous_starting_date = starting_date;
                starting_date = parts[4];
                Calendar c = Calendar.getInstance();
                c.setTime(step1.stringToDate(starting_date));
                c.add(Calendar.DATE, -1);
                previous_end_date = step1.dateToString(c.getTime());
                if (value_type == 1) {
                    list.add(String.format("%d,%s,%s,%s", group - 1, previous_starting_date, previous_end_date, previous_new_total));
                } else {
                    list.add(String.format("%d,%s,%s,%s", group - 1, previous_starting_date, previous_end_date, previous_up_to));
                }

            }
            last_date = parts[4];
            previous_new_total = parts[9];
            previous_up_to = parts[10];
        }

        if (value_type == 1) {
            list.add(String.format("%d,%s,%s,%s", group, starting_date, last_date, previous_new_total));
        } else {
            list.add(String.format("%d,%s,%s,%s", group, starting_date, last_date, previous_up_to));
        }

        br.close();
        //System.out.println(Arrays.toString(list.toArray()));
        return list;
    }

    public static void addChartElements(String[][] drawChart, long[] numValues) {
        // Fit data into chartS
        if (numValues.length < 79) {
            // Get Y axis scale
            double fitValueX = Math.floor(79 / numValues.length) + 3 * Math.round(numValues.length) / 79;
            // round down number to the nearest integer and adjust scale
            // Get X axis value
            long max = step2.findMaxMinLong(numValues)[0];
            long min = step2.findMaxMinLong(numValues)[1];
            long xAxisScale = (long) Math.ceil((max - min) / 23); // round to the nearest integer

            // plot values into chart
            for (int i = 0; i < numValues.length; i++) {
                int height = 22; // 22 points to represent the data according to the requirements // reset height after adding values
                //
                for (int rows = 0; i < drawChart.length; rows++) {
                    long positionValue = (xAxisScale * height) + min - (int) Math.floor(numValues.length);
                    if (positionValue - numValues[i] <= 0) {
                        for (int columns = 1; columns < drawChart[rows].length - 1; columns++) {
                            if (columns - 1 == i * fitValueX) {
                                drawChart[rows][columns] = "%";
                            }
                        }
                        break;
                    }
                    height -= 1; // Go to next line
                }
            }
        } else {
            System.out.println("\nOut of range. Data cannot be displayed");
            System.exit(0);
        }
    }

    public static void ChartDisplay(long[] numValues) {
        String[][] twoArr = new String[24][80]; // 24 rows , 80 columns

        for(int row = 0; row < twoArr.length; row++) {
            for(int col = 0; col < twoArr[row].length; col++) {
                // Assign empty space to the chart
                twoArr[row][col] = " ";
                twoArr[twoArr.length - 1][col] = "_";
            }
            twoArr[row][0] = "|";
        }

        // call function to add chart point
        addChartElements(twoArr, numValues);

        // Display chart
        for(int row = 0; row < twoArr.length; row++) {
            for(int col = 0; col < twoArr[row].length; col++) {
                // Assign empty space to the chart
                System.out.print(twoArr[row][col]);
            }
            System.out.println();
        }
    }
}
