/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.media

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player.EventListener
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.video.VideoListener
import kotlinx.android.synthetic.main.activity_video_media.*

class VideoMediaActivity: AppCompatActivity() {

    private val player by lazy { SimpleExoPlayer.Builder(this).build().apply {
        addListener(object : EventListener{
            override fun onPlaybackStateChanged(state: Int) {
                super.onPlaybackStateChanged(state)
            }

            override fun onPlayerError(error: ExoPlaybackException) {
                super.onPlayerError(error)
            }
        })
        addVideoListener(object : VideoListener{
            override fun onVideoSizeChanged(
                width: Int,
                height: Int,
                unappliedRotationDegrees: Int,
                pixelWidthHeightRatio: Float
            ) {
                super.onVideoSizeChanged(
                    width,
                    height,
                    unappliedRotationDegrees,
                    pixelWidthHeightRatio
                )
            }
        })
    } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_media)
        playerView.player = player
//        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
//
//        }.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            playUri(it)
        }.launch("video/*")
    }

    fun playUri(uri: Uri) {
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