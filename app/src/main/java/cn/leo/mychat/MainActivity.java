package cn.leo.mychat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.leo.mychat.core.Client;
import cn.leo.mychat.core.ClientListener;

public class MainActivity extends AppCompatActivity implements ClientListener, View.OnClickListener {

    private TextView mTv_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTv_code = (TextView) findViewById(R.id.tv_code);
        mTv_code.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "连接状态" + Client.getConnectStatus(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectSuccess() {
        Toast.makeText(this, "连接服务器成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectFailed() {
        Toast.makeText(this, "连接服务器失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onIntercept() {
        Toast.makeText(this, "连接断开", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataArrived(byte[] data) {
        //收到消息
        String msg = new String(data);
        mTv_code.setText(msg);
    }

    @Override
    public void onClick(View v) {
        Client.sendMsg("123".getBytes());
    }
}
