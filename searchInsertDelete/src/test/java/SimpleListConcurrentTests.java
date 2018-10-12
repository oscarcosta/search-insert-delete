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
        return LongStream.range(start, end).mapToObj(i -> new TestObject(i));
    }

    @ParameterizedTest
    @MethodSource("simpleListProvider")
    public void insertConcurrently(final SimpleList<TestObject> list) throws Exception {
        final int numObjects = 10000;
        final int numThreads = 5;

        List<TestObject> testObjects = buildTestObjects(0, numObjects).collect(Collectors.toList());

        ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
        final CountDownLatch latch = new CountDownLatch(numObjects);

        List<Future<Integer>> futureInserts = new ArrayList<>();
        for (int i = 0; i < numObjects; i++) {
            final TestObject object = testObjects.get(i);
            futureInserts.add(threadPool.submit(() -> {
                int countInserts = 0;
                if (list.insert(object)) {
                    //int index = list.search(object);
                    //TestObject tempObj = list.get(index);
                    //System.out.println("Inserted: " + tempObj);
                    countInserts++;
                }
                latch.countDown();
                return countInserts;
            }));
        }
        latch.await();

        int sumInserts = 0;
        for (Future<Integer> future : futureInserts) {
            sumInserts += future.get();
        }

        Assertions.assertEquals(sumInserts, numObjects);
        Assertions.assertEquals(list.size(), numObjects);
    }

    @ParameterizedTest
    @MethodSource("simpleListProvider")
    public void deleteConcurrently(final SimpleList<TestObject> list) throws Exception {
        final int numObjects = 10000;
        final int numThreads = 5;

        // Insert elements
        List<TestObject> testObjects = buildTestObjects(0, numObjects).collect(Collectors.toList());
        testObjects.forEach(testObject -> {
            try {
                list.insert(testObject);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
        final CountDownLatch latch = new CountDownLatch(numObjects);

        List<Future<Integer>> futureDeletes = new ArrayList<>();
        for (int i = 0; i < numObjects; i++) {
            final TestObject object = testObjects.get(i);
            futureDeletes.add(threadPool.submit(() -> {
                int countDeletes = 0;
                if (list.delete(object)) {
                    countDeletes++;
                }
                latch.countDown();
                return countDeletes;
            }));
        }
        latch.await();

        int sumDeletions = 0;
        for (Future<Integer> future : futureDeletes) {
            sumDeletions += future.get();
        }

        Assertions.assertEquals(sumDeletions, numObjects);
        Assertions.assertEquals(list.size(), 0);
    }

    @ParameterizedTest
    @MethodSource("simpleListProvider")
    public void searchConcurrently(final SimpleList<TestObject> list) throws Exception {
        final int numObjects = 10000;
        final int numThreads = 5;

        // Insert elements
        List<TestObject> testObjects = buildTestObjects(0, numObjects).collect(Collectors.toList());
        testObjects.forEach(testObject -> {
            try {
                list.insert(testObject);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
        final CountDownLatch latch = new CountDownLatch(numObjects);

        List<Future<Integer>> futureSearches = new ArrayList<>();
        for (int i = 0; i < numObjects; i++) {
            final TestObject object = testObjects.get(i);
            futureSearches.add(threadPool.submit(() -> {
                int countSearches = 0;
                if (list.search(object) >= 0) {
                    countSearches++;
                }
                latch.countDown();
                return countSearches;
            }));
        }
        latch.await();

        int sumSearches = 0;
        for (Future<Integer> future : futureSearches) {
            sumSearches += future.get();
        }

        Assertions.assertEquals(sumSearches, numObjects);
    }

    @ParameterizedTest
    @MethodSource("simpleListProvider")
    public void searchInsertDeleteConcurrently(final SimpleList<TestObject> list) throws Exception {
        final int numObjects = 10000;
        final int numThreads = 5;

        // Insert elements
        List<TestObject> testObjectsForDelete = buildTestObjects(0, numObjects).collect(Collectors.toList());
        testObjectsForDelete.forEach(testObject -> {
            try {
                list.insert(testObject);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        List<TestObject> testObjectsForSearch = buildTestObjects(numObjects, numObjects * 2).collect(Collectors.toList());
        testObjectsForSearch.forEach(testObject -> {
            try {
                list.insert(testObject);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        ExecutorService threadPool = Executors.newFixedThreadPool(numThreads);
        final CountDownLatch latch = new CountDownLatch(numObjects * 3);

        // INSERT
        List<TestObject> testObjectsForInsert = buildTestObjects(numObjects * 2, numObjects * 3).collect(Collectors.toList());
        List<Future<Integer>> futureInserts = new ArrayList<>();
        for (int i = 0; i < numObjects; i++) {
            final TestObject object = testObjectsForInsert.get(i);
            futureInserts.add(threadPool.submit(() -> {
                int countInserts = 0;
                if (list.insert(object)) {
                    //int index = list.search(object);
                    //TestObject tempObj = list.get(index);
                    //System.out.println("Inserted: " + tempObj);
                    countInserts++;
                }
                latch.countDown();
                return countInserts;
            }));
        }

        // SEARCH
        List<Future<Integer>> futureSearches = new ArrayList<>();
        for (int i = 0; i < numObjects; i++) {
            final TestObject object = testObjectsForSearch.get(i);
            futureSearches.add(threadPool.submit(() -> {
                int countSearches = 0;
                if (list.search(object) >= 0) {
                    countSearches++;
                }
                latch.countDown();
                return countSearches;
            }));
        }

        // DELETE
        List<Future<Integer>> futureDeletes = new ArrayList<>();
        for (int i = 0; i < numObjects; i++) {
            final TestObject object = testObjectsForDelete.get(i);
            futureDeletes.add(threadPool.submit(() -> {
                int countDeletes = 0;
                if (list.delete(object)) {
                    countDeletes++;
                }
                latch.countDown();
                return countDeletes;
            }));
        }

        // Wait!
        latch.await();

        int sumSearches = 0;
        for (Future<Integer> future : futureSearches) {
            sumSearches += future.get();
        }
        Assertions.assertEquals(sumSearches, numObjects);

        int sumDeletions = 0;
        for (Future<Integer> future : futureDeletes) {
            sumDeletions += future.get();
        }
        Assertions.assertEquals(sumDeletions, numObjects);

        int sumInserts = 0;
        for (Future<Integer> future : futureInserts) {
            sumInserts += future.get();
        }
        Assertions.assertEquals(sumInserts, numObjects);

    }
}