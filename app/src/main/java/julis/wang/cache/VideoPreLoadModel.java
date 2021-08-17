package julis.wang.cache;

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
    public int preLoadBytes;
    public String proxyUrl;
    public String originalUrl;
    public Map<String, String> header;

    public VideoPreLoadModel(int preLoadBytes, String proxyUrl, String originalUrl) {
        this(preLoadBytes, proxyUrl, originalUrl, new HashMap<>());
    }

    public VideoPreLoadModel(int preLoadBytes, String proxyUrl, String originalUrl, Map<String, String> header) {
        this.preLoadBytes = preLoadBytes;
        this.proxyUrl = proxyUrl;
        this.originalUrl = originalUrl;
        this.header = header;
    }

}
