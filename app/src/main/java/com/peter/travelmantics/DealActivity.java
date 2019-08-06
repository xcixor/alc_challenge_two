package com.peter.travelmantics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class DealActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 42;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private EditText mTitleEditText;
    private EditText mPriceEditText;
    private EditText mDescriptionEditText;
    private TravelDeal deal;
    private StorageReference ref;
    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        mDatabaseReference = FirebaseUtil.mDatabaseReference;

        mTitleEditText = (EditText)findViewById(R.id.txt_title);
        mPriceEditText = (EditText)findViewById(R.id.txt_price);
        mDescriptionEditText = (EditText)findViewById(R.id.txt_description);
        mImageView = (ImageView)findViewById(R.id.image);

        Intent intent = getIntent();
        TravelDeal deal = (TravelDeal) intent.getSerializableExtra("Deal");
        if (deal == null)
            deal = new TravelDeal();
        this.deal = deal;
        mTitleEditText.setText(deal.getmTitle());
        mPriceEditText.setText(deal.getmPrice());
        mDescriptionEditText.setText(deal.getmDescription());
        showImage(deal.getImgUrl());
        Button btnImg = findViewById(R.id.btn_img);
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent.createChooser(intent, "Insert Picture"), REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Uri uri = data.getData();
            ref = FirebaseUtil.mStorageRef.child(uri.getLastPathSegment());
            UploadTask uploadTask = ref.putFile(uri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String url = task.getResult().toString();
//                        String pictureName = task
                        deal.setImgUrl(url);
                        showImage(url);
                    } else {
                        Log.d("Exception", "Error occured while saving");
                    }
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu, menu);
        if (FirebaseUtil.isAdmin){
            menu.findItem(R.id.delete_menu).setVisible(true);
            menu.findItem(R.id.save_menu).setVisible(true);
            enableEditTexts(true);
        }else {
            menu.findItem(R.id.delete_menu).setVisible(false);
            menu.findItem(R.id.save_menu).setVisible(false);
            enableEditTexts(false);
        }
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

    private void enableEditTexts(boolean isEnabled) {
        mTitleEditText.setEnabled(isEnabled);
        mPriceEditText.setEnabled(isEnabled);
        mDescriptionEditText.setEnabled(isEnabled);
    }

    private void showImage(String url){
        if (url != null && url.isEmpty() == false) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get()
                    .load(url)
                    .resize(width, width*2/3)
                    .centerCrop()
                    .into(mImageView);
        }
    }
}
