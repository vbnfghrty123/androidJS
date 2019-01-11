package com.example.a402_24.day_03_register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Rv_boardSelected extends AppCompatActivity {

    TextView rv_board_write_date;
    TextView rv_board_location;
    ImageView write_member_profile;
    TextView write_member_id;

    TextView rv_board_watch;
    TextView rv_board_title;
    ImageView rv_board_image;

    TextView rv_board_comments;

    TextView rv_board_shared;

    TextView rv_board_heart;
    EditText rv_board_alert;
    Button rv_board_alertBtn;
    RecyclerView recyclerView;

    public static final String MyPREFERENCES = "MyPrefs";

    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<Alert> alertArrayList;
    public void setRefs(){
        // EditText 클릭 시 UI 강제로 밀어올려 키보드 input 에 위치 일치하도록
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        rv_board_write_date = findViewById(R.id.rv_board_write_date);
        rv_board_location = findViewById(R.id.rv_board_location);
        write_member_profile = findViewById(R.id.write_member_profile);
        write_member_id = findViewById(R.id.write_member_id);

        rv_board_watch = findViewById(R.id.rv_board_watch);
        rv_board_title = findViewById(R.id.rv_board_title);
        rv_board_image = findViewById(R.id.rv_board_image);

        rv_board_comments = findViewById(R.id.rv_board_comments);

        rv_board_shared = findViewById(R.id.rv_board_shared);

        rv_board_heart = findViewById(R.id.rv_board_heart);
        rv_board_alert = findViewById(R.id.rv_board_alert);
        rv_board_alertBtn =findViewById(R.id.rv_board_alertBtn);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
    }

    public void setEvents(){
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
       String member_gson = sharedPreferences.getString("info", null);
        final Member member = gson.fromJson(member_gson, Member.class);
        final Intent intent = getIntent();

        // 댓글 입력하기
        rv_board_alertBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {

                        try{
                            if(member == null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(),"로그인이 필요한 서비스입니다",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Rv_boardSelected.this,LoginActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }

                            String rv_board_index = "rv_board_index="+intent.getIntExtra("rv_board_index",0);
                            String rv_board_alert1 = "&alert_reason="+rv_board_alert.getText().toString();
                            String member_id = "&member_id="+member.getMember_id();
                            String member_profile = "&member_profile="+member.getMember_profile_pic();


                            URL url1 = new URL("http://192.168.10.24:8080/JS/android/rv_board/insertAlert");
                            HttpURLConnection httpURLConnection = (HttpURLConnection)url1.openConnection();
                            httpURLConnection.setRequestMethod("POST");
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.getOutputStream().write(rv_board_index.getBytes());
                            httpURLConnection.getOutputStream().write(rv_board_alert1.getBytes());
                            httpURLConnection.getOutputStream().write(member_id.getBytes());
                            httpURLConnection.getOutputStream().write(member_profile.getBytes());

                            httpURLConnection.getResponseCode();

                            rv_board_alert.setText("");

                        }catch (Exception e){

                        }
                    }
                });

                // 댓글 입력 시 바로 화면 새로고침 시키는 코드
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
    }

    public void load(){
        Intent intent = getIntent();



        // defaultValue 는 getIntExtra 했을때 값이 없는경우 임시로 defaultValue값을 넣어준다
        final String rv_board_index = "rv_board_index="+intent.getIntExtra("rv_board_index",0);
        Log.d("rv_board_index",rv_board_index);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://192.168.10.24:8080/JS/android/rv_board/selected");
                    HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();


                    httpUrlConnection.setRequestMethod("POST");
                    httpUrlConnection.setDoInput(true);
                    httpUrlConnection.getOutputStream().write(rv_board_index.getBytes());



                    if (httpUrlConnection.getResponseCode() == 200) {

                        BufferedReader br = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
                        StringBuffer sb = new StringBuffer();
                        String temp = null;
                        while((temp = br.readLine())!=null){
                            sb.append(temp);
                        }
                        String JsonRv_board = sb.toString();
                        Gson gson = new Gson();
                        final Rv_board rv_board = gson.fromJson(JsonRv_board,Rv_board.class);

                        rv_board_write_date.setText(rv_board.getRv_board_post_date());
                        rv_board_location.setText(rv_board.getRv_board_location());
                        write_member_id.setText(rv_board.getMember_id());
                        rv_board_watch.setText(Integer.toString(rv_board.getRv_board_count()));
                        rv_board_title.setText(rv_board.getRv_board_title());
                        rv_board_comments.setText(Integer.toString(rv_board.getRv_board_comments()));
                        rv_board_shared.setText(Integer.toString(rv_board.getRv_board_recommend()));
                        rv_board_heart.setText(Integer.toString(rv_board.getRv_board_heart()));


                        // 글쓴멤버 프로필사진 불러오기
                        try {
                            URL url1 = new URL("http://192.168.10.24:8080"+rv_board.getMember_profile());
                            HttpURLConnection httpURLConnection = (HttpURLConnection)url1.openConnection();
                            InputStream is = httpURLConnection.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(is);
                            Log.d("bitmap:",bitmap.toString());
                            write_member_profile.setImageBitmap(bitmap);
                        }catch(Exception e){

                        }

                        // 글 사진 불러오기
                        try {

                            URL url1 = new URL(rv_board.getRv_board_picture());
                            HttpURLConnection httpURLConnection = (HttpURLConnection)url1.openConnection();

                            InputStream inputStream = httpURLConnection.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            Log.d("bitmap2",bitmap.toString());
                            rv_board_image.setImageBitmap(bitmap);


                        }catch (Exception e){

                        }


                        // 글 댓글 수 가져오기
                        try{
                            URL url1 = new URL("http://192.168.10.24:8080/JS/android/rv_board/count");
                            HttpURLConnection httpURLConnection = (HttpURLConnection)url1.openConnection();

                            httpURLConnection.setRequestMethod("POST");
                            httpURLConnection.setDoInput(true);
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.getOutputStream().write(rv_board_index.getBytes());
                            if(httpURLConnection.getResponseCode()==200){
                                BufferedReader br1 = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                                StringBuffer sb1 = new StringBuffer();
                                String temp1 ;
                                while((temp1 = br1.readLine())!= null){
                                    sb1.append(temp1);
                                }
                                String loadAlertCount = sb1.toString();
                                Gson gson1 = new Gson();
                                Rv_board rvBoard = gson1.fromJson(loadAlertCount,Rv_board.class);
                                rv_board_comments.setText(Integer.toString(rvBoard.getRv_board_comments()));
                            }
                        }catch (Exception e){

                        }




                        // 글 댓글 불러오기
                        try{
                            URL url1 = new URL("http://192.168.10.24:8080/JS/android/rv_board/loadAlert");
                            HttpURLConnection httpURLConnection = (HttpURLConnection)url1.openConnection();
                            httpURLConnection.setRequestMethod("POST");
                            httpURLConnection.setDoOutput(true);
                            httpURLConnection.setDoInput(true);

                            httpURLConnection.getOutputStream().write(rv_board_index.getBytes());

                            if(httpURLConnection.getResponseCode() == 200){
                                BufferedReader br1 = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                                StringBuffer sb1 = new StringBuffer();
                                String temp1 ;
                                while((temp1 = br1.readLine())!= null){
                                    sb1.append(temp1);
                                }
                                String loadAlert = sb1.toString();

                                JSONArray jsonArray = new JSONArray(loadAlert);
                                ArrayList<Alert> alertList = new ArrayList<>();
                                for(int i = 0 ; i < jsonArray.length() ; i++){
                                    Alert alert = new Alert();
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    alert.setAlert_index(jsonObject.getInt("alert_index"));
                                    alert.setRv_board_index(jsonObject.getInt("rv_board_index"));
                                    alert.setMember_id(jsonObject.getString("member_id"));
                                    alert.setMember_profile(jsonObject.getString("member_profile"));
                                    alert.setAlert_reason(jsonObject.getString("alert_reason"));
                                    alert.setAlert_date(jsonObject.getString("alert_date"));
                                    alertList.add(alert);


                                }

                                alertArrayList = new ArrayList<>();
                                if(alertList != null) {
                                    Log.d("inRunOn:",alertList.toString());
                                    for (int i = 0; i < alertList.size(); i++) {
                                        alertArrayList.add(new Alert(alertList.get(i).getAlert_index(),alertList.get(i).getRv_board_index(),alertList.get(i).getMember_id(),alertList.get(i).getMember_profile(),alertList.get(i).getAlert_reason(),alertList.get(i).getAlert_date()));
                                    }

                                    RecyclerAdapter_alert myAdapter = new RecyclerAdapter_alert(alertArrayList);

                                    recyclerView.setAdapter(myAdapter);
                                }

                            }
                        }catch (Exception e){

                        }





                    }
                }catch(Exception e){

                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rv_board_selected);
        setRefs();
        setEvents();
        load();


    }




}
