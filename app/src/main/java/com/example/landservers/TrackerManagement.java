package com.example.landservers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.landservers.Common.Common;
import com.example.landservers.ViewHolder.TrackerViewHolder;
import com.example.landservers.models.Tracker;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class TrackerManagement extends AppCompatActivity {

    FloatingActionButton fabAdd;
    FirebaseDatabase database;
    DatabaseReference trackers;

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Tracker, TrackerViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker_management);

        //Init view

        fabAdd = (FloatingActionButton)findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateShipperLayout();

            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.recycler_trackers);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Firebase
        database = FirebaseDatabase.getInstance();
        trackers = database.getReference(Common.TRACKER_TABLE);

        //load all shippers
        loadAllShippers();

    }

    private void loadAllShippers() {
        FirebaseRecyclerOptions<Tracker> allTrackers = new FirebaseRecyclerOptions.Builder<Tracker>()
                .setQuery(trackers,Tracker.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Tracker, TrackerViewHolder>(allTrackers) {
            @Override
            protected void onBindViewHolder(@NonNull TrackerViewHolder holder, final int position, @NonNull final Tracker model) {
                holder.tracker_phone.setText(model.getPhone());
                holder.tracker_name.setText(model.getName());

                holder.btn_edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showEditDialog(adapter.getRef(position).getKey(),model);
                    }
                });
                holder.btn_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeShipper(adapter.getRef(position).getKey());
                    }
                });
            }

            @NonNull
            @Override
            public TrackerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.tracker_layout,viewGroup,false);
                return new TrackerViewHolder(itemView);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    private void showEditDialog(String key,Tracker model) {

        AlertDialog.Builder create_shipper_dialog = new AlertDialog.Builder(TrackerManagement.this);
        create_shipper_dialog.setTitle("Update Tracker");

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.create_tracker_layout,null);

        final MaterialEditText edt_name = (MaterialEditText)view.findViewById(R.id.edtName);
        final MaterialEditText edt_phone = (MaterialEditText)view.findViewById(R.id.edtPhone);
        final MaterialEditText edt_password= (MaterialEditText)view.findViewById(R.id.edtPassword);

        //Set data
        edt_name.setText(model.getName());
        edt_phone.setText(model.getPhone());
        edt_password.setText(model.getPassword());

        create_shipper_dialog.setView(view);

        create_shipper_dialog.setIcon(R.drawable.ic_landscape_black_24dp);

        create_shipper_dialog.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                Map<String,Object> update = new HashMap<>();
                update.put("name",edt_name.getText().toString());
                update.put("phone",edt_phone.getText().toString());
                update.put("password",edt_password.getText().toString());

                trackers.child(edt_phone.getText().toString())
                        .updateChildren(update)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(TrackerManagement.this, "Tracker updated !", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TrackerManagement.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        create_shipper_dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        create_shipper_dialog.show();
    }

    private void removeShipper(String key) {
        trackers.child(key)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(TrackerManagement.this, "Remove Successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TrackerManagement.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void showCreateShipperLayout() {
        AlertDialog.Builder create_shipper_dialog = new AlertDialog.Builder(TrackerManagement.this);
        create_shipper_dialog.setTitle("Create Shipper");

        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.create_tracker_layout,null);

        final MaterialEditText edt_name = (MaterialEditText)view.findViewById(R.id.edtName);
        final MaterialEditText edt_phone = (MaterialEditText)view.findViewById(R.id.edtPhone);
        final MaterialEditText edt_password= (MaterialEditText)view.findViewById(R.id.edtPassword);

        create_shipper_dialog.setView(view);

        create_shipper_dialog.setIcon(R.drawable.ic_landscape_black_24dp);

        create_shipper_dialog.setPositiveButton("CREATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                Tracker tracker = new Tracker();
                tracker.setName(edt_name.getText().toString());
                tracker.setPhone(edt_phone.getText().toString());
                tracker.setPassword(edt_password.getText().toString());

                trackers.child(edt_phone.getText().toString())
                        .setValue(tracker)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(TrackerManagement.this, "Tracker created !", Toast.LENGTH_SHORT).show();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TrackerManagement.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        create_shipper_dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        create_shipper_dialog.show();



    }
}
