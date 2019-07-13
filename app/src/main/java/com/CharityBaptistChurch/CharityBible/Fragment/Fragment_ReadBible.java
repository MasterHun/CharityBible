package com.CharityBaptistChurch.CharityBible.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.CharityBaptistChurch.CharityBible.R;
import com.CharityBaptistChurch.CharityBible.VerseListAdapter;

public class Fragment_ReadBible extends Fragment {

    View view;
    ListView listview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);


        view = inflater.inflate(R.layout.fragment_readbible,container,false);
        listview = (ListView)view.findViewById(R.id.lv1);


        VerseListAdapter adapter;
        adapter = new VerseListAdapter();

        listview.setAdapter(adapter);


        // #테스트 성경구절 삽입
        adapter.addItem("1  ","예수 그리스도의 종 바울은 사도로 부르심을 받아 [하나님]의 복음을 위해 구별되었는데 ");
        adapter.addItem("2  ","(이 복음은 그분께서 자신의 대언자들을 통하여 거룩한 성경 기록들에 미리 약속하신 것으로)");
        adapter.addItem("3  ","자신의 [아들] 예수 그리스도 우리 [주]에 관한 것이라. 그분께서는 육체로는 다윗의 씨에서 나셨고");
        adapter.addItem("4  ","거룩함의 영으로는 죽은 자들로부터 부활하심으로써 [하나님]의 [아들]로 권능 있게 밝히 드러나셨느니라.");
        adapter.addItem("5  ","그분으로 말미암아 우리가 은혜와 사도직을 받아 그분의 이름을 위하여 모든 민족들 가운데서 믿음에 순종하게 하였나니");
        adapter.addItem("6  ","너희도 그들 가운데서 예수 그리스도의 부르심을 받았느니라.");
        adapter.addItem("7  ","바울은, 로마에서 [하나님]께 사랑을 받고 성도로 부르심을 받은 모든 사람에게 편지하노니 [하나님] 우리 [아버지]와 [주] 예수 그리스도로부터 은혜와 평강이 너희에게 있기를 원하노라.");
        adapter.addItem("8  ","먼저 너희 모두로 인하여 예수 그리스도를 통해 나의 [하나님]께 감사하노니 이는 너희의 믿음이 온 세상에 두루 전하여졌기 때문이라.");
        adapter.addItem("9  ","내가 그분의 [아들]의 복음 안에서 내 영으로 섬기는 [하나님]께서 내 증인이 되시거니와 내가 기도할 때에 언제나 너희에 관하여 끊임없이 말하며");
        adapter.addItem("10  ","어찌하든지 이제라도 마침내 [하나님]의 뜻에 따라 순탄한 여정을 얻어 너희에게 가게 되기를 간구하노라.");
        adapter.addItem("11  ","내가 너희를 간절히 보고자 함은 내가 너희에게 어떤 영적 선물을 나누어 주어 너희를 굳게 세우고자 함이니");
        adapter.addItem("12  ","이것은 곧 너희와 나 사이의 공통된 믿음으로 말미암아 내가 너희와 함께 위로를 받고자 함이라.");
        adapter.addItem("13  ","형제들아, 이제 나는 너희가 이것을 모르기를 원치 아니하노니 곧 내가 너희 가운데서도 다른 이방인들 가운데서처럼 어떤 열매를 얻기 위해 여러 번 너희에게 가고자 하였으나 (이제껏 막혔도다.)");
        adapter.addItem("14  ","나는 그리스인이나 바바리인이나 지혜 있는 자나 지혜 없는 자에게 다 빚진 자니라.");
        adapter.addItem("15  ","그러므로 내 안에 있는 분량대로 나는 또한 로마에 있는 너희에게 복음을 선포할 준비가 되어 있노라.");
        adapter.addItem("16  ","내가 그리스도의 복음을 부끄러워하지 아니하노니 이는 그 복음이 믿는 모든 자를 구원에 이르게 하는 [하나님]의 권능이기 때문이라. 먼저는 유대인에게요 또한 그리스인에게로다.");
        adapter.addItem("17  ","복음에는 [하나님]의 의가 믿음에서 믿음까지 계시되어 있나니 이것은 기록된바, 의인은 믿음으로 살리라, 함과 같으니라.");



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
