package slotmachien.actions;

import observable.Observable;
import observable.Observer;

public abstract class Action implements Observer {
    
    public abstract void perform();
    
    public void notified(){
        perform();
    }
    
    // Yay syntax sugar
    public void performOnNotification(Observable o){
        o.addObserver(this);
    }
    
    public Action compose(Action other){
        return new ComposedAction(this, other);
    }

    private class ComposedAction extends Action {
        private Action left, right;

        public ComposedAction(Action left, Action right){
            this.left  = left;
            this.right = right;
        }

        public void perform(){
            left.perform();
            right.perform();
        }
        
    }
}

