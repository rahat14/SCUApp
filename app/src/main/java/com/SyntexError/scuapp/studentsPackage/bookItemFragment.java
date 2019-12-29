package com.SyntexError.scuapp.studentsPackage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SyntexError.scuapp.R;
import com.SyntexError.scuapp.adminPackage.addProductActivity;
import com.SyntexError.scuapp.adminPackage.viewholderForItemList;
import com.SyntexError.scuapp.models.modelForProduct;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class bookItemFragment extends Fragment {



    View view;
    Button addBtn  ;
    LinearLayoutManager layoutManager  ;
    RecyclerView mrecyclerView ;
    Context context ;
    DatabaseReference itemRef ;

    public FirebaseRecyclerAdapter<modelForProduct, viewholderForItemList> firebaseRecyclerAdapter ;
    public FirebaseRecyclerOptions<modelForProduct> options ; // seraching in the profile ;
    public bookItemFragment(){}
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.product_list_fragment, container, false);
        context = view.getContext();

        addBtn = view.findViewById(R.id.addItemBtn) ;
        mrecyclerView = view.findViewById( R.id.productList) ;
        TextView msg = (TextView) view.findViewById(R.id.mesgText);
        addBtn.setVisibility(View.INVISIBLE);

        msg.setText("Please Select The Item You Want To Book");
// setting up database and pointing to the item db
        itemRef = FirebaseDatabase.getInstance().getReference("items");
        // setting up the layout manager


        layoutManager = new LinearLayoutManager(context);
        mrecyclerView.setLayoutManager(layoutManager) ;
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mrecyclerView.setHasFixedSize(true);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent o = new Intent( getContext() , addProductActivity.class);
                startActivity(o);


            }
        });

        loadData() ;
        return  view ;
    }

    private void loadData() {
        // load item list  from  firebase database

        options = new FirebaseRecyclerOptions.Builder<modelForProduct>().setQuery(itemRef , modelForProduct.class).build() ;

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<modelForProduct, viewholderForItemList>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final viewholderForItemList viewholderForItemList, int i, @NonNull modelForProduct modelForProduct) {


                // setThe data to the row
                //        String imageLink , String itemdes ,String name  ,String quatitys  ,String  category ;

                int remainQuantity  =  Integer.valueOf(modelForProduct.getQuatity())- Integer.valueOf(modelForProduct.getCount()) ;

                viewholderForItemList.setDetails(modelForProduct.getImageLink() , modelForProduct.getItemdes()
                        , modelForProduct.getName() ,remainQuantity+"" , modelForProduct.getCategory());


                viewholderForItemList.deleteBtn.setVisibility(View.INVISIBLE);

                viewholderForItemList.setOnClickListener(new viewholderForItemList.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        //nameOfEquipment = i.getStringExtra("NAME") ;
                        //        quantityAvailable = i.getStringExtra("QUANTITY") ;
                        //        iteMID = i.getStringExtra("ITEMID") ;


                        Intent  m = new Intent(context , bookingPageActivity.class);
                        m.putExtra("NAME" , getItem(position).getName()) ;
                        m.putExtra("QUANTITY" , getItem(position).getQuatity()) ;
                        m.putExtra("ITEMID" , getItem(position).getItemID()) ;

                        startActivity(m);

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });





            }

            @NonNull
            @Override
            public viewholderForItemList onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_for_product, parent, false);
                viewholderForItemList viewHolder = new viewholderForItemList(itemView);

                return viewHolder;
            }
        } ;

        mrecyclerView.setLayoutManager(layoutManager);

        firebaseRecyclerAdapter.startListening();

        //setting adapter

        mrecyclerView.setAdapter(firebaseRecyclerAdapter);

    }



}
