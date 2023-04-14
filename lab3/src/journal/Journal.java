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

    public void addGrade(Student student, int grade) {
        synchronized (studentGradesMap) {
            if (grade < 0 || grade > 100) throw new RuntimeException("Wrong grade");

            studentGradesMap.get(student).add(grade);
        }
    }

    public List<Group> getGroupList() {
        synchronized (groupList) {
            return new ArrayList<>(groupList);
        }
    }

    public List<Integer> getGrades(Student student) {
        synchronized(studentGradesMap) {
            return this.studentGradesMap.get(student);
        }
    }
}
