import java.io.IOException;
import java.text.ParseException;
import java.util.*;



public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        String[] dates = basicFlow1();
        basicFlow2();
        basicFlow3(dates[0], dates[1]);
        end(dates[0], dates[1]);
    }

    public static String[] basicFlow1() throws IOException, ParseException {
        // Create a Scanner instance
        Scanner sc = new Scanner(System.in);
        String check;


        // Display the Welcome message.
        System.out.printf("\nWelcome to the COVID-19 Data Indexer (CDI) application!" +
                "\nUsing our solution, you can search for specific data from a specific date range and display it under a neat little chart!\n");
        do {
            System.out.printf("Let's begin shall we? (Y/N): ");
            check = sc.nextLine().toUpperCase(Locale.ROOT);
        } while (!Objects.equals(check, "N") && !Objects.equals(check, "Y")); // Validate user input, loop until the user input a "Y" or an "N";

        // If user decides not to use the application, print a friendly goodbye message
        if (Objects.equals(check, "N")) {
            System.out.println("\nAlright, goodbye! Stay safe and come back anytime!\n");
            System.exit(0);
        }

        // Print the current step
        System.out.printf("\n____________________________________________________________________________________________________________________\n");
        System.out.printf("Step 1: \n");

        // Call feature1(), where we will filter the data based on Location and Starting/End Dates and export to covid-data2.csv
        String[] dates = Step1.feature1();
        String starting_date = dates[0];
        String end_date = dates[1];

        // Return the dates for future uses
        return new String[]{starting_date, end_date};
    }

    public static void basicFlow2() throws IOException, ParseException {
        // Print the current step
        System.out.printf("____________________________________________________________________________________________________________________\n");
        System.out.printf("Step 2: \n");

        // Call feature2(), where we will group the filtered search in covid-data2.csv into groups and export to covid-data3-csv
        Step2.feature2();
    }

    public static void basicFlow3(String starting_date, String end_date) throws IOException, ParseException {
        // Print the current step
        System.out.printf("____________________________________________________________________________________________________________________\n");
        System.out.printf("Step 3: \n\n");

        // Call feature3(), where we will display the grouped data from covid-data3.csv in Tabular or Chart form, along with a neat overview
        Step3.feature3(starting_date, end_date);

    }

    public static void end(String starting_date, String end_date) throws IOException, ParseException {
        // Create a Scanner instance
        Scanner sc = new Scanner(System.in);

        // Ask the User what they want to do next after having successfully ran the program
        System.out.printf("You have successfully ran the program! Next, do you want to: \n");
        System.out.printf("          1. Start from the beginning (Step 1)\n");
        System.out.printf("          2. Change the grouping of the data (Step 2)\n");
        System.out.printf("          3. Display the Data again (Step 3)\n");
        System.out.printf("          4. Cease the program\n");

        String input = null;
        int check = 0;

        // Validate user's input: when the user inputs a wrong Datatype or a number not within the range, ask again
        do {
            if (input == null) {
                System.out.printf("Your choice: ");
            } else {
                System.out.printf("Invalid input! Only type in 1, 2, 3, or 4: ");
            }
            input = sc.nextLine();
            if (General.checkIfInt(input)) {
                check = Integer.parseInt(input);
            }
        } while (!General.checkIfInt(input) || (check > 4 || check < 1));

        // If the user wants to Start from the beginning, we run the program again from basicFlow1
        if (check == 1) {
            String[] dates = basicFlow1();
            basicFlow2();
            basicFlow3(dates[0], dates[1]);
        // If the user wants to Change the grouping of the Data, we run the program again from basicFlow2
        } else if (check == 2) {
            basicFlow2();
            basicFlow3(starting_date, end_date);
        // If the user wants to Display the grouped Data again, we run the program again from basicFlow3
        } else if (check == 3) {
            basicFlow3(starting_date, end_date);
        // If the user is done with the application, display a goodbye message, and exit the program
        } else {
            System.out.printf("\nAlrighty! Thank you for using our CDI application!\nStay sane and stay safe!\n");
            return;
        }

        // Run the end function again should option 1, 2, or 3 be chosen.
        end(starting_date, end_date);
    }
}