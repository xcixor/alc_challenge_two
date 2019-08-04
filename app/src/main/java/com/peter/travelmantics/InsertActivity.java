package com.peter.travelmantics;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertActivity extends AppCompatActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private EditText mTitleEditText;
    private EditText mPriceEditText;
    private EditText mDescriptionEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("traveldeals");

        mTitleEditText = (EditText)findViewById(R.id.txt_title);
        mPriceEditText = (EditText)findViewById(R.id.txt_price);
        mDescriptionEditText = (EditText)findViewById(R.id.txt_description);
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
        String title = mTitleEditText.getText().toString();
        String price = mPriceEditText.getText().toString();
        String description = mDescriptionEditText.toString();
        TravelDeal deal = new TravelDeal(title, description, price, "");
        mDatabaseReference.push().setValue(deal);
    }
}
