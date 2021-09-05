import java.text.*;
import java.util.*;
import java.io.*;

public class Step1 {

    // Basically contains the driver code of the entire first step
    public static String[] feature1() throws IOException, ParseException {
        // Create a System.in Scanner instance
        Scanner sc = new Scanner(System.in);

        // Declare and/or initialise variables used later on
        int check = 0, choice = 0;
        String input;
        String starting_date = "";
        String end_date = "";

        // At the end of this method, Restart the Search from the Beginning if the user chooses option 3
        do {
            // Declare and/or initialise variables used later on
            String geoArea = chooseGeoArea();
            boolean validDate = true;
            boolean validSDate = true, validEDate = true;

            // Prompts the user to input a valid Date Range. Should the range be invalid (either date is invalid or
            // both are), the user will be shown the associated error messages and prompted to re-input dates
            do {
                if (!validDate) {
                    System.out.printf("Please input valid dates!\n");
                }
                System.out.printf("How would you like to select your date range?\n");
                System.out.printf("          1. Start Date - End Date\n");
                System.out.printf("          2. Start Date - Days/Weeks After\n");
                System.out.printf("          3. Days/Weeks Before - End Date\n");

                input = null;

                // If the input is not an integer or is not in the given range, ask for the input again
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

                // Declare and/or initialise variables used later on
                starting_date = "";
                end_date = "";


                // Based on the input, the correct method will be called to gather dates. More on these methods below
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

                // Print an empty line (for formatting purposes only)
                System.out.println();

                // Check if the current Location and Starting Date are valid choices. If not, return error
                if (!checkValidDate(geoArea, starting_date)) {
                    System.out.println("ERROR: Invalid starting date!\n");
                    validSDate = false;
                }

                // Check if the current Location and End Date are valid choices. If not, return error
                if (!checkValidDate(geoArea, end_date)) {
                    System.out.println("ERROR: Invalid end date!\n");
                    validEDate = false;
                }

                // Should either or both of the above be invalid, loop through the date selection step again
                // This time, an additional message will be shown
                validDate = validSDate && validEDate;

            } while (!validDate);

            // Call the search function to find the data the user is looking for from covid-data.csv and export the data
            // to covid-data2.csv
            search("covid-data.csv", "covid-data2.csv", geoArea, starting_date, end_date);

            // Tell the user that the search has been completed, and ask them what they want to do next
            System.out.printf("___________________________________________________________________________________________                         ");
            System.out.printf("\nYour search has been successfully ran with the following date range: %s - %s!\nDo you want to: \n", starting_date, end_date);
            System.out.printf("          1. Print out the current query, then continue to the sorting step (Step 2)\n");
            System.out.printf("          2. Continue to the next step (Step 2)\n");
            System.out.printf("          3. Restart the Search from the Beginning\n");

            input = null;

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
                    check = Integer.parseInt(input);
                }
            } while (!General.checkIfInt(input) || (check > 3 || check < 1));
        } while (check == 3);

        // Print an empty line (for formatting purposes only)
        System.out.println();

        // If the user chooses option 1, prints out the filtered data and continue
        if (check == 1) {
            System.out.println("\nYour search result is as follows: ");
            General.printCSV("covid-data2.csv");
            System.out.println();
        }

        return new String[]{starting_date, end_date};
    }

    // Read data from file1, after having filtered the data with the geoArea and the starting/end dates, export to file2
    public static void search(String file1, String file2, String geoArea, String starting_date, String end_date) throws IOException, ParseException {
        BufferedReader br = new BufferedReader(new FileReader(file1));
        PrintWriter pw = new PrintWriter(new FileWriter(file2, false));

        br.readLine(); // Skip the first line to prevent errors.

        String line;

        System.out.println();
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");

            // Convert the current date in String type to Date type to perform conditions below
            Date current_date = General.stringToDate(parts[3]);

            // Write to file2 only if the geoArea matches with the location at 3rd column
            if ((Objects.equals(parts[2].toUpperCase(Locale.ROOT), geoArea.toUpperCase(Locale.ROOT))) &&
               // AND (starting_date <= current_date <= end_date)
               (((current_date.equals(General.stringToDate(starting_date))) || (current_date.after(General.stringToDate(starting_date))))
                  && ((current_date.before(General.stringToDate(end_date))) || (current_date.equals(General.stringToDate(end_date)))))) {
                pw.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s\n", parts[0], parts[1], parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]));
            }
        }

        br.close();
        pw.close();

        // After exporting to file2, an unnecessary empty last line is added, therefore the line is removed
        General.removeLastLine(file2);
    }

    // Method that allows user to input Starting Date step by step
    public static String inputStartingDate() {
        // Create a System.in Scanner instance
        Scanner sc = new Scanner(System.in);

        // Declare and/or initialise variables used later on
        String input;
        int starting_day = 0, starting_month = 0, starting_year = 0;

        System.out.printf("Please input the starting date step by step (Positive integer value only): \n");

        // If the input is not an integer or is not in the reasonable date range (1 - 31), ask for the input again
        do {
            System.out.printf("          - Day: ");

            input = sc.nextLine();
            if (General.checkIfInt(input)) {
                starting_day = Integer.parseInt(input);
            }
        } while (!General.checkIfInt(input) || (starting_day > 31 || starting_day < 1));

        // If the input is not an integer or is not in the reasonable month range (1 - 12), ask for the input again
        do {
            System.out.printf("          - Month: ");

            input = sc.nextLine();
            if (General.checkIfInt(input)) {
                starting_month = Integer.parseInt(input);
            }
        } while (!General.checkIfInt(input) || (starting_month > 12 || starting_month < 1));

        // If the input is not an integer or is not in the reasonable year range (1 - 9999), ask for the input again
        do {
            System.out.printf("          - Year: ");

            input = sc.nextLine();
            if (General.checkIfInt(input)) {
                starting_year = Integer.parseInt(input);
            }
        } while (!General.checkIfInt(input) || (starting_year > 9999 || starting_year < 1));

        System.out.println();

        // Return the string under the format of M/d/yyyy
        return String.format("%d/%d/%d", starting_month, starting_day, starting_year);

    }

    // Method that allows user to input End Date step by step. As it is Identical to inputStartingDate() above,
    // save for the variable names and word changes, please read the comment to check for the logic
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

    // Method that allows the user to input a Location (Geographical Area - a Country or a Continent)
    public static String chooseGeoArea() throws IOException {
        // Create a System.in Scanner instance
        Scanner sc = new Scanner(System.in);

        // Line 262 - 272: Read through the original csv file to find all listed Locations and store them into a set
        BufferedReader br = new BufferedReader(new FileReader("covid-data.csv"));
        Set<String> geo_list = new HashSet<String>();
        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            geo_list.add(parts[2].toUpperCase(Locale.ROOT));
        }

        br.close();

        String geoArea = "";

        // Ask the user to input a Continent/Country. If the inputted location is not found within the set created above
        // prompts the user to re-input the location
        do {
            System.out.printf("\nPlease input a valid Continent or Country to continue: ");
            geoArea = sc.nextLine();
        } while (!geo_list.contains(geoArea.toUpperCase(Locale.ROOT)));


        System.out.println();

        // Return the valid location
        return geoArea;
    }

    // Method that allows the user to check if a location and date combination is valid
    public static boolean checkValidDate(String location, String date) throws IOException {
        // Line 292 - 302: From the given location, find all the dates that goes with it
        BufferedReader br = new BufferedReader(new FileReader("covid-data.csv"));
        Set<String> locationAndDate = new HashSet<String>();
        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",");
            locationAndDate.add(String.format("%s,%s", parts[2].toUpperCase(Locale.ROOT), parts[3].toUpperCase(Locale.ROOT)));
        }

        br.close();

        // Check if the given location and date is a valid combination. Return true if so, and vice versa.
        return locationAndDate.contains(String.format("%s,%s", location.toUpperCase(Locale.ROOT), date.toUpperCase(Locale.ROOT)));
    }

    // The first method for the user to input their dates
    public static String[] one() {
        String starting_date = inputStartingDate();
//        System.out.println(starting_date);
        String end_date = inputEndDate();
//        System.out.println(end_date);

        return new String[]{starting_date, end_date};
    }

    // The second method for the user to input their dates
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

        // If the input is not an integer or is not in the given range, ask for the input again
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

        input = null;
        int days = 0, weeks = 0;

        // If the input is not an integer or is not positive, ask for the input again
        if (check == 1) {
            do {
                if (input == null) {
                    System.out.printf("Please input the number of days from the starting date: ");
                } else {
                    System.out.printf("Invalid input! Only type in positive integers: ");
                }
                input = sc.nextLine();
                if (General.checkIfInt(input)) {
                    days = Integer.parseInt(input);
                }
            } while (!General.checkIfInt(input) || (days < 1));

            c.add(Calendar.DATE, days);
            end_date = General.dateToString(c.getTime());

        } else if (check == 2) {
            do {
                if (input == null) {
                    System.out.printf("Please input the number of days from the starting date: ");
                } else {
                    System.out.printf("Invalid input! Only type in positive integers: ");
                }
                input = sc.nextLine();
                if (General.checkIfInt(input)) {
                    weeks = Integer.parseInt(input);
                }
            } while (!General.checkIfInt(input) || (weeks < 1));

            c.add(Calendar.DATE, weeks * 7);
            end_date = General.dateToString(c.getTime());
        }

//        System.out.println(starting_date);
//        System.out.println(end_date);
//        sc.close();
        return new String[]{starting_date, end_date};
    }

    // The third method for the user to input their dates. Similar to method two() above.
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

        // If the input is not an integer or is not in the given range, ask for the input again
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

        input = null;
        int days = 0, weeks = 0;

        // If the input is not an integer or is not positive, ask for the input again
        if (check == 1) {
            do {
                if (input == null) {
                    System.out.printf("Please input the number of days to the starting date: ");
                } else {
                    System.out.printf("Invalid input! Only type in positive integers: ");
                }
                input = sc.nextLine();
                if (General.checkIfInt(input)) {
                    days = Integer.parseInt(input);
                }
            } while (!General.checkIfInt(input) || (days < 1));

            c.add(Calendar.DATE, -1 * days);
            starting_date = General.dateToString(c.getTime());

        } else if (check == 2) {
            do {
                if (input == null) {
                    System.out.printf("Please input the number of days to the starting date: ");
                } else {
                    System.out.printf("Invalid input! Only type in positive integers: ");
                }
                input = sc.nextLine();
                if (General.checkIfInt(input)) {
                    weeks = Integer.parseInt(input);
                }
            } while (!General.checkIfInt(input) || (weeks < 1));

            c.add(Calendar.DATE, weeks * -7);
            starting_date = General.dateToString(c.getTime());
        }

//        System.out.println(starting_date);
//        System.out.println(end_date);
//        sc.close();
        return new String[]{starting_date, end_date};
    }
}
