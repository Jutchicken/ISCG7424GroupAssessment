package com.example.iscg7424groupassessment.admin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class FoodImage {

    private static final int MAX_SIZE = 400;

    public static String uriToBase64(Context context, Uri uri) {
        try {
            BitmapFactory.Options bounds = new BitmapFactory.Options();
            bounds.inJustDecodeBounds = true;
            InputStream in1 = context.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(in1, null, bounds);
            if (in1 != null) in1.close();

            int sample = 1;
            while (bounds.outWidth / sample > MAX_SIZE * 2
                    || bounds.outHeight / sample > MAX_SIZE * 2) {
                sample *= 2;
            }

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sample;
            InputStream in2 = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(in2, null, options);
            if (in2 != null) in2.close();
            if (bitmap == null) return null;

            int w = bitmap.getWidth();
            int h = bitmap.getHeight();
            float scale = Math.min(1f, (float) MAX_SIZE / Math.max(w, h));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap,
                    Math.round(w * scale), Math.round(h * scale), true);

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            scaled.compress(Bitmap.CompressFormat.JPEG, 70, out);
            return Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP);
        } catch (Exception e) {
            return null;
        }
    }

    public static Bitmap base64ToBitmap(String base64) {
        try {
            byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            return null;
        }
    }
}