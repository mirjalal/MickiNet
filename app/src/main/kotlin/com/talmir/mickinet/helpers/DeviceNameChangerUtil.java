package com.talmir.mickinet.helpers;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static android.os.Looper.getMainLooper;

/**
 * A class helps to change name (WifiDirect name, Bluetooth name)
 * of the device. Since, Kotlin implementation of the following
 * lines is quite possible. However, during Kotlin code execution
 * I got a cryptic error. To reduce searching time for that error
 * and also to keep the {{@link com.talmir.mickinet.screens.main.fragments.DeviceDetailFragment}}
 * class a bit more clean, I decided to keep that codes in Java...
 *
 * @author mirjalal
 * @since Jul 30, 2018
 */
public final class DeviceNameChangerUtil {
    /**
     * Changes device name using Java Reflection API explicitly.
     * Because of, Android have not gave any option to achieve
     * this functionality, I used this method to achieve it.
     *
     * @param activity current activity object
     * @param newName  new device name to be set
     * @return `true` if newName was set successfully, `false` otherwise.
     */
    public static boolean changeDeviceName(@NonNull FragmentActivity activity, @NonNull String newName) {
        final WifiP2pManager[] mManager = new WifiP2pManager[1];
        WifiP2pManager.Channel channel;
        try {
            mManager[0] = (WifiP2pManager) activity.getSystemService(Context.WIFI_P2P_SERVICE);
            assert mManager[0] != null;

            channel = mManager[0].initialize(
                    activity,
                    getMainLooper(),
                    () -> {}
            );

            Class[] paramTypes = new Class[3];
            paramTypes[0] = WifiP2pManager.Channel.class;
            paramTypes[1] = String.class;
            paramTypes[2] = WifiP2pManager.ActionListener.class;
            Method setDeviceName = mManager[0].getClass().getMethod("setDeviceName", paramTypes);
            setDeviceName.setAccessible(true);

            Object[] argList = new Object[3];
            argList[0] = channel;
            argList[1] = newName;
            argList[2] = new WifiP2pManager.ActionListener() {
                @Override
                public void onSuccess() {
                    // nothing to do here
                }

                @Override
                public void onFailure(int reason) {
                    // nothing to do in this method
                }
            };
            setDeviceName.invoke(mManager[0], argList);
            return true;
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            return false;
        }
    }
}
