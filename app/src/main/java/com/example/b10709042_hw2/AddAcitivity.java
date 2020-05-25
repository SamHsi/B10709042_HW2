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
    // Create local EditText fields for mNewGuestNameEditText and mNewPartySizeEditText
    private EditText mNewGuestNameEditText;
    private EditText mNewPartySizeEditText;
    private SQLiteDatabase mDb;
    // COMPLETED (13) Create a constant string LOG_TAG that is equal to the class.getSimpleName()
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
                // COMPLETED (9) First thing, check if any of the EditTexts are empty, return if so
                if (mNewGuestNameEditText.getText().length() == 0 ||
                        mNewPartySizeEditText.getText().length() == 0) {
                    return;
                }
                // COMPLETED (10) Create an integer to store the party size and initialize to 1
                //default party size to 1
                int partySize = 1;
                // COMPLETED (11) Use Integer.parseInt to parse mNewPartySizeEditText.getText to an integer
                try {
                    //mNewPartyCountEditText inputType="number", so this should always work
                    partySize = Integer.parseInt(mNewPartySizeEditText.getText().toString());
                } catch (NumberFormatException ex) {
                    // COMPLETED (12) Make sure you surround the Integer.parseInt with a try catch and log any exception
                    Log.e(LOG_TAG, "Failed to parse party size text to number: " + ex.getMessage());
                }

                // COMPLETED (14) call addNewGuest with the guest name and party size
                // Add guest info to mDb
                addNewGuest(mNewGuestNameEditText.getText().toString(), partySize);

                // COMPLETED (19) call mAdapter.swapCursor to update the cursor by passing in getAllGuests()
                // Update the cursor in the adapter to trigger UI to display the new list
                mAdapter.swapCursor(getAllGuests());

                // COMPLETED (20) To make the UI look nice, call .getText().clear() on both EditTexts, also call clearFocus() on mNewPartySizeEditText
                //clear UI text fields
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

    // COMPLETED (4) Create a new addNewGuest method
    /**
     * Adds a new guest to the mDb including the party count and the current timestamp
     *
     * @param name  Guest's name
     * @param partySize Number in party
     * @return id of new record added
     */

    private long addNewGuest(String name, int partySize) {
        // COMPLETED (5) Inside, create a ContentValues instance to pass the values onto the insert query
        ContentValues cv = new ContentValues();
        // COMPLETED (6) call put to insert the name value with the key COLUMN_GUEST_NAME
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME, name);
        // COMPLETED (7) call put to insert the party size value with the key COLUMN_PARTY_SIZE
        cv.put(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE, partySize);
        // COMPLETED (8) call insert to run an insert query on TABLE_NAME with the ContentValues created
        return mDb.insert(WaitlistContract.WaitlistEntry.TABLE_NAME, null, cv);
    }

    /**
     * Query the mDb and get all guests from the waitlist table
     *
     * @return Cursor containing the list of guests
     */
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
