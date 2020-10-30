/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.media

import android.app.Notification
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.exoplayer2.scheduler.PlatformScheduler
import com.google.android.exoplayer2.ui.DownloadNotificationHelper
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.NoOpCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import java.io.File
import java.util.concurrent.Executors

private const val JOB_ID = 1
private const val FOREGROUND_NOTIFICATION_ID = 1
private const val DOWNLOAD_NOTIFICATION_CHANNEL_ID = "download_channel"
private const val CHANNEL_DESCRIPTION_RESOURCE_ID = 0

class ExoDownloadService : DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    DOWNLOAD_NOTIFICATION_CHANNEL_ID,
    R.string.notification_channel_name,
    CHANNEL_DESCRIPTION_RESOURCE_ID
) {


    override fun getDownloadManager(): DownloadManager {
        val databaseProvider = ExoDatabaseProvider(this)
        val downloadCache = SimpleCache(
            File(filesDir, "download_exo"),
            NoOpCacheEvictor(),
            databaseProvider
        )

        val dataSourceFactory = DefaultHttpDataSourceFactory()
        val downloadExecutor = Executors.newFixedThreadPool( /* nThreads= */6)
        return DownloadManager(
            this,
            databaseProvider,
            downloadCache,
            dataSourceFactory,
            downloadExecutor
        )
    }

    override fun getScheduler(): PlatformScheduler? {
        return PlatformScheduler(this, JOB_ID)
    }

    override fun getForegroundNotification(downloads: MutableList<Download>): Notification {
        return DownloadNotificationHelper(this, DOWNLOAD_NOTIFICATION_CHANNEL_ID)
            .buildProgressNotification(this, 0, null, null, downloads)
    }

}