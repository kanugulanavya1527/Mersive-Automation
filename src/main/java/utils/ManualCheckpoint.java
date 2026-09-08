package utils;

import java.util.Scanner;

/**
 * Console-driven manual checkpoint helper.
 *
 * Used for baseline scenarios where automation can perform the action but
 * cannot verify the real-world expected result (physical pod display
 * content, or what a remote Teams participant actually hears/sees).
 *
 * Pattern: automate the action -> automate whatever check IS possible ->
 * call one of these methods to hand off to a human tester -> combine the
 * human's answer into the test's final PASS/FAIL. This mirrors the
 * "automate to the checkpoint, then pause" approach agreed with Kishore.
 *
 * NOTE: this blocks on System.in, so it only works when a tester is
 * attending the run interactively -- which matches where this framework is
 * today (run locally / manually), not yet an unattended ADO pipeline agent.
 * Once execution moves to an unattended pipeline agent, pause()/confirm()
 * calls should be swapped for "log + screenshot + continue" so the pipeline
 * doesn't hang waiting on stdin that nobody can answer.
 */
public class ManualCheckpoint {

    private static final Scanner IN = new Scanner(System.in);
    private static final String BAR = "=".repeat(70);

    /**
     * Blocks the script until the tester presses ENTER. Use this mid-sequence,
     * when a human (e.g. the "remote" participant on another laptop) needs to
     * perform an action before the script can continue.
     */
    public static void pause(String testCaseId, String instruction) {
        System.out.println();
        System.out.println(BAR);
        System.out.println("MANUAL ACTION NEEDED -- " + testCaseId);
        System.out.println(BAR);
        System.out.println(instruction);
        System.out.println();
        System.out.print("Press ENTER once done to resume the script... ");
        IN.nextLine();
        System.out.println("[ManualCheckpoint] Resumed.");
        System.out.println(BAR);
    }

    /**
     * Blocks the script and asks the tester to type PASS or FAIL. Use this at
     * the point where automation has done everything it can, and the final
     * expected result can only be judged by a human (physical display
     * content, audible sound, remote participant's view).
     *
     * @return true if the tester typed PASS (case-insensitive), false
     *         (including FAIL, blank, or anything else) otherwise.
     */
    public static boolean confirm(String testCaseId, String question) {
        System.out.println();
        System.out.println(BAR);
        System.out.println("MANUAL VALIDATION -- " + testCaseId);
        System.out.println(BAR);
        System.out.println(question);
        System.out.print("Type PASS or FAIL and press ENTER: ");
        String answer = IN.nextLine().trim();
        boolean passed = answer.equalsIgnoreCase("PASS");
        System.out.println("[ManualCheckpoint] Tester recorded: " + answer);
        System.out.println(BAR);
        return passed;
    }
}