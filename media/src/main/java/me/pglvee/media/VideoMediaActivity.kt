/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.media

import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.EventListener
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.StyledPlayerView

class VideoMediaActivity: AppCompatActivity() {

    private val mediaPlayIb: ImageButton by lazy { findViewById(R.id.mediaPlayIb) }

    private val player by lazy { SimpleExoPlayer.Builder(this).build().apply {
        addListener(object : EventListener {
            override fun onPlaybackStateChanged(state: Int) {
                when (state) {
                    Player.STATE_READY, Player.STATE_BUFFERING -> mediaPlayIb.visibility = View.GONE
                    Player.STATE_ENDED, Player.STATE_IDLE -> mediaPlayIb.visibility = View.VISIBLE
                }
            }
        })
    } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_media)
        val view = findViewById<StyledPlayerView>(R.id.playerView) //kotlin升级到1.4.20以后可以不用这样写
        view.player = player
        mediaPlayIb.setOnClickListener {
            view.dispatchMediaKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY))
        }
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let { playUri(it) }
        }.launch("video/*")
    }

    private fun playUri(uri: Uri) {
        val mediaItem = MediaItem.fromUri(uri)
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
}