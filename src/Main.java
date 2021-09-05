// Main.java
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

public class Main {
    public static String[] basicFlow() throws IOException, ParseException {
        Scanner sc = new Scanner(System.in);
        System.out.printf("\nWelcome to the COVID-19 Data Indexer (CDI) application!" +
                "\nUsing our solution, you can search for specific data from a specific date range and display it under a neat little chart!" +
                "\nLet's begin shall we? (Y/N): ");
        String check = sc.nextLine().toUpperCase(Locale.ROOT);

        System.out.println();

        if (!Objects.equals(check, "Y")) {
            System.out.println("Alright! Come back anytime!\n");
            return new String[]{};
        }

        String[] dates = step1.feature1();
        String starting_date = dates[0];
        String end_date = dates[1];

//        int[] arr = test4.feature2a();
        step2.feature2();


//        testtest.TabularDisplay(list);
//        testExtra.plotWholeChart(arr);
        step3.feature3(starting_date, end_date);
//        sc.close();

        return new String[]{starting_date, end_date};
    }

    public static void end(String starting_date, String end_date) throws IOException, ParseException {
        Scanner sc = new Scanner(System.in);

//        System.out.println("\n________________________________________________________________________________\n");

        System.out.printf("You have successfully ran the program! Next, do you want to: \n");
        System.out.printf("          1. Start from the beginning\n");
        System.out.printf("          2. Change the grouping of the data\n");
        System.out.printf("          3. Cease the program\n");
        System.out.printf("Your choice: ");
        int check = sc.nextInt();
        sc.nextLine(); // Workaround

        System.out.println("");

        if (check == 1) {
            basicFlow();
        } else if (check == 2) {
            //        int[] arr = test4.feature2a();

            step2.feature2();
            step3.feature3(starting_date, end_date);
//            sc.close();
        }
        end(starting_date, end_date);
    }

    public static void main(String[] args) throws IOException, ParseException {
        String[] dates = basicFlow();
        String starting_date = dates[0];
        String end_date = dates[1];
        end(starting_date, end_date);
    }
}
