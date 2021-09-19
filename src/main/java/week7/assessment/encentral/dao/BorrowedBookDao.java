package week7.assessment.encentral.dao;

import week7.assessment.encentral.entities.Book;
import week7.assessment.encentral.entities.BorrowedBook;
import week7.assessment.encentral.entities.Student;
import week7.assessment.encentral.utils.HibernateUtil;

import javax.persistence.Query;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BorrowedBookDao {
    public static boolean addBorrowedBook(BorrowedBook borrowedBook) {
        return HibernateUtil.doTransaction(session -> session.save(borrowedBook));
    }

    public static boolean removeBorrowedBook(BorrowedBook borrowedBook) {
        return HibernateUtil.doTransaction(session -> session.delete(borrowedBook));
    }

    public static BorrowedBook getBorrowedBook(int id) {
        AtomicReference<BorrowedBook> borrowedBook = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> borrowedBook.set(session.get(BorrowedBook.class, id)));

        return borrowedBook.get();
    }

    public static BorrowedBook getBorrowedBook(Book book, Student student) {
        AtomicReference<BorrowedBook> reference = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> {
            Query query = session.createQuery("FROM BorrowedBook WHERE student_id = :studentId AND book_id = :bookId");
            query.setParameter("studentId", student.getId());
            query.setParameter("bookId", book.getId());

            List<BorrowedBook> borrowedBooks = query.getResultList();
            if (borrowedBooks == null || borrowedBooks.isEmpty())
                reference.set(null);
            else
                reference.set(borrowedBooks.get(0));
        });

        return reference.get();
    }

    public static List<BorrowedBook> getAllBooks() {
        AtomicReference<List<BorrowedBook>> borrowedBooks = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> {
            borrowedBooks.set(session.createQuery("FROM BorrowedBook", BorrowedBook.class).list());
        });

        return borrowedBooks.get();
    }

    public static boolean studentHaveBorrowed(Student student) {
        AtomicReference<Boolean> reference = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> {
            Query query = session.createQuery("FROM BorrowedBook WHERE student_id = :studentId");
            query.setParameter("studentId", student.getId());

            List<Student> students = query.getResultList();
            reference.set(students != null && !students.isEmpty());
        });

        return reference.get();
    }
}
