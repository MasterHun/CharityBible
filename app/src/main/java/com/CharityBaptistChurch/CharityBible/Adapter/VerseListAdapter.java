package com.CharityBaptistChurch.CharityBible.Adapter;

import android.graphics.Color;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.CharityBaptistChurch.CharityBible.Items.ListViewItem;
import com.CharityBaptistChurch.CharityBible.R;
import com.CharityBaptistChurch.CharityBible.ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class VerseListAdapter extends BaseAdapter {

    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    //private ArrayList<ListViewItem> listViewItemList;
    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

//    public VerseListAdapter(Context context, int textViewResourceId, ArrayList<ListViewItem> listViewItemList)
//    {
//        super(context, textViewResourceId, listViewItemList);
//        this.listViewItemList = listViewItemList;
//    }

    public VerseListAdapter() {
        super();
    }

    // Adapter에 사용되는 데이터의 개수를 리턴 : 필수
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    // 지정된 위치(position)에 있는 데이터 리턴 : 필수
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    // 지정된 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴 : 필수
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(String strChapter, String strSection, String strVerse,int nReplaceNumber ) {
        ListViewItem item = new ListViewItem(strChapter, strSection, strVerse, nReplaceNumber);

//        item.setStrChapter(strChapter);
//        item.setStrSection(strSection);
//        item.setStrVerse(strVerse);
//        item.setReplaceNumber(nReplaceNumber);

        listViewItemList.add(item);
    }



    private int selectedPosition;
    private boolean selectable = true;
    public void setSelectedPosition(int position)
    {
        this.selectedPosition = position;
    }

    //private View rootView;
    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴 : 필수
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos = position;
        final Context context = parent.getContext();
        ViewHolder holder;

        // 리스트뷰 xml Layout을 inflate하여 convertView 참조 획득
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);           // 이부분은 생성자에 넣기도 한다.

            convertView = inflater.inflate(R.layout.verselist_item,parent,false);  // 전역으로 변경하는 이유는 리스트뷰가 계속 새로 불려서
            holder = new ViewHolder();
            holder.txtView1 = (TextView) convertView.findViewById(R.id.text_number);        // getview가 불릴때마다 findviewbyid를 안해줘도 되기때문에
            holder.txtView2 = (TextView) convertView.findViewById(R.id.text_verse) ;

            convertView.setTag(holder);
            //convertView = inflater.inflate(R.layout.verselist_item, parent, false);     // 레이아웃을 View에 연결해준다.
        }else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        if(selectable)
        {
            holder.txtView2.setBackgroundResource(R.drawable.selector_view_item);
            if(position == selectedPosition){
                holder.txtView2.setSelected(true);
                holder.txtView2.setBackgroundColor(context.getResources().getColor(R.color.ItemSelectedColor));
            }else
            {

            }
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
       // TextView textNumber = (TextView) convertView.findViewById(R.id.text_number) ;
       // TextView textVerse = (TextView) convertView.findViewById(R.id.text_verse) ;

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        ListViewItem listViewItem = listViewItemList.get(position);

//        if (1 == listViewItem.getReplaceNumber()) { // 이 번호에 따라서 색상을 매겨야 할 것 같다.
//            convertView.setBackgroundResource(R.color.colorPrimary);    // 배경색 변경
//
//            holder.txtView1.setTextColor(Color.parseColor("#FFFFFF"));
//            holder.txtView2.setTextColor(Color.parseColor("#FFFFFF"));
//
//            //textNumber.setTextColor(Color.parseColor("#FFFFFF"));
//            //textVerse.setTextColor(Color.parseColor("#FFFFFF"));
//        }else {
//            convertView.setBackgroundResource(R.color.colorWhite);    // 배경색 변경
//
//            holder.txtView1.setTextColor(Color.parseColor("#000000"));
//            holder.txtView2.setTextColor(Color.parseColor("#000000"));
//
//        }

//        if(convertView.isSelected())
//        {
//            convertView.setBackgroundResource(R.color.ItemPressedColor);
//            holder.txtView1.setTextColor(Color.parseColor("#000000"));
//            holder.txtView2.setTextColor(Color.parseColor("#000000"));
//        }
    //else{
//            convertView.setBackgroundResource(R.color.colorWhite);
//            holder.txtView1.setTextColor(Color.parseColor("#000000"));
//            holder.txtView2.setTextColor(Color.parseColor("#000000"));
//        }
        // 아이템 내 각 위젯에 데이터 반영
        holder.txtView1.setText(listViewItem.getStrChapter() + ":" + listViewItem.getStrSection() + " ");
        holder.txtView2.setText(listViewItem.getStrVerse());


        //convertView.setBackgroundResource(R.drawable.selector_view_item);
        // 리스트뷰 다중선택 기능 사용하기 위해서
//        if( true == convertView.isSelected() && false == holder.checkText.isChecked())
//        {
//            holder.checkText.setChecked(true);
//        }else
//        {
//            holder.checkText.setChecked(false);
//        }

        //textNumber.setText(listViewItem.getStrChapter() + ":" + listViewItem.getStrSection() + " ");
        //textVerse.setText(listViewItem.getStrVerse());

        return convertView;

    }
    public void setLayout()
    {

    }

}
