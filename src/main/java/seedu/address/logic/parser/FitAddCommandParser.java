package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.messages.FitAddCommandMessages.MESSAGE_ADD_EXERCISE_NAME_CONFLICTING_PREFIXES;
import static seedu.address.logic.messages.FitAddCommandMessages.MESSAGE_ADD_EXERCISE_VALUES_CONFLICTING_PREFIXES;
import static seedu.address.logic.messages.FitAddCommandMessages.MESSAGE_EXERCISE_NAME_PARAMETER_AND_DEFAULT_PREFIXES_MISSING;
import static seedu.address.logic.messages.FitAddCommandMessages.MESSAGE_INVALID_COMMAND_FORMAT_FITADD;
import static seedu.address.logic.messages.FitAddCommandMessages.MESSAGE_INVALID_INDEX_FITADD;
import static seedu.address.logic.messages.FitAddCommandMessages.MESSAGE_NO_INDEX_FITADD;
import static seedu.address.logic.messages.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXERCISE_ABS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXERCISE_ALL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXERCISE_ARM;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXERCISE_BACK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXERCISE_BREAK_BETWEEN_SETS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXERCISE_CHEST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXERCISE_LEG;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXERCISE_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXERCISE_REPS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXERCISE_SETS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EXERCISE_SHOULDER;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.FitAddCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.exercise.ExerciseToAdd;

/**
 * Parses input arguments and creates a new FitAddCommand object
 */
public class FitAddCommandParser implements Parser<FitAddCommand> {

    private ArgumentMultimap getArgMultiMap(String args) {
        return ArgumentTokenizer.tokenize(args, PREFIX_EXERCISE_NAME, PREFIX_EXERCISE_SETS,
            PREFIX_EXERCISE_REPS, PREFIX_EXERCISE_BREAK_BETWEEN_SETS, PREFIX_EXERCISE_ARM, PREFIX_EXERCISE_LEG,
            PREFIX_EXERCISE_CHEST, PREFIX_EXERCISE_BACK, PREFIX_EXERCISE_SHOULDER, PREFIX_EXERCISE_ABS,
            PREFIX_EXERCISE_ALL);
    }

    private void verifyClientIndexExists(ArgumentMultimap argumentMultimap) throws ParseException {
        if (argumentMultimap.isPreambleEmpty()) {
            throw new ParseException(MESSAGE_NO_INDEX_FITADD);
        }
    }

    private void verifyClientIndexSingleSegment(ArgumentMultimap argumentMultimap) throws ParseException {
        if (argumentMultimap.getPreambleSegmentNumber() != 1) {
            throw new ParseException(MESSAGE_INVALID_COMMAND_FORMAT_FITADD);
        }
    }

    private Index parseIndex(ArgumentMultimap argumentMultimap) throws ParseException {
        // Parse index of client to add exercise to
        Index index;
        try {
            index = ParserUtil.parseIndex(argumentMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(MESSAGE_INVALID_INDEX_FITADD, pe);
        }

        return index;
    }

    private void verifyNoDuplicatePrefixes(ArgumentMultimap argumentMultimap) throws ParseException {
        argumentMultimap.verifyNoDuplicatePrefixesFor(PREFIX_EXERCISE_NAME, PREFIX_EXERCISE_SETS,
            PREFIX_EXERCISE_REPS, PREFIX_EXERCISE_BREAK_BETWEEN_SETS, PREFIX_EXERCISE_ARM, PREFIX_EXERCISE_LEG,
            PREFIX_EXERCISE_CHEST, PREFIX_EXERCISE_BACK, PREFIX_EXERCISE_SHOULDER, PREFIX_EXERCISE_ABS,
            PREFIX_EXERCISE_ALL);
    }

    private void verifyNoArgumentValueForPrefixes(ArgumentMultimap argumentMultimap) throws ParseException {
        if (argumentMultimap.hasArgumentValueForPrefixes(PREFIX_EXERCISE_ARM, PREFIX_EXERCISE_LEG,
            PREFIX_EXERCISE_CHEST, PREFIX_EXERCISE_BACK, PREFIX_EXERCISE_SHOULDER, PREFIX_EXERCISE_ABS,
            PREFIX_EXERCISE_ALL)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT));
        }
    }

    private void verifyNoMissingPrefixes(boolean hasExerciseNamePrefix, boolean hasDefaultExercisePrefixes)
            throws ParseException {
        if (!hasExerciseNamePrefix && !hasDefaultExercisePrefixes) {
            throw new ParseException(MESSAGE_EXERCISE_NAME_PARAMETER_AND_DEFAULT_PREFIXES_MISSING);
        }
    }

    private void verifyNoConflictingPrefixes(boolean hasExerciseNamePrefix, boolean hasExerciseValuesPrefix,
                                             boolean hasDefaultExercisePrefixes)
            throws ParseException {
        if (hasExerciseNamePrefix && hasDefaultExercisePrefixes) {
            throw new ParseException(MESSAGE_ADD_EXERCISE_NAME_CONFLICTING_PREFIXES);
        }

        if (hasDefaultExercisePrefixes && hasExerciseValuesPrefix) {
            throw new ParseException(MESSAGE_ADD_EXERCISE_VALUES_CONFLICTING_PREFIXES);
        }
    }

    private ExerciseToAdd parseExerciseToAdd(ArgumentMultimap argumentMultimap) throws ParseException {
        // If individual exercise details are provided, add that exercise
        String exerciseName = ParserUtil.parseExerciseName(argumentMultimap.getValue(PREFIX_EXERCISE_NAME));
        Optional<Integer> exerciseSets = ParserUtil.parseExerciseSets(argumentMultimap.getValue(PREFIX_EXERCISE_SETS));
        Optional<Integer> exerciseReps = ParserUtil.parseExerciseReps(argumentMultimap.getValue(PREFIX_EXERCISE_REPS));
        Optional<Integer> exerciseBreakBetweenSets =
            ParserUtil.parseExerciseBreakBetweenSets(argumentMultimap.getValue(PREFIX_EXERCISE_BREAK_BETWEEN_SETS));

        return new ExerciseToAdd(exerciseName, exerciseSets, exerciseReps, exerciseBreakBetweenSets);
    }

    private Set<ExerciseToAdd> getDefaultExercises(ArgumentMultimap argumentMultimap) {
        Set<ExerciseToAdd> defaultExercises = new HashSet<>();

        if (argumentMultimap.contains(PREFIX_EXERCISE_ALL)) {
            defaultExercises.addAll(FitAddCommand.DEFAULT_ARM_EXERCISES);
            defaultExercises.addAll(FitAddCommand.DEFAULT_LEG_EXERCISES);
            defaultExercises.addAll(FitAddCommand.DEFAULT_CHEST_EXERCISES);
            defaultExercises.addAll(FitAddCommand.DEFAULT_BACK_EXERCISES);
            defaultExercises.addAll(FitAddCommand.DEFAULT_SHOULDER_EXERCISES);
            defaultExercises.addAll(FitAddCommand.DEFAULT_ABS_EXERCISES);
        } else {
            if (argumentMultimap.contains(PREFIX_EXERCISE_ARM)) {
                defaultExercises.addAll(FitAddCommand.DEFAULT_ARM_EXERCISES);
            }
            if (argumentMultimap.contains(PREFIX_EXERCISE_LEG)) {
                defaultExercises.addAll(FitAddCommand.DEFAULT_LEG_EXERCISES);
            }
            if (argumentMultimap.contains(PREFIX_EXERCISE_CHEST)) {
                defaultExercises.addAll(FitAddCommand.DEFAULT_CHEST_EXERCISES);
            }
            if (argumentMultimap.contains(PREFIX_EXERCISE_BACK)) {
                defaultExercises.addAll(FitAddCommand.DEFAULT_BACK_EXERCISES);
            }
            if (argumentMultimap.contains(PREFIX_EXERCISE_SHOULDER)) {
                defaultExercises.addAll(FitAddCommand.DEFAULT_SHOULDER_EXERCISES);
            }
            if (argumentMultimap.contains(PREFIX_EXERCISE_ABS)) {
                defaultExercises.addAll(FitAddCommand.DEFAULT_ABS_EXERCISES);
            }
        }

        return defaultExercises;
    }

    private Set<ExerciseToAdd> getExercisesToAdd(ArgumentMultimap argumentMultimap, boolean hasExerciseNamePrefix)
            throws ParseException {
        Set<ExerciseToAdd> exercisesToAdd = new HashSet<>();
        if (hasExerciseNamePrefix) {
            exercisesToAdd.add(parseExerciseToAdd(argumentMultimap));
        } else {
            exercisesToAdd.addAll(getDefaultExercises(argumentMultimap));
        }

        return exercisesToAdd;
    }

    /**
     * Parses the given {@code String} of arguments in the context of the FitAddCommand
     * and returns a FitAddCommand object for execution.
     *
     * @param args The string of arguments to be parsed
     * @return The FitAddCommand object for execution
     * @throws ParseException If the user input does not conform to the expected format
     */
    public FitAddCommand parse(String args) throws ParseException {
        requireNonNull(args);

        ArgumentMultimap argumentMultimap = getArgMultiMap(args);

        verifyClientIndexExists(argumentMultimap);
        verifyClientIndexSingleSegment(argumentMultimap);

        Index index = parseIndex(argumentMultimap);

        verifyNoArgumentValueForPrefixes(argumentMultimap);
        verifyNoDuplicatePrefixes(argumentMultimap);

        // Get existence of relevant prefixes
        boolean hasExerciseNamePrefix = argumentMultimap.contains(PREFIX_EXERCISE_NAME);
        boolean hasExerciseValuesPrefix = argumentMultimap.containsAny(PREFIX_EXERCISE_SETS,
            PREFIX_EXERCISE_REPS, PREFIX_EXERCISE_BREAK_BETWEEN_SETS);
        boolean hasDefaultExercisePrefixes = argumentMultimap.containsAny(PREFIX_EXERCISE_ARM, PREFIX_EXERCISE_LEG,
            PREFIX_EXERCISE_CHEST, PREFIX_EXERCISE_BACK, PREFIX_EXERCISE_SHOULDER, PREFIX_EXERCISE_ABS,
            PREFIX_EXERCISE_ALL);

        verifyNoMissingPrefixes(hasExerciseNamePrefix, hasDefaultExercisePrefixes);
        verifyNoConflictingPrefixes(hasExerciseNamePrefix, hasExerciseValuesPrefix, hasDefaultExercisePrefixes);

        return new FitAddCommand(index, getExercisesToAdd(argumentMultimap, hasExerciseNamePrefix));
    }
}
