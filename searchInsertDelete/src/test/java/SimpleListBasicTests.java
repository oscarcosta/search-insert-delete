
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class SimpleListBasicTests {

    private static Stream<SimpleList> simpleListProvider() {
        return Stream.of(new SIDLinkedList(), new LockedLinkedList<>());
    }

    @ParameterizedTest
    @MethodSource("simpleListProvider")
    public void insertElementShouldReturnTrue(final SimpleList<TestObject> list) throws Exception {
        Assertions.assertTrue(list.insert(new TestObject(1L)));
    }

    @ParameterizedTest
    @MethodSource("simpleListProvider")
    public void searchExistentElementShouldReturnItsPosition(final SimpleList<TestObject> list) throws Exception {
        TestObject obj = new TestObject(1L);
        list.insert(obj);

        Assertions.assertEquals(list.search(obj), 0);
    }

    @ParameterizedTest
    @MethodSource("simpleListProvider")
    public void searchNonExistentElementShouldReturnNegative(final SimpleList<TestObject> list) throws Exception {
        TestObject obj = new TestObject(1L);
        list.insert(obj);

        TestObject nonObj = new TestObject(2L);

            Assertions.assertTrue(list.search(nonObj) < 0);
    }

    @ParameterizedTest
    @MethodSource("simpleListProvider")
    public void deleteExistentElementShouldReturnTrue(final SimpleList<TestObject> list) throws Exception {
        TestObject obj = new TestObject(1L);
        list.insert(obj);

        Assertions.assertTrue(list.delete(obj));
    }

    @ParameterizedTest
    @MethodSource("simpleListProvider")
    public void deleteNonExistentElementShouldReturnFalse(final SimpleList<TestObject> list) throws Exception {
        TestObject obj = new TestObject(1L);
        list.insert(obj);

        TestObject nonObj = new TestObject(2L);

        Assertions.assertFalse(list.delete(nonObj));
    }
}
