package observable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractObservable<T extends Signal> implements Observable<T> {

    private List<Observer<T>> observers = new ArrayList<Observer<T>>();
    
    @Override
    public void notifyObservers(T signal) {
        for (Observer<T> o : observers){
            o.notified(signal);
        }
    }

    @Override
    public void addObserver(Observer<T> o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer<T> o) {
        observers.remove(o);
    }
    
    @Override
    public int observerCount(){
        return observers.size();
    }

}
