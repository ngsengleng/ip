import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.ListIterator;

public class Duke {
    private static final String END_OF_CONVERSATION = "bye";
    private static List<Task> listOfItems = new ArrayList<Task>();
    private static final String SEPARATOR = "########################";

    /**
     * Beautifies text output by wrapping it around a border.
     *
     * @param input
     */
    private static void outputWrapper(String input) {
        System.out.println(SEPARATOR);
        System.out.println(input);
        System.out.println(SEPARATOR);
    }

    /**
     * Beautifies list of items by wrapping it around a border.
     *
     * @param elements
     */
    private static void outputWrapper(List elements) {
        ListIterator it = elements.listIterator();
        System.out.println(SEPARATOR);
        while (it.hasNext()) {
            Integer number = it.nextIndex() + 1;
            System.out.println(number + ". " + it.next());
        }
        System.out.println(SEPARATOR);
    }

    /**
     * Marks an item on the list as completed.
     * @param input
     */
    private static void markAsDone(String input) {
        String[] keywords = input.split(" ");
        String command = keywords[0];
        Integer idx = keywords.length > 1 ? Integer.parseInt(keywords[1]) - 1 : -1;
        Task task = listOfItems.get(idx);
        task.setDone();
        String completionMessage = "You have successfully completed task " + keywords[1] + ":" + "\n";
        outputWrapper(completionMessage + task);
    }

    /**
     * function for printing the confirmation message for any item added to list
     * @param task
     */
    private static void printConfirmation(Task task) {
        String confirmationMessage = "You have successfully added an item:\n" + task + "\nto the list.\n";
        String numberOfItems = "There " + (listOfItems.size() > 1 ? "are " : "is ") + listOfItems.size()
                                        +  (listOfItems.size() > 1 ? " items " : " item ") + "in the list right now";
        outputWrapper(confirmationMessage + numberOfItems);
    }

    /**
     * Add an item to the list as todo.
     * @param input
     */
    private static void markAsTodo(String input) {
        // split input into command + text and date
        String[] keywords = input.split("/");
        // split input into command and text
        String[] elements = keywords[0].split(" ", 2);

        String command = elements[0];
        String textDescription = elements[1];
        Task todo = new Todo(textDescription);
        listOfItems.add(todo);
        printConfirmation(todo);
    }

    private static void markAsEvent(String input) {
        // split input into command + text and date
        String[] keywords = input.split("/");
        // split input into command and text
        String[] elements = keywords[0].split(" ", 2);

        String time = keywords[1];
        String command = elements[0];
        String textDescription = elements[1];
        Task event = new Event(textDescription, time);
        listOfItems.add(event);
        printConfirmation(event);
    }

    private static void markAsDeadline(String input) {
        // split input into command + text and date
        String[] keywords = input.split("/");
        // split input into command and text
        String[] elements = keywords[0].split(" ", 2);

        String time = keywords[1];
        String command = elements[0];
        String textDescription = elements[1];
        Task deadline = new Deadline(textDescription, time);
        listOfItems.add(deadline);
        printConfirmation(deadline);
    }
    /**
     * Handles the user's input and determines which command should be run.
     *
     * @param input
     */
    private static void handleInput(String input) {
        String command = input.split(" ")[0];
        switch(command) {
            case "items":
                outputWrapper(listOfItems);
                break;
            case "completed":
                markAsDone(input);
                break;
            case "todo":
                markAsTodo(input);
                break;
            case "event":
                markAsEvent(input);
                break;
            case "deadline":
                markAsDeadline(input);
                break;
            default:
                listOfItems.add(new Task(input));
                outputWrapper("added \"" + input + "\" to list");
                break;
        }
    }

    /**
     * Starts the current session for the bot.
     */
    private static void startBot() {
        Scanner i = new Scanner(System.in);
        String input = i.nextLine();

        while (!input.equals(END_OF_CONVERSATION)) {
            handleInput(input);
            input = i.nextLine();
        }
        outputWrapper("Thanks for using me. See you again soon!");
        i.close();
    }

    /**
     * Echos input of the user
     */
    private static void echoInput() {
        Scanner i = new Scanner(System.in);
        String input = i.nextLine();
        while (!input.equals(END_OF_CONVERSATION)) {
            outputWrapper(input);
            input = i.nextLine();
        }
        outputWrapper("Thanks for using me. See you again soon!");
        i.close();

    }
    public static void main(String[] args) {
        System.out.println("Hello this is Jeeves, your personal chatbot. What can i do you for today?");
        startBot();
    }
}
