package com.hjproject.daydaypj;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    private Realm realm;
    private RecyclerView rcv;
    private DayAdapter adapter;
    private DayInfo Day_main;
    public List<DayInfo> list = new ArrayList<>();
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_action_menu);
        setSupportActionBar(toolbar);

        rcv = findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        rcv.setLayoutManager(linearLayoutManager);

        Realm.init(this);
        realm = Realm.getDefaultInstance();
        Log.i("RealmManager",realm.getPath());
        RealmResults<DayInfo> realmResults = realm.where(DayInfo.class)
                .findAllAsync();
        for(DayInfo dayInfo : realmResults) {
            list.add(new DayInfo(dayInfo.getTitle(),dayInfo.getDate(),dayInfo.getDday(),dayInfo.getDcategory()));
            adapter =  new DayAdapter(MainActivity.this,list);
            rcv.setAdapter(adapter);
        }
        Log.d("MainActivity","경로"+realm.getConfiguration());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.day_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        // 앱 정보 나타내기
        if (id == R.id.action_add) {
            Toast.makeText(getApplicationContext(),"Add Day",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,DayActivity.class);
            if(adapter != null){
                intent.putExtra("num",adapter.getItemCount());
            }else{
                Toast.makeText(getApplicationContext(),"디데이 처음으로 추가!!",Toast.LENGTH_SHORT).show();;
            }
            startActivityForResult(intent,1);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        if(resultCode == RESULT_OK) {

            String title = data.getStringExtra("title");
            String date = data.getStringExtra("date");
            String dday = data.getStringExtra("dday");
            int dcategory = data.getIntExtra("category",1);
            Toast.makeText(this,title + "," + date,Toast.LENGTH_SHORT).show();

            realm.beginTransaction();
            Day_main = realm.createObject(DayInfo.class);
            Day_main.setTitle(title);
            Day_main.setDate(date);
            Day_main.setDday(dday);
            Day_main.setDcategory(dcategory);
            realm.commitTransaction();

            RealmResults<DayInfo> realmResults = realm.where(DayInfo.class)
                    .equalTo("title",title).equalTo("date",date).equalTo("dday",dday).equalTo("dcategory",dcategory)
                    .findAllAsync();

            list.add(new DayInfo(title,date,dday,dcategory));
            adapter = new DayAdapter(MainActivity.this,list);
            rcv.setAdapter(adapter);

        }
    }
}
