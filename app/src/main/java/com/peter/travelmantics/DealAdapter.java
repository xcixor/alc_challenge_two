package com.peter.travelmantics;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder>{
    private ArrayList<TravelDeal> mDeals;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    public DealAdapter(){
        FirebaseUtil.openFbReference("traveldeals");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;
        mDeals = FirebaseUtil.mDeals;
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                TravelDeal td = dataSnapshot.getValue(TravelDeal.class);
                Log.d("Deal", td.getmTitle());
                td.setmId(dataSnapshot.getKey());
                mDeals.add(td);
                notifyItemInserted(mDeals.size() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabaseReference.addChildEventListener(mChildEventListener);

    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(
                R.layout.rv_row, parent, false
        );
        return new DealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        TravelDeal deal = mDeals.get(position);
        holder.bind(deal);
    }

    @Override
    public int getItemCount() {
        return mDeals.size();
    }

    public class DealViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        TextView tvTitle;
        TextView tvDescription;
        TextView tvPrice;
        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_title);
            tvDescription = (TextView)itemView.findViewById(R.id.tv_description);
            tvPrice = (TextView)itemView.findViewById(R.id.tv_price);
            itemView.setOnClickListener(this);
        }
        public void bind(TravelDeal deal){
            tvTitle.setText(deal.getmTitle());
            tvDescription.setText(deal.getmDescription());
            tvPrice.setText(deal.getmPrice());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Log.d("click at", String.valueOf(position));
            TravelDeal selectedItem = mDeals.get(position);
            Intent intent = new Intent(view.getContext(), DealActivity.class);
            intent.putExtra("Deal", selectedItem);
            view.getContext().startActivity(intent);
        }
    }
}
