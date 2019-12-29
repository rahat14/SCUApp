package com.SyntexError.scuapp.adminPackage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.SyntexError.scuapp.R;
import com.SyntexError.scuapp.models.modelsForReq;
import com.SyntexError.scuapp.viewHolderForReq;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.Queue;


public class pendingRequestFragment extends Fragment {

    View view;
    Button addBtn  ;
    LinearLayoutManager layoutManager  ;
    RecyclerView mrecyclerView ;
    Context context ;
    DatabaseReference reqRef ;
    String userID  ;

    public FirebaseRecyclerAdapter<modelsForReq, viewHolderForReq> firebaseRecyclerAdapter ;
    public FirebaseRecyclerOptions<modelsForReq> options ; // seraching in the profile ;

    public pendingRequestFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.my_booking_fragment, container, false);


        view = inflater.inflate(R.layout.my_booking_fragment, container, false);
        context = view.getContext();
        mrecyclerView = view.findViewById(R.id.list);




// setting up database and pointing to the item db
        reqRef = FirebaseDatabase.getInstance().getReference("reqDb");
        // setting up the layout manager


        layoutManager = new LinearLayoutManager(context);
        mrecyclerView.setLayoutManager(layoutManager);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        mrecyclerView.setHasFixedSize(true);


        loadData();
        return view;
    }

    private void loadData() {
        // load item list  from  firebase database
        // TODO  Query  insert

        Query fireBaseQuery =  reqRef.orderByChild("status").equalTo("pending") ;
        options = new FirebaseRecyclerOptions.Builder<modelsForReq>().setQuery(fireBaseQuery, modelsForReq.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<modelsForReq, viewHolderForReq>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final viewHolderForReq viewholderForItemList, int i, @NonNull modelsForReq modelForProduct) {


                // setThe data to the row
                //        String imageLink , String itemdes ,String name  ,String quatitys  ,String  category ;


                //     String name , String quantity , String mail , String returnDate , String stats ;
                viewholderForItemList.setDetails(modelForProduct.getEquipName(), modelForProduct.getQuantity()
                        , modelForProduct.getUserName(), modelForProduct.getToDate(), modelForProduct.getStatus());

                viewholderForItemList.setOnClickListener(new viewHolderForReq.ClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {

                        Intent intent = new Intent(getContext()  , RequestDetailActivity.class);
                        intent.putExtra("name", getItem(position).getEquipName()) ;
                        intent.putExtra("quatity" , getItem(position).getQuantity()) ;
                        intent.putExtra("details", getItem(position).getComment()) ;
                        intent.putExtra("pickDate" , getItem(position).getFromDate()) ;
                        intent.putExtra("toDate", getItem(position).getToDate()) ;
                        intent.putExtra("reqid",getItem(position).getReqID() ) ;
                        intent.putExtra("userID" , getItem(position).getUserID()) ;


                        startActivity( intent);



                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });


            }


            @NonNull
            @Override
            public viewHolderForReq onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.row_for_request, parent, false);
                viewHolderForReq viewHolder = new viewHolderForReq(itemView);

                return viewHolder;
            }
        };

        mrecyclerView.setLayoutManager(layoutManager);

        firebaseRecyclerAdapter.startListening();

        //setting adapter

        mrecyclerView.setAdapter(firebaseRecyclerAdapter);

    }


}