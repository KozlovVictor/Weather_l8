package com.geekbrains.weather.city;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geekbrains.weather.data.Constants;
import com.geekbrains.weather.data.PreferencesData;
import com.geekbrains.weather.R;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<SelectedCity> cityList;
    private CreateActionFragment.OnHeadlineSelectedListener mCallback;
    private Context context;
    private PreferencesData preferencesData;

    public CustomAdapter(Context context, Activity activity, ArrayList<SelectedCity> cityList, CreateActionFragment.OnHeadlineSelectedListener mCallback) {
        this.cityList = cityList;
        this.mCallback = mCallback;
        this.context = context;
        preferencesData = new PreferencesData(activity);
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomAdapter.ViewHolder holder, final int position) {
        final SelectedCity selectedCity = cityList.get(position);
        holder.textView.setText(selectedCity.getCity());
        if (selectedCity.getSelected()) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.blue));
        } else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCity(selectedCity);
                mCallback.onArticleSelected(selectedCity);
            }
        });
    }

    private void selectCity(SelectedCity selectedCity) {
        int pos = cityList.size() + 1;
        for (int i = 0; i < cityList.size(); i++) {
            if (cityList.get(i).getCity().equals(selectedCity.getCity())) {
                pos = i;
                if (!cityList.get(i).getSelected()) {
                    cityList.get(i).setSelected(true);
                } else {
                    cityList.get(i).setSelected(false);
                }
            }
        }
        for (int i = 0; i < cityList.size(); i++) {
            if (i != pos) {
                cityList.get(i).setSelected(false);
            }
        }
        if (selectedCity.getSelected()) {
            preferencesData.saveSharedPreferences(Constants.CITY, selectedCity.getCity());
        } else {
            preferencesData.deleteSharedPreferences(Constants.CITY);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        private LinearLayout linearLayout;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.linearlayout_item);
            textView = itemView.findViewById(R.id.textview_item);
            cardView = itemView.findViewById(R.id.card_view);

        }
    }
}
