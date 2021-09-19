package week7.assessment.encentral.entities;

import javax.persistence.*;

@Table(name = "borrowed_books")
@Entity
public class BorrowedBook {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "book_id")
    private Book book;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "student_id")
    private Student student;

    public BorrowedBook(Book book, Student student) {
        this.book = book;
        this.student = student;
    }

    public BorrowedBook() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}