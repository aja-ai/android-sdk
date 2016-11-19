package ai.aja.sdk.widget.html;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class HtmlRenderingFuture implements Future<Bitmap> {

    private final CountDownLatch signal;

    private volatile Bitmap result = null;

    private volatile boolean cancelled = false;

    public HtmlRenderingFuture() {
        signal = new CountDownLatch(1);
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (isDone()) {
            return false;
        } else {
            signal.countDown();
            cancelled = true;
            return !isDone();
        }
    }

    public void done(Bitmap result) {
        this.result = result;
        signal.countDown();
    }

    @Override
    public Bitmap get() throws InterruptedException, ExecutionException {
        signal.await();
        return result;
    }

    @Override
    public Bitmap get(long timeout, @NonNull TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        signal.await(timeout, unit);
        return result;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public boolean isDone() {
        return signal.getCount() == 0;
    }

}
