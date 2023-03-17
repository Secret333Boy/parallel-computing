package journal;

import java.util.ArrayList;
import java.util.List;

public class Teacher {
    private final Journal journal;

    public Teacher(Journal journal) {
        this.journal = journal;
    }

    public void addGrades() throws InterruptedException {
        GradeThread gradeThread = new GradeThread(journal);

        gradeThread.start();
        gradeThread.join();
    }
}
