package seedu.address.logic.commands;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import static seedu.address.logic.messages.NoteCommandMessages.MESSAGE_ADD_NOTE_SUCCESS;
import static seedu.address.logic.messages.NoteCommandMessages.MESSAGE_DELETE_NOTE_SUCCESS;
import static seedu.address.logic.messages.NoteCommandMessages.MESSAGE_INVALID_INDEX_NOTE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Note;
import seedu.address.model.person.Person;

/**
 * Changes the note of an existing person in the address book.
 */
public class NoteCommand extends Command {

    private final Index index;
    private final Note note;

    /**
     * Constructs a NoteCommand object with the person index and note description
     *
     * @param index of the person in the filtered person list to edit the note
     * @param note  of the person to be updated to
     */
    public NoteCommand(Index index, Note note) {
        requireAllNonNull(index, note);

        this.index = index;
        this.note = note;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        //@@author bgopi23
        assert (model != null);
        //@@author
        List<Person> lastShownList = model.getFilteredPersonList();
        int listIndex = this.index.getZeroBased();

        if (listIndex >= lastShownList.size()) {
            throw new CommandException(MESSAGE_INVALID_INDEX_NOTE);
        }

        Person personToEdit = lastShownList.get(listIndex);

        Person editedPerson = new Person(
                personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getWeights(),
                personToEdit.getHeight(), this.note, personToEdit.getTags(), personToEdit.getExerciseSet());

        model.setPerson(personToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * Generates a command execution success message based on whether
     * the note is added to or removed from
     * {@code personToEdit}.
     */
    private String generateSuccessMessage(Person personToEdit) {
        String message = !this.note.getValue().isEmpty() ? MESSAGE_ADD_NOTE_SUCCESS : MESSAGE_DELETE_NOTE_SUCCESS;
        return String.format(message, personToEdit.getFormattedMessage());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NoteCommand)) {
            return false;
        }

        NoteCommand e = (NoteCommand) other;
        return this.index.equals(e.index) && this.note.equals(e.note);
    }

    /**
     * Returns the index of the object.
     */
    public Index getIndex() {
        return this.index;
    }
}
