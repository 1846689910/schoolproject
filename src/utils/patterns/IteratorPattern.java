package utils.patterns;

public class IteratorPattern {
    public static void main(String[] args) {
        Menu menu = new Menu(new String[]{"burger", "chips", "fish", "olive"});
        Iterator<String> it = menu.getIterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
}
/**
 * IteratorPattern Pattern:
 * to access the elements of an aggregate object sequentially without exposing its underlying implementation".

 The IteratorPattern pattern is also known as Cursor.
 * */

interface Iterator<E>{
    public boolean hasNext();
    public E next();
}

interface Iterable<E> {
    public Iterator<E> getIterator();
}

class Menu implements Iterable<String> {
    String[] menu;

    public Menu(String[] menu){
        this.menu = menu;
    }

    @Override
    public Iterator<String> getIterator(){
        return new Iterator<String>() {
            private int index;
            @Override
            public boolean hasNext() {
                return index < menu.length;
            }

            @Override
            public String next() {
                return menu[index ++];
            }
        };
    }
}

/** 使用java标准的Iterable和Iterator也一样 */
class NameList implements java.lang.Iterable<String> {
    String[] nameList;

    public NameList(String[] nameList){
        this.nameList = nameList;
    }

    @Override
    public java.util.Iterator<String> iterator() {
        return new java.util.Iterator<String>() {
            int idx = 0;
            @Override
            public boolean hasNext() {
                return idx < nameList.length;
            }

            @Override
            public String next() {
                return nameList[idx ++];
            }
        };
    }
}