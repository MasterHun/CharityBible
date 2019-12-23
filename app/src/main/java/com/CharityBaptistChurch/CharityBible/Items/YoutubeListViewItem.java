package com.CharityBaptistChurch.CharityBible.Items;

import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class YoutubeListViewItem {
//    private YouTubePlayerView m_youtubeplayerview;
 //   private YouTubeThumbnailView m_youTubeThumbnailView;

    private String strTitle;        // Youtube 영상 제목
    private String strContext;      // Youtube 내용
    private String strVideoId;      // Youtube 영상 식별번호
    private String strDate;         // Youtube 영상 업로드 시간

    // Getter
    public String getTitle() {
        return strTitle;
    }

    public String getContext() {
        return strContext;
    }

    public String getVideoId() {
        return strVideoId;
    }

    public String getDate() {
        return strDate;
    }

    // Setter
    public void setTitle(String strTitle) {
        this.strTitle = strTitle;
    }

    public void setContext(String strContext) {
        this.strContext = strContext;
    }

    public void setVideoId(String strVideoId) {
        this.strVideoId = strVideoId;
    }
    public void setDate(String strDate){
        this.strDate = strDate;
    }
}
