package duke;

import java.util.ListIterator;

/**
 * This class contains methods for printing information from the Tasklist to the command line.
 */
public class Ui {
    public Ui() {

    }

    public String listToPrintableString(Tasklist elements) {
        ListIterator<Task> it = elements.toIterable();
        if (elements.size() == 0) {
            return "You have no items in your list. Add some with [todo], [deadline] or [event]!";
        } else {
            String concatString = "";
            while (it.hasNext()) {
                Integer number = it.nextIndex() + 1;
                concatString = concatString + number + ". " + it.next() + "\n";
            }
            return concatString;
        }
    }

    public String printAdditionConfirmation(Task task, Tasklist listOfItems) {
        String confirmationMessage = "You have successfully added an item:\n" + task + "\nto the list.\n";

        String numberOfItems = String.format("There %s %s %s in the list right now",
                listOfItems.size() != 1 ? "are" : "is",
                listOfItems.size(),
                listOfItems.size() != 1 ? "items" : "item");
        return confirmationMessage + numberOfItems;
    }

    public String printDeletionConfirmation(Task task, Tasklist listOfItems) {
        String confirmationMessage = "You have successfully deleted an item:\n" + task + "\nfrom the list.\n";

        String numberOfItems = String.format("There %s %s %s in the list right now",
                listOfItems.size() != 1 ? "are" : "is",
                listOfItems.size(),
                listOfItems.size() != 1 ? "items" : "item");
        return confirmationMessage + numberOfItems;
    }
}
