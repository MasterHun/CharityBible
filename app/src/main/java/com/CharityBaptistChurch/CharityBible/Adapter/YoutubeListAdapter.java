package com.CharityBaptistChurch.CharityBible.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.CharityBaptistChurch.CharityBible.R;
import com.CharityBaptistChurch.CharityBible.Items.YoutubeListViewItem;
import com.CharityBaptistChurch.CharityBible.YoutubeViewHolder;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.HashMap;

public class YoutubeListAdapter extends BaseAdapter implements YouTubeThumbnailView.OnInitializedListener{


    private OnYotubeItemClick  m_YoutubeCallback;
    private ArrayList<YoutubeListViewItem> m_list = new ArrayList<>();
    HashMap<View, YouTubeThumbnailLoader> loaders = new HashMap<View,YouTubeThumbnailLoader>();

    public YoutubeListAdapter(OnYotubeItemClick a_YoutubeCallback){
        super();
        this.m_YoutubeCallback = a_YoutubeCallback;
    };
    @Override
    public int getCount() {
        return m_list.size();
    }

    @Override
    public Object getItem(int position) {
        return m_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void additem(String strTitle, String strContext, String strVideoId, String strDate)
    {
        YoutubeListViewItem youtubeitem = new YoutubeListViewItem();

        youtubeitem.setTitle(strTitle);
        youtubeitem.setContext(strContext);
        youtubeitem.setVideoId(strVideoId);
        youtubeitem.setDate(strDate);

        m_list.add(youtubeitem);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        final Context context = parent.getContext();
        View view = convertView;
        String videoId = m_list.get(position).getVideoId();
        YoutubeViewHolder holder;

        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            assert inflater != null;
            view = inflater.inflate(R.layout.youtube_playerview, parent, false);
            holder = new YoutubeViewHolder();
            YouTubeThumbnailView youTubePlayerView = view.findViewById(R.id.item_video);
            youTubePlayerView .setTag(videoId);
            youTubePlayerView .initialize("AIzaSyAGqpbHB3Dbke3gxgEOGYuLSAnSb7Q32Fo",this);

            holder.m_tvTitle = view.findViewById(R.id.tvTitle);
            holder.m_tvDate = view.findViewById(R.id.tvDate);

            Log.d("YoutubeListAdapter","Youtube A ["+position+"]");


            view.setTag(holder);

        }
        else
        {
            Log.d("YoutubeListAdapter","Youtube C ["+position+"]");
            holder = (YoutubeViewHolder) view.getTag();
            YouTubeThumbnailView thumbnail = view.findViewById(R.id.item_video);
            YouTubeThumbnailLoader loader = loaders.get(thumbnail);
            if(loader == null){
                thumbnail.setTag(videoId);
            }else{
                thumbnail.setTag(videoId);
                loader.setVideo(videoId);
            }
        }

        holder.m_LL_YoutubeItem = view.findViewById(R.id.LL_youtubeitem);
        holder.m_LL_YoutubeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                YoutubeListViewItem youtubeListViewItem = m_list.get(position);

                m_YoutubeCallback.OnClickItem(youtubeListViewItem.getVideoId());
                //Log.d("mhpark","Youtube B ["+position+"]"+youtubeListViewItem.getDate());
            }
        });

        YoutubeListViewItem youtubeListViewItem = m_list.get(position);
        //holder.tvContext.setText(youtubeListViewItem.getContext());
        holder.m_tvTitle.setText(youtubeListViewItem.getTitle());
        holder.m_tvDate.setText(youtubeListViewItem.getDate());

        return view;
    }

    //AIzaSyAGqpbHB3Dbke3gxgEOGYuLSAnSb7Q32Fo
    @Override
    public void onInitializationSuccess(YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
        String videoId = (String) view.getTag();
        loaders.put(view, loader);
        view.setImageResource(R.drawable.ic_launcher_foreground);
        loader.setVideo(videoId);

    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView view, YouTubeInitializationResult youTubeInitializationResult) {

    }

}
