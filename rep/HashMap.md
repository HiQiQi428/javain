# HashMap原理学习

### 静态变量

```java
static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16
static final int MAXIMUM_CAPACITY = 1 << 30;
static final float DEFAULT_LOAD_FACTOR = 0.75f;
static final int TREEIFY_THRESHOLD = 8;
static final int UNTREEIFY_THRESHOLD = 6;
static final int MIN_TREEIFY_CAPACITY = 64;
```

### Node

```java
static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;
}
```

### hash算法

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

以键值的 hashcode 高16位异或低16位得到。注意 >>> 运算符，无符号右移，忽略符号位，空位都以0补齐。

由于 hash 为 int 值，大小在-2 ^ 31到2 ^ 31，HashMap 用不了这么多的值，所以只选取16比特，为了降低碰撞发生的概率，使用高16位异或低16位来使32个比特都能影响到产生 hash 值。

### comparableClassFor

