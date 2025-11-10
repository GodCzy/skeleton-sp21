package IntList;

import static org.junit.Assert.*;
import org.junit.Test;

public class SquarePrimesTest {

    /**
     * Here is a test for isPrime method. Try running it.
     * It passes, but the starter code implementation of isPrime
     * is broken. Write your own JUnit Test to try to uncover the bug!
     */
    @Test
    public void testSquarePrimesSimple() {
        IntList lst = IntList.of(14, 15, 16, 17, 18);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("14 -> 15 -> 16 -> 289 -> 18", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesHardLevel1() {
        IntList lst = IntList.of(3, 7, 2, 10, 1000);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("9 -> 49 -> 4 -> 10 -> 1000", lst.toString());
        assertTrue(changed);
    }

    @Test
    public void testSquarePrimesHardLevel2() {
        IntList lst = IntList.of(13, 29, -17, -7, -100);
        boolean changed = IntListExercises.squarePrimes(lst);
        assertEquals("169 -> 841 -> -17 -> -7 -> -100", lst.toString());
        assertTrue(changed);
    }
}
