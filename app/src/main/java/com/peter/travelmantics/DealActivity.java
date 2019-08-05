package com.peter.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DealActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private EditText mTitleEditText;
    private EditText mPriceEditText;
    private EditText mDescriptionEditText;
    private TravelDeal deal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUtil.openFbReference("traveldeals");
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        mTitleEditText = (EditText)findViewById(R.id.txt_title);
        mPriceEditText = (EditText)findViewById(R.id.txt_price);
        mDescriptionEditText = (EditText)findViewById(R.id.txt_description);

        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if (deal == null)
            deal = new TravelDeal();
        this.deal = deal;
        mTitleEditText.setText(deal.getmTitle());
        mPriceEditText.setText(deal.getmPrice());
        mDescriptionEditText.setText(deal.getmDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_menu:
                saveDeal();
                Toast.makeText(this, "Deal saved!", Toast.LENGTH_LONG).show();
                clean();
                backToList();
                return true;
            case R.id.delete_menu:
                deleteDeal();
                Toast.makeText(this, "Deal deleted!", Toast.LENGTH_LONG).show();
                backToList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void clean() {
        mTitleEditText.setText("");
        mDescriptionEditText.setText("");
        mPriceEditText.setText("");
        mTitleEditText.requestFocus();
    }

    private void saveDeal() {
        deal.setmTitle(mTitleEditText.getText().toString());
        deal.setmPrice(mPriceEditText.getText().toString());
        deal.setmDescription(mDescriptionEditText.getText().toString());
        if (deal.getmId() == null) {
            mDatabaseReference.push().setValue(deal);
        }else {
            mDatabaseReference.child(deal.getmId()).setValue(deal);
        }
    }
    private void deleteDeal(){
        if (deal == null){
            Toast.makeText(this, "Please save the deal before deleting", Toast.LENGTH_SHORT).show();
        }
        mDatabaseReference.child(deal.getmId()).removeValue();
    }
    private void backToList(){
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
    }
}
