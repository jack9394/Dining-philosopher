package com.jl.diningphilosopher.models;

/**
 * Created by jack on 5/8/2016.
 */
public interface EventListener {
    public void updateAdapter(String string);
    public void updatePhilospherStatus(String philosopher,Enum state);
  //  public void stopServices();
}
