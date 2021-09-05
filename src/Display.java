import java.util.ArrayList;

public class Display {
    ArrayList<Group> groups;
    int type;

    public Display() {
    }

    public Display(ArrayList<Group> g, int t) {
        groups = g;
        type = t;
    }

    public void display() {
        System.out.println("");
    }
}

// Child method inherits from parent class
class TabularDisplay extends Display {

    public TabularDisplay() {
    }

    public TabularDisplay(ArrayList<Group> g, int t) {
        super(g, t);
    }

    // Set up the format of the table in console
    public void display() {
        if (type == 1) {
            System.out.println("+------------------------------------------------------------------------+");
            System.out.println("|       Group      |          Range          |     Value (New Total)     |");
            System.out.println("+------------------------------------------------------------------------+");
        } else if (type == 2) {
            System.out.println("+------------------------------------------------------------------------+");
            System.out.println("|       Group      |          Range          |       Value (Up To)       |");
            System.out.println("+------------------------------------------------------------------------+");
        }

        // Looping through the data needs to be displayed
        for (Group group : groups) {
            // Align items to fit intable
            System.out.printf("|%10d        | %10s - %10s |  %15s          |\n", group.group, group.starting_date, group.end_date, group.value);
        }
        // Bottom border of the table
        System.out.println("+------------------------------------------------------------------------+");
    }
}

class ChartDisplay extends Display {
    public ChartDisplay() {
    }

    public ChartDisplay(ArrayList<Group> g, int t) {
        super(g, t);
    }

    public void display() {
        long[] numValues = new long[groups.size()];

        for (int i = 0; i < groups.size(); i++) {
            numValues[i] = groups.get(i).value;
        }
        // Using 2 dimensional array to store data of Y and X axis
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
        Step3.addChartElements(twoArr, numValues);

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
