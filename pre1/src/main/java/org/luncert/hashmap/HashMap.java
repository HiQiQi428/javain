package org.luncert.hashmap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class HashMap<K,V> implements Map<K,V> {

	static int DEFAULT_CAPACITY = 1 << 4;
	static float DEFAULT_LOADFACTOR = 0.75f;

	Node<K,V>[] table;
	int capacity = DEFAULT_CAPACITY;
	float loadFactor = DEFAULT_LOADFACTOR;
	int threshold = (int) (capacity * loadFactor);
	int size = 0;
	
	private int hash(K key) {
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
		public K getKey() { return key; }

		@Override
		public V getValue() { return value; }

		@Override
		public V setValue(V value) {
			V oldValue = this.value;
			this.value = value;
			return oldValue;
		}

		@Override
		public String toString() {
			return key + "=" + value;
		}

	}

	@Override
	public int size() { return size; }

	@Override
	public boolean isEmpty() { return size == 0; }

	@Override
	public boolean containsKey(Object key) {
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	@Override
	public V get(Object key) {
		return null;
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
			Node<K,V> node = table[index], cur, next = node;
			do {
				cur = next;
				if ((cur.hash == hash) && cur.key.equals(key)) {
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
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		
	}

	@Override
	public void clear() {
		
	}

	@Override
	public Set<K> keySet() {
		return null;
	}

	@Override
	public Collection<V> values() {
		return null;
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < table.length && table[i] != null; i++)
			for (Node<K,V> tmp = table[i]; tmp != null; tmp = tmp.next)
				builder.append(tmp).append('\n');
		return builder.toString();
	}
    
}