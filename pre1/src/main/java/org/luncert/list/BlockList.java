package org.luncert.list;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class BlockList<E> implements List<E> {

    final static int BLOCK_OPACITY = 16;
    final static int DEFAULT_BLOCKS_OPACITY = 1;
    Block[] blocks;
    int blockSize;
    int elemSize;
    int opacity;

    private final static class Block {
        Object[] data;
        int opacity;
        int size;
        Block() {
            opacity = BlockList.BLOCK_OPACITY;
            data = new Object[opacity];
        }
        Block(int opacity) {
            this.opacity = opacity > BlockList.BLOCK_OPACITY ? opacity : BlockList.BLOCK_OPACITY;
            data = new Object[this.opacity];
        }
        boolean isFull() { return size == data.length; }
        void add(Object e) { data[size++] = e; }
        void add(int index, Object e) {
            if (size == opacity) {
                opacity += opacity >> 1;
                Object[] tmp = new Object[opacity];
                System.arraycopy(data, 0, tmp, 0, index);
                tmp[index] = e;
                System.arraycopy(data, index, tmp, index + 1, size - index);
                data = tmp;
            }
            else {
                System.arraycopy(data, index, data, index + 1, size - index - 1);
                data[index] = e;
            }
            size++;
        }
    }

    private final static class Index {
        int base;
        int offset;
        Index(int base, int offset) {
            this.base = base;
            this.offset = offset;
        }
    }

    public BlockList() { init(); }

    private void init() {
        blockSize = 0;
        elemSize = 0;
        opacity = DEFAULT_BLOCKS_OPACITY;
        blocks = new Block[opacity];
        blocks[blockSize++] = new Block();
    }

    /**
     * @return 标志avBlock是否还有元素,如果返回true则表示使用blocks[blockSize - 1],否则使用avBlock
     */
    private void opacityAdjust() {
        Block block = blocks[blockSize - 1];
        if (block.isFull()) {
            if (blockSize == blocks.length) {
                opacity = opacity << 1;
                Block[] tmp = new Block[opacity];
                System.arraycopy(blocks, 0, tmp, 0, blockSize);
                blocks = tmp;
            }
            blocks[blockSize++] = new Block(block.size + 16);
        }
    }

    /**
     * @param o
     * @return
     */
    private Index lastFindBlock(Object o) {
        Block block;
        for (int i = blockSize - 1; i > -1; i--) {
            block = blocks[i];
            for (int j = block.size - 1; j > -1; j--)
                if (block.data[j].equals(o))
                    return new Index(i, j);
        }
        return null;
    }

    /**
     * 寻找o所在block
     */
    private Index findBlock(Object o) {
        Block block;
        for (int i = 0; i < blockSize; i++) {
            block = blocks[i];
            for (int j = 0; j < block.size; j++)
                if (block.data[j].equals(o))
                    return new Index(i, j);
        }
        return null;
    }

    /**
     * 寻找index指向的block
     * @param index
     * @return 包含指定块和块内偏移
     */
    private Index findBlock(int index) {
        if (index >= elemSize || index < 0)
            throw new IndexOutOfBoundsException("index: " + index);
        for (int i = 0, total = 0; i < blockSize; i++) {
            total += blocks[i].size;
            if (total > index)
                return new Index(i, index - (total - blocks[i].size));
        }
        return null;
    }

    public int size() { return elemSize; }

    public boolean isEmpty() { return blockSize == 0; }

    public boolean contains(Object o) { return findBlock(o) != null; }

    /**
     * @return Object[]
     */
    public Object[] toArray() {
        Object[] array = new Object[elemSize];
        Block block;
        for (int i = 0, c = 0; i < blockSize; i++) {
            block = blocks[i];
            System.arraycopy(block.data, 0, array, c, block.size);
            c += block.size;
        }
        return array;
    }

    /**
     * @return T[]
     */
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length > elemSize) {
            Block block;
            for (int i = 0, c = 0; i < blockSize; i++) {
                block = blocks[i];
                System.arraycopy(block.data, 0, a, c, block.size);
                c += block.size;
            }
            a[elemSize] = null;
            return a;
        }
        else
            return (T[]) toArray();
    }

    /**
     * 在最后一个block的末尾添加元素
     */
    public boolean add(E e) {
        opacityAdjust();
        blocks[blockSize - 1].add(e);
        elemSize++;
        return true;
    }

    private void add(Index index, E e) {
        blocks[index.base].add(index.offset, e);
        elemSize++;
    }

    public void add(int index, E element) {
        opacityAdjust();
        Index i = findBlock(index);
        if (i != null)
            add(i, element);
    }

    /**
     * 如果block有一个以上元素则从block中删除o,并对齐,否则直接从blocks中删除这个block
     */
    @SuppressWarnings("unchecked")
    private E remove(Index i) {
        Block block = blocks[i.base];
        E oldValue = (E) block.data[i.offset];
        if (block.size == 1) {
            System.arraycopy(blocks, i.base + 1, blocks, i.base, blockSize - i.base - 1);
            blockSize--;
        }
        else {
            System.arraycopy(block.data, i.offset + 1, block.data, i.offset, block.size - i.offset - 1);
            block.size--;
        }
        elemSize--;
        return oldValue;
    }

    /**
     * @param o
     * @return
     */
    public boolean remove(Object o) {
        Index i = findBlock(o);
        return i != null ? remove(i) != null : false;
    }

    /**
     * @param index
     * @return
     */
    public E remove(int index) {
        Index i = findBlock(index);
        return i != null ? remove(i) : null;
    }
    
    public void clear() { init(); }

    /**
     * @param index
     * @return
     */
    @SuppressWarnings("unchecked")
    public E get(int index) {
        Index i = findBlock(index);
        return i != null ? (E) blocks[i.base].data[i.offset] : null;
    }

    @SuppressWarnings("unchecked")
    private E set(Index i, E e) {
        Block block = blocks[i.base];
        E oldValue = (E) block.data[i.offset];
        block.data[i.offset] = e;
        return oldValue;
    }
    
    /**
     * @param index
     * @param element
     * @return
     */
    public E set(int index, E element) {
        Index i = findBlock(index);
        return i != null ? set(i, element) : null;
    }
    
    /**
     * @param o
     * @return
     */
    public int indexOf(Object o) {
        Index i = findBlock(o);
        return i.base * (i.base + 1) / 2 * BLOCK_OPACITY + i.offset;
    }

    
    public int lastIndexOf(Object o) {
        Index i = lastFindBlock(o);
        return i.base * (i.base + 1) / 2 * BLOCK_OPACITY + i.offset;
    }
    
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    
    public boolean addAll(Collection<? extends E> c) {
        return false;
    }

    
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    
    public boolean retainAll(Collection<?> c) {
        return false;
    }
    
    public String toString() {
        StringBuilder builder = new StringBuilder().append('[');
        for (Block block : blocks) {
            for (int i = 0; i < block.size; i++)
                builder.append(block.data[i]).append(',');
        }
        int i = builder.length();
        builder.replace(i - 2, i, "]");
        return builder.toString();
    }

    public String prettyString() {
        StringBuilder builder = new StringBuilder().append("BlockLists [\n");
        Block block;
        for (int i = 0; i < blockSize; i++) {
            block = blocks[i];
            builder.append("opacity: ").append(block.opacity).append(", elem: ");
            for (int j = 0; j < block.size; j++)
                builder.append(block.data[j]).append(", ");
            builder.append('\n');
        }
        int i = builder.length();
        builder.replace(i - 3, i, "\n]");
        return builder.toString();
    }

    public int getBlockSize() { return blockSize; }

    public int getElemSize() { return elemSize; }

    public int getSpace() {
        int space = 0;
        for (int i = 0; i < blockSize; i++)
            space += blocks[i].opacity;
        return space;
    }

    public double getUsage() { return (double) getElemSize() / getSpace(); }

    public Iterator<E> iterator() { return new Itr(); }
    
    public ListIterator<E> listIterator() { return new ListItr(); }
    
    public ListIterator<E> listIterator(int index) {
        return null;
    }
    
	public List<E> subList(int fromIndex, int toIndex) {
		return null;
    }

    private class Itr implements Iterator<E> {
        Block block;
        int base;
        int offset;

        Itr() {
            block = blocks[base++];
        }

        public boolean hasNext() { return offset < block.size || base < blockSize; }

        @SuppressWarnings("unchecked")
        public E next() {
            if (offset == block.size) {
                if (base == blockSize)
                    throw new NoSuchElementException();
                block = blocks[base++];
                offset = 0;
            }
            return (E) block.data[offset++];
        }

        public void remove() {
            if (offset < block.size)
                BlockList.this.remove(new Index(base - 1, offset));
            else if (base < blockSize)
                BlockList.this.remove(new Index(base++, (offset = 0)));
            else
                throw new NoSuchElementException();
        }

    }

    private class ListItr extends Itr implements ListIterator<E> {

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public E previous() {
            return null;
        }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return 0;
        }

        /**
         * 修改下一个元素的值,然后移动下标
         */
        public void set(E e) {
            if (offset < block.size)
                BlockList.this.set(new Index(base - 1, offset++), e);
            else if (base < blockSize) {
                offset = 0;
                BlockList.this.set(new Index(base++, offset++), e);
            }
            else
                throw new NoSuchElementException();
        }

        @Override
        public void add(E e) {
            if (offset < block.size)
                BlockList.this.add(new Index(base - 1, offset++), e);
            else if (base < blockSize) {
                offset = 0;
                BlockList.this.add(new Index(base++, offset++), e);
            }
            else
                throw new NoSuchElementException();
        }

    }

}