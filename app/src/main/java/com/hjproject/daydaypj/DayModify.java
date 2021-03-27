package com.hjproject.daydaypj;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import io.realm.Realm;

public class DayModify extends AppCompatActivity {

    private final int ONE_DAY = 24 * 60 * 60 * 1000;
    private int chnum;
    // 현재 날짜를 알기 위해 사용
    private Calendar mCalendar;
    String titles, dates, ddays;  //받아오는 값
    String mtitles, mdates, mddays;  //보내는 값
    int dcategorys,mcategorys = 0;
    Spinner spinnerDropDown;
    private TextView mTvResult,tv_choose,category;
    private EditText title;
    private ImageButton inputdate;
    private CheckBox ch;
    Realm realm;
    private boolean checks = false;
    public List<DayInfo> list = new ArrayList<>();
    String[] categories = {
            "일정", "시험", "운동", "약속", "사랑", "기타"
    };
    // DatePicker 에서 날짜 선택 시 호출
    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker a_view, int a_year, int a_monthOfYear, int a_dayOfMonth) {
            // D-day 계산 결과 출력
            mTvResult.setText(getDday(a_year, a_monthOfYear, a_dayOfMonth));
            if(mTvResult.getText().toString().contains("일")){
                tv_choose.setText(a_year+"년 "+(a_monthOfYear+1)+"월 "+a_dayOfMonth+"일 부터");
            }else{
                tv_choose.setText(a_year+"년 "+(a_monthOfYear+1)+"월 "+a_dayOfMonth+"일 까지");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daymodify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.
                R.layout.simple_spinner_dropdown_item ,categories);

        Intent intent = getIntent();
        titles = intent.getStringExtra("title");
        dates = intent.getStringExtra("date");
        ddays = intent.getStringExtra("dday");
        dcategorys = intent.getIntExtra("category",1);
        Toast.makeText(getApplicationContext(),"수정",Toast.LENGTH_SHORT).show();

        Intent gintent = getIntent();
        chnum = gintent.getIntExtra("changenum",chnum);

        // chnum = MainActivity.itemcount;
        // Today 보여주기
        TextView tvDate = findViewById(R.id.tv_date);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 E요일 a hh:mm");
        tvDate.setText(sdf.format(d));
        mCalendar = new GregorianCalendar();
        spinnerDropDown = (Spinner)findViewById(R.id.spinnerm);
        mTvResult = findViewById(R.id.tv_result);
        tv_choose = findViewById(R.id.tv_choose);
        title = findViewById(R.id.title);
        inputdate = findViewById(R.id.btn_input_date);
        ch = findViewById(R.id.check1);

        spinnerDropDown.setAdapter(adapter);
        title.setText(titles);
        tv_choose.setText(dates);
        mTvResult.setText(ddays);
        spinnerDropDown.setSelection(dcategorys);
        //ch.setChecked(DayActivity.check);
        ch.setOnClickListener(new CheckBox.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    // TODO : CheckBox is checked.
                    //chnum++;
                    checks = true;
                    //num++;
                } else {
                    // TODO : CheckBox is unchecked.
                    // removeNotification();
                    checks = false;
                }
            }
        }) ;

        spinnerDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Get select item
                dcategorys = spinnerDropDown.getSelectedItemPosition();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        inputdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int year = mCalendar.get(Calendar.YEAR);
                final int month = mCalendar.get(Calendar.MONTH);
                final int day = mCalendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(DayModify.this, mDateSetListener, year, month, day);
                dialog.show();
            }
        });

    }
    public void createNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default").setOngoing(true);
        final DayInfo person = realm.where(DayInfo.class).equalTo("title",title.getText().toString()).findFirst();
        //커스텀 화면 만들기
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        remoteViews.setTextViewText(R.id.maintitle, person.getTitle());  //타이틀을 수정하면 오류가 난다 => titles 로 하면 바꾸면 안됨
        remoteViews.setTextViewText(R.id.mainmessage,person.getDate());
        remoteViews.setTextViewText(R.id.maindday,person.getDday());
        Toast.makeText(getApplicationContext(),chnum+"추가",Toast.LENGTH_SHORT).show();
        switch (dcategorys) {
            case 0:
                //remoteViews.setTextViewText(R.id.cata,  categories[person.getDcategory()]);
                remoteViews.setImageViewResource(R.id.mainimg, R.drawable.todo);
                break;
            case 1:
               // remoteViews.setTextViewText(R.id.cata,categories[person.getDcategory()]);
                remoteViews.setImageViewResource(R.id.mainimg, R.drawable.test);
                break;
            case 2:
               // remoteViews.setTextViewText(R.id.cata,  categories[person.getDcategory()]);
                remoteViews.setImageViewResource(R.id.mainimg, R.drawable.ic_excercise);
                break;
            case 3:
               // remoteViews.setTextViewText(R.id.cata, categories[person.getDcategory()]);
                remoteViews.setImageViewResource(R.id.mainimg, R.drawable.ic_promise);
                break;
            case 4:
               // remoteViews.setTextViewText(R.id.cata,  categories[person.getDcategory()]);
                remoteViews.setImageViewResource(R.id.mainimg, R.drawable.love);
                break;
            case 5:
                // remoteViews.setTextViewText(R.id.cata, categories[sid]);
                remoteViews.setImageViewResource(R.id.mainimg, R.drawable.jeje);
                break;
            default:
                category.setText("null");
        }

        //노티피케이션에 커스텀 뷰 장착
        builder.setContent(remoteViews);
        builder.setSmallIcon(R.drawable.today);


        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(chnum, builder.build());
    }

    public void removeNotification() {
        // Notification 제거
        NotificationManagerCompat.from(this).cancel(chnum);  //1이었음!!!!
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.daymod_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // 앱 정보 나타내기
        if (id == R.id.action_mod) {
            mtitles = title.getText().toString();
            mdates = tv_choose.getText().toString();
            mddays = mTvResult.getText().toString();
            mcategorys = dcategorys;
            // Toast.makeText(getApplicationContext(),"mod Day",Toast.LENGTH_SHORT).show();
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    final DayInfo results = realm.where(DayInfo.class).equalTo("title",titles).findFirst();
                    results.setTitle(mtitles);
                    results.setDate(mdates);
                    results.setDday(mddays);
                    results.setDcategory(mcategorys);
                    Toast.makeText(getApplicationContext(),mtitles+" 수정완료",Toast.LENGTH_SHORT).show();
                }
            });
            realm.close();
            Intent mod = new Intent(DayModify.this, MainActivity.class);
            startActivityForResult(mod,1);
            if(checks == true){
                createNotification();
            }else{
                removeNotification();
            }
        } else if (id == R.id.action_del) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("D-Day 삭제")        // 제목 설정
                    .setMessage("디데이를 삭제 하시겠습니까?")        // 메세지 설정
                    .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                    .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int whichButton){
                            removeNotification();
                            realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    final DayInfo results = realm.where(DayInfo.class).equalTo("title",titles).findFirst();
                                    if(results.isValid()){
                                        results.deleteFromRealm();
                                    }
                                    Toast.makeText(getApplicationContext()," delete complete",Toast.LENGTH_SHORT).show();
                                }
                            });
                            realm.close();
                            Intent mod = new Intent(DayModify.this, MainActivity.class);
                            startActivityForResult(mod,1);
                            finish();
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int whichButton){
                            dialog.cancel();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();


        }

        return super.onOptionsItemSelected(item);
    }

    public String getDday(int a_year, int a_monthOfYear, int a_dayOfMonth) {
        // D-day 설정
        final Calendar ddayCalendar = Calendar.getInstance();
        ddayCalendar.set(a_year, a_monthOfYear, a_dayOfMonth);

        // D-day 를 구하기 위해 millisecond 으로 환산하여 d-day 에서 today 의 차를 구한다.
        final long dday = ddayCalendar.getTimeInMillis() / ONE_DAY;
        final long today = Calendar.getInstance().getTimeInMillis() / ONE_DAY;
        long result = dday - today;

        // 출력 시 d-day 에 맞게 표시
        final String strFormat;
        if (result > 0) {
            strFormat = "D-%d";
        } else if (result == 0) {
            strFormat = "D-Day";
        } else {
            result *= -1;
            strFormat = "%d일";
        }

        final String strCount = (String.format(strFormat, result));
        return strCount;
    }
}
