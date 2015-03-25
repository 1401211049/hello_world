package com.example.lenovo.bean;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
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

import com.example.lenovo.myweather.MainActivity;
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
import com.example.lenovo.myweather.R;

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

/**
 * Created by Lenovo on 2015/3/26.
 */
public class TodayWeather {
    private String city;
    private String updatetime;
    private String wendu;
    private String shidu;
    private String pm25;
    private String quality;
    private String fengxiang;
    private String fengli;
    private String date;
    private String high;
    private String low;
    private String type;


    private String getCity() {
        return city;
    }

    private String getUpdatetime() {
        return updatetime;
    }

    private String getWendu() {
        return wendu;
    }

    private String getShidu() {
        return shidu;
    }

    private String getPm25() {
        return pm25;
    }

    private String getQuality() {
        return quality;
    }

    private String getFengxiang() {
        return fengxiang;
    }

    private String getFengli() {
        return fengli;
    }

    private String getDate() {
        return date;
    }

    private String getHigh() {
        return high;
    }

    private String getLow() {
        return low;
    }

    private String getType() {
        return type;
    }

    private void setCity(String city) {
        this.city = city;
    }

    private void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    private void setShidu(String shidu) {
        this.shidu = shidu;
    }

    private void setWendu(String wendu) {
        this.wendu = wendu;
    }

    private void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    private void setQuality(String quality) {
        this.quality = quality;
    }

    private void setFengXiang(String fengxiang) {
        this.fengxiang = fengxiang;
    }

    private void setFengLi(String fengli) {
        this.fengli = fengli;
    }

    private void setDate(String date) {
        this.date = date;
    }

    private void setHigh(String high) {
        this.high = high;
    }

    private void setLow(String low) {
        this.low = low;
    }

    private void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "TodayWeather{" +
                "city='" + city + '\'' +
                ".updatetime='" + updatetime + '\'' +
                ".wendu='" + wendu + '\'' +
                ".shidu='" + shidu + '\'' +
                ".pm25='" + pm25 + '\'' +
                ".quality='" + quality + '\'' +
                ".fengxiang='" + fengxiang + '\'' +
                ".fengli='" + fengli + '\'' +
                ".date='" + date + '\'' +
                ".high='" + high + '\'' +
                ".low='" + low + '\'' +
                ".type='" + type + '\'' +
                '}';
    }


    private TodayWeather parseXML(String xmldata) {
        TodayWeather todayWeather = null;
        try {
            int fengxiangCount = 0;
            int fengliCount = 0;
            int dateCount = 0;
            int highCount = 0;
            int lowCount = 0;
            int typeCount = 0;
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
                        if (xmlPullParser.getName().equals("resp")) {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengXiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengLi(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                        }
                        break;
                    return todayWeather;

                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    void updateTodayWeather(TodayWeather todayWeather) {
        Log.d("myapp3", todayWeather.toString());
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime() + "发布");
        humidityTv.setText("湿度: " + todayWeather.getShidu());
        pmDataTv.setText(todayWeather.getPm25());
        pmQualityTv.setText(todayWeather.getQuality());
        weekTv.setText(todayWeather.getDate());
        temperatureTv.setText(todayWeather.getHigh() + "~" + todayWeather.getLow());
        climateTv.setText(todayWeather.getType());
        windTv.setText("风力: " + todayWeather.getFengli());
        Toast.makeText(MainActivity.this, "更新成功!", Toast.LENGTH_SHORT).show();
    }


}


