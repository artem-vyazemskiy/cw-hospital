package main.entity.component;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class FullName {

    @Column(name = "first_name", length = 20)
    private String firstName;

    @Column(name = "last_name", length = 20)
    private String lastName;

    @Column(name = "father_name", length = 20)
    private String fatherName;

    public FullName() {
    }

    public FullName(String firstName, String lastName, String fatherName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fatherName = fatherName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullName fullName = (FullName) o;
        return Objects.equals(this.firstName, fullName.firstName) &&
                Objects.equals(this.lastName, fullName.lastName) &&
                Objects.equals(this.fatherName, fullName.fatherName);
    }

    @Override
    public String toString() {
        return "FullName{" +
                "firstName='" + this.firstName + '\'' +
                ", lastName='" + this.lastName + '\'' +
                ", fatherName='" + this.fatherName + '\'' +
                '}';
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFatherName() {
        return this.fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

}
