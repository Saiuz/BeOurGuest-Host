package com.lalit.hostbarcode;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class MyService extends Service {
    PendingIntent contentIntent;
    NotificationCompat.Builder mBuilder;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    NotificationManager mNotificationManager;
    String st;
    ArrayList<String>mnotes = new ArrayList<>();
    ArrayList<String>mnotes2 = new ArrayList<>();
    String date,time,table_no;
    long hj;
    String ara1[];
    String ara2[];
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        /*  Create and initiaise all the firebase objects used to display notification
         */
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            sp=getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            st = sp.getString("name"," ");
            editor = sp.edit();
//            Firebase.setAndroidContext(this);
//            Firebase mref=new Firebase("https://be-our-guest-host.firebaseio.com/"+st+"/orders/");
        DatabaseReference mref = database.getReference(st+"/orders/");
            mref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    String value = dataSnapshot.getValue(String.class);
                    String arr[] = value.split("_xzy@#@acb_");
                    String arr1[] = arr[0].split("-");
                    String daa=arr1[2]+arr1[1]+arr1[0];

                    if(arr1[0].length()==1){
                        daa= arr1[2]+arr1[1]+"0"+arr1[0];
                    }
                    if(arr1[1].length()==1){
                        daa= arr1[2]+"0"+arr1[1]+arr1[0];
                    }
                    if(arr1[0].length()==1&&arr1[1].length()==1){
                        daa= arr1[2]+"0"+arr1[1]+"0"+arr1[0];
                    }


                    String arr2[] = arr[1].split(":");
                    String tii= arr2[0]+arr2[1]+arr2[2];

                    if(arr2[0].length()==1){
                        tii= "0"+arr2[0]+arr2[1]+arr2[2];
                    }
                    if(arr2[1].length()==1){
                        tii= arr2[0]+"0"+arr2[1]+arr2[2];
                    }
                    if(arr2[2].length()==1){
                        tii= arr2[0]+arr2[1]+"0"+arr2[2];
                    }
                    if(arr2[0].length()==1&&arr2[1].length()==1){
                        tii= "0"+arr2[0]+"0"+arr2[1]+arr2[2];
                    }
                    if(arr2[0].length()==1&&arr2[2].length()==1){
                        tii= "0"+arr2[0]+arr2[1]+"0"+arr2[2];
                    }

                    if(arr2[1].length()==1&&arr2[2].length()==1){
                        tii= arr2[0]+"0"+arr2[1]+"0"+arr2[2];
                    }
                    if(arr2[1].length()==1&&arr2[2].length()==1&&arr2[0].length()==1){
                        tii= "0"+arr2[0]+"0"+arr2[1]+"0"+arr2[2];
                    }
                    if(arr[5].length()==1)
                    {
                        arr[5] = "0"+arr[5];
                    }
                    String fs = daa+tii+arr[5];
                    //   mnotes.add(fs);
                    //  mnotes2.add(arr[0]+"bbb"+arr[1]+"bbb"+arr[5]);
                    long x=Long.parseLong(fs);
                    hj = sp.getLong("hj",0);
                    if(hj<x)
                    {
                        contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                                new Intent(getApplicationContext(), ViewOrder.class), 0);

        /* Change the values of setSmallIcon, setContentTitle and setContentText
                which should be selected to display in notification.
         */
                        String e ="Date : "+arr[0]+"  Time : "+arr[1]+"\nCustomer Name : "+arr[2]+"\nKindly check the application for more info.";
                        mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.noti2)
                                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.l))
                                .setStyle(new NotificationCompat.BigTextStyle().bigText(e))
                                .setContentTitle("New order arrived from table "+arr[5])
                                .setContentText(e);
                        mBuilder.setContentIntent(contentIntent);
                        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                        mBuilder.setAutoCancel(true);
                        mNotificationManager =
                                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                        mNotificationManager.notify(1, mBuilder.build());
                        editor.putLong("hj",x);
                        editor.commit();
                    }
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
     //   ara1 = mnotes.toArray(new String[mnotes.size()]);
       // ara2 = mnotes2.toArray(new String[mnotes2.size()]);

        /* Edit contentIntent object to select the intent to be displayed
                when user clicks on notification.
                Here, my intent to be opened is SixthActivity
         */
        return START_STICKY;
    }
}
