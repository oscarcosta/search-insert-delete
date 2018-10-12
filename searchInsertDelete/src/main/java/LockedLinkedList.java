
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LockedLinkedList<E extends TimedObject> implements SimpleList<E> {

    private AtomicLong atomicLong = new AtomicLong();

    private final List<E> list = new LinkedList<>();

    private final ReadWriteLock searchLock = new ReentrantReadWriteLock(true);
    private final Lock insertLock = new ReentrantLock(true);

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public int search(E e) throws InterruptedException {
        searchLock.readLock().lock();
        try { // search operation
            return list.indexOf(e);
        } finally {
            searchLock.readLock().unlock();
        }
    }

    @Override
    public E get(int index) throws InterruptedException {
        searchLock.readLock().lock();
        try { // get operation
            return list.get(index);
        } finally {
            searchLock.readLock().unlock();
        }
    }

    @Override
    public boolean insert(E e) throws InterruptedException {
        insertLock.lock();
        searchLock.readLock().lock();
        try { // critical section
            e.setUID(atomicLong.getAndIncrement());
            return list.add(e);
        } finally {
            searchLock.readLock().unlock();
            insertLock.unlock();
        }
    }

    @Override
    public boolean delete(E e) throws InterruptedException {
        insertLock.lock();
        searchLock.writeLock().lock();
        try { // critical section
            return list.remove(e);
        } finally {
            searchLock.writeLock().unlock();
            insertLock.unlock();
        }
    }
}
