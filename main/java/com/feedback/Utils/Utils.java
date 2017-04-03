package com.feedback.Utils;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.feedback.FeedbackApplication;
import com.feedback.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class Utils {

    private MaterialDialog progressDialog;
    private Activity mActivity;

    public Utils(Activity activity) {
        this.mActivity = activity;
    }

    public static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9+._%-+]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
                    "(" +
                    "." +
                    "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
                    ")+"
    );

    public static boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }


    public static void alertBox(Context context, String msg) {
        new MaterialDialog.Builder(context)
                .content(msg)
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.black)
                .positiveColorRes(R.color.colorPrimaryDark)
                .positiveText("OK")
                .show();
    }

    public static void alertBox(Context context, int id) {
        new MaterialDialog.Builder(context)
                .content(context.getResources().getString(id))
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.black)
                .positiveColorRes(R.color.colorPrimaryDark)
                .positiveText("OK")
                .show();
    }

    public void showProgress() {
        progressDialog = new MaterialDialog.Builder(mActivity)
                .backgroundColorRes(R.color.white)
                .contentColorRes(R.color.black)
                .content("loading..")
                .progress(true, 0)
                .show();
        progressDialog.setCanceledOnTouchOutside(false);
    }

    public void hideProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public static String fileToBase64(File file) {
        try {
            InputStream inputStream = null;
            inputStream = new FileInputStream(file.getAbsolutePath());
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output64.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            output64.close();

            return output.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getCurrentDateTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateTime = sdf.format(new Date());
        return dateTime;
    }

    public static String compressImage(File file) {

        String filePath = getRealPathFromURI(file.toString());
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    private static String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "Feedback/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private static String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = FeedbackApplication.getInstance().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public static int get_count_of_days(String dateFrom, String dateTo, String format) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());

            Date Created_convertedDate = null, Expire_CovertedDate = null, todayWithZeroTime = null;
            try {
                Created_convertedDate = dateFormat.parse(dateFrom);
                Expire_CovertedDate = dateFormat.parse(dateTo);

                Date today = new Date();

                todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int c_year = 0, c_month = 0, c_day = 0;

            if (Created_convertedDate.after(todayWithZeroTime)) {
                Calendar c_cal = Calendar.getInstance();
                c_cal.setTime(Created_convertedDate);
                c_year = c_cal.get(Calendar.YEAR);
                c_month = c_cal.get(Calendar.MONTH);
                c_day = c_cal.get(Calendar.DAY_OF_MONTH);

            } else {
                Calendar c_cal = Calendar.getInstance();
                c_cal.setTime(todayWithZeroTime);
                c_year = c_cal.get(Calendar.YEAR);
                c_month = c_cal.get(Calendar.MONTH);
                c_day = c_cal.get(Calendar.DAY_OF_MONTH);
            }


    /*Calendar today_cal = Calendar.getInstance();
    int today_year = today_cal.get(Calendar.YEAR);
    int today = today_cal.get(Calendar.MONTH);
    int today_day = today_cal.get(Calendar.DAY_OF_MONTH);
    */

            Calendar e_cal = Calendar.getInstance();
            e_cal.setTime(Expire_CovertedDate);

            int e_year = e_cal.get(Calendar.YEAR);
            int e_month = e_cal.get(Calendar.MONTH);
            int e_day = e_cal.get(Calendar.DAY_OF_MONTH);

            Calendar date1 = Calendar.getInstance();
            Calendar date2 = Calendar.getInstance();

            date1.clear();
            date1.set(c_year, c_month, c_day);
            date2.clear();
            date2.set(e_year, e_month, e_day);

            long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

            float dayCount = (float) diff / (24 * 60 * 60 * 1000);
            return (int) dayCount*-1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


}