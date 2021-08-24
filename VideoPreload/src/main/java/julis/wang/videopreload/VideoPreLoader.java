package julis.wang.videopreload;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/*******************************************************
 *
 * Created by juliswang on 2021/08/16 19:12 
 *
 * Description : 
 *
 *
 *******************************************************/

public class VideoPreLoader {
    private static VideoPreLoader loader;
    private final Handler handler;
    private final HandlerThread handlerThread;
    private final List<String> cancelList = new CopyOnWriteArrayList<>();
    private final List<String> preLoadList = new CopyOnWriteArrayList<>();

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

    public static VideoPreLoader getInstance() {
        if (loader == null) {
            synchronized (VideoPreLoader.class) {
                if (loader == null) {
                    loader = new VideoPreLoader();
                }
            }
        }
        return loader;
    }

    public void addPreloadURL(final VideoPreLoadModel data) {
        if (preLoadList.contains(data.originalUrl)) {
            return;
        } else {
            preLoadList.add(data.originalUrl);
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                realDownLoad(data);
            }
        });
    }

    public void cancelPreloadURLIfNeeded(String url) {
        cancelList.add(url);
    }

    public void cancelAnyPreLoads() {
        handler.removeCallbacksAndMessages(null);
        cancelList.clear();
    }

    private void realDownLoad(VideoPreLoadModel data) {
        if (data == null || isCancel(data.originalUrl)) {
            return;
        }
        HttpURLConnection conn = null;
        try {
            URL myURL = new URL(data.proxyUrl);
            conn = (HttpURLConnection) myURL.openConnection();
            addConnectionHeader(conn, data);
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
        } catch (Exception ex) {
            Log.e("VideoPreLoader", ex.getMessage());
        } finally {
            if (conn != null) {
                try {
                    conn.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                conn.disconnect();
            }
            preLoadList.remove(data.originalUrl);
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

    /**
     * header中数据，可以根据情况自行添加
     *
     * @param conn
     * @param data
     */

    private void addConnectionHeader(HttpURLConnection conn, VideoPreLoadModel data) {
        Map<String, String> headerMap = data.header;
        if (headerMap == null) {
            headerMap = new HashMap<>();
        }
        Map<String, String> globalMap = VideoProxyHelper.getInstance().getGlobalHeaders();
        if (globalMap != null && !globalMap.isEmpty()) {
            headerMap.putAll(globalMap);
        }
        Set<String> keys = headerMap.keySet();
        for (String key : keys) {
            if (!TextUtils.isEmpty(key) && headerMap.get(key) != null) {
                conn.setRequestProperty(key, headerMap.get(key));
            }
        }

    }

}
