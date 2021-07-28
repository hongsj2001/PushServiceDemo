package org.techtown.pushservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var data : String
    val list = ArrayList<String>()

    //val pushintent = Intent(applicationContext,PushMessageService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pushintent = Intent(applicationContext, PushMessageService::class.java)
        startService(pushintent)


        /*val database = Firebase.database
        val myRef = database.getReference("receive")
        /*myRef.get().addOnSuccessListener {
            val key = it.child("receive").getValue() as HashMap<String, Any>
            val receive = key.get("receive").toString()
        }*/
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //data = dataSnapshot.getValue().toString()!!
                list.add(dataSnapshot.getValue().toString()!!)
                if (list[0] in "0") {
                    ShowNotification("택배요", "넹")
                } else {
                    ShowNotification("나는", "사실도둑")
                }

                /*if(data in "0"){
                    ShowNotification("값","0")
                }
                else{
                    ShowNotification("값","1")
                }*/

            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        myRef.addValueEventListener(postListener)*/
        //push()

        button.setOnClickListener {
            if(data in "0"){
                ShowNotification("값","0")
            }
            else{
                ShowNotification("값","1")
            }
        }
    }

    fun ShowNotification(Title: String, Body: String) {
        val pending = PendingIntent.getActivity(
            this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val builder = NotificationCompat.Builder(this, "id")

        builder.setSmallIcon(com.google.firebase.database.R.drawable.notification_icon_background)
            .setContentTitle(Title)
            .setContentText(Body)
            .setContentIntent(pending)
            .setAutoCancel(true)

        val NManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NManager.createNotificationChannel(
                NotificationChannel(
                    "id", "name",
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
        NManager.notify(0, builder.build())
    }

    fun push() {
        val list = ArrayList<String>()
        val database = Firebase.database
        val myRef = database.getReference("receive")
        /*myRef.get().addOnSuccessListener {
            val key = it.child("receive").getValue() as HashMap<String, Any>
            val receive = key.get("receive").toString()
        }*/
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                data = dataSnapshot.getValue().toString()!!
                if(data in "0"){
                    ShowNotification("값","0")
                }
                else{
                    ShowNotification("값","1")
                }

            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        myRef.addValueEventListener(postListener)
    }
}