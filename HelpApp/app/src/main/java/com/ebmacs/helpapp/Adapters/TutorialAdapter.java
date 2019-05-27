package com.ebmacs.helpapp.Adapters;

import android.content.Context;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ebmacs.helpapp.R;

import java.util.ArrayList;

public class TutorialAdapter extends PagerAdapter {


    private ArrayList<String> arrayList;
    private LayoutInflater inflater;
    private Context context;


    public TutorialAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList=arrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.item_tutorial, view, false);

        assert imageLayout != null;


        final TextView textView = imageLayout.findViewById(R.id.txtView);
        final LinearLayout parentLayout = imageLayout.findViewById(R.id.parentLayout);

        textView.setText(arrayList.get(position));

//        Picasso.get().load(arrayList.get(position)).into(imgView);
        view.addView(imageLayout, 0);


        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }


}
