import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SyncLinkedList<E> implements SimpleList<E> {

    private final List<E> list = new LinkedList<>();

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);

    @Override
    public int search(E e) throws InterruptedException {
        readWriteLock.readLock().lock();
        try { // search operation
            return list.indexOf(e);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public E get(int index) throws InterruptedException {
        readWriteLock.readLock().lock();
        try { // get operation
            return list.get(index);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public boolean insert(E e) throws InterruptedException {
        readWriteLock.writeLock().lock();
        try { // critical section
            return list.add(e);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean delete(E e) throws InterruptedException {
        // TODO lock the reading ???
        readWriteLock.writeLock().lock();
        try { // critical section
            return list.remove(e);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
