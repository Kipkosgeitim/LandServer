package com.example.landserver.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landserver.R;

public class LandOrderViewHolder extends RecyclerView.ViewHolder {
    public TextView txtLandId,txtLandPhone,txtLandStatus,txtLandAddress,txtLandDate;
    public Button btnEdit,btnRemove,btnDetails,btnDirection;


    public LandOrderViewHolder(@NonNull View itemView) {
        super(itemView);

        txtLandId = (TextView)itemView.findViewById(R.id.land_id);
        txtLandPhone=(TextView)itemView.findViewById(R.id.land_owner_phone);
        txtLandStatus=(TextView)itemView.findViewById(R.id.land_status);
        txtLandAddress=(TextView)itemView.findViewById(R.id.land_address);
        txtLandDate = (TextView)itemView.findViewById(R.id.land_order_date);

        btnEdit = (Button)itemView.findViewById(R.id.btnEdit);
        btnDetails = (Button)itemView.findViewById(R.id.btnDetail);
        btnDirection = (Button)itemView.findViewById(R.id.btnDirection);
        btnRemove = (Button)itemView.findViewById(R.id.btnRemove);



    }



}

