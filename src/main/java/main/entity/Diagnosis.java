package main.entity;

import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "diagnosis")
public class Diagnosis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "diagnosis", fetch = FetchType.EAGER)
    private List<Person> people;

    public Diagnosis() {
    }

    public Diagnosis(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Diagnosis{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                '}';
    }

    public long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Person> getPeople() {
        return this.people;
    }

}
