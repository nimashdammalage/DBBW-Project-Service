package dbbwproject.serviceunit.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "travel_doc_type", schema = "dbbw", catalog = "")
public class TravelDocTypeEntity {
    private int id;
    private String value;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "value", nullable = false, length = 50)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TravelDocTypeEntity that = (TravelDocTypeEntity) o;
        return id == that.id &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value);
    }
}
