package journal;

public class Main {
    public static void main(String[] args) {
        Group group1 = Group.randomGroup(50);
        Group group2 = Group.randomGroup(30);
        Group group3 = Group.randomGroup(40);

        Journal journal = new Journal();

        journal.addGroup(group1);
        journal.addGroup(group2);
        journal.addGroup(group3);

        Lecturer lecturer = new Lecturer(journal);
        Assistant assistant1 = new Assistant(journal, group1);
        Assistant assistant2 = new Assistant(journal, group2);
        Assistant assistant3 = new Assistant(journal, group3);

        for (int i = 0; i < 10; i++) {
            try {
                lecturer.addGrades();
                assistant1.addGrades();
                assistant2.addGrades();
                assistant3.addGrades();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(journal.getGrades(group1.getStudentsList().get(0)));
    }
}
