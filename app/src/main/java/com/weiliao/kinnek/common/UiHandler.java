package com.weiliao.kinnek.common;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * A thin wrapper of {@link Handler} to run a callback in UI thread. Any callback to this handler
 * is guarantee to run inside {@link android.app.Activity} lifecycle. However, it can be dropped if the
 * {@link android.app.Activity} has been stopped. This handler is safe to use with {@link android.app.FragmentTransaction}.
 */
public class UiHandler {

    private final static String LOG_TAG = "app";

    private final Handler mHandler;
    private boolean mEnabled = true;

    public UiHandler() {
        this(null);
    }

    public UiHandler(final UiCallback uiCallback) {
        if (uiCallback != null) {
            mHandler = new Handler(Looper.getMainLooper(), msg ->
                    uiCallback.handleUiMessage(msg, msg.what, mEnabled));
        } else {
            mHandler = new Handler(Looper.getMainLooper());
        }
    }

    /**
     * @return Inner Handler Object.
     */
    public Handler getHandler() {
        return mHandler;
    }

    /**
     * Causes the Runnable r to be added to the message queue. The runnable will be run on the UI thread
     * to which this handler is attached. If the activity has been stopped, the runnable will be dropped.
     *
     * @param r The Runnable that will be executed.
     * @return Returns true if the Runnable was successfully placed in to the message queue.
     * Returns false on failure, usually because the activity has been stopped.
     * @see Handler#post(Runnable)
     */
    public boolean post(final Runnable r) {
        if (mEnabled) {
            mHandler.post(r);
        } else {
            Log.d(LOG_TAG, "UiHandler is disabled in post(). Dropping Runnable");
        }
        return mEnabled;
    }

    /**
     * Causes the Runnable r to be added to the message queue. The runnable will be run on the UI thread
     * to which this handler is attached. If the activity has been stopped, the runnable will be dropped.
     *
     * @param r           The Runnable that will be executed.
     * @param delayMillis The delay (in milliseconds) until the Runnable will be executed.
     * @return Returns true if the Runnable was successfully placed in to the message queue.
     * Returns false on failure, usually because the activity has been stopped.
     * @see Handler#postDelayed(Runnable, long)
     */
    public boolean postDelayed(final Runnable r, long delayMillis) {
        if (mEnabled) {
            mHandler.postDelayed(r, delayMillis);
        } else {
            Log.d(LOG_TAG, "UiHandler is disabled in post(). Dropping Runnable");
        }
        return mEnabled;
    }

    /**
     * Remove any pending posts of Runnable r that are in the message queue.
     *
     * @param r The Runnable that will be removed.
     */
    public void removeCallbacks(Runnable r) {
        mHandler.removeCallbacks(r);
    }

    /**
     * To check whether the {@link UiHandler} is enabled or not. <I> It's safe to edit UI
     * if the {@link UiHandler} is enabled.</I>
     *
     * @return True if {@link UiHandler} is enabled, otherwise false.
     */
    public boolean isEnabled() {
        return mEnabled;
    }

    /**
     * To enable the {@link UiHandler} or not. <I> It's safe to edit UI if the {@link UiHandler} is enabled.</I>
     * When set its to be disabled, any pending runnables will be removed.
     *
     * @param enabled Whether to enabled the {@link UiHandler}.
     */
    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
        if (!mEnabled) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * Callback to be invoked by UiHandler in UI thread.
     */
    public interface UiCallback {
        /**
         * Handle the message that handled by UiHandler.
         *
         * @param msg     The message received in UI thread.
         * @param what    Them message what member.
         * @param enabled Whether current ui is enabled or not.
         * @return true if this message is handled successfully otherwise false.
         */
        boolean handleUiMessage(Message msg, int what, boolean enabled);
    }
}