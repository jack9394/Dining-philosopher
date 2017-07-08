package com.jl.diningphilosopher.models;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Random;

import static Utils.Utils.showMessage;

/**
 * Created by jack on 5/8/2016.
 */
public class Philosopher extends AsyncTask<Void, String, Void> {
    private static final int MAX_EATING_TIME = 1000;
    private static final int MAX_THINKING_TIME = 500;
    private final ChopStick _leftChopstick;
    private final ChopStick _rightChopstick;
    private final Random _randomise = new Random();
    private String _philosopherName;
    private int _counter = 0;
    private Context ctx;
    private EventListener eventListener;

    public Philosopher(String _philosopherName, ChopStick _rightChopstick, ChopStick _leftChopstick, Context ctx, EventListener eventListener) {
        this._philosopherName = _philosopherName;
        this._rightChopstick = _rightChopstick;
        this._leftChopstick = _leftChopstick;
        this.ctx = ctx;
        this.eventListener = eventListener;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        showMessage(ctx, values[0]);

    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            while (true) {
                action(_state.Thinking);
                synchronized (_leftChopstick) {
                    if (_leftChopstick.is_isUsed()) {
                        _leftChopstick.wait();
                    }
                    synchronized (_rightChopstick) {
                        _counter++;
                        if (_rightChopstick.is_isUsed()) {
                            _rightChopstick.wait();
                        }
                        if (_counter == 10) {
                            action(_state.Eating);
                            _leftChopstick.setUsed(false);
                            _rightChopstick.setUsed(false);
                            _leftChopstick.notify();
                            _rightChopstick.notify();
                            break;
                        }
                        action(_state.Eating);
                        _leftChopstick.setUsed(false);
                        _rightChopstick.setUsed(false);
                        _leftChopstick.notify();
                        _rightChopstick.notify();
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        try {
            action(_state.Finish);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void action(_state state) throws InterruptedException {
        switch (state) {
            case Thinking:
                eventListener.updatePhilospherStatus(_philosopherName,_state.Thinking);
                eventListener.updateAdapter(_philosopherName + ":thinking");
                Thread.sleep(_randomise.nextInt(MAX_THINKING_TIME));
                break;
            case Eating:
                eventListener.updatePhilospherStatus(_philosopherName,_state.Eating);
                eventListener.updateAdapter(_philosopherName + ":eating" + "|counter:" + _counter);
                Thread.sleep(_randomise.nextInt(MAX_EATING_TIME));
                break;
            case Finish:
                eventListener.updatePhilospherStatus(_philosopherName,_state.Finish);
        }

    }

    private enum _state {Thinking, Eating, Finish}
}
