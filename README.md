# Search-Insert-Delete Problem
### Two different implementations for the Search-Insert-Delete Problem from The Little Book of Semaphores

Both solutions are implemented in Java, each one using different components.

+ **LockedLinkedList** uses a *ReentrantReadWriteLock* and a *ReentrantLock* to guarantee the mutal exclusion access to the internal *List* from the operations (seach, insert, delete)

+ **SIDLinkedList** uses *Semaphores* and *Lightswitch* components to guarantee the mutal exclusion access to the internal *List* from the operations (seach, insert, delete)
