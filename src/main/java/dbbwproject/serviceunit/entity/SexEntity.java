package dbbwproject.serviceunit.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "sex", schema = "dbbw", catalog = "")
public class SexEntity {
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
        SexEntity sexEntity = (SexEntity) o;
        return id == sexEntity.id &&
                value == sexEntity.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }
}
