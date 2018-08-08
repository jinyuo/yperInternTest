package com.example.yperinterntest;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //리사이클러뷰 선언
    RecyclerView mRecyclerView;
    LinearLayoutManager layoutManager;
    RecyclerAdaptor mRecyclerAdaptor;
    ArrayList<RecyclerItem> recyclerItems=new ArrayList<>();

    //'아이템이 없습니다.' 표시 텍스트뷰.
    TextView noItemTv;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //앱바 : 안드로이드 스튜디오에서 기본적으로 제공하는 것보다 커스텀이 용이
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        noItemTv=findViewById(R.id.no_item_text);

        setRecyclerView();//리사이클러뷰
        itemClicked();//아이템 클릭 시
    }

    //추가 버튼 생성
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.additemmenu, menu);
        return true;
    }

    ///추가 버튼 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        //if로 써도 상관 없음.
        switch (item.getItemId()){
            case R.id.add_item_menu:
                Intent intentAdd = new Intent(this, AddItemActivity.class);
                startActivityForResult(intentAdd, 1);//AddItemActivity에게 응답 요청
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        //함수 리턴값이 부울형이라 리턴 처리도 해줘야 하는 듯
        return true;
    }

    public void setRecyclerView(){
        mRecyclerView = findViewById(R.id.item_recyclerview);
        //getApplicationContext() 대신 this 사용. getApplicationContext() 되도록 사용하지 말 것
        layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerAdaptor = new RecyclerAdaptor(recyclerItems);
        mRecyclerView.setAdapter(mRecyclerAdaptor);

        //실행 시점에서 저장 데이터 화면에 표시
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<RecyclerItem> storedItem = (ArrayList<RecyclerItem>) AppDatabase.getInstance(context).itemDao().getAllItem();
                for (RecyclerItem temp:storedItem) {
                    recyclerItems.add(temp);
                }
                mRecyclerAdaptor.notifyDataSetChanged();
                checkTVVisibility();
            }
        });
    }

    //인텐트 응답 받을 때 자동 호출
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        switch (resultCode){
            case RESULT_OK:
                //응답받은 값 저장
                String address = intent.getStringExtra("address");
                String time = intent.getStringExtra("time");

                //아이템 및 데이터 추가
                final RecyclerItem temp = new RecyclerItem();
                temp.setAddressTime(address, time);

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getInstance(context).itemDao().insert(temp);//데이터 추가

                        //리사이클러뷰에 넣을 아이템을 DB에서 가져옴. -> PK 반영 위함.
                        /*
                        RecyclerItem reTemp = AppDatabase.getInstance(context).itemDao().getItem(temp.getAddress(), temp.getTime());
                        recyclerItems.add(reTemp);*/
                        temp.setKey(AppDatabase.getInstance(context).itemDao().getItemKey(temp.getAddress(), temp.getTime()));
                        recyclerItems.add(temp);
                    }
                });

                //mRecyclerAdaptor.notifyDataSetChanged();
                mRecyclerAdaptor.notifyItemInserted(recyclerItems.size());
                noItemTv.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    public void itemClicked(){
        //아이템 선택 동작
        mRecyclerAdaptor.setOnItemClickListener(new RecyclerAdaptor.OnItemClickListener() {
            /*
            @Override
            public void onItemClick(View view, final int position) {
                //알림창 설정
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle(recyclerItems.get(position).getKey()+"번 아이템을 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //아이템 삭제
                                final RecyclerItem removed = recyclerItems.get(position);
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppDatabase.getInstance(context).itemDao().delete(removed);
                                    }
                                });
                                recyclerItems.remove(position);
                                mRecyclerAdaptor.notifyDataSetChanged();

                                checkTVVisibility();//텍스트뷰 표시
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                //알림창 생성
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }*/

            @Override
            public void onItemClickData(View view, final RecyclerItem removedItem) {
                //알림창 설정
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);
                alertDialogBuilder.setTitle(removedItem.getKey()+"번 아이템을 삭제하시겠습니까?")
                        .setCancelable(false)
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //아이템 삭제
                                AsyncTask.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppDatabase.getInstance(context).itemDao().delete(removedItem);
                                    }
                                });
                                int position = recyclerItems.indexOf(removedItem);
                                recyclerItems.remove(removedItem);
                                //mRecyclerAdaptor.notifyDataSetChanged();
                                mRecyclerAdaptor.notifyItemRemoved(position);

                                checkTVVisibility();//텍스트뷰 표시
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                //알림창 생성
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    void checkTVVisibility() {
        if(mRecyclerAdaptor.getItemCount()==0)
            noItemTv.setVisibility(View.VISIBLE);
        else
            noItemTv.setVisibility(View.INVISIBLE);
    }
}