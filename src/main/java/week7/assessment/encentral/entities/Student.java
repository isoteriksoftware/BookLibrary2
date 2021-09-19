package week7.assessment.encentral.entities;

import javax.persistence.*;

@Table(name = "student")
@Entity
public class Student {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  int id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "level", nullable = false)
    private String level;

    public Student(String fullName, String level) {
        this.fullName = fullName;
        this.level = level;
    }

    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", level='" + level + '\'' +
                '}';
    }
}