package week7.assessment.encentral;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import week7.assessment.encentral.dao.BookDao;
import week7.assessment.encentral.dao.BorrowedBookDao;
import week7.assessment.encentral.dao.StudentDao;
import week7.assessment.encentral.entities.Book;
import week7.assessment.encentral.entities.BorrowedBook;
import week7.assessment.encentral.entities.Student;

import java.util.List;
import java.util.Scanner;

/**
 * Driver class
 */
public class App {
    private static final int OPTION_ADD_STUDENT    = 1;
    private static final int OPTION_REMOVE_STUDENT = 2;
    private static final int OPTION_VIEW_STUDENTS  = 3;

    private static final int OPTION_ADD_BOOK       = 4;
    private static final int OPTION_REMOVE_BOOK    = 5;
    private static final int OPTION_VIEW_BOOKS     = 6;

    private static final int OPTION_BORROW_BOOK    = 7;
    private static final int OPTION_RETURN_BOOK    = 8;

    private static final Scanner scanner;
    private static final Logger logger;

    static {
        BasicConfigurator.configure();
        logger = LogManager.getLogger("BookLibrary");

        scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");

        // This is just to make sure we're connected to the DB before accepting any user input
        // Subsequent transactions will be faster!
        StudentDao.getAllStudents();
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println();
            int option = getChosenOption();
            System.out.println();

            if (option == OPTION_ADD_STUDENT)
                addStudent();
            else if (option == OPTION_REMOVE_STUDENT)
                removeStudent();
            else if (option == OPTION_VIEW_STUDENTS)
                showStudents();
            else if (option == OPTION_ADD_BOOK)
                addBook();
            else if (option == OPTION_REMOVE_BOOK)
                removeBook();
            else if (option == OPTION_VIEW_BOOKS)
                showBooks();
            else if (option == OPTION_BORROW_BOOK)
                borrowBook();
            else if (option == OPTION_RETURN_BOOK)
                returnBook();
        }
    }

    private static int getChosenOption() {
        int option;

        while (true) {
            logger.info("Please choose one of the following options to continue:");
            logger.info(String.format("%d -> Add a student", OPTION_ADD_STUDENT));
            logger.info(String.format("%d -> Remove a student", OPTION_REMOVE_STUDENT));
            logger.info(String.format("%d -> Show all students", OPTION_VIEW_STUDENTS));
            logger.info(String.format("%d -> Add a book", OPTION_ADD_BOOK));
            logger.info(String.format("%d -> Remove a book", OPTION_REMOVE_BOOK));
            logger.info(String.format("%d -> Show all books", OPTION_VIEW_BOOKS));
            logger.info(String.format("%d -> Borrow a book", OPTION_BORROW_BOOK));
            logger.info(String.format("%d -> Return a book", OPTION_RETURN_BOOK));

            String input = scanner.next();

            try {
                option = Integer.parseInt(input);
                break;
            } catch (Exception e) {
                logger.error(input + " is not a valid option");
            }

            System.out.println();
        }

        return option;
    }

    private static void addStudent() {
        logger.info("Enter the student's full name (first and last), followed by the level (line by line):");

        try {
            String fullName = scanner.next().trim();
            String level = scanner.next().trim();

            Student student = new Student(fullName, level);

            logger.info("Adding student >>>>>>");
            if (StudentDao.isStudentExists(student))
                logger.error("This student already exists!");
            else {
                if (StudentDao.addStudent(student))
                    logger.info("Student added!");
                else
                    logger.error("Failed to add student");
            }
        } catch (Exception e) {
            logger.error("Please provide all the required input in the appropriate format");
        }
    }

    private static void removeStudent() {
        logger.info("Enter the student's ID:");

        try {
            Student student = StudentDao.getStudent(Integer.parseInt(scanner.next()));
            if (student == null)
                logger.error("Student not found!");
            else {
                logger.info("Removing student >>>>>>");
                if (StudentDao.removeStudent(student))
                    logger.info("Student removed");
                else
                    logger.error("Failed to remove student");
            }
        } catch (Exception e) {
            logger.error("Please enter a valid student ID");
        }
    }

    private static void showStudents() {
        logger.info("Fetching students >>>>>>");
        List<Student> students = StudentDao.getAllStudents();

        if (students == null || students.isEmpty())
            logger.info("No student found");
        else {
            for (Student student : students)
                logger.info(student);
        }
    }

    private static void addBook() {
        logger.info("Enter the book's title followed by the author (line by line):");

        try {
            String[] input = new String[2];
            for (int i = 0; i < input.length; i++) {
                input[i] = scanner.next();
            }

            Book book = new Book(input[0], input[1]);
            if (BookDao.isBookExists(book))
                logger.error("This book already exists!");
            else {
                logger.info("Adding book >>>>>>");
                if (BookDao.addBook(book))
                    logger.info("Book added!");
                else
                    logger.error("Failed to add book");
            }
        } catch (Exception e) {
            logger.error("Please provide all the required input in the appropriate format");
        }
    }

    private static void removeBook() {
        logger.info("Enter the book's ID");

        try {
            Book book = BookDao.getBook(Integer.parseInt(scanner.next()));
            if (book == null)
                logger.error("Book not found!");
            else {
                logger.info("Removing book >>>>>>");
                if (BookDao.removeBook(book))
                    logger.info("Book removed");
                else
                    logger.error("Failed to remove book");
            }
        } catch (Exception e) {
            logger.error("Please enter a valid book ID");
        }
    }

    private static void showBooks() {
        logger.info("Fetching books >>>>>>");
        List<Book> books = BookDao.getAllBooks();

        if (books == null || books.isEmpty())
            logger.info("No book found");
        else {
            for (Book book : books)
                logger.info(book);
        }
    }

    private static void borrowBook() {
        Student student = null;
        Book book;

        logger.info("Enter the student's full name (first and last):");
        try {
            String fullName = scanner.next();
            student = StudentDao.getStudentByFullName(fullName);
            if (student == null)
                logger.error("Student not found!");
        } catch (Exception e) {
            logger.error("Please enter a valid student full name");
        }

        if (student != null) {
            if (BorrowedBookDao.studentHaveBorrowed(student))
                logger.error("This student have not returned the previously borrowed book! Please return it before borrowing another.");
            else {
                logger.info("Enter the book's ID:");

                try {
                    book = BookDao.getBook(Integer.parseInt(scanner.next()));
                    if (book == null)
                        logger.error("Book not found!");
                    else {
                        BorrowedBook borrowedBook = new BorrowedBook(book, student);

                        logger.info("Borrowing book >>>>>>");
                        if (BorrowedBookDao.addBorrowedBook(borrowedBook))
                            logger.info("Book borrowed to " + student.getFullName());
                        else
                            logger.error("Failed to borrow book");
                    }
                } catch (Exception e) {
                    logger.error("Please enter a valid book ID");
                }
            }
        }
    }

    private static void returnBook() {
        Student student = null;
        Book book;

        logger.info("Enter the student's full name (first and last):");
        try {
            String fullName = scanner.next();
            student = StudentDao.getStudentByFullName(fullName);
            if (student == null)
                logger.error("Student not found!");
        } catch (Exception e) {
            logger.error("Please enter a valid student full name");
        }

        if (student != null) {
            logger.info("Enter the book's ID:");

            try {
                book = BookDao.getBook(Integer.parseInt(scanner.next()));
                if (book == null)
                    logger.error("Book not found!");
                else {
                    BorrowedBook borrowedBook = BorrowedBookDao.getBorrowedBook(book, student);
                    if (borrowedBook != null) {
                        logger.info("Returning book >>>>>>");
                        if (BorrowedBookDao.removeBorrowedBook(borrowedBook))
                            logger.info("Book returned.");
                        else
                            logger.error("Failed to return book");
                    }
                    else
                        logger.error("This book is not borrowed to " + student.getFullName());
                }
            } catch (Exception e) {
                logger.error("Please enter a valid book ID");
            }
        }
    }
}