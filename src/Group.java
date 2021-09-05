public class Group {
    int group;
    String starting_date;
    String end_date;
    long value;

    public Group() {
    }

    public Group(int g, String s, String e, long v) {
        group = g;
        starting_date = s;
        end_date = e;
        value = v;
    }

    public String toString() {
        return String.format("%d,%s,%s,%d", group, starting_date, end_date, value);
    }
}
