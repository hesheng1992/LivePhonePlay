package com.example.m1838.livephoneplay.service;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by m1838 on 2017/5/18.  继承墙纸服务
 */

public class VideoLiveWallpaper extends WallpaperService {

    public static final String VIDEO_PARAMS_CONTROL_ACTION = "com.zhy.livewallpaper";
    public static final String KEY_ACTION = "action";
    public static final int ACTION_VOICE_SILENCE = 110;
    public static final int ACTION_VOICE_NORMAL = 111;
    @Override
    public Engine onCreateEngine() {
        return new VideoEngine();
    }

    /**
     * 设置静音
     * @param context
     */
    public static void voiceSilence(Context context) {
        Intent intent = new Intent(VideoLiveWallpaper.VIDEO_PARAMS_CONTROL_ACTION);
        intent.putExtra(VideoLiveWallpaper.KEY_ACTION, VideoLiveWallpaper.ACTION_VOICE_SILENCE);
        context.sendBroadcast(intent);
    }

    /**
     * 设置一般音量
     * @param context
     */
    public static void voiceNormal(Context context) {
        Intent intent = new Intent(VideoLiveWallpaper.VIDEO_PARAMS_CONTROL_ACTION);
        intent.putExtra(VideoLiveWallpaper.KEY_ACTION, VideoLiveWallpaper.ACTION_VOICE_NORMAL);
        context.sendBroadcast(intent);
    }

    /**
     * 设置到墙纸
     * @param context
     */
    public static void setToWallPaper(Context context) {
        final Intent intent = new Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
        intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                new ComponentName(context, VideoLiveWallpaper.class));
        context.startActivity(intent);
    }


    class VideoEngine extends Engine{

        private MediaPlayer mMediaPlayer;

        private BroadcastReceiver mVideoParamsControlReceiver;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            IntentFilter intentFilter = new IntentFilter(VIDEO_PARAMS_CONTROL_ACTION);
            registerReceiver(mVideoParamsControlReceiver=new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    int action = intent.getIntExtra(KEY_ACTION, -1);
                    switch (action){
                        //静音
                        case ACTION_VOICE_SILENCE:
                            mMediaPlayer.setVolume(0,0);
                            break;
                        case ACTION_VOICE_NORMAL:
                            mMediaPlayer.setVolume(1.0f, 1.0f);
                            break;
                    }
                }
            },intentFilter);
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (visible) {
                mMediaPlayer.start();
            } else {
                mMediaPlayer.pause();
            }
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            unregisterReceiver(mVideoParamsControlReceiver);
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setSurface(holder.getSurface());
            AssetManager assetManager=getApplicationContext().getAssets();
            try {
                AssetFileDescriptor fileDescriptor = assetManager.openFd("duan.mp4");
                mMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),
                        fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                mMediaPlayer.setLooping(true);
                mMediaPlayer.setVolume(0, 0);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


}
