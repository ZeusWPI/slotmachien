package observable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractObservable implements Observable {

    private List<Observer> observers = new ArrayList<Observer>();
    
    @Override
    public void notifyObservers() {
        for (Observer o : observers){
            o.notified();
        }
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }
    
    @Override
    public int observerCount(){
        return observers.size();
    }

}
