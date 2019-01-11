package com.example.a402_24.day_03_register;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RecyclerAdapter_board extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    // 4번째 실행
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView rv_board_write_date;
        TextView rv_board_location;
        TextView rv_board_watch;
        ImageView member_profile;
        TextView write_member_id;
        TextView rv_board_title;
        ImageView rv_board_image;



        TextView rv_board_comments;

        TextView rv_board_sharedNum;

        TextView rv_board_heartNum;


        MyViewHolder(View itemView) {
            super(itemView);
            rv_board_write_date = itemView.findViewById(R.id.rv_board_write_date);
            rv_board_location = itemView.findViewById(R.id.rv_board_location);

            member_profile = itemView.findViewById(R.id.write_member_profile);
            write_member_id = itemView.findViewById(R.id.write_member_id);
            rv_board_title = itemView.findViewById(R.id.rv_board_title);
            rv_board_image = itemView.findViewById(R.id.rv_board_image);
            rv_board_watch = itemView.findViewById(R.id.rv_board_watch);

            rv_board_comments = itemView.findViewById(R.id.rv_board_comments);

            rv_board_sharedNum = itemView.findViewById(R.id.rv_board_shared);

            rv_board_heartNum = itemView.findViewById(R.id.rv_board_heart);

            Log.d("실행1", "실행1");
        }


    }

    // 1번째 실행
    private ArrayList<Rv_board> Rv_boardArrayList;

    RecyclerAdapter_board(ArrayList<Rv_board> Rv_boardArrayList) {
        this.Rv_boardArrayList = Rv_boardArrayList;
        Log.d("실행2", "실행2");
    }

    // cardView 의 높이설정
    public static int getDpToPixel(Context context) {
        float px = 0;
        float bottomMargin;
        try {
            px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 500, context.getResources().getDisplayMetrics());
        } catch (Exception e) {

        }
        return (int) px;
    }


    // cardView 의 margin
    public static int getDpToPixel2(Context context) {
        float bottomMargin = 0;
        try {
            bottomMargin = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics());
        } catch (Exception e) {

        }
        return (int) bottomMargin;
    }

    // 3번째 실행
    // 여기서 참조할 layout 과 해당 layout 의 크기 조절을 해준다
    // 이 return 값이 위에 MyViewHolder 로 가고
    // AlertDialog.Bulider( 다이얼로그) 에서 해준것처럼
    // 앞에 view 변수명과 함께 .findViewById 를 선언해주면 해당 layout 의 각 id 값을 참조할 수 있다
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.onlylayout_cardview_board, null, false);
        RecyclerView.LayoutParams recyclerView = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getDpToPixel(v.getContext()));
        recyclerView.bottomMargin = getDpToPixel2(v.getContext());
        recyclerView.topMargin = getDpToPixel2(v.getContext());
        recyclerView.rightMargin = getDpToPixel2(v.getContext());
        recyclerView.leftMargin = getDpToPixel2(v.getContext());
        v.setLayoutParams(recyclerView);
        Log.d("실행3", "실행3");


        return new MyViewHolder(v);
    }


    // 마지막(5번째) 실행 >> 그 후로 2번 제외되고 5>3>1>4 순서로 실행
    // 안에 내용을 설정해준다
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        Log.d("실행4", "실행4");
        final MyViewHolder myViewHolder = (MyViewHolder) viewHolder;

        // 글 작성날짜 불러오기
        myViewHolder.rv_board_write_date.setText(Rv_boardArrayList.get(i).getRv_board_post_date());

        // 글 주소 불러오기
        myViewHolder.rv_board_location.setText(Rv_boardArrayList.get(i).getRv_board_location());

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                // 멤버 프로필사진 불러오기
                final String aaa = Rv_boardArrayList.get(i).getMember_profile();
                Log.d("myProfile", aaa);
                try {
                    URL url = new URL("http://192.168.10.24:8080"+aaa);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    Log.d("bitmap1", bitmap.toString());

                    myViewHolder.member_profile.setImageBitmap(bitmap);
                } catch (Exception e) {

                }



                if (Rv_boardArrayList.get(i).getRv_board_picture() != null) {
                    // 글 사진 불러오기
                    final String bbb = Rv_boardArrayList.get(i).getRv_board_picture();
                    Log.d("bbb", bbb);
                    try {

                        URL url = new URL(bbb);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                        InputStream inputStream = httpURLConnection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        Log.d("bitmap2", bitmap.toString());
                        myViewHolder.rv_board_image.setImageBitmap(bitmap);


                    } catch (Exception e) {

                    }
                }



                // 글 댓글 수 가져오기
                try {

                    String rv_board_index = "rv_board_index=" + Rv_boardArrayList.get(i).getRv_board_index();

                    URL url1 = new URL("http://192.168.10.24:8080/JS/android/rv_board/count");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url1.openConnection();

                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.getOutputStream().write(rv_board_index.getBytes());
                    if (httpURLConnection.getResponseCode() == 200) {
                        BufferedReader br1 = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        StringBuffer sb1 = new StringBuffer();
                        String temp1;
                        while ((temp1 = br1.readLine()) != null) {
                            sb1.append(temp1);
                        }
                        String loadAlertCount = sb1.toString();
                        Gson gson1 = new Gson();
                        Rv_board rvBoard = gson1.fromJson(loadAlertCount, Rv_board.class);

                        myViewHolder.rv_board_comments.setText(Integer.toString(rvBoard.getRv_board_comments()));
                    }
                } catch (Exception e) {

                }




            }

        });


        // 글 작성 멤버 아이디 불러오기
        myViewHolder.write_member_id.setText(Rv_boardArrayList.get(i).getMember_id());

        // 글 제목 불러오기
        myViewHolder.rv_board_title.setText(Rv_boardArrayList.get(i).getRv_board_title());

        // 글 조회수 불러오기
        myViewHolder.rv_board_watch.setText(Integer.toString(Rv_boardArrayList.get(i).getRv_board_count()));

// 글 댓글수 불러오기
        myViewHolder.rv_board_comments.setText(Integer.toString(Rv_boardArrayList.get(i).getRv_board_comments()));
// 글 공유횟수 불러오기
        myViewHolder.rv_board_sharedNum.setText(Integer.toString(Rv_boardArrayList.get(i).getRv_board_recommend()));
        // 글 좋아요 불러오기
        myViewHolder.rv_board_heartNum.setText(Integer.toString(Rv_boardArrayList.get(i).getRv_board_heart()));


    }

    // 2번째 실행
    @Override
    public int getItemCount() {
        Log.d("실행5", "실행5");
        return Rv_boardArrayList.size();
    }
}
