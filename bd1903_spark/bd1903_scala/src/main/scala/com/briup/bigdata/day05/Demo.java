package com.briup.bigdata.day05;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @program: bd1903_spark
 * @package: com.briup.bigdata.day05
 * @filename: Demo.java
 * @create: 2019/11/19 15:22
 * @author: 29314
 * @description: .
 **/

public class Demo implements Comparable<Demo>, List<Demo> {
    @Override
    public int compareTo(Demo o) {
        return 0;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<Demo> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return null;
    }

    @Override
    public boolean add(Demo demo) {
        return false;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends Demo> c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection<? extends Demo> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public Demo get(int index) {
        return null;
    }

    @Override
    public Demo set(int index, Demo element) {
        return null;
    }

    @Override
    public void add(int index, Demo element) {

    }

    @Override
    public Demo remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<Demo> listIterator() {
        return null;
    }

    @Override
    public ListIterator<Demo> listIterator(int index) {
        return null;
    }

    @Override
    public List<Demo> subList(int fromIndex, int toIndex) {
        return null;
    }
}
