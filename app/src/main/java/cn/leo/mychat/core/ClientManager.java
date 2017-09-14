package cn.leo.mychat.core;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import java.util.ArrayList;
import java.util.List;


public class ClientManager implements ClientListener {
    public static final int STATUS_ONLINE = 1;
    public static final int STATUS_OFFLINE = 0;
    private int status;
    private static String ip;
    private static int port;
    private ClientCore client;
    private List<ClientListener> mListeners = new ArrayList<>();
    private Handler mHandler = new Handler(Looper.getMainLooper());//切换线程

    public ClientManager(String ip, int port) {
        this.ip = ip;
        this.port = port;
        client = ClientCore.startClient(ip, port, this); //连接服务器
    }

    public void send(byte[] bytes) {
        client.sendMsg(bytes);
    }

    @Override
    public void onIntercept() {
        status = STATUS_OFFLINE;
        SystemClock.sleep(5000);//中断5秒后重连
        client = ClientCore.startClient(ip, port, this);// 重新连接服务器
        for (final ClientListener listener : mListeners) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onIntercept();
                }
            });
        }
    }

    @Override
    public void onDataArrived(final byte[] data) {
        status = STATUS_ONLINE;
        for (final ClientListener listener : mListeners) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onDataArrived(data);
                }
            });
        }
    }

    @Override
    public void onConnectSuccess() {
        status = STATUS_ONLINE;
        for (final ClientListener listener : mListeners) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onConnectSuccess();
                }
            });
        }
    }

    @Override
    public void onConnectFailed() {
        status = STATUS_OFFLINE;
        SystemClock.sleep(5000);//中断5秒后重连
        client = ClientCore.startClient(ip, port, this);// 重新连接服务器
        for (final ClientListener listener : mListeners) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onConnectFailed();
                }
            });
        }
    }

    public int getStatus() {
        return status;
    }

    public void addListener(ClientListener listener) {
        if (!mListeners.contains(listener))
            mListeners.add(listener);
    }

    //观察者模式，一定要移除对象，可能会造成内存泄漏
    public void removeListener(ClientListener listener) {
        mListeners.remove(listener);
    }

}
