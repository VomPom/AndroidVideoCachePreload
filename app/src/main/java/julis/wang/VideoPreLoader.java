package julis.wang;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*******************************************************
 *
 * Created by juliswang on 2021/08/16 19:12 
 *
 * Description : 
 *
 *
 *******************************************************/

public class VideoPreLoader {
    private final Handler handler;
    private final HandlerThread handlerThread;
    private final List<String> cancelList = new ArrayList<>();

    private VideoPreLoader() {
        handlerThread = new HandlerThread("VideoPreLoaderThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
    }

    void addPreloadURL(final VideoPreLoadModel data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                realPreload(data);
            }
        });
    }

    void cancelPreloadURLIfNeeded(String url) {
        cancelList.add(url);
    }

    void cancelAnyPreLoads() {
        handler.removeCallbacksAndMessages(null);
        cancelList.clear();
    }

    private void realPreload(VideoPreLoadModel data) {
        if (data == null || isCancel(data.originalUrl)) {
            return;
        }
        HttpURLConnection conn;
        try {
            URL myURL = new URL(data.proxyUrl);
            conn = (HttpURLConnection) myURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            byte[] buf = new byte[1024];
            int downLoadedSize = 0;
            do {
                int numRead = is.read(buf);
                downLoadedSize += numRead;
                if (downLoadedSize >= data.preLoadBytes || numRead == -1) { //Reached  preload range or end of Input stream.
                    break;
                }
            } while (true);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isCancel(String url) {
        if (TextUtils.isEmpty(url)) {
            return true;
        }
        for (String cancelUrl : cancelList) {
            if (cancelUrl.equals(url)) {
                return true;
            }
        }
        return false;
    }
}
