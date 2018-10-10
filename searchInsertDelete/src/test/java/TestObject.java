import java.time.LocalDateTime;
import java.util.Objects;

public class TestObject implements TimedObject {

    Long id;
    String name;
    LocalDateTime dateTime;

    public TestObject() {
    }

    public TestObject(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestObject testObject = (TestObject) o;
        return Objects.equals(id, testObject.id) &&
                Objects.equals(name, testObject.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "TestObject{id=" + id + ", name='" + name + ", dateTime=" + dateTime + "'}";
    }
}
