package me.devtools4.telegram.df;

public class RingBuffer<T> {

  private final int capacity;
  private final T[] data;
  private int writeSequence, readSequence;

  @SuppressWarnings("unchecked")
  public RingBuffer(int capacity) {
    this.capacity = capacity;
    this.data = (T[]) new Object[this.capacity];
    this.readSequence = 0;
    this.writeSequence = -1;
  }

  public boolean offer(T element) {
    if (!isFull()) {
      var nextWriteSeq = writeSequence + 1;
      data[nextWriteSeq % capacity] = element;

      writeSequence++;
      return true;
    }

    return false;
  }

  public T poll() {
    if (!isEmpty()) {
      var nextValue = data[readSequence % capacity];
      readSequence++;
      return nextValue;
    }

    return null;
  }

  public int capacity() {
    return capacity;
  }

  public int size() {
    return (writeSequence - readSequence) + 1;
  }

  public boolean isEmpty() {
    return writeSequence < readSequence;
  }

  public boolean isFull() {
    return size() >= capacity;
  }
}