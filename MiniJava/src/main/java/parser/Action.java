package parser;

public class Action {
    public abstract class ActionCode {
        abstract act getActionCode();
    }
    public class ShiftAction extends ActionCode {

        @Override
        act getActionCode() {
            return act.shift;
        }
    }
    public class AcceptAction extends ActionCode {

        @Override
        act getActionCode() {
            return act.accept;
        }
    }
    public class ReduceAction extends ActionCode {

        @Override
        act getActionCode() {
            return act.reduce;
        }
    }

    //if action = shift : number is state
    //if action = reduce : number is number of rule
    public ActionCode action;
    public int number;

    public Action(act action, int number) {
        setAction(action);
        this.number = number;
    }

    public String toString() {
        switch (getAction()) {
            case accept:
                return "acc";
            case shift:
                return "s" + number;
            case reduce:
                return "r" + number;
        }
        return getAction().toString() + number;
    }

    public act getAction() {
        return action.getActionCode();
    }

    public void setAction(act actionCode) {
        switch (actionCode) {
            case accept:
                action = new AcceptAction();
                break;
            case shift:
                action = new ShiftAction();
                break;
            case reduce:
                action = new ReduceAction();
                break;
            default:
                throw new IllegalArgumentException("action code non-existent");
        }
    }
}

enum act {
    shift, reduce, accept
}
