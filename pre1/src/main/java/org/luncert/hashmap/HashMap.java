package org.luncert.hashmap;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Consumer;

public class HashMap<K,V> implements Map<K,V> {

	static int DEFAULT_CAPACITY = 1 << 4;
	static float DEFAULT_LOADFACTOR = 0.75f;

	Node<K,V>[] table;
	int capacity = DEFAULT_CAPACITY;
	float loadFactor = DEFAULT_LOADFACTOR;
	int threshold = (int) (capacity * loadFactor);
	int size = 0;

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

		public K getKey() { return key; }

		public V getValue() { return value; }

		public V setValue(V value) {
			V oldValue = this.value;
			this.value = value;
			return oldValue;
		}

		public Node<K,V> find(int hash, Object key) {
			if (key != null) {
				Node<K,V> node = this;
				do {
					if (node.key == key || node.hash == hash && node.key.equals(key))
						return node;
				} while ((node = node.next) != null);
			}
			return null;
		}

		public String toString() {
			return key + "=" + value;
		}

	}

	@Override
	public int size() { return size; }

	@Override
	public boolean isEmpty() { return size == 0; }

	
	private int hash(Object key) {
		int h;
		return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
	}

	@SuppressWarnings("unchecked")
	private void resize() {
		if (table == null || table.length == 0)
			table = (Node<K,V>[]) new Node[capacity];
		else {
			capacity <<= 1;
			threshold <<= 1;
			Node<K,V>[] oldTable = table;
			table = (Node<K,V>[]) new Node[capacity];
			// 拷贝元素
			for (int i = 0; i < oldTable.length; i++) {
				Node<K,V> tmp = oldTable[i];
				if (oldTable[i] != null) {
					oldTable[i] = null;
					for (; tmp != null; tmp = tmp.next) {
						int hash = tmp.hash;
						int index = (capacity - 1) & hash;
						if (table[index] == null)
							table[index] = tmp;
						else {
							Node<K,V> e = table[index];
							while (e.next != null)
								e = e.next;
							e.next = tmp;
						}
					}
				}
			}
		}
	}

	@Override
	public boolean containsKey(Object key) {
		return getNode(key) != null;
	}

	@Override
	public boolean containsValue(Object value) {
		if (size > 0) {
			for (int i = 0; i < table.length; i++) {
				Node<K,V> node = table[i];
				while (node != null) {
					if (node.value.equals(value))
						return true;
					node = node.next;
				}
			}
		}
		return false;
	}

	@Override
	public V get(Object key) {
		Node<K,V> node = getNode(key);
		return (node == null) ? null : node.value;
	}

	private Node<K,V> getNode(Object key) {
		int hash = hash(key);
		int index = (capacity - 1) & hash;
		return (table[index] == null) ? null : table[index].find(hash, key);
	}

	@Override
	public V put(K key, V value) {
		if (table == null || table.length == 0 || size + 1 >= threshold)
			resize();
		int hash = hash(key);
		int index = (capacity - 1) & hash;
		if (table[index] == null)
			table[index] = new Node<K, V>(hash, key, value, null);
		else {
			// 散列冲突
			Node<K,V> node = table[index], cur, next = node;
			do {
				cur = next;
				if (cur.key == key || cur.hash == hash && cur.key.equals(key)) {
					V oldValue = cur.value;
					cur.value = value;
					return oldValue;
				}
			}
			while ((next = cur.next) != null);
			cur.next = new Node<K, V>(hash, key, value, null);
		}
		size++;
		return value;
	}

	@Override
	public V remove(Object key) {
		int hash = hash(key);
		int index = (capacity - 1) & hash;
		if (table[index] != null) {
			Node<K,V> node = table[index], pre = node;
			do {
				if (node.key == key || node.hash == hash && key.equals(node.key)) {
					size--;
					if (node == table[index]) {
						table[index] = null;
						break;
					}
					else {
						pre.next = node.next;
						return node.value;
					}
				}
				pre = node;
				node = node.next;
			}
			while (node != null);
		}
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		m.forEach((key, value) -> put(key, value));
	}

	@Override
	public void clear() {
        if (table != null && size > 0) {
            size = 0;
            for (int i = 0; i < table.length; i++)
				table[i] = null;
        }
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < table.length && table[i] != null; i++)
			for (Node<K,V> tmp = table[i]; tmp != null; tmp = tmp.next)
				builder.append(tmp).append('\n');
		return builder.toString();
	}

	public Set<K> keySet() { return new KeySet(); }

	public Collection<V> values() { return new Values(); }

	public Set<Entry<K, V>> entrySet() { return new EntrySet(); }

	final class KeySet extends AbstractSet<K> {

		public Iterator<K> iterator() { return new KeyIterator(); }

		public final int size() { return size; }

		public final void clear() { HashMap.this.clear(); }

	}

	final class Values extends AbstractCollection<V> {

		public final Iterator<V> iterator() { return new ValueIterator(); }
		public final int size() { return size; }
		public final void clear() { HashMap.this.clear(); }
		public final boolean contains(Object o) { return containsValue(o); }
        public final void forEach(Consumer<? super V> action) {
			if (action == null)
				throw new NullPointerException();
            if (size > 0 && table != null) {
                for (int i = 0; i < table.length; ++i) {
                    for (Node<K,V> node = table[i]; node != null; node = node.next)
                        action.accept(node.value);
                }
            }
		}
		
	}

	final class EntrySet extends AbstractSet<Map.Entry<K,V>> {

		public Iterator<Entry<K, V>> iterator() { return new EntrySetIterator(); }

		public int size() { return size; }

	}

	abstract class HashIterator {
		int index = -1;
		Node<K,V> current, next;

		HashIterator() {
			current = getNode();
			next = getNext();
		}

		final Node<K,V> getNode() {
			Node<K,V> node = null;
			for (index++; index < size && (node = table[index]) == null; index++);
			return node;
		}

		final Node<K,V> getNext() {
			if (current != null && current.next != null)
				return current.next;
			else
				return getNode();
		}

		final Node<K,V> nextNode() {
			if (current == null)
				throw new NoSuchElementException();
			Node<K,V> node = current;
			current = next;
			next = getNext();
			return node;
		}

	}

	final class KeyIterator extends HashIterator implements Iterator<K> {

		public boolean hasNext() { return current != null; }

		public K next() { return nextNode().key; }

	}

	final class ValueIterator extends HashIterator implements Iterator<V> {

		public boolean hasNext() { return current != null; }

		public V next() { return nextNode().value; }

	}

	final class EntrySetIterator extends HashIterator implements Iterator<Map.Entry<K,V>> {

		public boolean hasNext() { return current != null; }

		public Entry<K, V> next() { return nextNode(); }

	}

}