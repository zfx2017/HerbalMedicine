package com.example.ifish.test;
        import android.content.Context;
        import android.content.IntentFilter;
        import android.media.Ringtone;
        import android.media.RingtoneManager;
        import android.net.ConnectivityManager;
        import android.net.Uri;
        import android.os.SystemClock;
        import android.os.Vibrator;
        import android.support.annotation.Nullable;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Chronometer;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.ifish.test.receiver.NetWorkStateReceiver;
        import com.example.ifish.test.utils.Config;

        import java.util.Locale;
        import java.util.Timer;
        import java.util.TimerTask;
public class MainActivity extends AppCompatActivity {
    private Chronometer timer;
    private Timer timer1;
    private TextView textView;
    private TimerTask timerTask;
    private EditText editText;
    NetWorkStateReceiver netWorkStateReceiver;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timer = (Chronometer) findViewById(R.id.timer);
        textView = (TextView) findViewById(R.id.text);
        editText = (EditText)findViewById(R.id.editText);
        timer1 = new Timer();

    }
    public void btnClick(View view) {
        timer.setBase(SystemClock.elapsedRealtime());//计时器清零
        int hour = (int) ((SystemClock.elapsedRealtime() - timer.getBase()) / 1000 / 60);
        timer.setFormat("0"+String.valueOf(hour)+":%s");
        timer.start();
    }
    public void stopClick(View view) {
        timer.stop();
    }
    public void startClick(View view) {
     if(Config.getNETWORK()==0)
     {
         Toast.makeText(this, "无网络连接",Toast.LENGTH_SHORT).show();
     }else {
         timerTask = new TimerTask() {
             int min = Integer.parseInt(editText.getText().toString());
             int cnt = min * 60;

             @Override
             public void run() {
                 runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         textView.setText(getStringTime(cnt--));
                         if (cnt == 860) {
                             startAlarm(getApplicationContext());
                             Vibrator();
                         }
                     }
                 });
             }
         };
         timer1.schedule(timerTask, 0, 1000);
     }

    }
    private String getStringTime(int cnt) {
        int hour = cnt/3600;
        int min = cnt % 3600 / 60;
        int second = cnt % 60;
        return String.format(Locale.CHINA,"%02d:%02d:%02d",hour,min,second);
    }
    public void stopClick1(View view) {
        if (!timerTask.cancel()){
            timerTask.cancel();
            timer1.cancel();
        }
    }
    private void Vibrator() {
        Vibrator vibrator = (Vibrator)this.getSystemService(this.VIBRATOR_SERVICE);
        long[] patter = {1000, 1000, 2000, 50};
        vibrator.vibrate(patter, 0);
    }
    private static void startAlarm(Context context) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (notification == null) return;
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }
    protected void onResume() {
        if (netWorkStateReceiver == null) {
            netWorkStateReceiver = new NetWorkStateReceiver();
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);
        System.out.println("注册");
        super.onResume();
    }

    //onPause()方法注销
    @Override
    protected void onPause() {
        unregisterReceiver(netWorkStateReceiver);
        System.out.println("注销");
        super.onPause();
    }
}
