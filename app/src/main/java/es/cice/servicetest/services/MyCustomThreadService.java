package es.cice.servicetest.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import es.cice.servicetest.R;
import es.cice.servicetest.TestNotificationActivity;

public class MyCustomThreadService extends Service {

    private static final String HANDLER_THREAD_NAME="MyCustomThread";
    private MyCustomHandler mHandler;
    public static final int CUSTOM_WHAT=1;
    private static final String TAG=MyCustomThreadService.class.getCanonicalName();
    public MyCustomThreadService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //cada vez que se llame a este método queremos que el trabajo se haga en el hilo
        Message msg=mHandler.obtainMessage(CUSTOM_WHAT);
        Log.d(TAG, "[Thread: " + Thread.currentThread().getId()+ "]" + "onStarCommand");
        mHandler.sendMessage(msg);
        return START_NOT_STICKY;
    }

    @Override
    //para la configuración inicial???
    public void onCreate() {
        super.onCreate();
        //creamos un hilo q tiene una cola de mensajes
        HandlerThread mThread=new HandlerThread(HANDLER_THREAD_NAME);
        //lanzar el hilo
        mThread.start();
        //construimos el looper asociado al hilo y luego creamos el handler con el looper asociado al hilo
        mHandler=new MyCustomHandler(mThread.getLooper());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public class MyCustomHandler extends Handler {
        //a través del looper queda asociado el handler con el hilo
        public MyCustomHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CUSTOM_WHAT:
                    Log.d(TAG, "[Thread: " + Thread.currentThread().getId()+ "]" +" handleMessage...");
                    for(int i=0; i<10; i++){

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "[Thread: " + Thread.currentThread().getId()+ "]" +
                                " MyCustomThreadService ejecutándose....");

                    }
                    //creamos un intext explícito
                    Intent i=new Intent(getApplicationContext(), TestNotificationActivity.class);
                    PendingIntent pIntent = PendingIntent.getActivity(getApplicationContext(),1,i,0);
                    Notification.Builder builder= new Notification.Builder(getApplicationContext());
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

                    break;
            }
        }
    }

}
