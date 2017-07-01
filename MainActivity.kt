package com.example.m1838.livephoneplay

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.CheckBox
import com.example.m1838.livephoneplay.service.VideoLiveWallpaper

/**
 * 视频桌面
 */
class MainActivity : AppCompatActivity() {

    private var checkBox: CheckBox? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkBox = findViewById(R.id.id_cb_voice) as CheckBox
        checkBox!!.setOnCheckedChangeListener { compoundButton, b ->
            if (b) {
                // 静音
                VideoLiveWallpaper.voiceSilence(applicationContext)
            } else {
                VideoLiveWallpaper.voiceNormal(applicationContext)
            }
        }
    }

    fun setVideoToWallPaper(view: View) {
        VideoLiveWallpaper.setToWallPaper(this)
    }

    /**
     * 取消视频桌面按钮
     * @param view
     */
    fun setQuexiao(view: View) {

    }
}
