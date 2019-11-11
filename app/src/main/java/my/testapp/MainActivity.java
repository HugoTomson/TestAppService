package my.testapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;
import my.testapp.Adapters.AdapterPerson;
import my.testapp.Interfaces.IUpdater;
import my.testapp.model.Person;

public class MainActivity extends AppCompatActivity implements IUpdater {
    private Timer mTimer;
    private MyTimerTask mMyTimerTask;
    private int period = 10, age_from=22,age_to=35;
    private AdapterPerson adapter;
    public final static String action = "get_data";
    MyService mService;
    boolean mBound = false;

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            mService = binder.getService();
            mService.updater=MainActivity.this;
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, MyService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rec = (RecyclerView) findViewById(R.id.rec);
        rec.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rec.setLayoutManager(llm);
        adapter = new AdapterPerson(this, new Person[0]);
        rec.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (data != null) {

            String s=data.getStringExtra("age_from");
            if(!s.equals(""))
                age_from=Integer.parseInt(s);
            if(age_from<18 ||age_to<age_from)
                age_from=18;
            else if(age_from>59)
                age_from=59;

            s=data.getStringExtra("age_to");
            if(!s.equals(""))
                age_to=Integer.parseInt(s);
            if(age_to<19)
                age_to=19;
            else if(age_to>60)
                age_to=60;

            s=data.getStringExtra("period");
            if(!s.equals(""))
                period=Integer.parseInt(s);
            if(period<2)
                period=2;
            else if(period>30)
                period=30;
            findViewById(R.id.try_upd).setVisibility(View.GONE);
            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.rec).setVisibility(View.GONE);
            startTimer();
        }
    }

    void startTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        mMyTimerTask = new MyTimerTask();
        mTimer.schedule(mMyTimerTask, period*1000, period*1000);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, Settings_Activity.class);
                intent.putExtra("period",period);
                intent.putExtra("age_from",age_from);
                intent.putExtra("age_to",age_to);

                startActivityForResult(intent, 1);
                return true;
            case R.id.update:
                findViewById(R.id.try_upd).setVisibility(View.GONE);
                findViewById(R.id.loader).setVisibility(View.VISIBLE);
                findViewById(R.id.rec).setVisibility(View.GONE);
                startTimer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void update(Person[] persons) {
        adapter.updateUdapter(persons);
        findViewById(R.id.try_upd).setVisibility(View.GONE);
        findViewById(R.id.loader).setVisibility(View.GONE);
        findViewById(R.id.rec).setVisibility(View.VISIBLE);
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            final Person[] persons = new Person[10];
            String[] names_m = getResources().getStringArray(R.array.names_m);
            String[] names_w = getResources().getStringArray(R.array.names_w);
            String[] s_names_m = getResources().getStringArray(R.array.s_names_m);
            String[] s_names_w = getResources().getStringArray(R.array.s_names_w);
            String[] job = getResources().getStringArray(R.array.names_job);
            Random rand = new Random();
            for (int i = 0; i < persons.length; i++) {
                int gender = rand.nextInt(2);
                if (gender == 0) {
                    persons[i] = new Person(names_m[rand.nextInt(names_m.length)], s_names_m[rand.nextInt(s_names_m.length)], "M", 18 + rand.nextInt(age_to - 18), job[rand.nextInt(job.length)]);
                } else {
                    persons[i] = new Person(names_w[rand.nextInt(names_w.length)], s_names_w[rand.nextInt(s_names_w.length)], "Ж", 18 + rand.nextInt(age_to - 18), job[rand.nextInt(job.length)]);
                }
            }
            Intent intent = new Intent(MainActivity.action);
            intent.putExtra("persons", persons);
            sendBroadcast(intent);
        }
    }

}
