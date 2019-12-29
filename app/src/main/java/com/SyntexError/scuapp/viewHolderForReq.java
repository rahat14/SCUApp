package com.SyntexError.scuapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class viewHolderForReq extends RecyclerView.ViewHolder {

    View mview;



    public viewHolderForReq(@NonNull View itemView) {
        super(itemView);

         mview = itemView ;
        //item click


        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());

            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
          return  true ;
            }
        });


    }

    public  void setDetails(String name , String quantity , String mail , String returnDate , String stats) {

        TextView eqName = (TextView) mview.findViewById(R.id.product_name_tv);
        TextView eqQuantity = (TextView) mview.findViewById(R.id.quantityTv_tv);
        TextView usereame = (TextView) mview.findViewById(R.id.username_name_tv);
        TextView retruDate = (TextView) mview.findViewById(R.id.returnDate_tv);
        TextView statusTv = mview.findViewById(R.id.status_tv);


        eqName.setText("Equipment Name : " + name);
        eqQuantity.setText("Quantity : "+ quantity);
        usereame.setText("User Email : "+ mail);
        retruDate.setText("Return Date : " + returnDate);
        statusTv.setText("Status : "+ stats);


    }
    private viewHolderForReq.ClickListener mClickListener;


    //interface to send callbacks
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(viewHolderForReq.ClickListener clickListener)
    {
        mClickListener = clickListener;
    }




}
