package com.example.landserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.landserver.Common.Common;
import com.example.landserver.Remote.APIService;
import com.example.landserver.ViewHolder.LandOrderViewHolder;
import com.example.landserver.ViewHolder.LandViewHolder;
import com.example.landserver.models.DataMessage;
import com.example.landserver.models.MyResponse;
import com.example.landserver.models.Requests;
import com.example.landserver.models.Token;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandStatus extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Requests, LandOrderViewHolder> adapter;

    FirebaseDatabase db;
    DatabaseReference requests;

    MaterialSpinner spinner,trackSpinner;
    APIService mService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_status);

        /**Firebase**/
        db = FirebaseDatabase.getInstance();
        requests = db.getReference("Counties").child(Common.currentUser.getCountyId()).child("Requests");

        //init service
        mService = Common.getFCMClient();


        /**init**/
        recyclerView = (RecyclerView)findViewById(R.id.listOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadOrders();

    }

    private void loadOrders() {
        FirebaseRecyclerOptions<Requests> options = new FirebaseRecyclerOptions.Builder<Requests>()
                .setQuery(requests,Requests.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<Requests, LandOrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LandOrderViewHolder viewHolder, final int position, @NonNull final Requests model) {

                viewHolder.txtLandId.setText(adapter.getRef(position).getKey());
                viewHolder.txtLandStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtLandAddress.setText(model.getAddress());
                viewHolder.txtLandPhone.setText(model.getPhone());
                viewHolder.txtLandDate.setText(Common.getDate(Long.parseLong(adapter.getRef(position).getKey())));

                /**even button**/
                viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showUpdateDialog(adapter.getRef(position).getKey(),
                                adapter.getItem(position));

                    }
                });

                viewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOrder(adapter.getRef(position).getKey());
                    }
                });

                viewHolder.btnDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent orderDetail = new Intent(LandStatus.this, LandDetails.class);
                        Common.currentRequest = model;
                        orderDetail.putExtra("LandId", adapter.getRef(position).getKey());
                        startActivity(orderDetail);

                    }
                });

                viewHolder.btnDirection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent trackingOrder = new Intent(LandStatus.this, TrackingLand.class);
                        Common.currentRequest = model;
                        startActivity(trackingOrder);

                    }
                });
            }

            @NonNull
            @Override
            public LandOrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.land_order_layout,viewGroup,false);
                return new LandOrderViewHolder(itemView);
            }
        };
        adapter.startListening();

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    private void deleteOrder(String key) {
        requests.child(key).removeValue();
        adapter.notifyDataSetChanged();
    }

    private void showUpdateDialog(String key, final Requests item){
        final AlertDialog.Builder alertDialog= new AlertDialog.Builder(LandStatus.this);
        alertDialog.setTitle("Update Land Order");
        alertDialog.setMessage("Please choose status");

        final LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.update_land_order_layout,null);

        spinner = (MaterialSpinner)view.findViewById(R.id.statusSpinner);
        spinner.setItems("Placed","On my way","Shipping");

        trackSpinner = (MaterialSpinner)view.findViewById(R.id.trackSpinner);

        /**Load all shipper phone to spinner**/
        final List<String> trackList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Common.TRACKER_TABLE)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot trackerSnapshot:dataSnapshot.getChildren())
                            trackList.add(trackerSnapshot.getKey());
                        trackSpinner.setItems(trackList);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



        alertDialog.setView(view);

        final String localKey =key;
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
                item.setStatus(String.valueOf(spinner.getSelectedIndex()));

                if (item.getStatus().equals("2"))
                {
                    /** Copy item table "OrderNeedShip"**/
                    FirebaseDatabase.getInstance().getReference(Common.ORDER_NEED_SHIP_TABLE)
                            .child(trackSpinner.getItems().get(trackSpinner.getSelectedIndex()).toString())
                            .child(localKey)
                            .setValue(item);

                    requests.child(localKey).setValue(item);
                    adapter.notifyDataSetChanged();//add to update item size
                    sendLandOrderStatusToUser(localKey, item);

                    sendLandOrderShipRequestToBuy(trackSpinner.getItems().get(trackSpinner.getSelectedIndex()).toString(),item );
                }

                else {
                    requests.child(localKey).setValue(item);
                    adapter.notifyDataSetChanged();//add to update item size
                    sendLandOrderStatusToUser(localKey, item);
                }
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();

            }
        });
        alertDialog.show();

    }

    private void sendLandOrderShipRequestToBuy(String trackerPhone, Requests item ) {
        DatabaseReference tokens = db.getReference("Tokens");
        tokens.child(trackerPhone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists())
                        {
                            Token token = dataSnapshot.getValue(Token.class);


                            Map<String,String> dataSend = new HashMap<>();
                            dataSend.put("Title","Land Purchase Order");
                            dataSend.put("Message","Your have new land order you need to track");
                            DataMessage dataMessage = new DataMessage(token.getToken(),dataSend);

                            mService.sendNotification(dataMessage)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            if (response.body().success == 1){

                                                Toast.makeText(LandStatus.this, "Sent to Tracker", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(LandStatus.this, "Failed to send notification !", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Log.e("ERROR",t.getMessage());

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void sendLandOrderStatusToUser(final String key, final Requests item) {
        DatabaseReference tokens = db.getReference("Tokens");
        tokens.child(item.getPhone())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists())
                        {
                            Token token = dataSnapshot.getValue(Token.class);


                            Map<String,String>dataSend = new HashMap<>();
                            dataSend.put("Title","Land Purchase Order");
                            dataSend.put("Message","Your Land order "+ key +" was updated");
                            DataMessage dataMessage = new DataMessage(token.getToken(),dataSend);

                            mService.sendNotification(dataMessage)
                                    .enqueue(new Callback<MyResponse>() {
                                        @Override
                                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                            if (response.body().success == 1){

                                                Toast.makeText(LandStatus.this, "Your Land order was updated !!", Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                Toast.makeText(LandStatus.this, "Land Order was updated but failed to send notification !", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                        @Override
                                        public void onFailure(Call<MyResponse> call, Throwable t) {
                                            Log.e("ERROR",t.getMessage());

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
}
