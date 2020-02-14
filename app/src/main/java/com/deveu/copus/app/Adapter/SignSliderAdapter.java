package com.deveu.copus.app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.deveu.copus.app.Datas.slide;
import com.deveu.copus.app.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SignSliderAdapter extends PagerAdapter {


    private Context mcontext;
    private List<slide> mList;


    public SignSliderAdapter(Context mcontext, List<slide> mList) {
        this.mcontext = mcontext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater=(LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View slideLayout= inflater.inflate(R.layout.item_signslider,null);
        ImageView slideImg=slideLayout.findViewById(R.id.book_picofslider);
        TextView slideText=slideLayout.findViewById(R.id.slide_tittletest);


        Picasso.get().load(mList.get(position).getImage()).into(slideImg);
        slideText.setText(mList.get(position).getTitle());


        container.addView(slideLayout);
        return slideLayout;

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
