package org.techtown.pushservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.database.*
import com.google.firebase.database.R
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PushMessageService : Service() {
    lateinit var data : String

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        push()
        return Service.START_REDELIVER_INTENT

    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
    fun ShowNotification(Title:String , Body:String){
        val pending = PendingIntent.getActivity(this,0, Intent(this,MainActivity::class.java),
            PendingIntent.FLAG_CANCEL_CURRENT)
        val builder = NotificationCompat.Builder(this,"id")

        builder.setSmallIcon(R.drawable.notification_icon_background)
            .setContentTitle(Title)
            .setContentText(Body)
            .setContentIntent(pending)
            .setAutoCancel(true)

        val NManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NManager.createNotificationChannel(
                NotificationChannel("id","name",
                    NotificationManager.IMPORTANCE_HIGH)
            )
        }
        NManager.notify(0,builder.build())
    }

    fun push() {
        val database = Firebase.database
        val myRef = database.getReference("receive")
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                data = dataSnapshot.getValue().toString()!!
                if(data in "0"){
                    ShowNotification("택배","나는 도둑")
                    myRef.setValue(2)
                }
                else if(data in "1"){
                    ShowNotification("택배","나는 택배왕")
                    myRef.setValue(2)

                }
            }
            override fun onCancelled(error: DatabaseError){

            }

        }
        myRef.addValueEventListener(postListener)
    }
}