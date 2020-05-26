package com.example.b10709042_hw2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.b10709042_hw2.data.WaitlistDBHelper;

public class AddAcitivity extends AppCompatActivity {
    private EditText mNewGuestNameEditText;
    private EditText mNewPartySizeEditText;
    private SQLiteDatabase mDb;
    private final static String LOG_TAG = AddAcitivity.class.getSimpleName();
    private GuestListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_acitivity);
        mNewGuestNameEditText = (EditText) this.findViewById(R.id.name);
        mNewPartySizeEditText = (EditText) this.findViewById(R.id.id);

        WaitlistDBHelper dbHelper = new WaitlistDBHelper(this);
        mDb = dbHelper.getWritableDatabase();
        Button ok = findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNewPartySizeEditText.getText().length() == 0 || mNewGuestNameEditText.getText().length() == 0) {
                    return;
                }
                int partySize = 1;
                String name="";
                try {
                    partySize = Integer.parseInt(mNewPartySizeEditText.getText().toString());
                    name=mNewGuestNameEditText.getText().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                mNewGuestNameEditText.getText().clear();
                mNewPartySizeEditText.getText().clear();
                mNewPartySizeEditText.clearFocus();
                Intent intent=new Intent(AddAcitivity.this,MainActivity.class);
                intent.putExtra("guestName",name);
                intent.putExtra("partySize",partySize);
                startActivity(intent);
            }
        });
        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
