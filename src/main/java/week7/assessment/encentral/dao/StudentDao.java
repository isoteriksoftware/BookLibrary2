package week7.assessment.encentral.dao;

import week7.assessment.encentral.entities.Student;
import week7.assessment.encentral.utils.HibernateUtil;

import javax.persistence.Query;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class StudentDao {
    public static boolean addStudent(Student student) {
        return HibernateUtil.doTransaction(session -> session.save(student));
    }

    public static boolean removeStudent(Student student) {
        return HibernateUtil.doTransaction(session -> session.delete(student));
    }

    public static Student getStudent(int id) {
        AtomicReference<Student> student = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> student.set(session.get(Student.class, id)));

        return student.get();
    }

    public static List<Student> getAllStudents() {
        AtomicReference<List<Student>> students = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> {
            students.set(session.createQuery("FROM Student", Student.class).list());
        });

        return students.get();
    }

    public static boolean isStudentExists(Student student) {
        AtomicReference<Boolean> reference = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> {
            Query query = session.createQuery("FROM Student WHERE full_name = :fullName AND level = :theLevel");
            query.setParameter("fullName", student.getFullName());
            query.setParameter("theLevel", student.getLevel());

            List<Student> students = query.getResultList();
            reference.set(students != null && !students.isEmpty());
        });

        return reference.get();
    }

    public static Student getStudentByFullName(String fullName) {
        AtomicReference<Student> reference = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> {
            Query query = session.createQuery("FROM Student WHERE full_name = :fullName");
            query.setParameter("fullName", fullName);

            List<Student> students = query.getResultList();
            if (students == null || students.isEmpty())
                reference.set(null);
            else
                reference.set(students.get(0));
        });

        return reference.get();
    }
}
