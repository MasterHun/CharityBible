package com.CharityBaptistChurch.CharityBible.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.text.LineBreaker;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.CharityBaptistChurch.CharityBible.R;

import java.util.List;

// 참고 ->> https://thepassion.tistory.com/294

/*
* @class    : VerseRecyclerViewAdapter
* @data     : 2020/01/19
* @lastedit : 2020/04/18
* @author   : 박명훈
* @brief    : 커스텀 RecyclerViewAdapter 이다.
 */
public class VerseRecyclerViewAdapter_Search extends RecyclerView.Adapter<VerseRecyclerViewAdapter_Search.StdViewHolder> {

    public interface OnListItemLongSelectedInterface {
        void onItemLongSelected(View v, int position);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener;
    private OnListItemLongSelectedInterface mLongListener;

    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);

    private Context mContext;
    private  List<String> mdataVerse, mdataNumber;
    private RecyclerView recyclerView;

    private int m_nFontSize;

    public VerseRecyclerViewAdapter_Search(Context context
            , RecyclerView recyclerView
            , OnListItemSelectedInterface listener
            , OnListItemLongSelectedInterface longListener) {
        this.mContext = context;
        this.mListener = listener;
        this.mLongListener = longListener;
        this.recyclerView = recyclerView;
        this.m_nFontSize = 15;

    }

    public VerseRecyclerViewAdapter_Search(Context context, RecyclerView recyclerView)
    {
        this.mContext = context;
        this.recyclerView = recyclerView;
    }



    public void setFontSize(int m_nFontSize) {
        this.m_nFontSize = m_nFontSize;
    }

    public void setData(List<String> dataNumber, List<String> dataVerse) {
        Log.d("Verse_Adapter","setData");
        mdataVerse = dataVerse;
        mdataNumber = dataNumber;

        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @NonNull
    @Override
    public StdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("Verse_Adapter","OnCreateViewHolder");
        LayoutInflater inflate = LayoutInflater.from(mContext);
        View view = inflate.inflate(R.layout.verselist_item, parent, false);

        return new StdViewHolder(view);
    }

    @Override
    public int getItemCount() {
        Log.d("Verse_Adapter","getItemCount");
        return mdataVerse.size();
    }

    @Override
    public void onBindViewHolder(@NonNull StdViewHolder holder, int position) {
        Log.d("Verse_Adapter","OnBindViewHolder");

        holder.textVerse.setTextSize(TypedValue.COMPLEX_UNIT_SP,m_nFontSize);
        holder.textNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, m_nFontSize);


        holder.textVerse.setText(mdataVerse.get(position));
        holder.textNumber.setText(mdataNumber.get(position));
        holder.itemView.setSelected(isItemSelected(position));

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

    public class StdViewHolder extends RecyclerView.ViewHolder {
        public TextView textVerse;
        public TextView textNumber;

        @RequiresApi(api = Build.VERSION_CODES.Q)
        public StdViewHolder(@NonNull View itemView) {

            super(itemView);
            this.textVerse  = itemView.findViewById(R.id.text_verse);
            this.textNumber = itemView.findViewById(R.id.text_number);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.textVerse.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
            }

            Log.d("Verse_Adapter","StdViewHolder");
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();

                    mListener.onItemSelected(v, getAdapterPosition());
                    toggleItemSelected(pos);

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mLongListener.onItemLongSelected(v, getAdapterPosition());
                    return false;
                }
            });
        }
    }


}