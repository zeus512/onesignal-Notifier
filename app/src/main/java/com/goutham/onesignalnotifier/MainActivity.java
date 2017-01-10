package com.goutham.onesignalnotifier;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    Button bt;
    EditText body,rurl,title,img_url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         bt = (Button) findViewById(R.id.btn);
          body =(EditText)findViewById(R.id.msg);
          rurl =(EditText)findViewById(R.id.url);
          title=(EditText)findViewById(R.id.sicon);
          img_url=(EditText)findViewById(R.id.photo_url);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String jsonResponse;

                    URL url = new URL("https://onesignal.com/api/v1/notifications");
                    HttpURLConnection con = (HttpURLConnection)url.openConnection();
                    con.setUseCaches(false);
                    con.setDoOutput(true);
                    con.setDoInput(true);

                    con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    con.setRequestProperty("Authorization", "Basic MGI0MTE1NWUtZTRkNi00NWYxLWIwMWQtMDQyNjQ0YzBjY2Zh");
                    con.setRequestMethod("POST");
                    String tt=title.getText().toString();
                    String bd=body.getText().toString();
                    String ru=rurl.getText().toString();
                    String img=img_url.getText().toString();

                    String strJsonBody = "{"
                            +   "\"app_id\": \"94a38ed3-22d3-4067-bf72-83ab26cea1ab\","
                            +   "\"included_segments\": [\"All\"],"
                            +   "\"data\": {\"foo\": \"bar\"},"
                            +   "\"url\": \""+ru+"\","
                            +   "\"headings\":{\"en\": \""+tt+"\"},"
                            +   "\"big_picture\": \""+img+"\","
                            +   "\"contents\": {\"en\": \""+bd+"\"}"
                            + "}";


                    System.out.println("strJsonBody:\n" + strJsonBody);

                    byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                    con.setFixedLengthStreamingMode(sendBytes.length);

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(sendBytes);

                    int httpResponse = con.getResponseCode();
                    System.out.println("httpResponse: " + httpResponse);

                    if (  httpResponse >= HttpURLConnection.HTTP_OK
                            && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                        Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                        jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                        scanner.close();
                    }
                    else {
                        Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                        jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                        scanner.close();
                    }
                    System.out.println("jsonResponse:\n" + jsonResponse);
                    bt.setVisibility(View.GONE);
                    if(httpResponse==200){
                        Toast.makeText(MainActivity.this,"message succesfully sent",Toast.LENGTH_SHORT).show();
                        bt.setVisibility(View.VISIBLE);
                    }
                    else if(httpResponse==400){
                        Toast.makeText(MainActivity.this,"there is a problem in your message",Toast.LENGTH_SHORT).show();
                        bt.setVisibility(View.VISIBLE);
                    }
                    else if(httpResponse==500){
                        Toast.makeText(MainActivity.this,"we are facing server down please try again later",Toast.LENGTH_SHORT).show();
                        bt.setVisibility(View.VISIBLE);
                    }


                } catch(Throwable t) {
                    t.printStackTrace();
                }






            }

        });
    }
}