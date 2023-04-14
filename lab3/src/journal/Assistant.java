package journal;

public class Assistant {
    private final Journal journal;

    private final Group specifiedGroup;

    public Assistant(Journal journal, Group specifiedGroup) {
        this.journal = journal;
        this.specifiedGroup = specifiedGroup;
    }

    public void addGrades() throws InterruptedException {
        GradeThread gradeThread = new GradeThread(journal, specifiedGroup);

        gradeThread.start();
        gradeThread.join();
    }
}
