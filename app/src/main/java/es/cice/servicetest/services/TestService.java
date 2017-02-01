package es.cice.servicetest.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import es.cice.servicetest.R;
import es.cice.servicetest.TestNotificationActivity;

public class TestService extends Service {

    private String TAG ="TestService";
    public TestService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //intent que hace que el servicio arranque, primer parámetro
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "[Thread: " + Thread.currentThread().getId()+ "]" +"onsStartCommand...");
        for(int i=0; i<10; i++){

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "[Thread: " + Thread.currentThread().getId()+ "]" +"TestService ejecutándose....");

        }
        //creamos un intext explícito
        Intent i=new Intent(getApplicationContext(), TestNotificationActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(),1,i,0);
        Notification.Builder builder= new Notification.Builder(this);
        //configuramos el builder
        builder.setSmallIcon(R.drawable.ic_test_notification)
                .setContentTitle("testIntentService")
                .setContentIntent(pIntent)
                .setContentText("test Intent Service finished...");

        //construimos una notificacion
        Notification notification=builder.build();
        //la enviamos, usando un servicio del sistema
        NotificationManager nm= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(11111, notification);   //11111es un identificador

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
