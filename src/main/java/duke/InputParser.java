package duke;

public class InputParser {
    public InputParser() {

    }

    public String getCommand(String input) {
        return input.split(" ")[0];
    }


}
