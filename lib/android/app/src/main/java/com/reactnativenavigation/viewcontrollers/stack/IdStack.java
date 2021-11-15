package com.reactnativenavigation.viewcontrollers.stack;

import com.reactnativenavigation.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

import static com.reactnativenavigation.utils.CollectionUtils.*;

public class IdStack<E> implements Iterable<String> {

	private final ArrayList<String> deque = new ArrayList<>();
	private final Map<String, E> map = new HashMap<>();

	public void push(String id, E item) {
		deque.add(id);
		map.put(id, item);
	}

	public void set(String id, E item, int index) {
		deque.add(index, id);
		map.put(id, item);
	}

	public E peek() {
		return isEmpty() ? null : map.get(last(deque));
	}

	public E pop() {
		return isEmpty() ? null : map.remove(removeLast(deque));
	}

	public boolean isEmpty() {
		return deque.isEmpty();
	}

	public int size() {
		return deque.size();
	}

	public String peekId() {
		return last(deque);
	}

	public void clear() {
		deque.clear();
		map.clear();
	}

	public E get(final String id) {
		return map.get(id);
	}

	public E get(final int index) {
		return map.get(deque.get(index));
	}

	public boolean containsId(final String id) {
		return deque.contains(id);
	}

	public E remove(final String id) {
		if (!containsId(id)) {
			return null;
		}
		deque.remove(id);
		return map.remove(id);
	}

	public boolean isTop(final String id) {
		return StringUtils.isEqual(id, peekId());
	}

	@NonNull
	@Override
	public Iterator<String> iterator() {
		return deque.iterator();
	}


	public List<E> values() {
		return map(deque, map::get);
	}

	public void remove(Iterator<String> iterator, String id) {
		iterator.remove();
		map.remove(id);
	}
}
