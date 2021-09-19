package week7.assessment.encentral.dao;

import week7.assessment.encentral.entities.Book;
import week7.assessment.encentral.utils.HibernateUtil;

import javax.persistence.Query;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BookDao {
    public static boolean addBook(Book book) {
        return HibernateUtil.doTransaction(session -> session.save(book));
    }

    public static boolean removeBook(Book book) {
        return HibernateUtil.doTransaction(session -> session.delete(book));
    }

    public static Book getBook(int id) {
        AtomicReference<Book> book = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> book.set(session.get(Book.class, id)));

        return book.get();
    }

    public static List<Book> getAllBooks() {
        AtomicReference<List<Book>> books = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> {
            books.set(session.createQuery("FROM Book", Book.class).list());
        });

        return books.get();
    }

    public static boolean isBookExists(Book book) {
        AtomicReference<Boolean> reference = new AtomicReference<>();

        HibernateUtil.doTransaction(session -> {
            Query query = session.createQuery("FROM Book B WHERE title = :theTitle AND author = :theAuthor");
            query.setParameter("theTitle", book.getTitle());
            query.setParameter("theAuthor", book.getAuthor());

            List<Book> books = query.getResultList();
            reference.set(books != null && !books.isEmpty());
        });

        return reference.get();
    }
}
