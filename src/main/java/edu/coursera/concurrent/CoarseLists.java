package edu.coursera.concurrent;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/** Wrapper class for two lock-based concurrent list implementations. */
public final class CoarseLists {
  /**
   * An implementation of the ListSet interface that uses Java locks to protect against concurrent
   * accesses.
   *
   */
  public static final class CoarseList extends ListSet {
    /*
     * Declare a lock for this class to be used in implementing the
     * concurrent add, remove, and contains methods below.
     */

    private final ReentrantLock lock = new ReentrantLock();

    public CoarseList() {
      super();
    }

    @Override
    boolean add(final Integer object) {
        lock.lock();
      try {
        Entry pred = this.head;
        Entry curr = pred.next;

        while (curr.object.compareTo(object) < 0) {
          pred = curr;
          curr = curr.next;
        }

        if (object.equals(curr.object)) {
          return false;
        } else {
          final Entry entry = new Entry(object);
          entry.next = curr;
          pred.next = entry;
          return true;
        }
      } finally {
        lock.unlock();
      }
    }

    @Override
    boolean remove(final Integer object) {
        lock.lock();
      try {
        Entry pred = this.head;
        Entry curr = pred.next;

        while (curr.object.compareTo(object) < 0) {
          pred = curr;
          curr = curr.next;
        }

        if (object.equals(curr.object)) {
          pred.next = curr.next;
          return true;
        } else {
          return false;
        }
      } finally {
        lock.unlock();
      }
    }

    @Override
    boolean contains(final Integer object) {
        lock.lock();
      try {
        Entry pred = this.head;
        Entry curr = pred.next;

        while (curr.object.compareTo(object) < 0) {
          pred = curr;
          curr = curr.next;
        }
        return object.equals(curr.object);
      } finally {
        lock.unlock();
      }
    }
  }

  /**
   * An implementation of the ListSet interface that uses Java read-write locks to protect against
   * concurrent accesses.
   *
   */
  public static final class RWCoarseList extends ListSet {
    /*
     * Declare a read-write lock for this class to be used in
     * implementing the concurrent add, remove, and contains methods below.
     */

    // Concurrent Reads to shared Object Can be Interleaved without data Races, Concurrent R/W or W
    // -> data Races
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public RWCoarseList() {
      super();
    }

    @Override
    boolean add(final Integer object) {
        readWriteLock.writeLock().lock();
      try {
        Entry pred = this.head;
        Entry curr = pred.next;

        while (curr.object.compareTo(object) < 0) {
          pred = curr;
          curr = curr.next;
        }

        if (object.equals(curr.object)) {
          return false;
        } else {
          final Entry entry = new Entry(object);
          entry.next = curr;
          pred.next = entry;
          return true;
        }
      } finally {
        readWriteLock.writeLock().unlock();
      }
    }

    @Override
    boolean remove(final Integer object) {
        readWriteLock.writeLock().lock();
      try {
        Entry pred = this.head;
        Entry curr = pred.next;

        while (curr.object.compareTo(object) < 0) {
          pred = curr;
          curr = curr.next;
        }

        if (object.equals(curr.object)) {
          pred.next = curr.next;
          return true;
        } else {
          return false;
        }
      } finally {
        readWriteLock.writeLock().unlock();
      }
    }

    @Override
    boolean contains(final Integer object) {
      try {
        readWriteLock.readLock().lock();
        Entry pred = this.head;
        Entry curr = pred.next;
        while (curr.object.compareTo(object) < 0) {
          pred = curr;
          curr = curr.next;
        }
        return object.equals(curr.object);
      } finally {
        readWriteLock.readLock().unlock();
      }
    }
  }
}
