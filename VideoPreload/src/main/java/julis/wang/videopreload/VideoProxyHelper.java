package julis.wang.videopreload;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.headers.HeaderInjector;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/*******************************************************
 *
 * Created by juliswang on 2021/08/16 23:14 
 *
 * Description : 
 *
 *
 *******************************************************/

public class VideoProxyHelper {
    private static final long DEFAULT_CACHE_LENGTH = 512 * 1024 * 1024;
    private static volatile VideoProxyHelper videoProxyHelper;
    private HttpProxyCacheServer proxy;
    private long maxCacheLength = DEFAULT_CACHE_LENGTH;
    private Map<String, String> globalHeaders = new HashMap<>();

    public static VideoProxyHelper getInstance() {
        if (videoProxyHelper == null) {
            synchronized (VideoProxyHelper.class) {
                if (videoProxyHelper == null) {
                    videoProxyHelper = new VideoProxyHelper();
                }
            }
        }
        return videoProxyHelper;
    }

    HttpProxyCacheServer getProxy(Context context) {
        if (proxy == null && context != null) {
            proxy = newProxy(context);
        }
        return proxy;
    }

    private HttpProxyCacheServer newProxy(Context context) {
        return new HttpProxyCacheServer.Builder(context)
                .cacheDirectory(CacheUtils.getVideoCacheDir(context))
                .headerInjector(new GlobalHeadersInjector())
                .maxCacheSize(maxCacheLength)
                .build();
    }


    public void setBuildConfig(Map<String, Object> argumentMap) {
        if (argumentMap == null) {
            return;
        }
        if (argumentMap.get("maxCacheLength") != null) {
            this.maxCacheLength = Long.parseLong(String.valueOf(argumentMap.get("maxCacheLength")));
        }
    }

    public String getProxyUrl(Context context, VideoPreLoadModel videoPreLoadModel) {
        HttpProxyCacheServer proxy = VideoProxyHelper.getInstance().getProxy(context);
        VideoPreLoader.getInstance().addPreloadURL(videoPreLoadModel);
        return proxy.getProxyUrl(videoPreLoadModel.getOriginalUrl(), false);
    }

    public long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            if (fileList == null) {
                return -1;
            }
            for (File value : fileList) {
                size += value.isDirectory() ? getFolderSize(value) : value.length();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public class GlobalHeadersInjector implements HeaderInjector {

        @Override
        public Map<String, String> addHeaders(String url) {
            return globalHeaders;
        }
    }

    Map<String, String> getGlobalHeaders() {
        return globalHeaders;
    }
}
