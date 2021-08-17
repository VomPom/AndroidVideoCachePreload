package julis.wang;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

import cn.jzvd.Jzvd;
import julis.wang.cache.VideoProxyHelper;


public class MainActivity extends AppCompatActivity {
    private EditText etUrl;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        Map<String, Object> config = new HashMap<>();
        config.put("maxCacheLength", 5 * 100 * 1024); //设置默认预加载大小
        VideoProxyHelper.getInstance().setBuildConfig(config);

        recyclerView = findViewById(R.id.rv_video_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new VideoAdapter());
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

}