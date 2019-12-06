package map;

import main.java.map.Placeable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaceableTest {

    Placeable m1, m2, m3, m4, m5, m6, m7;

    @BeforeEach
    void setUp() {
        m1 = new Placeable(1, 2, 3, 3, 0);
        m2 = new Placeable(0, 1, 5, 5, 0);
        m3 = new Placeable(4, 5, 3, 3, 0);

        m4 = new Placeable(6, 2, 2, 2, 1);
        m5 = new Placeable(7, 7, 2, 2, 1);
        m6 = new Placeable(8, 5, 1, 1, 1);
        m7 = new Placeable(9, 1, 1, 1, 1);
    }

    @Test
    void intersects() {
        // without offset
        assertTrue(m1.intersects(m2));
        assertTrue(m2.intersects(m1));

        assertTrue(m2.intersects(m3));
        assertTrue(m3.intersects(m2));

        assertFalse(m1.intersects(m3));
        assertFalse(m3.intersects(m1));

        // with offset
        assertFalse(m4.intersects(m2));
        assertFalse(m2.intersects(m4));

        assertTrue(m5.intersects(m3)); //m5 has a offset of 1
        assertFalse(m3.intersects(m5)); //m3 has no offset so they dont intersect!

        // offsets are allowed to intersect - y-Axis
        assertFalse(m5.intersects(m6));
        assertFalse(m6.intersects(m5));
        // offsets are allowed to intersect - x-Axis
        assertFalse(m4.intersects(m7));
        assertFalse(m7.intersects(m4));

    }

    @Test
    void contains() {
        assertTrue(m2.contains(m1));
        assertFalse(m1.contains(m2));

        assertFalse(m3.contains(m2));
        assertFalse(m3.contains(m1));
        assertFalse(m1.contains(m3));
        assertFalse(m2.contains(m3));
    }
}