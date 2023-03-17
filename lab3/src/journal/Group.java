package journal;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private final List<Student> studentsList = new ArrayList<>();

    public void addStudent(Student student) {
        if (studentsList.contains(student)) throw new RuntimeException("Student is already in this group");

        studentsList.add(student);
    }

    public void deleteStudent(Student student) {
        if (!studentsList.contains(student)) throw new RuntimeException("Student doesn't exists in this group");

        studentsList.remove(student);
    }

    public static Group randomGroup(int capacity) {
        Group group = new Group();
        for (int i = 0; i < capacity; i++) {
            group.addStudent(new Student());
        }

        return group;
    }

    public List<Student> getStudentsList() {
        return new ArrayList<>(studentsList);
    }
}
