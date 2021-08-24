# AndroidVideoCachePreload
基于 AndroidVideoCache 预加载的 Demo
相关介绍博客：[基于AndroidVideoCache的预加载](https://blog.csdn.net/u010107153/article/details/107091077)

## Usage

```java
VideoPreLoadModel videoPreLoadModel = new VideoPreLoadModel(PRE_LOAD_SIZE, originalUrl); //设预加载的大小，以及播放地址
String playUrl = VideoProxyHelper.getInstance().getProxyUrl(context, videoPreLoadModel); //通过代理获取到代理播放地址
xxxPlayer.setPlayUrl(playUrl);
```

存储的缓存文件保存在：/sdcard/Android/data/{package_name}/cache

