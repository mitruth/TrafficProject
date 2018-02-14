package com.ricamaps.smartass.ricamaps;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerViewOptions;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements AsyncResponse{

    private MapView mapView;
    private MapboxMap mapboxM;
    final int[] time = new int[2];


    String response;
    TextView txtResponse;

    Client myClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, "pk.eyJ1IjoibHVjaWFubHV0YXMiLCJhIjoiY2oybmp2enNvMDAzOTJxcWtlb3Rnem1raCJ9.SPYOA-LW7SyjLRH1utNweg");
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);


        final Button btnStart = findViewById(R.id.btnAddStart);
        final Button btnEnd = findViewById(R.id.btnAddEnd);

        final Marker[] startP = new Marker[1];
        final Marker[] endP = new Marker[1];

        final TimePicker timePicker = findViewById(R.id.timePicker);
        final Button btnOk = findViewById(R.id.btnOk);
        final Button btnTime = findViewById(R.id.btnSel);
        final Button btnNav = findViewById(R.id.startButton);
        final TextView clockBg = findViewById(R.id.clockBg);
        final TextView dur = findViewById(R.id.lblDuration);
        final Button btnClear = findViewById(R.id.btnClear);


        final String[] response = {""};

        final int[] time = new int[2];

        Calendar rightNow = Calendar.getInstance();

        final Date dateS = new Date();
        time[0] = rightNow.get(Calendar.HOUR_OF_DAY);
        time[1] = rightNow.get(Calendar.MINUTE);
        time[2] = rightNow.get(Calendar.SECOND);

        timePicker.setVisibility(View.INVISIBLE);
        btnOk.setVisibility(View.INVISIBLE);
        clockBg.setVisibility(View.INVISIBLE);


        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnStart.setEnabled(false);
                btnEnd.setEnabled(true);
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnEnd.setEnabled(false);
                btnStart.setEnabled(true);
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                btnEnd.setEnabled(false);
                btnStart.setEnabled(false);
                btnTime.setEnabled(false);
                btnNav.setVisibility(View.INVISIBLE);
                timePicker.setVisibility(View.VISIBLE);
                btnOk.setVisibility(View.VISIBLE);
                clockBg.setVisibility(View.VISIBLE);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void onClick(View v) {
                btnEnd.setEnabled(true);
                btnStart.setEnabled(true);
                btnTime.setEnabled(true);
                btnNav.setVisibility(View.VISIBLE);
                timePicker.setVisibility(View.INVISIBLE);
                btnOk.setVisibility(View.INVISIBLE);
                time[0] = timePicker.getHour();
                time[1] = timePicker.getMinute();
                clockBg.setVisibility(View.INVISIBLE);
                String str;
                if (time[1] < 10) {
                    str = String.valueOf(time[0]) + ":0" + String.valueOf(time[1]);
                } else {
                    str = String.valueOf(time[0]) + ":" + String.valueOf(time[1]);
                }
                btnTime.setText(str);
            }
        });

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final MapboxMap mapboxMap) {

                btnNav.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public void onClick(View v) {
                        String sendStr = startP[0].getPosition().getLatitude() + "-" +
                                startP[0].getPosition().getLongitude() + "-" +
                                endP[0].getPosition().getLatitude() + "-" +
                                endP[0].getPosition().getLongitude() + "-" +
                                time[0] + "-" +
                                time[1];

                        mapboxM = mapboxMap;
                        String[] str;
                        str = getData(sendStr);
                        new DrawGeoJson().execute(str[0]);
                        dur.setText(str[1]+"m "+ str[2]+"s");
                    }
                });

                btnClear.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        mapboxMap.clear();
                        btnNav.setEnabled(false);
                        startP[0] = null;
                        endP[0] = null;
                        dur.setText("");

                        // data acquisition
                        String sendStr = "ans" + "-" + startP[0].getPosition().getLatitude() + "-" +
                                startP[0].getPosition().getLongitude() + "-" +
                                endP[0].getPosition().getLatitude() + "-" +
                                endP[0].getPosition().getLongitude() + "-" +
                                getDiff(dateS);
                        try {
                            sendData(v, sendStr);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });

                mapboxMap.setOnMapClickListener(new MapboxMap.OnMapClickListener() {
                    @Override


                    public void onMapClick(@NonNull LatLng point) {
                        if (!btnStart.isEnabled()) {
                            btnStart.setEnabled(true);
                            if (startP[0] != null) {
                                mapboxMap.removeMarker(startP[0]);
                            }

                            startP[0] = mapboxMap.addMarker(new MarkerViewOptions()
                                    .position(new LatLng(point.getLatitude(), point.getLongitude()))
                                    .title("Start")
                            );
                            if (endP[0] != null) {
                                btnNav.setEnabled(true);
                            } else {
                                btnNav.setEnabled(false);
                            }
                        }
                        if (!btnEnd.isEnabled()) {
                            btnEnd.setEnabled(true);

                            if (endP[0] != null) {
                                mapboxMap.removeMarker(endP[0]);
                            }

                            endP[0] = mapboxMap.addMarker(new MarkerViewOptions()
                                    .position(new LatLng(point.getLatitude(), point.getLongitude()))
                                    .title("Start")
                            );
                            if (startP[0] != null) {
                                btnNav.setEnabled(true);
                            } else {
                                btnNav.setEnabled(false);
                            }
                        }
                    }

                    ;
                });

            }
        });
    }

    public String getDiff(Date dateS){
        String ans = "";
        Date dateE = new Date();

        long difference = (dateE.getTime() - dateS.getTime())/1000; // returns in milliseconds so we divide by 1000 to get seconds

        return String.valueOf(difference);

    }

    public String sendData(View v, String str) throws ExecutionException, InterruptedException {
        Client ct = new Client(this);
        String r= ct.execute(str).get();
        return r;
    }

    private String[] getData(String sendStr){
        String str = "";
        try {
            str = sendData(mapView, sendStr);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return str.split("-");

    }


    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void processFinish(String output) {

    }

    private class DrawGeoJson extends AsyncTask<String, Void, List<LatLng>> {

        String str;

        @Override
        protected List<LatLng> doInBackground(String... params) {

            str = params[0];
            ArrayList<LatLng> points = new ArrayList<>();

            try {
                // Parse JSON
                System.out.println(str);
                JSONObject json = new JSONObject(str);
                JSONArray features = json.getJSONArray("features");
                JSONObject feature = features.getJSONObject(0);
                JSONObject geometry = feature.getJSONObject("geometry");
                if (geometry != null) {
                    String type = geometry.getString("type");

                    // Our GeoJSON only has one feature: a line string
                    if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("LineString")) {

                        // Get the Coordinates
                        JSONArray coords = geometry.getJSONArray("coordinates");
                        for (int lc = 0; lc < coords.length(); lc++) {
                            JSONArray coord = coords.getJSONArray(lc);
                            LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                            points.add(latLng);
                        }
                    }
                }
            } catch (Exception exception) {
                System.out.println("----------ERROR------------");
            }

            return points;
        }


        @Override
        protected void onPostExecute(List<LatLng> points) {
            super.onPostExecute(points);

            if (points.size() > 0) {

                // Draw polyline on map
                mapboxM.addPolyline(new PolylineOptions()
                        .addAll(points)
                        .color(Color.parseColor("#f44242"))
                        .width(5));
            }
        }
    }

}