import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SIDLinkedList<E> implements SimpleList<E> {

    private final List<E> list = new LinkedList<>();

    private final Semaphore insertMutex = new Semaphore(1);
    private final Semaphore noSearcher = new Semaphore(1);
    private final Semaphore noInserter = new Semaphore(1);
    private final Lightswitch searchSwitch = new Lightswitch();
    private final Lightswitch insertSwitch = new Lightswitch();

    @Override
    public int search(E e) throws InterruptedException {
        searchSwitch.lock(noSearcher);
        try { // search operation
            return list.indexOf(e);
        } finally {
            searchSwitch.unlock(noSearcher);
        }
    }

    @Override
    public E get(int index) throws InterruptedException {
        searchSwitch.lock(noSearcher);
        try { // get operation
            return list.get(index);
        } finally {
            searchSwitch.unlock(noSearcher);
        }
    }

    @Override
    public boolean insert(E e) throws InterruptedException {
        insertSwitch.lock(noInserter);
        insertMutex.acquire();
        try { // critical section
            return list.add(e);
        } finally {
            insertMutex.release();
            insertSwitch.unlock(noInserter);
        }
    }

    @Override
    public boolean delete(E e) throws InterruptedException {
        noSearcher.acquire();
        noInserter.acquire();
        try { // critical section
            return list.remove(e);
        } finally {
            noInserter.release();
            noSearcher.release();
        }
    }
}

