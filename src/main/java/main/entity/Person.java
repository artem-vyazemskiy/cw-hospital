package main.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import main.entity.component.FullName;

@Entity
@Table(name = "people")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Embedded
    private FullName fullName;

    @ManyToOne()
    @JoinColumn(name = "ward_id", nullable = false)
    private Ward ward;

    @ManyToOne
    @JoinColumn(name = "diagnosis_id", nullable = false)
    private Diagnosis diagnosis;

    public Person() {
    }

    public Person(FullName fullName, Ward ward, Diagnosis diagnosis) {
        this.fullName = fullName;
        this.ward = ward;
        this.diagnosis = diagnosis;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + this.id +
                ", fullName=" + this.fullName +
                ", ward=" + this.ward +
                ", diagnosis=" + this.diagnosis +
                '}';
    }

    public long getId() {
        return this.id;
    }

    public FullName getFullName() {
        return this.fullName;
    }

    public void setFullName(FullName fullName) {
        this.fullName = fullName;
    }

    public Ward getWard() {
        return this.ward;
    }

    public void setWard(Ward ward) {
        this.ward = ward;
    }

    public Diagnosis getDiagnosis() {
        return this.diagnosis;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
    }

}
