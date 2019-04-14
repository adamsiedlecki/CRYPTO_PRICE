package com.example.crypto_price;


import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonReader;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {


    TextView header;
    TextView btcPriceField;
    TextView ethPriceField;
    TextView xmrPriceField;
    Button refreshButton;

    String btcPrice;
    String ethPrice;
    String xmrPrice;

    public void changeColor() {
        // start your timer on button click
        new CountDownTimer(1000, 1000) {

            public void onTick(long millisUntilFinished) {
                header.setBackgroundResource(R.color.white);
            }

            @Override
            public void onFinish() {
                header.setBackgroundResource(R.color.black);
            }

        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        header = findViewById(R.id.header);
        btcPriceField = findViewById(R.id.btcPrice);
        ethPriceField = findViewById(R.id.ethPrice);
        xmrPriceField = findViewById(R.id.xmrPrice);
        refreshButton = findViewById(R.id.refreshButton);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //header.setBackgroundResource(R.color.white);
                changeColor();
                AsyncTask.execute(new Runnable() {

                    public String getPrice(HttpsURLConnection httpsURLConnection) throws IOException {
                        String price = "";
                        if (httpsURLConnection.getResponseCode() == 200) {
                            // Success
                            InputStream responseBody = httpsURLConnection.getInputStream();
                            InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                            JsonReader jsonReader = new JsonReader(responseBodyReader);
                            jsonReader.beginObject();
                            while (jsonReader.hasNext()) {
                                String key = jsonReader.nextName();
                                if (key.equals("last")) {
                                    price = jsonReader.nextString() + " $";
                                } else {
                                    jsonReader.skipValue();
                                }
                            }
                            jsonReader.close();
                        } else {
                            // Error handling code goes here
                        }
                        return price;
                    }

                    @Override
                    public void run() {
                        try {
                            URL bitbayBtcURL = new URL("https://bitbay.net/API/Public/BTC/ticker.json");
                            HttpsURLConnection apiBtcConnection = (HttpsURLConnection) bitbayBtcURL.openConnection();
                            apiBtcConnection.setRequestProperty("btc", "BTC/ticker.json");
                            btcPrice = getPrice(apiBtcConnection);
                            btcPriceField.setText(btcPrice);
                            System.out.println("CENA BTC" + btcPrice);

                            URL bitbayEthURL = new URL("https://bitbay.net/API/Public/ETH/ticker.json");
                            HttpsURLConnection apiEthConnection = (HttpsURLConnection) bitbayEthURL.openConnection();
                            apiEthConnection.setRequestProperty("eth", "ETH/ticker.json");
                            ethPrice = getPrice(apiEthConnection);
                            ethPriceField.setText(ethPrice);

                            URL bitbayXmrURL = new URL("https://bitbay.net/API/Public/XMR/ticker.json");
                            HttpsURLConnection apiXmrConnection = (HttpsURLConnection) bitbayXmrURL.openConnection();
                            apiXmrConnection.setRequestProperty("xmr", "XMR/ticker.json");
                            xmrPrice = getPrice(apiXmrConnection);
                            xmrPriceField.setText(xmrPrice);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                });

            }

        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
