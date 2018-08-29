package org.luncert;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class HashMap<K, V> extends AbstractMap<K,V> implements Map<K,V>, Cloneable, Serializable {

    private static final long serialVersionUID = 4005637285527262429L;

    static final int MAXIMUM_CAPACITY = 1 << 30;

    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    
    static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

		@Override
		public final K getKey() { return key; }

		@Override
        public final V getValue() { return value; }
        
        @Override
        public final String toString() { return key + "=" + value; }

        @Override
        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

		@Override
		public final V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public final boolean equals(Object o) {
            if (o == this)
                return true;
            if (o instanceof Map.Entry) {
                Map.Entry<?, ?> e = (Map.Entry<?, ?>) o;
                if (Objects.equals(key, e.getKey()) &&
                    Objects.equals(value, e.getValue()))
                    return true;
            }
            return false;
        }
        
    }

    static int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16); // 用 key.hashCode 高16位异或低16位，高16位置0
    }

    static Class<?> comparableClassFor(Object x) {
        if (x instanceof Comparable) {
            Class<?> c;
            Type[] ts, as;
            Type t;
            ParameterizedType p;
            if ((c = x.getClass()) == String.class)
                return c;
            if ((ts = c.getGenericInterfaces()) != null) {
                for (int i = 0; i < ts.length; i++) {
                    // 遍历 x 继承的接口，eg.List<String>
                    if (((t = ts[i]) instanceof ParameterizedType) &&
                        ((p = (ParameterizedType) t).getRawType() == Comparable.class) &&
                        (as = p.getActualTypeArguments()) != null &&
                        as.length == 1 && as[0] == c)
                        return c;
                }
            }
        }
        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    static int compareComparables(Class<?> klass, Object k, Object x) {
        return (x == null || x.getClass() != klass) ? 0 : ((Comparable) k).compareTo(x);
    }

    static int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : ((n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1);
    }

    transient Node<K,V>[] table;

    transient Set<Map.Entry<K,V>> entrySet;

    transient int size;

    transient int modCount;

    int threshold;

    final float loadFactor;

    public HashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factory: " + loadFactor);
        this.loadFactor = loadFactor;
        this.threshold = tableSizeFor(initialCapacity);
    }

    public HashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    public HashMap(Map<? extends K, ? extends V> m) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        // putMapEntries(m, false);
    }

    final void putMapEntries(Map<? extends K, ? extends V> m, boolean evict) {
        int s = m.size();
        if (s > 0) {
            if (table == null) {
                float ft = ((float) s / loadFactor) + 1.0f;
                int t = ((ft < (float) MAXIMUM_CAPACITY) ? (int) ft : MAXIMUM_CAPACITY);
                if (t < threshold)
                    threshold = tableSizeFor(t);
            }
            else if (s > threshold) {
                // resize();
            }
            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                // putVal(hash(key), key, value, false, evict);
            }
        }
    }

	@Override
	public Set<Entry<K, V>> entrySet() {
		return null;
	}
    
}