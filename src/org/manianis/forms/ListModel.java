package org.manianis.forms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.AbstractListModel;

public class ListModel<E> extends AbstractListModel<E> implements List<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	List<E> listItems = new ArrayList<>();

	@Override
	public int getSize() {
		return listItems.size();
	}

	@Override
	public E getElementAt(int index) {
		return listItems.get(index);
	}

	public void clear() {
		int index1 = listItems.size() - 1;
		listItems.clear();
		if (index1 >= 0) {
			fireIntervalRemoved(this, 0, index1);
		}
	}

	public E remove(int index) {
		E rv = listItems.get(index);
		listItems.remove(index);
		fireIntervalRemoved(this, index, index);
		return rv;
	}

	public void add(int index, E element) {
		listItems.add(index, element);
		fireIntervalAdded(this, index, index);
	}

	public int size() {
		return listItems.size();
	}

	public boolean contains(Object elem) {
		return listItems.contains(elem);
	}

	public int indexOf(Object elem) {
		return listItems.indexOf(elem);
	}

	public int lastIndexOf(Object elem) {
		return listItems.lastIndexOf(elem);
	}

	@Override
	public boolean isEmpty() {
		return listItems.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return listItems.iterator();
	}

	@Override
	public Object[] toArray() {
		return listItems.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return listItems.toArray(a);
	}

	@Override
	public boolean add(E e) {
		int index = listItems.size();
		boolean res = listItems.add(e);
		fireIntervalAdded(this, index, index);
		return res;
	}

	@Override
	public boolean remove(Object obj) {
		int index = indexOf(obj);
		boolean rv = listItems.remove(obj);
		if (index >= 0) {
			fireIntervalRemoved(this, index, index);
		}
		return rv;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return listItems.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		if (c.isEmpty()) {
			return false;
		}
		int startIndex = getSize();
		boolean res = listItems.addAll(c);
		fireIntervalAdded(this, startIndex, getSize() - 1);
		return res;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		if (index < 0 || index > getSize()) {
			throw new ArrayIndexOutOfBoundsException("index out of range: " + index);
		}
		if (c.isEmpty()) {
			return false;
		}
		boolean res = listItems.addAll(index, c);
		fireIntervalAdded(this, index, index + c.size() - 1);
		return res;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		int index1 = listItems.size() - 1;
		boolean res = listItems.removeAll(c);
		if (index1 >= 0) {
			fireIntervalRemoved(this, 0, index1);
		}
		return res;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return listItems.retainAll(c);
	}

	@Override
	public E get(int index) {
		return listItems.get(index);
	}

	@Override
	public E set(int index, E element) {
		E item = listItems.set(index, element);
		fireContentsChanged(this, index, index);
		return item;
	}

	@Override
	public ListIterator<E> listIterator() {
		return listItems.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return listItems.listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return listItems.subList(fromIndex, toIndex);
	}

}
