package com.pmnm.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public final class CircularQueue<E extends Serializable> implements Deque<E>, Serializable {

	private static final long serialVersionUID = 341944174576430374L;
	
	private  ArrayList<E> elements;
	private int index;
	
	public CircularQueue() { this(6); }
	
	public CircularQueue(int capacity) {
		super();
		elements = new ArrayList<>(capacity);
		index = -1;
	}
	
	public E getNext() {
		index = (index + 1) % elements.size();
		return elements.get(index);
	}
	
	@Override
	public boolean isEmpty() { return elements.isEmpty(); }

	@Override
	public Object[] toArray() { return elements.toArray(); }

	@Override
	public <T> T[] toArray(T[] a) { return elements.toArray(a); }

	@Override
	public boolean containsAll(Collection<?> c) { return elements.containsAll(c); }

	@Override
	public boolean removeAll(Collection<?> c) { return elements.removeAll(c); }

	@Override
	public boolean retainAll(Collection<?> c) { return elements.retainAll(c); }

	@Override
	public void clear() { elements.clear(); }

	@Override
	public void addFirst(E e) { elements.add(0, e); }

	@Override
	public void addLast(E e) { elements.add(e); }

	@Override
	public boolean offerFirst(E e) { addFirst(e); return true; }

	@Override
	public boolean offerLast(E e) { addLast(e); return true; }

	@Override
	public E removeFirst() { return elements.remove(0); }

	@Override
	public E removeLast() { return elements.remove(elements.size() - 1); }

	@Override
	public E pollFirst() { 
		if (isEmpty()) return null;
		return removeFirst();
	}

	@Override
	public E pollLast() {
		if (isEmpty()) return null;
		return removeLast();
	}

	@Override
	public E getFirst() { return elements.get(0); }

	@Override
	public E getLast() { return elements.get(elements.size() - 1); }

	@Override
	public E peekFirst() {
		if (isEmpty()) return null;
		return getFirst();
	}

	@Override
	public E peekLast() {
		if (isEmpty()) return null;
		return getLast();
	}

	@Override
	public boolean removeFirstOccurrence(Object o) { return elements.remove(o); }

	@Override
	public boolean removeLastOccurrence(Object o) {
		// TODO 
		return false;
	}

	@Override
	public boolean add(E e) { addLast(e); return true; }

	@Override
	public boolean offer(E e) { offerLast(e); return true; }

	@Override
	public E remove() { return removeFirst(); }

	@Override
	public E poll() { return pollFirst(); }

	@Override
	public E element() { return getFirst(); }

	@Override
	public E peek() { return peekFirst(); }

	@Override
	public boolean addAll(Collection<? extends E> c) { return elements.addAll(c); }

	@Override
	public void push(E e) { addFirst(e); }

	@Override
	public E pop() { return removeFirst(); }

	@Override
	public boolean remove(Object o) { return elements.remove(o); }

	@Override
	public boolean contains(Object o) { return elements.contains(o); }

	@Override
	public int size() { return elements.size(); }

	@Override
	public Iterator<E> iterator() { return elements.iterator(); }

	@Override
	public Iterator<E> descendingIterator() { return new ReverseIterator(this); }

	public class ReverseIterator implements Iterator<E> {

		private CircularQueue<E> q;
		private int index;
		
		public ReverseIterator(CircularQueue<E> q) {
			this.q = q;
			index = q.elements.size() - 1;
		}
		
		@Override
		public boolean hasNext() { return index >= 0; }

		@Override
		public E next() { 
			if(!hasNext()){
				throw new NoSuchElementException();
			}
			return q.elements.get(index--);
		}
	}
}
