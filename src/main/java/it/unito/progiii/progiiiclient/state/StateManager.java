package it.unito.progiii.progiiiclient.state;

import javafx.beans.property.SimpleObjectProperty;

public class StateManager {

    private SimpleObjectProperty<ConnectState> state;

    public StateManager() {
        state = new SimpleObjectProperty<>();
        state.set(ConnectState.DISCONNECTED);
    }

    public SimpleObjectProperty<ConnectState> stateProperty() {
        return state;
    }

    public void setState(ConnectState state) {
        this.state.set(state);
    }
}
