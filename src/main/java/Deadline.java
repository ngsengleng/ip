public class Deadline extends Task {

    private String datetime;

    public Deadline(String description, String time) {
        super(description);
        datetime = time;
    }

    @Override
    public String toString() {
        String eventMarker = "[D]";
        String timestamp = "(" + datetime.split(" ", 2)[0] + ":"
                + " " + datetime.split(" ", 2)[1] + ")";
        if (isDone) {
            return eventMarker + hasCross + " " + item + " " + timestamp;
        } else {
            return eventMarker + hasNoCross + " " + item + " " + timestamp;
        }
    }
}
