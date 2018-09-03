package org.luncert.list;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;

public class ArrayList<E> extends AbstractList<E> implements List<E>, Cloneable {

    private static final int DEFAULT_CAPACITY = 10;
    private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};

    /**
     * Integer.MAX_VALUE - 8;
     */
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    Object[] data;

    int size;

    public ArrayList() {
        this.data = DEFAULTCAPACITY_EMPTY_ELEMENTDATA;
    }

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

    public boolean contains(Object o) { return indexOf(o) >= 0; }

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
        int i = indexOf(o);
        if (i != -1) {
            removeInternal(i);
            return true;
        }
        return false;
    }

    private boolean batchRemove(Collection<?> c, boolean complement) {
        final Object[] data = this.data;
        int r = 0, w = 0;
        boolean modified = false;
        try {
            for (; r < size; r++)
                if (c.contains(data[r]) == complement)
                    data[w++] = data[r];
        } finally {
            // 处理try语句中没处理完的元素
            if (r != size) {
                System.arraycopy(data, r, data, w, size - r);
                w += size - r;
            }
            if (w != size) {
                for (int i = w; i < size; i++)
                    data[i] = null;
                size = w;
                modified = true;
            }
        }
        return modified;
        
    }

    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, false);
    }

    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, true);
    }

    /**
     * 遍历查询
     */
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
        return new Itr();
    }

    public ListIterator<E> listIterator() {
        return new ListItr(0);
    }

    public ListIterator<E> listIterator(int index) {
        rangeCheck(index);
        return new ListItr(index);
    }

    private class Itr implements Iterator<E> {
        int cursor;
        int lastRet = -1;

        Itr() {}

        public boolean hasNext() { return cursor != size; }

        @SuppressWarnings("unchecked")
        public E next() {
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            Object[] data = ArrayList.this.data;
            if (i >= data.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            return (E) data[lastRet = i];
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            try {
                ArrayList.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @SuppressWarnings("unchecked")
        public void forEachRemaining(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            final int size = ArrayList.this.size;
            int i = cursor;
            if (i >= size)
                return;
            final Object[] data = ArrayList.this.data;
            if (i >= data.length)
                throw new ConcurrentModificationException();
            while (i != size)
                consumer.accept((E) data[i++]);
            cursor = i;
            lastRet = i - 1;
        }

    }

    private class ListItr extends Itr implements ListIterator<E> {

        ListItr(int index) {
            super();
            cursor = index;
        }

        public boolean hasPrevious() { return cursor != 0; }

        public int nextIndex() { return cursor; }
        
        public int previousIndex() { return cursor - 1; }

        @SuppressWarnings("unchecked")
        public E previous() {
            int i = cursor - 1;
            if (i < 0)
                throw new NoSuchElementException();
            Object[] data = ArrayList.this.data;
            if (i >= data.length)
                throw new ConcurrentModificationException();
            cursor = i;
            return (E) data[lastRet = i];
        }
        
        public void set(E e) {
            if (lastRet < 0)
                throw new NoSuchElementException();
            try {
                ArrayList.this.set(lastRet, e);
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        public void add(E e) {
            try {
                int i = cursor;
                ArrayList.this.add(i, e);
                cursor = i + 1;
                lastRet = -1;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

    }

	public List<E> subList(int fromIndex, int toIndex) {
		return null;
	}
    
}