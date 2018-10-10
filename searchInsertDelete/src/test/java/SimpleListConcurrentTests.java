import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class SimpleListConcurrentTests {

    private static Stream<SimpleList> simpleListProvider() {
        return Stream.of(new SIDLinkedList(), new LockedLinkedList<>());
    }

    private static Stream<TestObject> buildTestObjects(int start, int end) {
        return LongStream.range(start, end).mapToObj(i -> new TestObject(i, "Test " + i));
    }

    @ParameterizedTest
    @MethodSource("simpleListProvider")
    public void insertAndSeachConcurrently(final SimpleList<TestObject> list) throws Exception {
        int numObjects = 100;
        int numThreads = 10;

        List<TestObject> testObjects = buildTestObjects(1, numObjects).collect(Collectors.toList());

        ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
        final CountDownLatch latch = new CountDownLatch(numThreads);

        List<Future<Integer>> futureInserts = new ArrayList<>();
        List<Future<Integer>> futuresSearch = new ArrayList<>();
        for (int i = 0; i < numThreads / 2; i++) {
            futureInserts.add(threadPool.submit(() -> {
                int countInserts = 0;
                for (int j = 0; j < numObjects / numThreads; j++) {
                    TestObject object = testObjects.get(j);
                    if (list.insert(object)) {
                        System.out.println("Insert: " + object);
                        countInserts++;
                    }
                }
                latch.countDown();
                return countInserts;
            }));

            futuresSearch.add(threadPool.submit(() -> {
                int countSearchs = 0;
                for (TestObject object : testObjects) {
                    int index = list.search(object);
                    if (index >= 0) {
                        System.out.println("Search: " + object);
                        countSearchs++;
                    }
                }
                latch.countDown();
                return countSearchs;
            }));
        }
        latch.await();

        int sumInserts = 0;
        for (Future<Integer> future : futureInserts) {
            sumInserts += future.get();
        }

        int sumSearches = 0;
        for (Future<Integer> future : futuresSearch) {
            sumSearches += future.get();
        }
        System.out.println(sumInserts + "/" + sumSearches);

        Assertions.assertEquals(sumSearches, sumInserts);

    }
}