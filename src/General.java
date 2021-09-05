// General.java
import java.io.*;
import java.text.*;
import java.util.*;

public class General {
    public static String[] splitString(String line) {
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

        return parts;
    }

    public static Date stringToDate(String s) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        Calendar c = Calendar.getInstance();
        c.setTime(sdf.parse(s));
        return c.getTime();
    }

    public static String dateToString(Date d) {
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/yyyy");
        return sdf.format(d);
    }

    public static int countLines(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        int count = 0;
        while (br.readLine() != null) {
            count++;
        }
        return count;
    }

    public static void printCSV(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(filename));
        int count = 0;
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    public static void removeLastLine(String filename) throws IOException { // Scanner
        int limit = countLines(filename);

        BufferedReader br1 = new BufferedReader(new FileReader(filename));
        BufferedWriter bw1 = new BufferedWriter(new FileWriter("tmp.txt"));

        int count = 0;
        String line;
        while ((line = br1.readLine()) != null) {
            count++;
            if (count == limit) {
                bw1.write(String.format("%s", line));
                break;
            } else {
                bw1.write(String.format("%s\n", line));
            }

        }

        br1.close();
        bw1.close();

        BufferedReader br2 = new BufferedReader(new FileReader("tmp.txt"));
        BufferedWriter bw2 = new BufferedWriter(new FileWriter(filename));

        int count2 = 0;
        String line2;
        while ((line2 = br2.readLine()) != null) {
            count2++;
            if (count2 == count) {
                bw2.write(String.format("%s", line2));
                break;
            } else {
                bw2.write(String.format("%s\n", line2));
            }
        }

        br2.close();
        bw2.close();

        File original = new File("tmp.txt");
        original.delete();
    }

    public static boolean checkIfInt(String input) {
        try {
            double isNum = Double.parseDouble(input);
            if (isNum == Math.floor(isNum)) {
//                System.out.println("Input is Integer");
                return true;
            } else {
//                System.out.println("Input is Double");
                return false;
            }
        } catch (Exception e) {
            if (input.toCharArray().length == 1) {
//                System.out.println("Input is Character");
                return false;
            } else {
//                System.out.println("Input is String");
                return false;
            }
        }
    }
}