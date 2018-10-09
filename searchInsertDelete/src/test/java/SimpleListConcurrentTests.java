import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class SimpleListConcurrentTests {

    private static Stream<SimpleList> simpleListProvider() {
        return Stream.of(new SIDLinkedList(), new SyncLinkedList<>());
    }

    @ParameterizedTest
    @MethodSource("simpleListProvider")
    public void insertElementShouldReturnTrue(final SimpleList<TestObject> list) throws Exception {

    }

}
