package julis.wang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import cn.jzvd.JzvdStd;
import julis.wang.cache.VideoPreLoadModel;
import julis.wang.cache.VideoPreLoader;
import julis.wang.cache.VideoProxyHelper;

/*******************************************************
 *
 * Created by juliswang on 2021/08/17 23:04 
 *
 * Description : 
 *
 *
 *******************************************************/

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {
    private static final String TEST_URL_FORMAT = "https://laravel-admin.org/test/videos/K0%d.mp4";
    private static final int PRE_LOAD_SIZE = 5 * 1024 * 1024;

    @NotNull
    @Override
    public VideoHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_item, null);
        return new VideoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull VideoHolder holder, int position) {
        holder.updateView(position);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    static class VideoHolder extends RecyclerView.ViewHolder {
        private final JzvdStd jzvdStd;
        private final Context context;

        public VideoHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            jzvdStd = itemView.findViewById(R.id.jz_video);
            jzvdStd.preloading = false; // 禁止库里面的预加载功能，方便测试VideoCache的功能
        }

        public void updateView(int position) {
            String originalUrl = String.format(TEST_URL_FORMAT, 10 + position);
            String proxyUrl = getProxyURL(originalUrl);
            VideoPreLoadModel videoPreLoadModel = new VideoPreLoadModel(PRE_LOAD_SIZE, proxyUrl, originalUrl);
            VideoPreLoader.getInstance().addPreloadURL(videoPreLoadModel);
            jzvdStd.setUp(proxyUrl, originalUrl);
            jzvdStd.titleTextView.setText(originalUrl);
        }

        private String getProxyURL(String videoUrl) {
            return VideoProxyHelper.getInstance().getProxyUrl(context, videoUrl);
        }
    }
}
