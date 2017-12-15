package com.hugoo.mplus.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by felix on 2017/12/14.
 */

public class MessageReceiver extends BroadcastReceiver {
    private static final String TAG = "mplus";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

    }

}
