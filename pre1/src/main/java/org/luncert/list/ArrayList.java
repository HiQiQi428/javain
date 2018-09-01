package org.luncert.list;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;

public class ArrayList<E> implements List<E>, Cloneable {

    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    /**
     * Integer.MAX_VALUE - 8;
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    Object[] data;

    int size;

    @SuppressWarnings("unchecked")
    private E dataAt(int index) {
        return (E) data[index];
    }

    private void rangeCheck(int index) {
        if (index >= size || index < 0)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private void ensureCapacity(int minCapacity) {
        grow(calculateCapacity(minCapacity));
    }

    private int calculateCapacity(int minCapacity) {
        return (data == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) ? Math.max(DEFAULT_CAPACITY, minCapacity) : minCapacity;
    }

    private void grow(int minCapacity) {
        if (minCapacity < 0)
            throw new IllegalArgumentException();
        int oldCap = data.length;
        int newCap = oldCap + (oldCap >> 1);
        if (newCap < minCapacity)
            newCap = minCapacity;
        if (newCap > MAX_ARRAY_SIZE)
            newCap = (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
        data = Arrays.copyOf(data, newCap);
    }

    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }

    public Object[] toArray() { return Arrays.copyOf(data, size); }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size)
            return (T[]) Arrays.copyOf(data, size, a.getClass());
        System.arraycopy(data, 0, a, 0, size);
        if (a.length > size)
            a[size] = null;
        return a;
    }

    public boolean contains(Object o) {
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        return false;
    }

    public void clear() {
        for (int i = 0; i < size; i++)
            data[i] = null;
        size = 0;
    }

    public E get(int index) {
        rangeCheck(index);
        return dataAt(index);
    }

    public E set(int index, E element) {
        rangeCheck(index);
        E oldValue = dataAt(index);
        data[index] = element;
        return oldValue;
    }

    public boolean add(E e) {
        ensureCapacity(size + 1);
        data[size++] = e;
        return true;
    }

    public void add(int index, E element) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        ensureCapacity(size + 1);
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = element;
        size++;
    }

    public boolean addAll(Collection<? extends E> c) {
        int len = c.size();
        if (len > 0) {
            ensureCapacity(size + len);
            System.arraycopy(c.toArray(), 0, data, size, len);
            size += len;
            return true;
        }
        return false;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        rangeCheck(index);
        int len = c.size();
        if (len > 0) {
            ensureCapacity(size + len);
            System.arraycopy(data, index, data, index + len, size - index);
            System.arraycopy(c.toArray(), 0, data, index, len);
            size += len;
            return true;
        }
        return false;
    }

    private void removeInternal(int index) {
        if (index < size - 1)
            System.arraycopy(data, index + 1, data, index, size - index - 1);
        data[--size] = null;
    }

    public E remove(int index) {
        rangeCheck(index);
        E oldValue = dataAt(index);
        removeInternal(index);
        return oldValue;
    }

    public boolean remove(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (null == data[i]) {
                    removeInternal(i);
                    return true;
                }
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(data[i])) {
                    removeInternal(i);
                    return true;
                }
        }
        return false;
    }

    private void batchRemove(Collection<?> c, boolean complement) {

    }

    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        batchRemove(c, false);
        return false;
    }

    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        batchRemove(c, true);
        return false;
    }

    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++)
                if (null == data[i])
                    return i;
        } else {
            for (int i = 0; i < size; i++)
                if (o.equals(data[i]))
                    return i;
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        if (o == null) {
            for (int i = size - 1; i >= 0; i--)
                if (null == data[i])
                    return i;
        } else {
            for (int i = size - 1; i >= 0; i--)
                if (o.equals(data[i]))
                    return i;
        }
        return -1;
    }

    public Object clone() {
        try {
            ArrayList<?> v = (ArrayList<?>) super.clone();
            v.data = Arrays.copyOf(data, size);
            return v;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    public Iterator<E> iterator() {
        return null;
    }

    public ListIterator<E> listIterator() {
        return null;
    }

    public ListIterator<E> listIterator(int index) {
        return null;
    }

	public List<E> subList(int fromIndex, int toIndex) {
		return null;
	}
    
}