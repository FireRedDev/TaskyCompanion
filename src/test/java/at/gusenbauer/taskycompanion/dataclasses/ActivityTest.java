package at.gusenbauer.taskycompanion.dataclasses;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ActivityTest {

    @Test
    public void TestActivityGeneration() {
        Activity test = new Activity("Meeting Steiermark", "Familie", "Reisen", 60 * 20, "Weinviertel", LocalDate.of(2020, 6, 27), Color.ORANGE);
        assertTrue(test.getDueDate().equals(LocalDate.of(2020, 6, 27)) && test.getDescription().equals("Meeting Steiermark") &&
                test.getDuration() == 1200 && test.getTags().isEmpty() == true && test.getProject().equals("Reisen") && test.getCity().equals("Weinviertel") && test.getColor().equals(Color.ORANGE));
    }
}