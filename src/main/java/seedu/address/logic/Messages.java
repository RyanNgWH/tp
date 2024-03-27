package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command, please type 'help' for possible commands";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_INVALID_PARAMETER_FORMAT = "Invalid parameter format! \n%1$s";
    public static final String MESSAGE_NAME_PARAMETER_MISSING = "Name parameter missing! \n%1$s";
    public static final String MESSAGE_PHONE_PARAMETER_MISSING = "Phone number parameter missing! \n%1$s";
    public static final String MESSAGE_NO_PARAMETERS = "No parameters specified! \n%1$s";
    public static final String MESSAGE_NO_INDEX = "No index specified! \n%1$s";
    public static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "Invalid index provided. \n%1$s";
    public static final String MESSAGE_PERSONS_FOUND_OVERVIEW = "%1$d clients found!";
    public static final String MESSAGE_NO_CLIENTS_FOUND = "No clients found!";
    public static final String MESSAGE_ONE_CLIENT_FOUND = "1 client found!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_NO_CLIENTS_TO_LIST = "No clients to list!";
    public static final String MESSAGE_ONE_CLIENT_LISTED = "1 client listed!";
    public static final String MESSAGE_ALL_CLIENTS_LISTED = "%1$d clients listed!";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Email: ")
                .append(person.getEmail())
                .append("; Address: ")
                .append(person.getAddress())
                .append("; Note: ")
                .append(person.getNote())
                .append("; Tags: ");
        person.getTags().forEach(builder::append);
        return builder.toString();
    }

}
