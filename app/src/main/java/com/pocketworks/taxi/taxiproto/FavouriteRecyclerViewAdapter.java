package com.pocketworks.taxi.taxiproto;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class FavouriteRecyclerViewAdapter extends RecyclerView.Adapter<FavouriteRecyclerViewAdapter.ViewHolder> {

    private final List<Favourite> mValues;
    private final FavouritesFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;
    private TaxiAppDBHelper dbHelper;

    public FavouriteRecyclerViewAdapter(List<Favourite> items, FavouritesFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;

    }

    public void changeList(List<Favourite> list){
        if ( mValues != null)
            mValues.clear();

        mValues.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item_favourite, parent, false);
        mContext = parent.getContext();
        dbHelper = TaxiAppDBHelper.getInstance(mContext);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        Favourite f = mValues.get(position);
        holder.mAddress.setText(f.address);
        holder.mCity.setText(f.city_name);


        holder.mRemove.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                  dbHelper.deleteFavourite(holder.mItem.id);
                                                  mValues.remove(position);
                                                  notifyDataSetChanged();

                                              }
                                          });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    Intent intent = new Intent(mContext, BookingFragment.class);
                                    // pass the item information

                    intent.putExtra("address", holder.mItem.getAddress());
                    intent.putExtra("city", holder.mItem.getCity_name());

                    mContext.startActivity(intent);
                    
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mAddress;
        public final TextView mCity;
        public final ImageButton mRemove;
        public Favourite mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mAddress = (TextView) view.findViewById(R.id.address);
            mCity = (TextView) view.findViewById(R.id.city);
            mRemove = (ImageButton) view.findViewById(R.id.removeFav);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mCity.getText() + "'";
        }
    }
}
