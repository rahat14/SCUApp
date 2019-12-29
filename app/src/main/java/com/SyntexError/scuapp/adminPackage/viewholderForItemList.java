package com.SyntexError.scuapp.adminPackage;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.SyntexError.scuapp.R;
import com.squareup.picasso.Picasso;

public class viewholderForItemList  extends RecyclerView.ViewHolder {

    View mview  ;
public  ImageView productImage ;
    public  ImageView  deleteBtn ;

    public viewholderForItemList(@NonNull View itemView) {


        super(itemView);

        mview  = itemView ;

    }

    public  void setDetails(String imageLink , String itemdes ,String name  ,String quatitys  ,String  category  )
    {
        TextView title , quantity  , details ;


        deleteBtn = mview.findViewById(R.id.deleteBtn) ;
        title = (TextView) mview.findViewById(R.id.product_name_tv);
        quantity = mview.findViewById(R.id.quantityTv) ;
        details = (TextView) mview.findViewById(R.id.product_details_tv);
        productImage = mview.findViewById(R.id.productPic) ;


        Picasso.get().load(imageLink).into(productImage) ; // loading image via image loader acitvity

        title.setText(name);
        quantity.setText(quatitys);
        details.setText(" Item Category : " + category + "\n Details : "+itemdes );

        //item click
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getAdapterPosition());

            }
        });

    }

    private viewholderForItemList.ClickListener mClickListener;


    //interface to send callbacks
    public interface ClickListener{
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public void setOnClickListener(viewholderForItemList.ClickListener clickListener)
    {
        mClickListener = clickListener;
    }


}
