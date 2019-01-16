package com.example.a402_24.day_03_register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class reportList extends AppCompatActivity {
    private static final String ip ="http://192.168.10.24:8080";
    public static final String MyPREFERENCES = "MyPrefs";
    private ArrayList<Report> reportList;
    RecyclerView qrecyclerView;




    RecyclerView.LayoutManager mLayoutManager;


    public void setRefs(){

        Intent intent =getIntent();

        final Gson gson = new Gson();
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String member_gson = sharedPreferences.getString("info", null);
        final Member member = gson.fromJson(member_gson, Member.class);

        qrecyclerView = findViewById(R.id.recycler_view1);
        qrecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        qrecyclerView.setLayoutManager(mLayoutManager);

        if(intent.getStringExtra("type").equals("rv_board")){

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(ip+"/JS/android/reportList/Rv_board");
                        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
                        httpUrlConnection.setRequestMethod("POST");
                        httpUrlConnection.setDoInput(true);

                        if (httpUrlConnection.getResponseCode() == 200) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
                            StringBuffer sb = new StringBuffer();
                            String temp = null;
                            while((temp = br.readLine())!=null){
                                sb.append(temp);
                            }
                            String JsonMember = sb.toString();
                            JSONArray jsonArray = new JSONArray(JsonMember);
                            Log.d("aaa111",JsonMember);
                            reportList = new ArrayList<>();
                            for( int i = 0 ; i < jsonArray.length() ; i++){
                                Report report = new Report();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                report.setRv_board_index(jsonObject.getInt("rv_board_index"));
                                report.setRv_board_title(jsonObject.getString("rv_board_title"));
                                report.setRv_board_content(jsonObject.getString("rv_board_content"));
                                if(jsonObject.has("rv_board_picture")) {
                                    report.setRv_board_picture(jsonObject.getString("rv_board_picture"));
                                }
                                report.setReport_reason(jsonObject.getString("report_reason"));
                                report.setReport_member_id(jsonObject.getString("report_member_id"));
                                report.setReporter_member_id(jsonObject.getString("reporter_member_id"));
                                report.setReport_date(jsonObject.getString("report_date"));




                                reportList.add(report);
                            }

                            ArrayList<Report> reportArrayList = new ArrayList<>();
                            if(reportList != null) {

                                for (int i = 0; i < reportList.size(); i++) {
                                    reportArrayList.add(new Report(reportList.get(i).getRv_board_index(),reportList.get(i).getRv_board_title(),reportList.get(i).getRv_board_content(),reportList.get(i).getRv_board_picture(),reportList.get(i).getReport_reason(),reportList.get(i).getReport_member_id(),reportList.get(i).getReporter_member_id(),reportList.get(i).getReport_date()));

                                }

                                RecyclerAdapter_report myAdapter = new RecyclerAdapter_report(reportArrayList);

                                qrecyclerView.setAdapter(myAdapter);
                            }



                        }
                    }catch (Exception e){

                    }
                }
            });
        } else if(intent.getStringExtra("type").equals("alert")){

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(ip+"/JS/android/reportList/Alert");
                        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
                        httpUrlConnection.setRequestMethod("POST");
                        httpUrlConnection.setDoInput(true);

                        if (httpUrlConnection.getResponseCode() == 200) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
                            StringBuffer sb = new StringBuffer();
                            String temp = null;
                            while((temp = br.readLine())!=null){
                                sb.append(temp);
                            }
                            String JsonMember = sb.toString();
                            JSONArray jsonArray = new JSONArray(JsonMember);
                            reportList = new ArrayList<>();
                            for( int i = 0 ; i < jsonArray.length() ; i++){
                                Report report = new Report();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                report.setAlert_index(jsonObject.getInt("alert_index"));
                                report.setAlert_reason(jsonObject.getString("alert_reason"));

                                report.setReport_reason(jsonObject.getString("report_reason"));
                                report.setReport_member_id(jsonObject.getString("report_member_id"));
                                report.setReporter_member_id(jsonObject.getString("reporter_member_id"));
                                report.setReport_date(jsonObject.getString("report_date"));




                                reportList.add(report);
                            }
                            ArrayList<Report> reportArrayList = new ArrayList<>();
                            if(reportList != null) {

                                for (int i = 0; i < reportList.size(); i++) {

                                    reportArrayList.add(new Report());

                                }

                                RecyclerAdapter_report myAdapter = new RecyclerAdapter_report(reportArrayList);

                                qrecyclerView.setAdapter(myAdapter);
                            }




                        }
                    }catch (Exception e){

                    }
                }
            });
        } else {

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(ip+"/JS/android/reportList/Message");
                        HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
                        httpUrlConnection.setRequestMethod("POST");
                        httpUrlConnection.setDoInput(true);

                        if (httpUrlConnection.getResponseCode() == 200) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
                            StringBuffer sb = new StringBuffer();
                            String temp = null;
                            while((temp = br.readLine())!=null){
                                sb.append(temp);
                            }
                            String JsonMember = sb.toString();
                            JSONArray jsonArray = new JSONArray(JsonMember);
                            reportList = new ArrayList<>();
                            for( int i = 0 ; i < jsonArray.length() ; i++){
                                Report report = new Report();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                report.setMessage_id(jsonObject.getInt("message_id"));
                                report.setMessage_title(jsonObject.getString("message_title"));
                                report.setMessage_content(jsonObject.getString("message_content"));
                                if(jsonObject.has("message_picture")) {
                                    report.setMessage_picture(jsonObject.getString("message_picture"));
                                }
                                report.setReport_reason(jsonObject.getString("report_reason"));
                                report.setReport_member_id(jsonObject.getString("report_member_id"));
                                report.setReporter_member_id(jsonObject.getString("reporter_member_id"));
                                report.setReport_date(jsonObject.getString("report_date"));




                                reportList.add(report);
                            }


                            ArrayList<Report> reportArrayList = new ArrayList<>();
                            if(reportList != null) {

                                for (int i = 0; i < reportList.size(); i++) {

                                    reportArrayList.add(new Report());

                                }

                                RecyclerAdapter_report myAdapter = new RecyclerAdapter_report(reportArrayList);

                                qrecyclerView.setAdapter(myAdapter);
                            }


                        }
                    }catch (Exception e){

                    }
                }
            });
        }



    }

    public void setEvent(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        setRefs();
        setEvent();
    }
}
