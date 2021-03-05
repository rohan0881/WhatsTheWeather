package rohan.com.whatstheweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText2;
    TextView resultTextView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText2 = findViewById(R.id.editText2);
        resultTextView4 = findViewById(R.id.resultTextView4);
        }

    public void getWeather(View view) {
        try {
            DownloadTask downloadTask = new DownloadTask();
            String encodedCityName = URLEncoder.encode(editText2.getText().toString(), "UTF-8");

            downloadTask.execute("https://openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=439d4b804bc8187953eb36d2a8c26a02");
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText2.getWindowToken(), 0);

        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Could not find weather:(",Toast.LENGTH_SHORT).show();

        }
            }
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();

                }
                return result;


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could not find weather:(", Toast.LENGTH_SHORT).show();

            }
            return  null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                Log.i("Weather content",weatherInfo);
                JSONArray arr = new JSONArray(weatherInfo);
                String message ="";

                for(int i=0;i<arr.length();i++)
                {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");

                    if (!main.equals("") && !description.equals(""))
                    {
                        message += main + ":" + description + "\r\n";
                    }
            }
                if (!message.equals(""))
                {

                    resultTextView4.setText(message);
                }else
                {
                    Toast.makeText(getApplicationContext(),"Could not find weather:(",Toast.LENGTH_SHORT).show();
                }

            }catch(Exception e)
            {
                Toast.makeText(getApplicationContext(),"Could not find weather:(",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}

