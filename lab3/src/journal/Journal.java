package journal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Journal {
    private final Map<Student, List<Integer>> studentGradesMap = new HashMap<>();

    private final List<Group> groupList = new ArrayList<>();

    public synchronized void addGroup(Group group) {
        groupList.add(group);

        for (Student student : group.getStudentsList()) {
            if (!studentGradesMap.containsKey(student))
                studentGradesMap.put(student, new ArrayList<>());
        }
    }

    public synchronized void addGrade(Student student, int grade) {
        if (grade < 0 || grade > 100) throw new RuntimeException("Wrong grade");

        studentGradesMap.get(student).add(grade);
    }

    public synchronized List<Group> getGroupList() {
        return new ArrayList<>(groupList);
    }

    public synchronized List<Integer> getGrades(Student student) {
        return this.studentGradesMap.get(student);
    }
}
