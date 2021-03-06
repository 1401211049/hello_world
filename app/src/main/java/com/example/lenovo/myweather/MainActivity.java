package com.example.lenovo.myweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lenovo.util.NetUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.zip.GZIPInputStream;

public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    private ImageView mUpdateBtn;
    private TextView cityTv, timeTv, humidityTv, weekTv,pmDataTv, pmQualityTv, temperatureTv, climateTv, windTv;
    private ImageView weatherImg, pmImg;

    void initView(){
        cityTv=  (TextView) findViewById(R.id.city);
        timeTv=  (TextView)  findViewById(R.id.time);
        humidityTv=  (TextView)  findViewById(R.id.humidity);
        weekTv=  (TextView) findViewById(R.id.week_today);
        pmDataTv=  (TextView)  findViewById(R.id.pm_data);
        pmQualityTv=  (TextView)  findViewById(R.id.pm2_5_quality);
        pmImg=  (ImageView)  findViewById(R.id.pm2_5_img);
        temperatureTv=  (TextView)  findViewById(R.id.temperature);
        climateTv=  (TextView)  findViewById(R.id.climate);
        windTv=  (TextView) findViewById(R.id.wind);
        weatherImg=  (ImageView)  findViewById(R.id.weather_img);
        cityTv.setText("N/A") ;
        timeTv.setText("N/A");
        humidityTv.setText( "N/A") ;
        pmDataTv.setText("N/A") ;
        pmQualityTv.setText( "N/A") ;
        weekTv.setText("N/A");
        temperatureTv.setText("N/A") ;
        climateTv.setText( "N/A") ;
        windTv. setText("N/A");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
        mUpdateBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdateBtn.setOnClickListener(this);
        initView();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.title_update_btn) {
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code", "101010100");
            Log.d("myWeather", cityCode);
            if(NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
                Log.d("myWeather","网络OK");
                queryWeatherCode(cityCode);
            }else
            {
                Log.d("myWeather","网络挂了");
                Toast.makeText(MainActivity.this,"网络挂了!",Toast.LENGTH_LONG).show();
            }
            queryWeatherCode(cityCode);
        }
    }

    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet(address);
                    HttpResponse httpResponse = httpclient.execute(httpget);
                    if (httpResponse.getStatusLine().getStatusCode() == 200) {
                        HttpEntity entity = httpResponse.getEntity();

                        InputStream responseStream = entity.getContent();
                        responseStream = new GZIPInputStream(responseStream);
                        BufferedReader reader = new BufferedReader(new InputStreamReader(responseStream));
                        StringBuilder response = new StringBuilder();
                        String str;
                        while ((str = reader.readLine()) != null) {
                            response.append(str);
                        }
                        String responseStr = response.toString();
                        Log.d("myWeather", responseStr);
                        parseXML(responseStr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void parseXML(String xmldata) {
        try {
            int fengxiangCount=0;
            int fengliCount=0;
            int dateCount=0;
            int highCount=0;
            int lowCount=0;
            int typeCount=0;
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myapp2", "parseXML");
            while (eventType != xmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("city")) {
                            eventType = xmlPullParser.next();
                            Log.d("myapp2", "city: " + xmlPullParser.getText());
                        }
                        else if (xmlPullParser.getName().equals("updatetime")) {
                            eventType = xmlPullParser.next();
                            Log.d("myapp2", "updatetime: " + xmlPullParser.getText());
                        }
                        else if (xmlPullParser.getName().equals("shidu")) {
                            eventType = xmlPullParser.next();
                            Log.d("myapp2", "shidu: " + xmlPullParser.getText());
                        }
                        else if (xmlPullParser.getName().equals("pm25")) {
                            eventType = xmlPullParser.next();
                            Log.d("myapp2", "pm2.5: " + xmlPullParser.getText());
                        }
                        else if (xmlPullParser.getName().equals("quality")) {
                            eventType = xmlPullParser.next();
                            Log.d("myapp2", "quality: " + xmlPullParser.getText());
                        }
                        else if (xmlPullParser.getName().equals("fengxiang")&& fengxiangCount==0) {
                            eventType = xmlPullParser.next();
                            Log.d("myapp2", "fengxiang: " + xmlPullParser.getText());
                            fengxiangCount++;
                        }
                        else if (xmlPullParser.getName().equals("fengli")&& fengliCount==0) {
                            eventType = xmlPullParser.next();
                            Log.d("myapp2", "fengli: " + xmlPullParser.getText());
                            fengliCount++;
                        }
                        else if (xmlPullParser.getName().equals("date")&& dateCount==0) {
                            eventType = xmlPullParser.next();
                            Log.d("myapp2", "date: " + xmlPullParser.getText());
                            dateCount++;
                        }
                        else if (xmlPullParser.getName().equals("high")&& highCount==0) {
                            eventType = xmlPullParser.next();
                            Log.d("myapp2", "high: " + xmlPullParser.getText());
                            highCount++;
                        }
                        else if (xmlPullParser.getName().equals("low")&& lowCount==0) {
                            eventType = xmlPullParser.next();
                            Log.d("myapp2", "low: " + xmlPullParser.getText());
                            lowCount++;
                        }
                        else if (xmlPullParser.getName().equals("type")&& typeCount==0) {
                            eventType = xmlPullParser.next();
                            Log.d("myapp2", "type: " + xmlPullParser.getText());
                            typeCount++;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}