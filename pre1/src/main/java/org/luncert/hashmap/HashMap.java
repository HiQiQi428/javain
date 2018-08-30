package org.luncert.hashmap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class HashMap<K,V> implements Map<K,V> {

	static int DEFAULT_CAPACITY = 1 << 4;
	static float DEFAULT_THRESHOLD = 0.75f;

	Node<K,V>[] data;
	int capacity = DEFAULT_CAPACITY;
	float threshold = DEFAULT_THRESHOLD;
	
	private int hash(K key) {
		if (key == null)
			return 0;
		else {
			int h = key.hashCode();
			h &= h >>> 16;
			return h;
		}
	}

	private void putVal(int hash, K key, V value) {

	}

	private void resize() {

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

	}
	
	@SuppressWarnings("unchecked")
	HashMap() {
		data = new Node[capacity];
	}

	@Override
	public int size() { return data.length; }

	@Override
	public boolean isEmpty() { return size() == 0; }

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
		putVal(hash(key), key, value);
		if (size() > threshold * capacity)
			resize();
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
    
}