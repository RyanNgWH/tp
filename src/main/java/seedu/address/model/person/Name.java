package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;
import static seedu.address.model.person.messages.NameMessages.MESSAGE_CONSTRAINTS;
import static seedu.address.model.person.messages.NameMessages.VALIDATION_REGEX;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name extends Attribute<String> {

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        super(name);
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_CONSTRAINTS);
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    /**
     * Determines if the name value stored is a match with a specified string.
     * Returns true if specified value is a substring of the name value stored.
     *
     * @param otherValue Other value to check against
     *
     * @return True if specified value is a match, False otherwise
     */
    @Override
    public boolean isMatch(Object otherValue) {
        if (!(otherValue instanceof String)) {
            return false;
        }

        String other = (String) otherValue;

        return this.getValue().trim().toLowerCase().contains(other.trim().toLowerCase());
    }

    @Override
    public String toString() {
        return this.getValue();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return this.getValue().equals(otherName.getValue());
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }

}
