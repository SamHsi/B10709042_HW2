package com.example.b10709042_hw2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.example.b10709042_hw2.data.WaitlistContract;
import com.example.b10709042_hw2.data.WaitlistDBHelper;



public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private GuestListAdapter mAdapter;
    // Create a local field SQLiteDatabase called mDb
    private SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView waitlistRecyclerView;

        // Set local attributes to corresponding views
        waitlistRecyclerView = (RecyclerView) this.findViewById(R.id.all_guests_list_view);

        // Set layout for the RecyclerView, because it's a list we are using the linear layout
        waitlistRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create a WaitlistDbHelper instance, pass "this" to the constructor
        // Create a DB helper (this will create the DB if run for the first time)
        WaitlistDBHelper dbHelper = new WaitlistDBHelper(this);

        // Get a writable database reference using getWritableDatabase and store it in mDb
        // Keep a reference to the mDb until paused or killed. Get a writable database
        // because you will be adding restaurant customers
        mDb = dbHelper.getWritableDatabase();

        // Run the getAllGuests function and store the result in a Cursor variable
        Cursor cursor = getAllGuests();

        // Pass the entire cursor to the adapter rather than just the count
        // Create an adapter for that cursor to display the data
        mAdapter = new GuestListAdapter(this, cursor);

        // Link the adapter to the RecyclerView
        waitlistRecyclerView.setAdapter(mAdapter);
        setupSharedPreferences();

        // COMPLETED (3) Create a new ItemTouchHelper with a SimpleCallback that handles both LEFT and RIGHT swipe directions
        // Create an item touch helper to handle swiping items off the list
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            // COMPLETED (4) Override onMove and simply return false inside
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                //do nothing, we only care about swiping
                return false;
            }

            // COMPLETED (5) Override onSwiped
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                new AlertDialog.Builder(MainActivity.this).setTitle(R.string.warning).setMessage(R.string.warningcheck)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // COMPLETED (8) Inside, get the viewHolder's itemView's tag and store in a long variable id
                        //get the id of the item being swiped
                        long id = (long) viewHolder.itemView.getTag();
                        Log.d(LOG_TAG, String.valueOf(id));
                        // COMPLETED (9) call removeGuest and pass through that id
                        //remove from DB
                        removeGuest(id);
                        // COMPLETED (10) call swapCursor on mAdapter passing in getAllGuests() as the argument
                        //update the list
                        mAdapter.swapCursor(getAllGuests());
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    }
                }).create().show();
            }
            //COMPLETED (11) attach the ItemTouchHelper to the waitlistRecyclerView
        }).attachToRecyclerView(waitlistRecyclerView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    // Create a private method called getAllGuests that returns a cursor
    /**
     * Query the mDb and get all guests from the waitlist table
     *
     * @return Cursor containing the list of guests
     */
    private Cursor getAllGuests() {
        // Inside, call query on mDb passing in the table name and projection String [] order by COLUMN_TIMESTAMP
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainactivitymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings) {
            Intent openSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(openSettingsActivity);
            return true;
        }
        else if (id == R.id.add) {
            Intent openSettingsActivity = new Intent(this, AddAcitivity.class);
            startActivity(openSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // COMPLETED (1) Create a new function called removeGuest that takes long id as input and returns a boolean
    /**
     * Removes the record with the specified id
     *
     * @param id the DB id to be removed
     * @return True: if removed successfully, False: if failed
     */
    private boolean removeGuest(long id) {
        // COMPLETED (2) Inside, call mDb.delete to pass in the TABLE_NAME and the condition that WaitlistEntry._ID equals id
        return mDb.delete(WaitlistContract.WaitlistEntry.TABLE_NAME, WaitlistContract.WaitlistEntry._ID + "=" + id, null) > 0;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
