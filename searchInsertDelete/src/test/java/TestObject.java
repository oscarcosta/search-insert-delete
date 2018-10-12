
import java.util.Objects;

public class TestObject implements TimedObject {

    Long id;

    Long uid;

    public TestObject(Long id) {
        this.id = id;
    }

    @Override
    public void setUID(Long uid) {
        this.uid = uid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestObject object = (TestObject) o;
        return Objects.equals(id, object.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TestObject{" +
                "id=" + id +
                ", uid=" + uid +
                '}';
    }
}
