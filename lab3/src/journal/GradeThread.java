package journal;

import java.util.List;

public class GradeThread extends Thread {
    private final Journal journal;

    public GradeThread(Journal journal) {
        this.journal = journal;
    }

    @Override
    public void run() {
        List<Group> groupList = journal.getGroupList();

        for (Group group : groupList) {
            List<Student> studentList = group.getStudentsList();

            for (Student student : studentList) {
                journal.addGrade(student, Math.toIntExact(Math.round(Math.random() * 100)));
            }
        }
    }
}
