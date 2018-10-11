import java.util.concurrent.Semaphore;

public class Lightswitch {

    private int counter = 0;
    private final Semaphore mutex = new Semaphore(1);

    public void lock(Semaphore semaphore) throws InterruptedException {
        mutex.acquire();
        try {
            counter++;
            if (counter == 1) {
                semaphore.acquire();
            }
        } finally {
            mutex.release();
        }
    }

    public void unlock(Semaphore semaphore) throws InterruptedException {
        mutex.acquire();
        try {
            counter--;
            if (counter == 0) {
                semaphore.release();
            }
        } finally {
            mutex.release();
        }
    }
}
