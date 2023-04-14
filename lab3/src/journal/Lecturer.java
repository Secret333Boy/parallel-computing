package journal;

public class Lecturer {
    private final Journal journal;

    public Lecturer(Journal journal) {
        this.journal = journal;
    }

    public void addGrades() throws InterruptedException {
        GradeThread gradeThread = new GradeThread(journal, null);

        gradeThread.start();
        gradeThread.join();
    }
}
