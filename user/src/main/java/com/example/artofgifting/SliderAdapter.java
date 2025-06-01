package com.example.artofgifting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.artofgifting.Models.ProductModel;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.MySliderAdapter> {
    Context context;
    List<String> adbannerarraylist;

    public SliderAdapter(Context context, ArrayList<String> adbannerarraylist) {
        this.context = context;
        this.adbannerarraylist = adbannerarraylist;
    }

    @Override
    public int getCount() {
        return adbannerarraylist.size();
    }

    @Override
    public MySliderAdapter onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.raw_flipper, parent, false);
        return new MySliderAdapter(inflate);
    }

    @Override
    public void onBindViewHolder(MySliderAdapter viewHolder, int position) {
        Picasso picasso = null;
        picasso.get().load(adbannerarraylist.get(position)).into(viewHolder.imageView);

    }

    public class MySliderAdapter extends SliderViewAdapter.ViewHolder {
        ImageView imageView;

        public MySliderAdapter(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.slider);

        }
    }
}
