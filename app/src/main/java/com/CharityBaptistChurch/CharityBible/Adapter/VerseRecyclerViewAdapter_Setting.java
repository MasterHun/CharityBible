package com.CharityBaptistChurch.CharityBible.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.CharityBaptistChurch.CharityBible.R;

import java.util.List;

// 참고 ->> https://thepassion.tistory.com/294

/*
* @class    : VerseRecyclerViewAdapter_Setting
* @data     : 2020/03/21
* @lastedit : 2020/03/21
* @author   : 박명훈
* @brief    : 커스텀 RecyclerViewAdapter 이다. 설정페이지에서 사용하기위해서 생성함
 */
public class VerseRecyclerViewAdapter_Setting extends RecyclerView.Adapter<VerseRecyclerViewAdapter_Setting.StdViewHolder> {


    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);

    private Context mContext;
    private List<String> mdataVerse, mdataNumber;
    private RecyclerView recyclerView;
    private int mFontSize = 15;

    private boolean m_bCompare = false;             // true:성경비교x/false:성경비교o

    public VerseRecyclerViewAdapter_Setting(Context context
            , RecyclerView recyclerView
            , boolean bCompare) {
        this.mContext = context;

        this.recyclerView = recyclerView;
        this.m_bCompare = bCompare;

    }

    public VerseRecyclerViewAdapter_Setting(Context context
            , RecyclerView recyclerView){
        this.mContext = context;
        this.recyclerView = recyclerView;
        this.m_bCompare = false;

    }


    public void setData(List<String> dataNumber, List<String> dataVerse) {
        Log.d("Verse_Adapter","setData");
        mdataVerse = dataVerse;
        mdataNumber = dataNumber;

        notifyDataSetChanged();
    }

    public void setFontSize(int a_nFontSize)
    {
        mFontSize = a_nFontSize;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public StdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Verse_Adapter","OnCreateViewHolder");
        LayoutInflater inflate = LayoutInflater.from(mContext);
        View view = inflate.inflate(R.layout.verselist_item, parent, false);

        StdViewHolder vh = new StdViewHolder(view);
        return vh;
    }



    @Override
    public int getItemCount() {
        Log.d("Verse_Adapter","getItemCount");
        return mdataVerse.size();
    }

    @Override
    public void onBindViewHolder(@NonNull StdViewHolder holder, int position) {
        Log.d("Verse_Adapter","OnBindViewHolder");

        holder.textNumber.setTextSize(mFontSize);
        holder.textVerse.setTextSize(mFontSize);
        if(m_bCompare) {
            if (position % 2 == 0) {
                holder.textVerse.setText(mdataVerse.get(position));
                //   holder.textVerse.setBackgroundColor(Color.WHITE);
                holder.textVerse.setTextColor(Color.BLACK);

                holder.textNumber.setText(mdataNumber.get(position));
                //     holder.textNumber.setBackgroundColor(Color.WHITE);
                holder.textNumber.setTextColor(Color.BLACK);

                holder.itemView.setSelected(isItemSelected(position));
            } else {
                holder.textVerse.setText(mdataVerse.get(position));
                //   holder.textVerse.setBackgroundColor(Color.BLACK);
                holder.textVerse.setTextColor(Color.BLUE);

                holder.textNumber.setText(mdataNumber.get(position));
                //  holder.textNumber.setBackgroundColor(Color.BLACK);
                holder.textNumber.setTextColor(Color.BLUE);

                holder.itemView.setSelected(isItemSelected(position));
            }
        }else
        {
            holder.textVerse.setText(mdataVerse.get(position));
            holder.textNumber.setText(mdataNumber.get(position));
            holder.itemView.setSelected(isItemSelected(position));
        }
    }

    private void toggleItemSelected(int position) {
        Log.d("Verse_Adapter","toggleItemSelected");
        if (mSelectedItems.get(position, false)) {
            mSelectedItems.delete(position);
            notifyItemChanged(position);
        } else {
            mSelectedItems.put(position, true);
            notifyItemChanged(position);
        }
    }

    private boolean isItemSelected(int position) {
        return mSelectedItems.get(position, false);
    }

    static class StdViewHolder extends RecyclerView.ViewHolder {
        TextView textVerse;
        TextView textNumber;

        StdViewHolder(@NonNull View itemView) {

            super(itemView);
            this.textVerse= itemView.findViewById(R.id.text_verse);
            this.textNumber= itemView.findViewById(R.id.text_number);

            Log.d("Verse_Adapter","StdViewHolder");
        }
    }

}