import java.util.Objects;

public class TestObject {
    Long id;
    String name;

    public TestObject() {
    }

    public TestObject(Long id, String name) {
        this.id = id;
        this.name = name;
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
}
