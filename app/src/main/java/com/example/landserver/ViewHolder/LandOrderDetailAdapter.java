package com.example.landserver.ViewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.landserver.R;
import com.example.landserver.models.LandOrder;

import java.util.List;

class MyViewHolder extends RecyclerView.ViewHolder{
    public TextView name,sizeOfLand,landPrice,landTitle;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        name = (TextView)itemView.findViewById(R.id.land_location_name);
        sizeOfLand = (TextView)itemView.findViewById(R.id.size_of_land);
        landPrice = (TextView)itemView.findViewById(R.id.land_price);
        landTitle = (TextView)itemView.findViewById(R.id.land_title);

    }
}

public class LandOrderDetailAdapter extends RecyclerView.Adapter<MyViewHolder> {

   List<LandOrder> myLandOrders;

    public LandOrderDetailAdapter(List<LandOrder> myLandOrders) {
        this.myLandOrders = myLandOrders;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.land_order_detail_layout,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LandOrder landOrder = myLandOrders.get(position);
        holder.name.setText(String.format("Name : %s",landOrder.getLandNameLocation()));
        holder.sizeOfLand.setText(String.format("Size Of Land : %s",landOrder.getSizeOfLand()));
        holder.landPrice.setText(String.format("Land Price : %s",landOrder.getPrice()));
        holder.landTitle.setText(String.format("Land Title : %s",landOrder.getLandTitle()));
    }

    @Override
    public int getItemCount() {
        return myLandOrders.size();
    }
}
