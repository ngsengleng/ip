import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Duke {
    private static final String END_OF_CONVERSATION = "bye";
    private static List<Task> listOfItems = new ArrayList<>();
    private static final String SEPARATOR = "########################";
    private static final Path FILE_PATH = Paths.get(System.getProperty("user.dir"), "data/Duke.txt");
    private static Storage storage;
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
        if (elements.size() == 0) {
            System.out.println("You have no items in your list. Add some with [todo], [deadline] or [event]!");
        } else {
            while (it.hasNext()) {
                Integer number = it.nextIndex() + 1;
                System.out.println(number + ". " + it.next());
            }
        }

        System.out.println(SEPARATOR);
    }

    /**
     * Marks an item on the list as completed.
     *
     * @param input
     */
    private static void markAsDone(String input) {
        String[] keywords = input.split(" ");
        String command = keywords[0];
        Integer idx = keywords.length > 1 ? Integer.parseInt(keywords[1]) - 1 : -1;
        Task task = listOfItems.get(idx);
        task.setDone();
        String completionMessage = String.format("You have successfully completed task %s:\n", keywords[1]);
        outputWrapper(completionMessage + task);
    }

    /**
     * Prints the confirmation message for any item added to list.
     *
     * @param task
     */
    private static void printAdditionConfirmation(Task task) {
        String confirmationMessage = "You have successfully added an item:\n" + task + "\nto the list.\n";

        String numberOfItems = String.format("There %s %s %s in the list right now",
                listOfItems.size() != 1 ? "are" : "is",
                listOfItems.size(),
                listOfItems.size() != 1 ? "items" : "item");
        outputWrapper(confirmationMessage + numberOfItems);
    }

    /**
     * Prints the confirmations message for any item deleted from the list.
     *
     * @param task
     */
    private static void printDeletionConfirmation(Task task) {
        String confirmationMessage = "You have successfully deleted an item:\n" + task + "\nfrom the list.\n";

        String numberOfItems = String.format("There %s %s %s in the list right now",
                listOfItems.size() != 1 ? "are" : "is",
                listOfItems.size(),
                listOfItems.size() != 1 ? "items" : "item");
        outputWrapper(confirmationMessage + numberOfItems);
    }
    /**
     * Adds an item to the list as todo.
     *
     * @param input
     */
    private static void markAsTodo(String input) throws DukeException {
        // split input into command and text
        String[] elements = input.split(" ", 2);
        if (elements.length == 1) {
            throw new DukeException("you need to describe the todo in format: todo (description)!");
        }
        String textDescription = elements[1];
        Task todo = new Todo(textDescription);
        listOfItems.add(todo);
        printAdditionConfirmation(todo);
    }

    /**
     * Adds an item to the list as an event.
     *
     * @param input
     */
    private static void markAsEvent(String input) throws DukeException {
        String[] keywords = input.split(" ", 2);
        if (keywords.length == 1) {
            throw new DukeException("you need to describe your event to me in format: event (description)!");
        }
        // split input into command and text
        String[] elements = keywords[1].split(" /at ", 2);
        if (elements.length == 1) {
            throw new DukeException("you need to tell me the time in format: (task) /at (time)!");
        }
        String time = elements[1];
        String textDescription = elements[0];
        Task event = new Event(textDescription, time);
        listOfItems.add(event);
        printAdditionConfirmation(event);
    }
    /**
     * Adds an item to the list as deadline.
     *
     * @param input
     */
    private static void markAsDeadline(String input) throws DukeException {
        String[] keywords = input.split(" ", 2);
        if (keywords.length == 1) {
            throw new DukeException("you need to describe your deadline to me in format: deadline (description)!");
        }
        String[] elements = keywords[1].split(" /by ", 2);
        if (elements.length == 1) {
            throw new DukeException("you need to tell me the time in format: (task) /by (time)!");
        }
        String time = elements[1];
        String textDescription = elements[0];
        Task deadline = new Deadline(textDescription, time);
        listOfItems.add(deadline);
        printAdditionConfirmation(deadline);
    }

    /**
     * Deletes an item from the list by passing the index of the task to be deleted.
     *
     * @param input
     * @throws DukeException
     */
    private static void deleteItem(String input) throws DukeException {
        try {
            Integer idx = Integer.parseInt(input.split(" ", 2)[1]) - 1;
            Task item = listOfItems.get(idx);
            listOfItems.remove(item);
            printDeletionConfirmation(item);
        } catch (NumberFormatException e) {
            throw new DukeException("you need to choose a number from the list in the form: delete (list index)!");
        } catch (IndexOutOfBoundsException e) {
            throw new DukeException("you need to pick an item from within the list!");
        }
    }

    /**
     * Handles invalid commands from the user.
     *
     * @throws DukeException
     */
    private static void markAsInvalid(String input) throws DukeException {
        if (input.equals("")) {
            throw new DukeException("you need to type something!");
        } else {
            throw new DukeException("your command is invalid!");
        }
    }

    /**
     * Handles the user's input and determines which command should be run.
     *
     * @param input
     */
    private static void handleInput(String input) {
        String command = input.split(" ")[0];
        try {
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
                case "delete":
                    deleteItem(input);
                    break;
                default:
                    markAsInvalid(input);
                    break;
            }
        } catch (DukeException e) {
            outputWrapper(e.getMessage());
        }

    }

    /**
     * Starts the current session for the bot.
     */
    private static void startBot()  {
        try {
            if (Files.notExists(FILE_PATH)) {
                File f = new File(FILE_PATH.toString());
                f.getParentFile().mkdirs();
                f.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("An error occurred when opening the file. Try rerunning Duke again.");
        }
        storage = new Storage(FILE_PATH);
        listOfItems = storage.load();
        Scanner i = new Scanner(System.in);
        String input = i.nextLine();

        while (!input.equals(END_OF_CONVERSATION)) {
            handleInput(input);
            input = i.nextLine();
        }

        try {
            storage.save(listOfItems);
        } catch (IOException e) {

        }

        outputWrapper("Thanks for using me. See you again soon!");
        i.close();
    }

    /**
     * Echos input of the user.
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
