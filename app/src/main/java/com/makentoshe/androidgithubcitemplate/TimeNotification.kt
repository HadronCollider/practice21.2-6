package com.makentoshe.androidgithubcitemplate

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.NotificationCompat


public class TimeNotification : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "noti234", Toast.LENGTH_SHORT).show()
        var txt = ""
        if (intent!!.hasExtra("noti_text")){
            txt = intent!!.getStringExtra("noti_text")
        }
        val intentTL = Intent(context, MainActivity::class.java)
        intentTL.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, intentTL, 0)
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(context!!, "CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Time to Learn smt!")
                .setContentText(txt)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
        val notification = builder.build()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }
}