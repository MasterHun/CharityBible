package com.CharityBaptistChurch.CharityBible.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.CharityBaptistChurch.CharityBible.R;
import com.CharityBaptistChurch.CharityBible.Items.YoutubeListViewItem;
import com.CharityBaptistChurch.CharityBible.YoutubeViewHolder;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.ArrayList;
import java.util.HashMap;

public class YoutubeListAdapter extends BaseAdapter implements YouTubeThumbnailView.OnInitializedListener{


    private ArrayList<YoutubeListViewItem> m_list = new ArrayList<YoutubeListViewItem>();

    HashMap<View, YouTubeThumbnailLoader> loaders = new HashMap<View,YouTubeThumbnailLoader>();
    public YoutubeListAdapter(){
        super();
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
    public View getView(int position, View convertView, ViewGroup parent) {


        final Context context = parent.getContext();
        View view = convertView;
        String videoId = m_list.get(position).getVideoId();
        YoutubeViewHolder holder;

        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.youtube_playerview, parent, false);
            holder = new YoutubeViewHolder();
            YouTubeThumbnailView youTubePlayerView = view.findViewById(R.id.item_video);
            youTubePlayerView = (YouTubeThumbnailView)view.findViewById(R.id.item_video);
            youTubePlayerView .setTag(videoId);
            youTubePlayerView .initialize("AIzaSyAGqpbHB3Dbke3gxgEOGYuLSAnSb7Q32Fo",this);

            holder.tvTitle = (TextView)view.findViewById(R.id.tvTitle);
            holder.tvContext = (TextView)view.findViewById(R.id.tvContext);
            view.setTag(holder);

        }
        else
        {
            holder = (YoutubeViewHolder) view.getTag();
            YouTubeThumbnailView thumbnail = (YouTubeThumbnailView) view.findViewById(R.id.item_video);
            YouTubeThumbnailLoader loader = loaders.get(thumbnail);
            if(loader == null){
                thumbnail.setTag(videoId);
            }else{
   //              thumbnail.setImageResource(R.drawable.ic_volume_black_24dp);
                loader.setVideo(videoId);
            }
        }

        YoutubeListViewItem youtubeListViewItem = m_list.get(position);
        holder.tvContext.setText(youtubeListViewItem.getContext());
        holder.tvTitle.setText(youtubeListViewItem.getTitle());

        return view;
    }

    //AIzaSyAGqpbHB3Dbke3gxgEOGYuLSAnSb7Q32Fo
    @Override
    public void onInitializationSuccess(YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
        String videoId = (String) view.getTag();
        loaders.put(view, loader);
    //    view.setImageResource(R.drawable.ic_launcher_foreground);
        loader.setVideo(videoId);

    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView view, YouTubeInitializationResult youTubeInitializationResult) {

    }

}
