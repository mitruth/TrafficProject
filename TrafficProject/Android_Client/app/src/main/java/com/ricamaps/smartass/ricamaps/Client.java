package com.ricamaps.smartass.ricamaps;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends AsyncTask<String,Void,String> {

    Socket s;
    PrintWriter pw;
    String message;
    Context c;
    Handler h = new Handler();
    String response = "";
    TextView textResponse;


    Client(Context c)
    {
        this.c=c;

    }


    @Override
    protected String doInBackground(String... params) {
        try
        {
            message = params[0];
            response = "empty";
            s = new Socket("95.77.251.141",7900);
            pw = new PrintWriter(s.getOutputStream());
            pw.println(message);
            pw.flush();
            h.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(c,"Message sent",Toast.LENGTH_LONG).show();
                }
            });
            //pw.close();
            BufferedReader in =
                    new BufferedReader(
                            new InputStreamReader(s.getInputStream()));
            response = in.readLine();
            pw.close();
            return response;

        }catch(IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        //do stuff
        super.onPostExecute(result);
    }

    private String getRes(String str) {
        //handle value
        return str;
    }

}
