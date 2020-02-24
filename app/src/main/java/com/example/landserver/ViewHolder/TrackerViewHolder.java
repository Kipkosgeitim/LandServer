package com.example.landserver.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.landserver.Interface.ItemClickListener;
import com.example.landserver.R;

public class TrackerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView tracker_name,tracker_phone;
    public Button btn_edit,btn_remove;
    private ItemClickListener itemClickListener;


    public TrackerViewHolder(@NonNull View itemView) {
        super(itemView);

        tracker_name  = (TextView)itemView.findViewById(R.id.tracker_name);
        tracker_phone = (TextView)itemView.findViewById(R.id.tracker_phone);
        btn_edit = (Button)itemView.findViewById(R.id.btnEdit);
        btn_remove = (Button)itemView.findViewById(R.id.btnRemove);


    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }
}
