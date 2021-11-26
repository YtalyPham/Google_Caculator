package com.example.ongk_caculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.ViewCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import de.hdodenhof.circleimageview.CircleImageView;

public class CaculatorscreenActivity extends AppCompatActivity {
    Button btnDiv,btnAdd,btnSub,btnMulti,btnBack,btnLogout,btnClear;
    EditText txtA,txtB;
    TextView txtResult;
    GoogleSignInClient mGoogleSignInClient;
    private ServiceConnection serviceConnection;
    private boolean isConnected;
    private MyService myService;
    private NotificationManagerCompat notificationManager;
    CircleImageView imgHinh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caculatorscreen);

        notificationManager= NotificationManagerCompat.from(this);
        btnBack=findViewById(R.id.btnBack);
        btnLogout=findViewById(R.id.btnLogout);
        imgHinh=findViewById(R.id.imgHinh);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAnimation();
                switch (view.getId()) {
                    case R.id.btnLogout:
                        signOut();
                        break;
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAnimation();
                Intent intent = new Intent(CaculatorscreenActivity.this,MainActivity.class);

                ActivityOptionsCompat optionsCompat=ActivityOptionsCompat.makeSceneTransitionAnimation(
                        CaculatorscreenActivity.this,btnBack,
                        ViewCompat.getTransitionName(btnBack)
                );
                startActivity(intent, optionsCompat.toBundle());
            }
        });

        initView();
        connectService();

    }
    private void connectService() {

        Intent intent = new Intent(this, MyService.class);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MyService.MyBinder myBinder = (MyService.MyBinder) service;

                myService = myBinder.getService();
                isConnected = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                isConnected = false;
                myService = null;
            }
        };
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    void initView()
    {
        btnDiv=findViewById(R.id.btnDiv);
        btnAdd=findViewById(R.id.btnAdd);
        btnMulti=findViewById(R.id.btnMulti);
        btnSub=findViewById(R.id.btnSub);
        btnClear=findViewById(R.id.btnClear);
        txtA = (EditText) findViewById(R.id.txtNumA);
        txtB = (EditText) findViewById(R.id.txtNumB);
        txtResult= (TextView) findViewById(R.id.txtResult);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAnimation();
                startAnimation();
                if(!isConnected){
                    return;
                }
                int result = myService.add(
                        Integer.parseInt(txtA.getText().toString()),
                        Integer.parseInt(txtB.getText().toString()));
                txtResult.setText(txtA.getText()+" + "+txtB.getText()+" = "+result);
                txtA.setText("");
                txtB.setText("");
                Toast.makeText(myService, "Result:" + result, Toast.LENGTH_SHORT).show();
            }
        });

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAnimation();
                startAnimation();
                if(!isConnected){
                    return;
                }

                int result = myService.sub(
                        Integer.parseInt(txtA.getText().toString()),
                        Integer.parseInt(txtB.getText().toString()));
                txtResult.setText(txtA.getText()+" - "+txtB.getText()+" = "+result);
                txtA.setText("");
                txtB.setText("");
                Toast.makeText(myService, "Result:" + result, Toast.LENGTH_SHORT).show();

            }
        });

        btnDiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAnimation();
                startAnimation();
                if(!isConnected){
                    return;
                }

                int result = myService.div(
                        Integer.parseInt(txtA.getText().toString()),
                        Integer.parseInt(txtB.getText().toString()));
                txtResult.setText(txtA.getText()+" : "+txtB.getText()+" = "+result);
                txtA.setText("");
                txtB.setText("");
                Toast.makeText(myService, "Result:" + result, Toast.LENGTH_SHORT).show();

            }
        });

        btnMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopAnimation();
                startAnimation();
                if(!isConnected){
                    return;
                }

                int result = myService.multi(
                        Integer.parseInt(txtA.getText().toString()),
                        Integer.parseInt(txtB.getText().toString()));
                txtResult.setText(txtA.getText()+" * "+txtB.getText()+" = "+result);
                txtA.setText("");
                txtB.setText("");
                Toast.makeText(myService, "Result:" + result, Toast.LENGTH_SHORT).show();

            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAnimation();
                startAnimation();
                txtA.setText("");
                txtB.setText("");
                txtResult.setText("");
            }
        });
    }
    public void sendOnChanel1(View view){
        String title = "Caculator";
        txtResult= (TextView) findViewById(R.id.txtResult);
        String message= txtResult.getText().toString();
        Notification notification= new NotificationCompat.Builder(this, App.Chanel_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1,notification);
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    public void startAnimation(){
        Runnable runnable= new Runnable() {
            @Override
            public void run() {
                imgHinh.animate().rotationBy(360).withEndAction(this).setDuration(10000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };
        imgHinh.animate().rotationBy(360).withEndAction(runnable).setDuration(10000)
                .setInterpolator(new LinearInterpolator()).start();
    }

    public void stopAnimation(){
        imgHinh.animate().cancel();
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(CaculatorscreenActivity.this,"Logout succesful",Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

}