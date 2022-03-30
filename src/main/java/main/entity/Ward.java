package main.entity;

import java.util.List;
import javax.persistence.*;

@Entity
@Table(name = "wards")
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", length = 50, nullable = false, unique = true)
    private String name;

    @Column(name = "max_count")
    private int maxCount;

    @OneToMany(mappedBy = "ward", fetch = FetchType.EAGER)
    private List<People> peoples;

    public Ward() {
    }

    public Ward(String name, int maxCount) {
        this.name = name;
        this.maxCount = maxCount;
    }

    @Override
    public String toString() {
        return "Ward{" +
                "id=" + this.id +
                ", name='" + this.name + '\'' +
                ", maxCount=" + this.maxCount +
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

    public int getMaxCount() {
        return this.maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public List<People> getPeoples() {
        return this.peoples;
    }

}
