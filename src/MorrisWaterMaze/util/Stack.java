package MorrisWaterMaze.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;


public final class Stack<T> implements Iterable<T>
{
    private final Deque<T>
        deque = new ArrayDeque<>();
    
    
    public void push(T element)
    {
        deque.addFirst(element);
    }
    
    public T peek()
    {
        return deque.peekFirst();
    }
    
    public void clear()
    {
        deque.clear();
    }
    
    @Override
    public Iterator<T> iterator()
    {
        return deque.iterator();
    }
    
    @Override
    public void forEach(Consumer<? super T> action)
    {
        deque.descendingIterator()
             .forEachRemaining(action);
    }
    
    public Stream<T> stream()
    {
        return deque.stream();
    }
}
