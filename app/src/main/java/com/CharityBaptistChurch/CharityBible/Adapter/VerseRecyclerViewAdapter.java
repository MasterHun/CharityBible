package com.CharityBaptistChurch.CharityBible.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.CharityBaptistChurch.CharityBible.R;

import java.util.ArrayList;
import java.util.List;

// 참고 ->> https://thepassion.tistory.com/294
//public class VerseRecyclerViewAdapter extends RecyclerView.Adapter<VerseRecyclerViewAdapter.StdViewHolder>{
//
//    public interface OnListItemLongSelectedInterface{
//        void onItemLongSelected(View v, int position);
//    }
//
//    public interface OnListItemSelectedInterface{
//        void OnListItemSelectedInterface(View v, int position);
//    }
//
//    private OnListItemSelectedInterface mListener;
//    private OnListItemLongSelectedInterface mLongListener;
//
//
//    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0); // 다중선택시 선택 Position을 저장하는공간
//
//
//
//    // ----------------
//
//    Context mContext;
//    RecyclerView mRecyclerView;
//    List<String> mdata;
//
//    public VerseRecyclerViewAdapter(Context context
//            , OnListItemSelectedInterface listner
//            , OnListItemLongSelectedInterface longListener
//            , RecyclerView recyclerview) {
//        this.mContext = context;
//        this.mListener =listner;
//        this.mLongListener = longListener;
//        this.mRecyclerView = recyclerview;
//    }
//
//    public void setData(ArrayList<String> data){
//        this.mdata = data;
//        notifyDataSetChanged();     // 어댑터에서 변화를 갱신해주는 함수.
//    }
//
//
//    @NonNull
//    @Override
//    public StdViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        View view = inflater.inflate(R.layout.verselist_item, viewGroup, false);
//
//        StdViewHolder vh = new StdViewHolder(view);
//        return vh;
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull StdViewHolder holder, int position) {
//        holder.textView.setText(mdata.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return mdata.size();
//    }
//
//
//
//    public static class StdViewHolder extends RecyclerView.ViewHolder {
//        public TextView textView;
//
//        public StdViewHolder(@NonNull View itemView){
//            super(itemView);
//            this.textView = itemView.findViewById(R.id.text_verse);
//
//            itemView.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    int position = getAdapterPosition();
//
//
//
//                    Log.d("RecyclerView","position="+getAdapterPosition());
//                }
//            });
//
//            itemView.setOnLongClickListener(new View.OnLongClickListener(){
//                @Override
//                public boolean onLongClick(View v) {
//
//                    Log.d("RecyclerView","Long position="+getAdapterPosition());
//                    return false;
//                }
//            });
//        }
//    }
//}

public class VerseRecyclerViewAdapter extends RecyclerView.Adapter<VerseRecyclerViewAdapter.StdViewHolder> {

    public interface OnListItemLongSelectedInterface {
        void onItemLongSelected(View v, int position);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    private OnListItemSelectedInterface mListener;
    private OnListItemLongSelectedInterface mLongListener;


    private SparseBooleanArray mSelectedItems = new SparseBooleanArray(0);

    Context mContext;
    List<String> mdata;
    RecyclerView recyclerView;

    public VerseRecyclerViewAdapter(Context context
            , RecyclerView recyclerView
            , OnListItemSelectedInterface listener
            , OnListItemLongSelectedInterface longListener) {
        this.mContext = context;
        this.mListener = listener;
        this.mLongListener = longListener;
        this.recyclerView = recyclerView;
    }

    public void setData(List<String> data) {
        mdata = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(mContext);
        View view = inflate.inflate(R.layout.verselist_item, parent, false);

        StdViewHolder vh = new StdViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StdViewHolder holder, int position) {
        holder.textView.setText(mdata.get(position));

        holder.itemView.setSelected(isItemSelected(position));
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class StdViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public StdViewHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.text_verse);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    toggleItemSelected(position);

                    Log.d("test", "position = " + position);
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

    private void toggleItemSelected(int position) {

        if (mSelectedItems.get(position, false) == true) {
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

    // Clear는 사용할일 없을듯
//    public void clearSelectedItem() {
//        int position;
//
//        for (int i = 0; i < mSelectedItems.size(); i++) {
//            position = mSelectedItems.keyAt(i);
//            mSelectedItems.put(position, false);
//            notifyItemChanged(position);
//        }
//
//        mSelectedItems.clear();
//    }
}