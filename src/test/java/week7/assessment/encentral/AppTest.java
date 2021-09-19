package week7.assessment.encentral;

import  static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import week7.assessment.encentral.dao.BookDao;
import week7.assessment.encentral.dao.BorrowedBookDao;
import week7.assessment.encentral.dao.StudentDao;
import week7.assessment.encentral.entities.Book;
import week7.assessment.encentral.entities.BorrowedBook;
import week7.assessment.encentral.entities.Student;

import java.util.List;

public class AppTest {
    @BeforeClass
    public static void setup() {
        // Add some students
        StudentDao.addStudent(new Student("Imran Abdulmalik", "200"));
        StudentDao.addStudent(new Student("Abdulmalik Imran", "400"));

        // Add some books
        BookDao.addBook(new Book("The Journey of an Encentral Intern", "Intern"));
        BookDao.addBook(new Book("The Journey of an Encentral Intern 2nd Edition", "Intern"));
    }

    @Test
    public void testBooksAdded() {
        List<Book> books = BookDao.getAllBooks();

        assertEquals(2, books.size());

        Book book1 = books.get(0);
        Book book2 = books.get(1);

        assertNotNull(book1);
        assertNotNull(book2);
        assertEquals("Intern", book1.getAuthor());
        assertEquals("The Journey of an Encentral Intern 2nd Edition", book2.getTitle());
    }

    @Test
    public void testStudentsAdded() {
        List<Student> students = StudentDao.getAllStudents();

        assertEquals(2, students.size());

        Student student1 = students.get(0);
        Student student2 = students.get(1);

        assertNotNull(student1);
        assertNotNull(student2);
        assertEquals("200", student1.getLevel());
        assertEquals("Abdulmalik Imran", student2.getFullName());
    }

    @Test
    public void testBorrowBook() {
        Book book = BookDao.getBook(1);
        Student student = StudentDao.getStudent(1);

        // borrow book
        BorrowedBookDao.addBorrowedBook(new BorrowedBook(book, student));

        assertTrue(BorrowedBookDao.studentHaveBorrowed(student));

        // return book
        BorrowedBook borrowedBook = BorrowedBookDao.getBorrowedBook(book, student);
        assertNotNull(borrowedBook);
        assertTrue(BorrowedBookDao.removeBorrowedBook(borrowedBook));

        // student can borrow again
        assertFalse(BorrowedBookDao.studentHaveBorrowed(student));
    }

    @Test
    public void testReturnBook() {
        Book book = BookDao.getBook(1);
        Student student = StudentDao.getStudent(1);

        // return book
        BorrowedBook borrowedBook = BorrowedBookDao.getBorrowedBook(book, student);
        assertNotNull(borrowedBook);
        assertTrue(BorrowedBookDao.removeBorrowedBook(borrowedBook));

        // student can borrow again
        assertFalse(BorrowedBookDao.studentHaveBorrowed(student));
    }
}













