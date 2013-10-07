package com.klinker.android.messaging_sliding.slide_over;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.*;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.klinker.android.messaging_donate.R;
import com.klinker.android.messaging_donate.utils.ContactUtil;

public class HaloView extends ViewGroup {
    public Context mContext;

    public Bitmap halo;

    public Paint haloPaint;
    public Paint haloNewPaint;
    public int haloAlpha = 255;
    public int haloNewAlpha = 0;

    public int haloColor;
    public int haloUnreadColor;

    public boolean animating = false;

    public SharedPreferences sharedPrefs;

    public int height;
    public int width;

    public HaloView(Context context) {
        super(context);

        mContext = context;
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);

        halo = BitmapFactory.decodeResource(getResources(),
                R.drawable.halo_bg);

        haloPaint = new Paint();
        haloPaint.setAlpha(haloAlpha);

        haloNewPaint = new Paint();
        haloNewPaint.setAlpha(haloNewAlpha);

        haloColor = sharedPrefs.getInt("slideover_color", context.getResources().getColor(R.color.white));
        haloUnreadColor = sharedPrefs.getInt("slideover_unread_color", context.getResources().getColor(R.color.holo_red));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Display d = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        height = d.getHeight();
        width = d.getWidth();

        if (haloAlpha != 0) {
            haloPaint.setAlpha(haloAlpha);
            haloPaint.setColorFilter(new PorterDuffColorFilter(haloColor, PorterDuff.Mode.MULTIPLY));
            canvas.drawBitmap(halo, 0, 0, haloPaint);
        }

        if (haloNewAlpha != 0) {
            haloNewPaint.setAlpha(haloNewAlpha);
            haloNewPaint.setColorFilter(new PorterDuffColorFilter(haloUnreadColor, PorterDuff.Mode.MULTIPLY));

            if (sharedPrefs.getBoolean("contact_pics_slideover", true)) {
                canvas.drawBitmap(halo, 0, 0, haloPaint);
                Paint contactPaint = new Paint();
                contactPaint.setAlpha(210);
                canvas.drawBitmap(getClip(), 0, 0, contactPaint);
            } else {
                canvas.drawBitmap(halo, 0, 0, haloNewPaint);
            }
        }
    }

    public Bitmap getClip()
    {
        String number = ArcView.newConversations.get(ArcView.newConversations.size() - 1)[2];
        Bitmap bitmap = Bitmap.createScaledBitmap(ContactUtil.getFacebookPhoto(number, mContext), halo.getWidth(), halo.getHeight(), false);


        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2,
                bitmap.getHeight() / 2, (bitmap.getWidth() / 2) - (bitmap.getWidth()/25), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
