package com.example.landserver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.landserver.Common.Common;
import com.example.landserver.ViewHolder.LandOrderDetailAdapter;

public class LandDetails extends AppCompatActivity {

    TextView land_id,land_owner_phone,land_address,land_total,land_comment;
    String land_id_value="";
    RecyclerView stLands;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_land_details);

        land_id = (TextView)findViewById(R.id.land_id);
        land_owner_phone = (TextView)findViewById(R.id.land_owner_phone);
        land_address= (TextView)findViewById(R.id.land_address);
        land_total = (TextView)findViewById(R.id.land_total);
        land_comment = (TextView)findViewById(R.id.land_comment);

        stLands = (RecyclerView)findViewById(R.id.stLand);
        stLands.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        stLands.setLayoutManager(layoutManager);

        if (getIntent() !=null)
            land_id_value = getIntent().getStringExtra("LandId");

        //Set value
        land_id.setText(land_id_value);
        land_owner_phone.setText(Common.currentRequest.getPhone());
        land_total.setText(Common.currentRequest.getTotal());
        land_address.setText(Common.currentRequest.getAddress());
        land_comment.setText(Common.currentRequest.getComment());

        LandOrderDetailAdapter adapter = new LandOrderDetailAdapter(Common.currentRequest.getLands());
        adapter.notifyDataSetChanged();
        stLands.setAdapter(adapter);
    }
}

