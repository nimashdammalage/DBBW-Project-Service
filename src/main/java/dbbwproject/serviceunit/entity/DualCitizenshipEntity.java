package dbbwproject.serviceunit.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "dual_citizenship", schema = "dbbw", catalog = "")
public class DualCitizenshipEntity {
    private int id;
    private int value;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "value", nullable = false)
    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DualCitizenshipEntity that = (DualCitizenshipEntity) o;
        return id == that.id &&
                value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }
}
