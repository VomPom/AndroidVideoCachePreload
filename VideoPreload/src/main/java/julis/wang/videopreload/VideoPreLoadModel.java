package julis.wang.videopreload;

import java.util.HashMap;
import java.util.Map;

/*******************************************************
 *
 * Created by juliswang on 2021/08/16 19:13 
 *
 * Description : 
 *
 *
 *******************************************************/

public class VideoPreLoadModel {
    public int preLoadBytes; //预加载大小
    public String originalUrl;
    public Map<String, String> header;

    public VideoPreLoadModel(int preLoadBytes, String originalUrl) {
        this(preLoadBytes, originalUrl, new HashMap<>());
    }

    public VideoPreLoadModel(int preLoadBytes, String originalUrl, Map<String, String> header) {
        this.preLoadBytes = preLoadBytes;
        this.originalUrl = originalUrl;
        this.header = header;
    }

    public int getPreLoadBytes() {
        return preLoadBytes;
    }

    public void setPreLoadBytes(int preLoadBytes) {
        this.preLoadBytes = preLoadBytes;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }
}
