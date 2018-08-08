package com.example.yperinterntest;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddItemActivity extends MainActivity {
    EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        //앱바 사용
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.textColorPrimary));

        //앱바 제목 변경
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("추가");


        //앱바 위로 버튼 표시
        actionBar.setDisplayHomeAsUpEnabled(true);

        editText2=findViewById(R.id.editText2);
    }

    //완료 버튼 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.finishmenu, menu);
        return true;
    }

    //완료 버튼 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //입력값 저장
        String addressText = editText2.getText().toString();

        switch (item.getItemId()){
            case R.id.finish_item_menu:
                Intent sendIntent = new Intent();

                //시각 처리
                long currentTime = System.currentTimeMillis();
                Date date = new Date(currentTime);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd hh:mm");
                String timeStr = dateFormat.format(date);

                //값 전달
                sendIntent.putExtra("address", addressText);
                sendIntent.putExtra("time", timeStr);
                setResult(RESULT_OK, sendIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}