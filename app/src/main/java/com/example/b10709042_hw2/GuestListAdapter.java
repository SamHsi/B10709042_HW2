package com.example.b10709042_hw2;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.b10709042_hw2.data.WaitlistContract;

import java.util.prefs.Preferences;


public class GuestListAdapter extends RecyclerView.Adapter<GuestListAdapter.GuestViewHolder> {

    private Context mContext;
    private int color;
    // Replace the mCount with a new Cursor field called mCursor
    // Holds on to the cursor to display the waitlist
    private Cursor mCursor;

    // COMPLETED (2) Modify the constructor to accept a cursor rather than an integer
    /**
     * Constructor using the context and the db cursor
     * @param context the calling context/activity
     * @param cursor the db cursor with waitlist data to display
     */
    public GuestListAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        // Set the local mCount to be equal to count
        this.mCursor = cursor;
    }

    @Override
    public GuestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.guest_list_item, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GuestViewHolder holder, int position) {
        // COMPLETED (5) Move the cursor to the passed in position, return if moveToPosition returns false
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null
        // COMPLETED (6) Call getString on the cursor to get the guest's name
        String name = mCursor.getString(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_GUEST_NAME));
        // COMPLETED (7) Call getInt on the cursor to get the party size
        int partySize = mCursor.getInt(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry.COLUMN_PARTY_SIZE));
        long id = mCursor.getLong(mCursor.getColumnIndex(WaitlistContract.WaitlistEntry._ID));
        // COMPLETED (8) Set the holder's nameTextView text to the guest's name
        // Display the guest name
        setupColor();
        holder.nameTextView.setText(name);
        // COMPLETED (9) Set the holder's partySizeTextView text to the party size
        // Display the party count
        holder.partySizeTextView.setText(String.valueOf(partySize));
        holder.partySizeTextView.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);


        holder.itemView.setTag(id);

    }

    // Modify the getItemCount to return the mCount value rather than 0
    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    // COMPLETED (15) Create a new function called swapCursor that takes the new cursor and returns void
    /**
     * Swaps the Cursor currently held in the adapter with a new one
     * and triggers a UI refresh
     *
     * @param newCursor the new cursor that will replace the existing one
     */
    public void swapCursor(Cursor newCursor) {
        // COMPLETED (16) Inside, check if the current cursor is not null, and close it if so
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        // COMPLETED (17) Update the local mCursor to be equal to  newCursor
        mCursor = newCursor;
        // COMPLETED (18) Check if the newCursor is not null, and call this.notifyDataSetChanged() if so
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    public void setupColor(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String c =sharedPreferences.getString("display","red");
        switch (c){
            case ("red"):{
                color= Color.RED;
                break;
            }
            case ("blue"):{
                color= Color.BLUE;
                break;
            }
            case ("green"):{
                color= Color.GREEN;
                break;
            }
        }
    }


    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */
    class GuestViewHolder extends RecyclerView.ViewHolder {

        // Will display the guest name
        TextView nameTextView;
        // Will display the party size number
        TextView partySizeTextView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews
         *
         * @param itemView The View that you inflated in
         *                 {@link GuestListAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public GuestViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name_text_view);
            partySizeTextView = (TextView) itemView.findViewById(R.id.party_size_text_view);
        }

    }
}
