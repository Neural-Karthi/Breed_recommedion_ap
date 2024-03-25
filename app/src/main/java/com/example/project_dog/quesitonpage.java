package com.example.project_dog;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class quesitonpage extends AppCompatActivity {
    RadioGroup rgroup1,rgroup2,rgroup3,rgroup4,rgroup5,rgroup6,rgroup7;
    Button searchdata,backbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quesitonpage);

        searchdata = findViewById(R.id.searchbtn);
        backbtn=findViewById(R.id.searchback);
        rgroup1 = findViewById(R.id.rgroup1);
        rgroup2 = findViewById(R.id.rgroup2);
        rgroup3 = findViewById(R.id.rgroup3);
        rgroup4 = findViewById(R.id.rgroup4);
        rgroup5 = findViewById(R.id.rgroup5);
        rgroup6 = findViewById(R.id.rgroup6);
        rgroup7 = findViewById(R.id.rgroup7);

        searchdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAnyRadioButtonSelected(rgroup1) || isAnyRadioButtonSelected(rgroup2) || isAnyRadioButtonSelected(rgroup3) ||
                        isAnyRadioButtonSelected(rgroup4) || isAnyRadioButtonSelected(rgroup5) || isAnyRadioButtonSelected(rgroup6) ||
                        isAnyRadioButtonSelected(rgroup7)) {
                    Toast.makeText(getBaseContext(),"Please fill all the details",Toast.LENGTH_LONG).show();
                } else {


                    // Perform HTTP POST request
                    performHttpRequest();

                }
            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void callprocess(){

    }
    private void performHttpRequest() {

        int question1selected=rgroup1.getCheckedRadioButtonId(), question2selected=rgroup2.getCheckedRadioButtonId(),question3selected=rgroup3.getCheckedRadioButtonId(),question4selected=rgroup4.getCheckedRadioButtonId(),question5selected=rgroup5.getCheckedRadioButtonId(),question6selected=rgroup6.getCheckedRadioButtonId(),question7selected=rgroup7.getCheckedRadioButtonId();
        RadioButton qbtn1=(RadioButton)findViewById(question1selected);
        RadioButton qbtn2=(RadioButton)findViewById(question2selected);
        RadioButton qbtn3=(RadioButton)findViewById(question3selected);
        RadioButton qbtn4=(RadioButton)findViewById(question4selected);
        RadioButton qbtn5=(RadioButton)findViewById(question5selected);
        RadioButton qbtn6=(RadioButton)findViewById(question6selected);
        RadioButton qbtn7=(RadioButton)findViewById(question7selected);

        ProgressDialog progressDialog = new ProgressDialog(quesitonpage.this);
        progressDialog.setMessage("Loading results...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 2000);


        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpURLConnection conn = null;
                try {
                    URL url = new URL("https://2af6-34-147-63-177.ngrok-free.app/breed_prediction");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    // Create JSON object
                    JSONObject inputJsonObj = new JSONObject();
                    inputJsonObj.put("age", qbtn1.getText().toString().toLowerCase());
                    inputJsonObj.put("lifestyle", qbtn2.getText().toString().toLowerCase());
                    inputJsonObj.put("size", qbtn3.getText().toString().toLowerCase());
                    inputJsonObj.put("trainability", qbtn4.getText().toString().toLowerCase());
                    inputJsonObj.put("maintenance", qbtn5.getText().toString().toLowerCase());
                    inputJsonObj.put("barking", qbtn6.getText().toString().toLowerCase());
                    inputJsonObj.put("friendliness", qbtn7.getText().toString().toLowerCase());

                    // Write JSON content to output stream
                    DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                    os.writeBytes(inputJsonObj.toString());
                    os.flush();
                    os.close();

                    // Get response code
                    int responseCode = conn.getResponseCode();
                    Log.d("ResponseCode", "Response Code: " + responseCode);

                    // Read response
                    InputStream inputStream = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Extract the array data from the JSON response
                    String jsonString = response.toString();
                    Log.d("TAG", "Result: " + jsonString);
                    int startIndex = jsonString.indexOf("[") + 1;
                    int endIndex = jsonString.lastIndexOf("]");
                    String arrayDataString = jsonString.substring(startIndex, endIndex);
                    String[] arrayData = arrayDataString.split(", ");
                    for (int i = 0; i < arrayData.length; i++) {
                        arrayData[i] = arrayData[i].replace("\"", "").replace("\\", "");
                    }
                    ArrayList<String> list = new ArrayList<>(Arrays.asList(arrayData));

                    // Build the message for AlertDialog
                    StringBuilder messageBuilder = new StringBuilder();
                    int count=1;
                    for (String prediction : list) {
                        messageBuilder.append(count).append(".").append(prediction).append("\n");
                        count++;
                    }

                    // Show the AlertDialog with the message
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(quesitonpage.this);
                            builder.setTitle("Top Prediction Result");
                            builder.setMessage(messageBuilder.toString());
                            builder.setPositiveButton("OK", null);
                            builder.show();

                        }
                    });

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Log.e("HttpRequestError", "Error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToast("Failed to connect to server. Please try again later.");
                        }
                    });
                } finally {
                    if (conn != null) {
                        conn.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showToast(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(quesitonpage.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean isAnyRadioButtonSelected(RadioGroup radioGroup) {
        return radioGroup.getCheckedRadioButtonId() == -1;
    }
}
