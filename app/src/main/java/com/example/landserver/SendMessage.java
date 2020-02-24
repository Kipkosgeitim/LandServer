package com.example.landserver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.landserver.Common.Common;
import com.example.landserver.Remote.APIService;
import com.example.landserver.models.DataMessage;
import com.example.landserver.models.MyResponse;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SendMessage extends AppCompatActivity {

    MaterialEditText edtTitle,edtMessage;
    Button btnSend;

    APIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        mService = Common.getFCMClient();

        edtTitle = (MaterialEditText)findViewById(R.id.edtTitle);
        edtMessage = (MaterialEditText)findViewById(R.id.edtMessage);

        btnSend = (Button)findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Notification2 notification = new Notification2(edtTitle.getText().toString(),edtMessage.getText().toString());
                // DataMessage toTopic = new DataMessage();
                // toTopic.to = new StringBuilder("/topics/").append(Common.topicName).toString();
                //toTopic.notification =notification;

                Map<String,String> notification = new HashMap<String, String>();
                notification.put("Title ",edtTitle.getText().toString());
                notification.put("Message ",edtMessage.getText().toString());
                DataMessage toTopic = new DataMessage();
                toTopic.to = new StringBuilder("/topics/").append(Common.topicName).toString();
                toTopic.data =notification;



                mService.sendNotification(toTopic)
                        .enqueue(new Callback<MyResponse>() {
                            @Override
                            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                if (response.isSuccessful())
                                    Toast.makeText(SendMessage.this, "Message Sent", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<MyResponse> call, Throwable t) {
                                Toast.makeText(SendMessage.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
            }
        });

    }
}
