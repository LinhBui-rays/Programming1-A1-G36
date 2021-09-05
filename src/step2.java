// step2.java
import java.io.*;
import java.text.ParseException;
import java.util.*;

public class step2 {
    public static void main(String[] args) throws IOException {
    }


    public static int[] feature2a() throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.printf("How would you like to group your data?\n");
        System.out.printf("          1. No grouping (Each day is a group)\n");
        System.out.printf("          2. Group by Number of Groups\n");
        System.out.printf("          3. Group by Number of Days in 1 Group\n");
        System.out.printf("Your choice: ");
        int choice = sc.nextInt();
        sc.nextLine(); // Workaround

        int lines = step1.countLines("covid-data2.csv");

        int[] arr = new int[]{0,0};

        if (choice == 1) {
            arr = new int[]{lines, 1};
        } else if (choice == 2) {
            System.out.printf("\nHow many groups do you want to divide into? (Positive number only): ");
            int n_group = sc.nextInt();
            arr = findNumberOfDaysAndNumberOfGroups(lines, n_group);
        } else if (choice == 3) {
            arr = findNumberOfGroupsFromGivenDate(lines);
        }

        return arr;
    }

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

    public static int countFreq(int arr[], int n) {
        int count = 0;
        for (int j : arr) {
            if (n == j) {
                count++;
            }
        }
        return count;
    }

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

    public static int[] findNumberOfGroupsFromGivenDate(int total_days) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Please input the number of days in one group: ");
        int day_number = sc.nextInt();
        sc.nextLine(); // Workaround

        int floored_num_of_groups = total_days / day_number;
        float true_num_of_groups = (float) total_days / day_number;

        if ((total_days % floored_num_of_groups == 0) && (floored_num_of_groups != true_num_of_groups)) {
            System.out.printf("\n%d days cannot be grouped into groups of %d\n", total_days, day_number);
            int[] arr = findNumberOfDaysForEachGroup(total_days, floored_num_of_groups);
            int days_in_1_group = findMaxMin(arr)[0];
            int num_of_groups = countFreq(arr, days_in_1_group);

            System.out.printf("But %d days can be grouped into %d group(s) of %d day(s)\n", total_days, num_of_groups, days_in_1_group);
            return new int[]{num_of_groups, days_in_1_group};

        } else if (total_days % day_number != 0) {
            System.out.printf("\n%d days cannot be grouped into groups of %d\n", total_days, day_number);
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
            return new int[]{num_groups_with_larger_days, larger_num_of_days_in_1_group, num_groups_with_smaller_days, smaller_num_of_days_in_1_group};

        } else {
            System.out.printf("\n%d days can be grouped into %d group(s) of %d day(s)\n", total_days, floored_num_of_groups, day_number);
            return new int[]{floored_num_of_groups, day_number};
        }
        // return
    }

    public static void feature2() throws IOException, ParseException {
        Scanner sc = new Scanner(System.in);

        int[] arr = step2.feature2a();

        BufferedReader br = new BufferedReader(new FileReader("covid-data2.csv"));
        BufferedWriter bw = new BufferedWriter(new FileWriter("covid-data3.csv", false));

        long previous_vaccinated = 0;
        System.out.printf("\nWhich metrics do you want to use for calculations?\n");
        System.out.printf("          1. New cases\n");
        System.out.printf("          2. New deaths\n");
        System.out.printf("          3. New people vaccinated\n");
        System.out.printf("Your choice: ");
        int metric = sc.nextInt();
        sc.nextLine(); // Workaround
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
        System.out.printf("Your choice: ");
        int type = sc.nextInt();

        if (type == 1) {
            bw.write(String.format("new_total\n"));
        } else if (type == 2) {
            bw.write(String.format("up_to\n"));
        }

        long new_total = 0, up_to = 0;
        int i = 0, group = 0;
        int new_vaccinated = 0;
        String line;

        // Divisible
        if (arr.length == 2) {
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (Objects.equals(parts[4], "")) {
                    parts[4] = "0";
                }
                if (Objects.equals(parts[5], "")) {
                    parts[5] = "0";
                }
                if (Objects.equals(parts[6], "")) {
                    parts[6] = "0";
                }
                if (Objects.equals(parts[7], "")) {
                    parts[7] = "0";
                }

                //System.out.printf("%s %s %s %s %s %s %s %s\n", parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]);

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

        // arr[0]:num_groups_with_larger_days    2
        // arr[1]:larger_num_of_days_in_1_group  8
        // arr[2]:num_groups_with_smaller_days   2
        // arr[3]:smaller_num_of_days_in_1_group 7

        if (arr.length == 4) {
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (Objects.equals(parts[4], "")) {
                    parts[4] = "0";
                }
                if (Objects.equals(parts[5], "")) {
                    parts[5] = "0";
                }
                if (Objects.equals(parts[6], "")) {
                    parts[6] = "0";
                }
                if (Objects.equals(parts[7], "")) {
                    parts[7] = "0";
                }

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

        step1.removeLastLine("covid-data3.csv");

        System.out.println("\nData has been successfully sorted and processed!\n");
//        System.out.println(findVaccinatedBeforeDate("Vietnam", "3/9/2021"));
    }

    public static long findVaccinatedBeforeDate(String location, String date) throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader("covid-data.csv"));
        String line;
        long max = 0;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",", -1);
//            System.out.printf("'%s' '%s' '%s' '%s' '%s' '%s' '%s' '%s'\n", parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]);
            if (Objects.equals(parts[4], "")) {
                parts[4] = "0";
            }
            if (Objects.equals(parts[5], "")) {
                parts[5] = "0";
            }
            if (Objects.equals(parts[6], "")) {
                parts[6] = "0";
            }
            if (Objects.equals(parts[7], "")) {
                parts[7] = "0";
            }

            if (Objects.equals(parts[2], location) && step1.stringToDate(parts[3]).before(step1.stringToDate(date))) {
                if (max < Long.parseLong(parts[6])) {
                    max = Long.parseLong(parts[6]);
                }
            }
        }

        br.close();
        return max;
    }
}
