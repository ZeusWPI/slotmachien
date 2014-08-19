package slotmachien;

public abstract class Action {
    public abstract void performAction();
    public Action compose(Action other){
        return new ComposedAction(this, other);
    }

    private class ComposedAction extends Action {
        private Action left, right;

        public ComposedAction(Action left, Action right){
            this.left  = left;
            this.right = right;
        }

        public void performAction(){
            left.performAction();
            right.performAction();
        }
    }
}

