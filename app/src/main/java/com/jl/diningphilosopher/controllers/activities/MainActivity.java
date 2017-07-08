package com.jl.diningphilosopher.controllers.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.jl.diningphilosopher.R;
import com.jl.diningphilosopher.models.ChopStick;
import com.jl.diningphilosopher.models.EventListener;
import com.jl.diningphilosopher.models.Philosopher;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayAdapter<String> adapter;
    private Button start;
    private ListView philosopherListView;
    private ArrayList arrayList = new ArrayList();
    private TextView p1, p2, p3, p4, p5;
    private boolean isRunning = false;
    final Philosopher[] philosophers1 = new Philosopher[5];
    final EventListener eventListener = new EventListener() {
        @Override
        public void updateAdapter(final String string) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.add(string);
                    adapter.notifyDataSetChanged();
                    philosopherListView.setSelection(adapter.getCount() - 1);
                }
            });
        }

        @Override
        public void updatePhilospherStatus(final String name, final Enum state) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    switch (name) {
                        case "P1":
                            if (state.name().startsWith("F"))
                                p1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                            p1.setText(state.name());
                            break;
                        case "P2":
                            if (state.name().startsWith("F"))
                                p2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                            p2.setText(state.name());
                            break;
                        case "P3":
                            if (state.name().startsWith("F"))
                                p3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                            p3.setText(state.name());
                            break;
                        case "P4":
                            if (state.name().startsWith("F"))
                                p4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                            p4.setText(state.name());
                            break;
                        case "P5":
                            if (state.name().startsWith("F"))
                                p5.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red));
                            p5.setText(state.name());
                            break;
                    }
                }
            });

        }
    };
    ChopStick[] chopsticks = new ChopStick[5];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int i = 0; i < chopsticks.length; i++) {
            chopsticks[i] = new ChopStick();

        }
        initiateTask();

        start = (Button) findViewById(R.id.start_stop_btn);
        philosopherListView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, R.layout.item_list, arrayList);
        p1 = (TextView) findViewById(R.id.P1_status_txt);
        p2 = (TextView) findViewById(R.id.P2_status_txt);
        p3 = (TextView) findViewById(R.id.P3_status_txt);
        p4 = (TextView) findViewById(R.id.P4_status_txt);
        p5 = (TextView) findViewById(R.id.P5_status_txt);

        philosopherListView.setAdapter(adapter);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isRunning) {
                    stopTask();
                    initiateTask();
                    isRunning=false;
                    start.setText("Start");
                } else {
                    p1.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                    p2.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                    p3.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                    p4.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                    p5.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                    p1.setText("");
                    p2.setText("");
                    p3.setText("");
                    p4.setText("");
                    p5.setText("");
                    adapter.clear();




        /*
        give every philosoper 2 chopsticks
        first philosopher will take his _rightChopstick  chopstick first and the remaining philosophers will take their _leftChopstick chopstick first forks
        chopsticks[(i + 1) % chopsticks.length] is the modulus of chopsticks and it will give 1,2,3,4,0
         */
                    for (int i = 0; i < philosophers1.length; i++) {

                        philosophers1[i].executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    }
                    start.setText("Stop");
                    isRunning = true;
                }
            }
        });
    }

public void initiateTask(){
    for (int i = 0; i < philosophers1.length; i++) {
        ChopStick left = chopsticks[i];
        ChopStick right = chopsticks[(i + 1) % chopsticks.length];
        if (i == 0) {
            philosophers1[i] = new Philosopher("P" + (i + 1), right, left, getApplicationContext(), eventListener);
        } else {
            philosophers1[i] = new Philosopher("P" + (i + 1), left, right, getApplicationContext(), eventListener);
        }
    }
}
    public void stopTask(){
        for (int i = 0; i<philosophers1.length;i++) {
            philosophers1[i].cancel(true);
        }
    }
}
