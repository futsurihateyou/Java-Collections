import java.util.Iterator;
import java.util.HashSet;

public class CarHashSet implements CarSet {

    private static final int INITIAL_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    private int size = 0;
    private Entry[] array = new Entry[INITIAL_CAPACITY];

    @Override
    public boolean add(Car car) {
        if (size >= (array.length * LOAD_FACTOR)) {
            increaseArray();
        }
        boolean added = add(car, array);
        if (added) {
            size++;
        }
        return added;
    }

    private boolean add(Car car, Entry[] dst) {
        int position = getElementPosition(car, dst.length);
        if (dst[position] == null) {
            Entry entry = new Entry(car, null);
            dst[position] = entry;
            return true;
        } else {
            Entry existedElement = dst[position];
            while (true) {
                if (existedElement.value.equals(car)) {
                    return false;
                } else if (existedElement.next == null) {
                    existedElement.next = new Entry(car, null);
                    return true;
                } else {
                    existedElement = existedElement.next;
                }
            }
        }
    }

    @Override
    public boolean remove(Car car) {
        int position = getElementPosition(car, array.length);
        if (array[position] == null) {
            return false;
        }
        Entry secondLast = array[position];
        Entry last = secondLast.next;
        if (secondLast.value.equals(car)) {
            array[position] = last;
            size--;
            return true;
        }
        while (last != null) {
            if (last.value.equals(car)) {
                secondLast.next = last.next;
                size--;
                return true;
            } else {
                secondLast = last;
                last = last.next;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Car car) {
        int position = getElementPosition(car, array.length);
        Entry currentElement = array[position];
        while (currentElement != null) {
            if (currentElement.value.equals(car)) {
                return true;
            }
            currentElement = currentElement.next;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        array = new Entry[INITIAL_CAPACITY];
        size = 0;
    }

    @Override
    public Iterator<Car> iterator() {
        return new Iterator<Car>() {

            int index = 0;
            int arrayIndex = 0;
            Entry entry;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public Car next() {
                while(array[arrayIndex] == null){
                    arrayIndex++;
                }
                if(entry == null){
                    entry = array[arrayIndex];
                }
                Car result = entry.value;
                entry = entry.next;
                if(entry == null){
                    arrayIndex++;
                }
                index++;
                return result;
            }
        };
    }

    private void increaseArray() {
        Entry[] newArray = new Entry[array.length * 2];
        for (Entry entry : array) {
            Entry existedElement = entry;
            while (existedElement != null) {
                add(existedElement.value, newArray);
                existedElement = existedElement.next;
            }
        }
        array = newArray;
    }

    private int getElementPosition(Car car, int arrayLength) {
        return Math.abs(car.hashCode() % arrayLength);
    }

    private static class Entry {
        private Car value;
        private Entry next;

        public Entry(Car value, Entry next) {
            this.value = value;
            this.next = next;
        }
    }
}