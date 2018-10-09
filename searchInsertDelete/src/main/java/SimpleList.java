public interface SimpleList<E> {

    int search(E e) throws InterruptedException;

    E get(int index) throws InterruptedException;

    boolean insert(E e) throws InterruptedException;

    boolean delete(E e) throws InterruptedException;
}
