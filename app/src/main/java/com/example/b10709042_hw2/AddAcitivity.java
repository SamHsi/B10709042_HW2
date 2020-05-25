package com.example.b10709042_hw2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.b10709042_hw2.data.WaitlistContract;
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
                if (mNewGuestNameEditText.getText().length() == 0 ||
                        mNewPartySizeEditText.getText().length() == 0) {
                    return;
                }
                int partySize = 1;
                try {
                    partySize = Integer.parseInt(mNewPartySizeEditText.getText().toString());
                } catch (NumberFormatException ex) {
                    Log.e(LOG_TAG, "Failed to parse party size text to number: " + ex.getMessage());
                }
                addNewGuest(mNewGuestNameEditText.getText().toString(), partySize);
                mAdapter.swapCursor(getAllGuests());
                mNewPartySizeEditText.clearFocus();
                mNewGuestNameEditText.getText().clear();
                mNewPartySizeEditText.getText().clear();
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

    private long addNewGuest(String name, int partySize) {
        ContentValues cv = new ContentValues();
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, name);
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, partySize);
        return mDb.insert(WaitlistContract.WaitlistEntry.TABLE_NAME, null, cv);
    }

    private Cursor getAllGuests() {
        return mDb.query(
                WaitlistContract.WaitlistEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WaitlistContract.WaitlistEntry.COLUMN_TIMESTAMP
        );
    }
}
