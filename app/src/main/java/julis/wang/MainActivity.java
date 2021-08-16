package julis.wang;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;


public class MainActivity extends AppCompatActivity {
    private EditText etUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        etUrl = findViewById(R.id.et_url);
        JzvdStd jzvdStd = findViewById(R.id.jz_video);
        jzvdStd.preloading = false;
        jzvdStd.setUp(etUrl.getText().toString(), "TestVideo");
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