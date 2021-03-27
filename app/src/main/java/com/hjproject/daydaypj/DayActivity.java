package com.hjproject.daydaypj;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
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
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DayActivity extends AppCompatActivity implements Serializable {
    private DayAdapter adapter;
    // Millisecond 형태의 하루(24 시간)
    private final int ONE_DAY = 24 * 60 * 60 * 1000;
    private int number,numberss = 0;
    // 현재 날짜를 알기 위해 사용
    private Calendar mCalendar;
    String titles, date, dday;
    int dcategory = 0;
    // D-day result
    private TextView mTvResult,tv_choose,category;
    private EditText title;
    int sid = 0;
    public static boolean check = false;
    Spinner spinnerDropDown;
    String[] categories = {
            "일정", "시험", "운동", "약속", "사랑", "기타"
    };
    public static Context mContext;
    private boolean checks = false;
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
        setContentView(R.layout.daywrite);
        //mContext = this;
        // 한국어 설정 (ex: date picker)
        Locale.setDefault(Locale.KOREAN);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.
                R.layout.simple_spinner_dropdown_item ,categories);
        // 현재 날짜를 알기 위해 사용
        mCalendar = new GregorianCalendar();
        spinnerDropDown =(Spinner)findViewById(R.id.spinnerm);
        mTvResult = findViewById(R.id.tv_result);
        tv_choose = findViewById(R.id.tv_choose);
        title = findViewById(R.id.title);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);

        spinnerDropDown.setAdapter(adapter);


        spinnerDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // Get select item
                sid = spinnerDropDown.getSelectedItemPosition();
                Toast.makeText(getBaseContext(), "You have selected : " + categories[sid],
                        Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        // Today 보여주기
        TextView tvDate = findViewById(R.id.tv_date);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일 E요일 a hh:mm");
        tvDate.setText(sdf.format(d));
        Intent gintent = getIntent();
        numberss = gintent.getIntExtra("num",numberss);

        // D-day 보여주기

        // Input date click 시 date picker 호출
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View a_view) {
                final int year = mCalendar.get(Calendar.YEAR);
                final int month = mCalendar.get(Calendar.MONTH);
                final int day = mCalendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(DayActivity.this, mDateSetListener, year, month, day);
                dialog.show();

            }
        };

        findViewById(R.id.btn_input_date).setOnClickListener(clickListener);
        final CheckBox checkBox = (CheckBox) findViewById(R.id.check1) ;
        checkBox.setOnClickListener(new CheckBox.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    // TODO : CheckBox is checked.
                    checks = true;
                } else {
                    // TODO : CheckBox is unchecked.
                    checks = false;
                }
            }
        }) ;
    }
    public void createNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default").setOngoing(true);
        //커스텀 화면 만들기
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.custom_notification);
        remoteViews.setTextViewText(R.id.maintitle, title.getText());
        remoteViews.setTextViewText(R.id.mainmessage, tv_choose.getText());
        remoteViews.setTextViewText(R.id.maindday, mTvResult.getText());
        Toast.makeText(getApplicationContext(),numberss+"추가1",Toast.LENGTH_SHORT).show();
        switch (sid) {
            case 0:
               // remoteViews.setTextViewText(R.id.cata,  categories[sid]);
                remoteViews.setImageViewResource(R.id.mainimg, R.drawable.todo);
                break;
            case 1:
               // remoteViews.setTextViewText(R.id.cata,  categories[sid]);
                remoteViews.setImageViewResource(R.id.mainimg, R.drawable.test);
                break;
            case 2:
               // remoteViews.setTextViewText(R.id.cata,  categories[sid]);
                remoteViews.setImageViewResource(R.id.mainimg, R.drawable.ic_excercise);
                break;
            case 3:
               // remoteViews.setTextViewText(R.id.cata,  categories[sid]);
                remoteViews.setImageViewResource(R.id.mainimg, R.drawable.ic_promise);
                break;
            case 4:
               // remoteViews.setTextViewText(R.id.cata, categories[sid]);
                remoteViews.setImageViewResource(R.id.mainimg, R.drawable.love);
                break;
            case 5:
                // remoteViews.setTextViewText(R.id.cata, categories[sid]);
                remoteViews.setImageViewResource(R.id.mainimg, R.drawable.jeje);
                break;
            default:

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
        notificationManager.notify(numberss, builder.build());

    }

    public void removeNotification() {
        // Notification 제거
        NotificationManagerCompat.from(this).cancel(numberss);  //1이었음!!!!
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dayadd_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // 앱 정보 나타내기
        if (id == R.id.action_save) {
            if(checks == true){
                createNotification();
            }else{
                removeNotification();
            }
            Toast.makeText(getApplicationContext(),"Save Day",Toast.LENGTH_SHORT).show();
            titles = title.getText().toString();
            date = tv_choose.getText().toString();
            dday = mTvResult.getText().toString();
            dcategory = sid;
            Intent add = new Intent(DayActivity.this, MainActivity.class);
            add.putExtra("title",titles);
            add.putExtra("date",date);
            add.putExtra("dday",dday);
            add.putExtra("category",sid);
            setResult(RESULT_OK,add);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * D-day 반환
     */
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
