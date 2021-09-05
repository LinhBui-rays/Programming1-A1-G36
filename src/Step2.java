import java.io.*;
import java.text.ParseException;
import java.util.*;

public class Step2 {
    public static void main(String[] args) throws IOException {
    }

    // This method executes the first half of feature 2
    public static int[] feature2a() throws IOException {
        // Create a System.in Scanner instance
        Scanner sc = new Scanner(System.in);

        // Declare and/or initialise variables used later on
        String input = null;
        int choice = 0;
        int[] arr = new int[]{0,0};

        // If the user does not agree to the recommended grouping by method 3, prompts the user to choose again
        do {
            if (arr[0] == -1 && arr[1] == -1) {
                input = null;
            }

            System.out.printf("\nHow would you like to group your data?\n");
            System.out.printf("          1. No grouping (Each day is a group)\n");
            System.out.printf("          2. Group by Number of Groups\n");
            System.out.printf("          3. Group by Number of Days in 1 Group\n");

            // If the input is not an integer or is not in the given range, ask for the input again
            do {
                // This is to ensure that the message the user is shown the first time differs from the message they see
                // after having made a mistake
                if (input == null) {
                    System.out.printf("Your choice: ");
                } else {
                    System.out.printf("Invalid input! Only type in 1,2 or 3: ");
                }
                input = sc.nextLine();
                if (General.checkIfInt(input)) {
                    choice = Integer.parseInt(input);
                }
            } while (!General.checkIfInt(input) || (choice > 3 || choice < 1));

            // This is the total number of days
            int lines = General.countLines("covid-data2.csv");

            // Declare and/or initialise variables used later on
            int n_group = 1, i = 0;
            String extra = null;

            // If the user choose No grouping, return an array of {lines, 1}, meaning there are as many groups as there
            // are days, and each group consist of 1 day
            if (choice == 1) {
                arr = new int[]{lines, 1};
            } else if (choice == 2) {
                System.out.println();
                input = null;

                do {
                    if (input == null) {
                        System.out.printf("How many groups do you want to divide into? (Positive integer value only): ");
                    } else if (n_group > lines) {
                        System.out.printf("The inputted number of days exceeded the total number of days! Please enter a valid number of days: ");
                    } else {
                        System.out.print("How many groups do you want to divide into? (Positive integer value only): ");
                    }

                    input = sc.nextLine();
                    if (General.checkIfInt(input)) {
                        n_group = Integer.parseInt(input);
                    } else {
                        n_group = 0;
                    }
                } while (!General.checkIfInt(input) || (n_group < 1 || n_group > lines));

                arr = findNumberOfDaysAndNumberOfGroups(lines, n_group);
            } else {
                arr = findNumberOfGroupsFromGivenDate(lines);
            }
        } while (arr[0] == -1 && arr[1] == -1);
        return arr;
    }

    // A method that find the number of groups and their respective days
    public static int[] findNumberOfDaysAndNumberOfGroups(int total_days, int n_group) {
        if (total_days % n_group != 0) {
            int[] arr = findNumberOfDaysForEachGroup(total_days, n_group);
            int[] maxmin = findMaxMin(arr);
//            System.out.println(Arrays.toString(arr));
            int larger_num_of_days_in_1_group = maxmin[0];
//            System.out.println(max);
            int num_groups_with_larger_days = countFreq(arr, larger_num_of_days_in_1_group);
            int smaller_num_of_days_in_1_group = maxmin[1];
//            System.out.println(min);
            int num_groups_with_smaller_days = countFreq(arr, smaller_num_of_days_in_1_group);

            System.out.printf("%d days can be grouped into %d group(s) of %d day(s), and %d group(s) of %d day(s)\n", total_days, num_groups_with_larger_days, larger_num_of_days_in_1_group, num_groups_with_smaller_days, smaller_num_of_days_in_1_group);
            return new int[]{num_groups_with_larger_days, larger_num_of_days_in_1_group, num_groups_with_smaller_days, smaller_num_of_days_in_1_group};

        } else {
            System.out.printf("\n%d days can be grouped into %d group(s) of %d day(s)\n", total_days, n_group, total_days / n_group);
            return new int[]{n_group, total_days / n_group};
        }
    }

    // A method that find the number of days from a number of groups
    public static int[] findNumberOfDaysForEachGroup(int total_days, int num_of_groups) {
        int days_in_1_group = total_days / num_of_groups;
        int tmp_n = days_in_1_group * num_of_groups;
        int[] arr = new int[num_of_groups];


        if (total_days % num_of_groups == 0) {
            for (int i = 0; i < num_of_groups; i++) {
                arr[i] = days_in_1_group;
            }
        } else {
            for (int i = 0; i < num_of_groups; i++) {
                arr[i] = days_in_1_group;
            }

            for (int i = 0; i < num_of_groups; i++) {
                arr[i] += 1;
                tmp_n += 1;
                if (tmp_n == total_days) {
                    break;
                }
            }
        }

//        System.out.println(Arrays.toString(arr));
        return arr;
    }

    // This method counts how many times something repeats in an array
    public static int countFreq(int arr[], int n) {
        int count = 0;
        for (int j : arr) {
            if (n == j) {
                count++;
            }
        }
        return count;
    }

    // Find max and min values of an array and return them as an array of 2 long elements
    public static long[] findMaxMinLong(long[] arr) {
        long max = arr[0];
        long min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
            if (min > arr[i]) {
                min = arr[i];
            }
        }

        return new long[]{max, min};
    }

    // Find max and min values of an array and return them as an array of 2 long elements
    public static int[] findMaxMin(int[] arr) {
        int max = arr[0];
        int min = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
            if (min > arr[i]) {
                min = arr[i];
            }
        }

        return new int[]{max, min};
    }

    // A method that finds the number of group from a given number of days per group
    public static int[] findNumberOfGroupsFromGivenDate(int total_days) {
        Scanner sc = new Scanner(System.in);
        int day_number = 1, i = 0;
        String input = null;

        do {
            if (input == null) {
                System.out.printf("Please input the number of days in one group (Positive integer value only): ");
            } else if (day_number > total_days) {
                System.out.printf("The inputted number of days exceeded the total number of days! Please enter a valid number of days: ");
            } else {
                System.out.printf("Please input the number of days in one group (Positive integer value only): ");
            }

            input = sc.nextLine();
            if (General.checkIfInt(input)) {
                day_number = Integer.parseInt(input);
            } else {
                day_number = 0;
            }
        } while (!General.checkIfInt(input) || (day_number < 1 || day_number > total_days));


        int floored_num_of_groups = total_days / day_number;
        float true_num_of_groups = (float) total_days / day_number;
        String check;

        if ((total_days % floored_num_of_groups == 0) && (floored_num_of_groups != true_num_of_groups)) {
            System.out.printf("%d days cannot be grouped into groups of %d\n", total_days, day_number);
            int[] arr = findNumberOfDaysForEachGroup(total_days, floored_num_of_groups);
            int days_in_1_group = findMaxMin(arr)[0];
            int num_of_groups = countFreq(arr, days_in_1_group);

            System.out.printf("But %d days can be grouped into %d group(s) of %d day(s)\n", total_days, num_of_groups, days_in_1_group);

            do {
                System.out.printf("Do you want to use this recommended grouping? (Y/N): ");
                check = sc.nextLine().toUpperCase(Locale.ROOT);
            } while (!Objects.equals(check, "N") && !Objects.equals(check, "Y"));

            if (Objects.equals(check, "Y")) {
                return new int[]{num_of_groups, days_in_1_group};
            } else if (Objects.equals(check, "N")) {
                return new int[]{-1,-1};
            }

        } else if (total_days % day_number != 0) {
            System.out.printf("%d days cannot be grouped into groups of %d\n", total_days, day_number);
            int[] arr = findNumberOfDaysForEachGroup(total_days, floored_num_of_groups);
            int[] maxmin = findMaxMin(arr);
//            System.out.println(Arrays.toString(arr));
            int larger_num_of_days_in_1_group = maxmin[0];
//            System.out.println(max);
            int num_groups_with_larger_days = countFreq(arr, larger_num_of_days_in_1_group);
            int smaller_num_of_days_in_1_group = maxmin[1];
//            System.out.println(min);
            int num_groups_with_smaller_days = countFreq(arr, smaller_num_of_days_in_1_group);

            System.out.printf("But %d days can be grouped into %d group(s) of %d day(s), and %d group(s) of %d day(s)\n", total_days, num_groups_with_larger_days, larger_num_of_days_in_1_group, num_groups_with_smaller_days, smaller_num_of_days_in_1_group);

            do {
                System.out.printf("Do you want to use this recommended grouping? (Y/N): ");
                check = sc.nextLine().toUpperCase(Locale.ROOT);
            } while (!Objects.equals(check, "N") && !Objects.equals(check, "Y"));

            if (Objects.equals(check, "Y")) {
                return new int[]{num_groups_with_larger_days, larger_num_of_days_in_1_group, num_groups_with_smaller_days, smaller_num_of_days_in_1_group};
            } else if (Objects.equals(check, "N")) {
                return new int[]{-1,-1};
            }

        } else {
            System.out.printf("\n%d days can be grouped into %d group(s) of %d day(s)\n", total_days, floored_num_of_groups, day_number);
            return new int[]{floored_num_of_groups, day_number};
        }
         return new int[]{-1,-1};
    }

    // This method continues from feature2a, and does most of the calculation. The calculated data is stored into a file
    // called covid-data3.csv
    public static void feature2() throws IOException, ParseException {
        Scanner sc = new Scanner(System.in);
        int metric = 0, type = 0, check = 0;
        String input = null;

        int[] arr = Step2.feature2a();

        BufferedReader br = new BufferedReader(new FileReader("covid-data2.csv"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("covid-data3.csv", false));

        long previous_vaccinated = 0;
        System.out.printf("\nWhich metrics do you want to use for calculations?\n");
        System.out.printf("          1. New cases\n");
        System.out.printf("          2. New deaths\n");
        System.out.printf("          3. New people vaccinated\n");

        // This is to ensure that the message the user is shown the first time differs from the message they see
        // after having made a mistake
        do {
            if (input == null) {
                System.out.printf("Your choice: ");
            } else {
                System.out.printf("Invalid input! Only type in 1, 2, or 3: ");
            }
            input = sc.nextLine();
            if (General.checkIfInt(input)) {
                metric = Integer.parseInt(input);
            }
        } while (!General.checkIfInt(input) || (metric > 3 || metric < 1));

        if (metric == 3) {
            BufferedReader tmp_br = new BufferedReader(new FileReader("covid-data2.csv"));
            String tmp_line = tmp_br.readLine();
            String[] tmp_parts = tmp_line.split(",", -1);
            previous_vaccinated = findVaccinatedBeforeDate(tmp_parts[2], tmp_parts[3]);
            tmp_br.close();
        }
        bw.write(String.format("%d,",metric));
        metric += 3;

        System.out.printf("\nWhat do you want to calculate?\n");
        System.out.printf("          1. New Total\n");
        System.out.printf("          2. Up To\n");

        input = null;

        // This is to ensure that the message the user is shown the first time differs from the message they see
        // after having made a mistake
        do {
            if (input == null) {
                System.out.printf("Your choice: ");
            } else {
                System.out.printf("Invalid input! Only type in 1, or 2: ");
            }
            input = sc.nextLine();
            if (General.checkIfInt(input)) {
                type = Integer.parseInt(input);
            }
        } while (!General.checkIfInt(input) || (type > 2 || type < 1));

        if (type == 1) {
            bw.write(String.format("new_total\n"));
        } else if (type == 2) {
            bw.write(String.format("up_to\n"));
        }

        long new_total = 0, up_to = 0;
        int i = 0, group = 0;
        int new_vaccinated = 0;
        String line;

        // if length == 2 => The days are split into a number of groups with equal number of days
        if (arr.length == 2) {
            while ((line = br.readLine()) != null) {
                String[] parts = General.splitString(line);


                if (metric == 6) {
                    if (parts[6].equals("0")) {
                        parts[6] = String.format("%d", previous_vaccinated);
                    } else {
                        new_vaccinated = (int) (Long.parseLong(parts[6]) - previous_vaccinated);
                        previous_vaccinated = Long.parseLong(parts[6]);
                    }
                }

                if (i % arr[1] == 0) {
                    new_total = 0;
//                    System.out.printf("\nGroup %d\n", group);
                    group++;
                }

                if (metric == 6) {
                    new_total += new_vaccinated;
                    up_to += new_vaccinated;
                    new_vaccinated = 0;
                } else {
                    new_total += Long.parseLong(parts[metric]);
                    up_to += Long.parseLong(parts[metric]);
                }

                bw.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%d,%d\n", group, parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], new_total, up_to));

                i++;
            }
        }

        // if length == 4 => The days are split into 2 types of groups, each type has their own number of members
        if (arr.length == 4) {
            while ((line = br.readLine()) != null) {
                String[] parts = General.splitString(line);

                if (metric == 6) {
                    if (parts[6].equals("0")) {
                        parts[6] = String.format("%d", previous_vaccinated);
                    } else {
                        new_vaccinated = (int) (Long.parseLong(parts[6]) - previous_vaccinated);
                        previous_vaccinated = Long.parseLong(parts[6]);
                    }
                }

                if (group <= arr[0]) {
                    if (i % arr[1] == 0) {
                        new_total = 0;
//                    System.out.printf("\nGroup %d\n", group);
                        group++;
                        i = 0;
                    }
                } else if (group < arr[2] + arr[0]) {
                    if (i % arr[3] == 0) {
                        new_total = 0;
//                    System.out.printf("\nGroup %d\n", group);
                        group++;
                        i = 0;
                    }
                }

                if (metric == 6) {
                    new_total += new_vaccinated;
                    up_to += new_vaccinated;
                    new_vaccinated = 0;

                } else {
                    new_total += Long.parseLong(parts[metric]);
                    up_to += Long.parseLong(parts[metric]);
                }

                bw.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%d,%d\n", group, parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], new_total, up_to));

                i++;
//                System.out.println(group + " " + i);
            }
        }

        br.close();
        bw.close();

        General.removeLastLine("covid-data3.csv");

        System.out.printf("___________________________________________________________________________________________                         ");

        System.out.printf("\nData has been successfully sorted and processed with the following criteria:\n");
        if (arr.length == 2) {
            System.out.printf("- Grouping: %d group(s) of %d day(s)\n", arr[0], arr[1]);
        } else if (arr.length == 4) {
            System.out.printf("- Grouping: %d group(s) of %d day(s) and %d group(s) of %d day(s)\n", arr[0], arr[1], arr[2], arr[3]);
        }
        System.out.printf("- Metric: ");
        if (metric == 4) {
            System.out.printf("New cases\n");
        } else if (metric == 5) {
            System.out.printf("New deaths\n");
        } else if (metric == 6) {
            System.out.printf("New people vaccinated\n");
        }
        System.out.printf("- Calculation type: ");
        if (type == 1) {
            System.out.printf("New Total\n");
        } else if (type == 2) {
            System.out.printf("Up To\n");
        }

        System.out.printf("\nDo you want to:\n");
        System.out.printf("          1. Restart the Search from the Beginning\n");
        System.out.printf("          2. Restart Step 2\n");
        System.out.printf("          3. Continue to Displaying the Data (Step 3)\n");

        input = null;

        do {
            if (input == null) {
                System.out.printf("Your choice: ");
            } else {
                System.out.printf("Invalid input! Only type in 1,2 or 3: ");
            }
            input = sc.nextLine();
            if (General.checkIfInt(input)) {
                check = Integer.parseInt(input);
            }
        } while (!General.checkIfInt(input) || (check > 3 || check < 1));

        if (check == 1) {
            Main.basicFlow1();
            Main.basicFlow2();
        } else if (check == 2) {
            System.out.println();
            feature2();
        }

    }

    // Method to find the vaccinated data previous to the users' input date (for calculation purpose)
    public static long findVaccinatedBeforeDate(String location, String date) throws IOException, ParseException {
        // Read CSV file
        BufferedReader br = new BufferedReader(new FileReader("covid-data.csv"));
        String line;
        long max = 0;

        while ((line = br.readLine()) != null) {
            String[] parts = General.splitString(line);
//            System.out.printf("'%s' '%s' '%s' '%s' '%s' '%s' '%s' '%s'\n", parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]);

            if (Objects.equals(parts[2], location) && General.stringToDate(parts[3]).before(General.stringToDate(date))) {
                if (max < Long.parseLong(parts[6])) {
                    max = Long.parseLong(parts[6]);
                }
            }
        }

        br.close();
        return max;
    }
}
