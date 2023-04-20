package journal;

import java.util.ArrayList;
import java.util.List;

public class GradeThread extends Thread {
    private final Journal journal;
    private final Group specifiedGroup;

    public GradeThread(Journal journal, Group specifiedGroup) {
        this.journal = journal;
        this.specifiedGroup = specifiedGroup;
    }

    @Override
    public void run() {
        List<Group> groupList = journal.getGroupList();

        if (specifiedGroup != null) {
            groupList = new ArrayList<>();
            groupList.add(specifiedGroup);
        };

        for (Group group : groupList) {
            List<Student> studentList = group.getStudentsList();

            for (Student student : studentList) {
                journal.addGrade(student, Math.toIntExact(Math.round(Math.random() * 100)));
            }
        }
    }
}
