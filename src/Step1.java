// Step1.java
import java.text.*;
import java.util.*;
import java.io.*;

public class Step1 {
    public static void main(String[] args) throws ParseException, IOException {
//        feature1();
    }

    public static String[] feature1() throws IOException, ParseException {
        Scanner sc = new Scanner(System.in);
        int check = 0, choice = 0;
        String input = null;

        String starting_date = "";
        String end_date = "";

        do {
            String geoArea = chooseGeoArea();
            boolean validDate = true;

            do {
                if (!validDate) {
                    System.out.printf("Please input valid dates!\n");
                }
                System.out.printf("How would you like to select your date range?\n");
                System.out.printf("          1. Start Date - End Date\n");
                System.out.printf("          2. Start Date - Days/Weeks Afters\n");
                System.out.printf("          3. Days/Weeks Before - End Date\n");

                do {
                    if (input == null) {
                        System.out.printf("Your choice: ");
                    } else {
                        System.out.printf("Invalid input! Only type in 1, 2, or 3: ");
                    }
                    input = sc.nextLine();
                    if (General.checkIfInt(input)) {
                        choice = Integer.parseInt(input);
                    }
                } while (!General.checkIfInt(input) || (choice > 3 || choice < 1));


                starting_date = "";
                end_date = "";

                if (choice == 1) {
                    System.out.println();
                    String[] dates = one();
                    starting_date = dates[0];
                    end_date = dates[1];
                } else if (choice == 2) {
                    System.out.println();
                    String[] dates = two();
                    starting_date = dates[0];
                    end_date = dates[1];
                } else if (choice == 3){
                    System.out.println();
                    String[] dates = three();
                    starting_date = dates[0];
                    end_date = dates[1];
                }

                System.out.println();

                if (checkValidDate(geoArea, starting_date)) {
                    validDate = true;
                } else {
                    System.out.println("ERROR: Invalid starting date!\n");
                    validDate = false;
                }

                if (checkValidDate(geoArea, end_date)) {
                    validDate = true;
                } else {
                    System.out.println("ERROR: Invalid end date!\n");
                    validDate = false;
                }
            } while (!validDate);

            search("covid-data.csv", "covid-data2.csv", geoArea, starting_date, end_date);

            System.out.printf("___________________________________________________________________________________________                         ");

            System.out.printf("\nYour search has been successfully ran with the following date range: %s - %s!\nDo you want to: \n", starting_date, end_date);
            System.out.printf("          1. Print out the current query, then continue to the sorting step (Step 2)\n");
            System.out.printf("          2. Continue to the next step (Step 2)\n");
            System.out.printf("          3. Restart the Search from the Beginning\n");

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

        } while (check == 3);

        System.out.println();

        if (check == 1) {
            System.out.println("\nYour search result is as follows: ");
            General.printCSV("covid-data2.csv");
            System.out.println();
        }

        return new String[]{starting_date, end_date};
    }

    public static void search(String file1, String file2, String geoArea, String starting_date, String end_date) throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader(file1));
        PrintWriter pw = new PrintWriter(new FileWriter(file2, false));
        br.readLine(); // Basically skip the first line to prevent errors.

        String line = "";

        System.out.println();
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");

            Date current_date = General.stringToDate(parts[3]);

            if ((Objects.equals(parts[2].toUpperCase(Locale.ROOT), geoArea.toUpperCase(Locale.ROOT))) &&
                    (((current_date.equals(General.stringToDate(starting_date))) || (current_date.after(General.stringToDate(starting_date)))) &&
                            ((current_date.before(General.stringToDate(end_date))) || (current_date.equals(General.stringToDate(end_date)))))) {
//                System.out.println(line);
//                System.out.printf("%s,%s,%s,%s,%s,%s,%s,%s\n", parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]);
                pw.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]));
//                System.out.printf(
//                if (!current_date.equals(stringToDate(end_date))) {
//                pw.write("\n");
//                    System.out.printf(
//                }
            }
        }
        pw.close();
        General.removeLastLine(file2);
    }

    public static String inputStartingDate() {
        Scanner sc = new Scanner(System.in);
        String input;
        int starting_day = 0, starting_month = 0, starting_year = 0;

        System.out.printf("Please input the starting date step by step (Positive integer value only): \n");

        do {
            System.out.printf("          - Day: ");

            input = sc.nextLine();
            if (General.checkIfInt(input)) {
                starting_day = Integer.parseInt(input);
            }
        } while (!General.checkIfInt(input) || (starting_day > 31 || starting_day < 1));

        do {
            System.out.printf("          - Month: ");

            input = sc.nextLine();
            if (General.checkIfInt(input)) {
                starting_month = Integer.parseInt(input);
            }
        } while (!General.checkIfInt(input) || (starting_month > 12 || starting_month < 1));

        do {
            System.out.printf("          - Year: ");

            input = sc.nextLine();
            if (General.checkIfInt(input)) {
                starting_year = Integer.parseInt(input);
            }
        } while (!General.checkIfInt(input) || (starting_year > 9999 || starting_year < 1));

        System.out.println();

        return String.format("%d/%d/%d", starting_month, starting_day, starting_year);

    }

    public static String inputEndDate() {
        Scanner sc = new Scanner(System.in);
        int end_day = 0, end_month = 0, end_year = 0;
        String input;
        System.out.printf("Please input the end date step by step (Positive integer value only): \n");



        do {
            System.out.printf("          - Day: ");

            input = sc.nextLine();
            if (General.checkIfInt(input)) {
                end_day = Integer.parseInt(input);
            }
        } while (!General.checkIfInt(input) || (end_day > 31 || end_day < 1));

        do {
            System.out.printf("          - Month: ");

            input = sc.nextLine();
            if (General.checkIfInt(input)) {
                end_month = Integer.parseInt(input);
            }
        } while (!General.checkIfInt(input) || (end_month > 12 || end_month < 1));

        do {
            System.out.printf("          - Year: ");

            input = sc.nextLine();
            if (General.checkIfInt(input)) {
                end_year = Integer.parseInt(input);
            }
        } while (!General.checkIfInt(input) || (end_year > 9999 || end_year < 1));

//        System.out.println();

        return String.format("%d/%d/%d", end_month, end_day, end_year);
    }

    public static String chooseGeoArea() throws IOException {
        Scanner sc = new Scanner(System.in);

        BufferedReader br = new BufferedReader(new FileReader("covid-data.csv"));
        Set<String> geo_list = new HashSet<String>();
        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            geo_list.add(parts[2].toUpperCase(Locale.ROOT));
        }

        br.close();

        String geoArea = "";

        do {
            System.out.printf("\nPlease input a valid Continent or Country to continue: ");
            geoArea = sc.nextLine();
        } while (!geo_list.contains(geoArea.toUpperCase(Locale.ROOT)));


        System.out.println();
        return geoArea;
    }

    public static boolean checkValidDate(String location, String date) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("covid-data.csv"));
        Set<String> locationAndDate = new HashSet<String>();
        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            locationAndDate.add(String.format("%s,%s", parts[2].toUpperCase(Locale.ROOT), parts[3].toUpperCase(Locale.ROOT)));
        }

        br.close();

        return locationAndDate.contains(String.format("%s,%s", location.toUpperCase(Locale.ROOT), date.toUpperCase(Locale.ROOT)));
    }

    public static String[] one() {
        String starting_date = inputStartingDate();
//        System.out.println(starting_date);
        String end_date = inputEndDate();
//        System.out.println(end_date);

        return new String[]{starting_date, end_date};
    }

    public static String[] two() throws ParseException {
        Scanner sc = new Scanner(System.in);
        String input = null;
        int check = 0;

        Calendar c = Calendar.getInstance();

        String starting_date = inputStartingDate();
        String end_date = "";

        c.setTime(General.stringToDate(starting_date));

        System.out.printf("Please choose the date addition unit:\n");
        System.out.printf("          1. Day\n");
        System.out.printf("          2. Week\n");

        do {
            if (input == null) {
                System.out.printf("Your choice: ");
            } else {
                System.out.printf("Invalid input! Only type in 1, or 2: ");
            }
            input = sc.nextLine();
            if (General.checkIfInt(input)) {
                check = Integer.parseInt(input);
            }
        } while (!General.checkIfInt(input) || (check > 2 || check < 1));

        if (check == 1) {
            System.out.printf("Please input the number of days from the starting date: ");
            int days = sc.nextInt();
            sc.nextLine(); // Workaround

            while (days < 1) {
                System.out.printf("Invalid input! The number of days must be a positive integer: ");
                days = sc.nextInt();
                sc.nextLine(); // Workaround
            }

            c.add(Calendar.DATE, days);
            end_date = General.dateToString(c.getTime());

        } else if (check == 2) {
            System.out.printf("Please input the number of weeks from the starting date: ");
            int weeks = sc.nextInt();
            sc.nextLine(); // Workaround

            while (weeks < 1) {
                System.out.printf("Invalid input! The number of weeks must be a positive integer: ");
                weeks = sc.nextInt();
                sc.nextLine(); // Workaround
            }

            c.add(Calendar.DATE, weeks * 7);
            end_date = General.dateToString(c.getTime());
        }

//        System.out.println(starting_date);
//        System.out.println(end_date);
//        sc.close();
        return new String[]{starting_date, end_date};
    }

    public static String[] three() throws ParseException {
        Scanner sc = new Scanner(System.in);
        String input = null;
        int check = 0;

        Calendar c = Calendar.getInstance();

        String end_date = inputEndDate();
        String starting_date = "";

        c.setTime(General.stringToDate(end_date));

        System.out.printf("Please choose the date subtraction unit:\n");
        System.out.printf("          1. Day\n");
        System.out.printf("          2. Week\n");

        do {
            if (input == null) {
                System.out.printf("Your choice: ");
            } else {
                System.out.printf("Invalid input! Only type in 1, or 2: ");
            }
            input = sc.nextLine();
            if (General.checkIfInt(input)) {
                check = Integer.parseInt(input);
            }
        } while (!General.checkIfInt(input) || (check > 2 || check < 1));


        if (check == 1) {
            System.out.printf("Please input the number of days to the end date: ");
            int days = sc.nextInt();
            sc.nextLine(); // Workaround

            while (days < 1) {
                System.out.printf("Invalid input! The number of days must be a positive integer: ");
                days = sc.nextInt();
                sc.nextLine(); // Workaround
            }

            c.add(Calendar.DATE, -1 * days);
            starting_date = General.dateToString(c.getTime());

        } else if (check == 2) {
            System.out.printf("Please input the number of weeks to the end date: ");
            int weeks = sc.nextInt();
            sc.nextLine(); // Workaround

            while (weeks < 1) {
                System.out.printf("Invalid input! The number of weeks must be a positive integer: ");
                weeks = sc.nextInt();
                sc.nextLine(); // Workaround
            }

            c.add(Calendar.DATE, -7 * weeks);
            starting_date = General.dateToString(c.getTime());
        }

//        System.out.println(starting_date);
//        System.out.println(end_date);
//        sc.close();
        return new String[]{starting_date, end_date};
    }
}