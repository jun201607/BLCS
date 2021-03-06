package blcs.lwb.lwbtool.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.text.TextUtils;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 * TODO 图片处理工具类
 * 1、Bitmap转化为Drawable
 * 2、Drawable转化为Bitmap
 * 3、获得Resources的Bitmap资源
 * 4、byte[] 字节 吗转为 Bitmap
 * 5、Bitmap 转为字节码 byte[]
 * 6、通过文件路径获取到bitmap
 * 7、压缩图片
 * 8、缩放/裁剪图片
 * 9、获得本地的图片
 * 10、根据资源id获取指定大小的Bitmap对象
 * 11、根据文件路径获取指定大小的Bitmap对象
 * 12、获取指定大小的Bitmap对象
 * 13、将压缩的bitmap保存到SDCard卡临时文件夹，用于上传
 */
public class BitmapUtils
{
	/**
	 * 1、Bitmap转化为Drawable
	 */
	public static Drawable bitmap2Drawable(Bitmap bmp)
	{
		BitmapDrawable bd = new BitmapDrawable(bmp);
		return bd;
	}

	/**
	 * 2、Drawable转化为Bitmap
	 */
	public static Bitmap drawable2Bitmap(Drawable d)
	{
		BitmapDrawable bd = (BitmapDrawable) d;
		Bitmap bm = bd.getBitmap();
		return bm;
	}

	/**
	 * 3、获得Resources的Bitmap资源
	 */
	public static Bitmap getBitmapFromResources(Activity act, int resId)
	{
		Resources res = act.getResources();
		return BitmapFactory.decodeResource(res, resId);
	}

	/**
	 * 4、byte[]字节码转为Bitmap
	 */
	public static Bitmap convertBytes2Bimap(byte[] b)
	{
		if (b.length == 0)
		{
			return null;
		}
		return BitmapFactory.decodeByteArray(b, 0, b.length);
	}

	/**
	 * 5、Bitmap转为字节码byte[]
	 */
	public static byte[] convertBitmap2Bytes(Bitmap bm)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}



	/**
	 * 7、压缩图片
	 * @param srcPath 图片路径
	 * @param width 压缩的宽
	 * @param height 压缩的高
	 */
	public static Bitmap compressImageFromFile(String srcPath, int width, int height)
	{
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		newOpts.inJustDecodeBounds = true;// 只读边,不读内容
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		newOpts.inJustDecodeBounds = false;
		// 读取出图片实际的宽高
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		int be = 1;
		if (w > h && w > width)
		{
			be = (int) (w / width);
		}
		else if (w < h && h > height)
		{
			be = (int) (h / height);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置压缩比例
		newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
		newOpts.inPurgeable = true;// 同时设置才会有效
		newOpts.inInputShareable = true;// 当系统内存不够时候图片自动被回收
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}

	/**
	 *  8、缩放/裁剪图片
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight)
	{
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		return newbm;
	}

	/**
	 *  9、获得本地的图片
	 * @param url
	 * @return
	 */
	public static Bitmap getLoacalBitmap(String url)
	{

		try
		{
			FileInputStream fis = new FileInputStream(url);
			return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片

		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
	}



	/**
	 *  10、根据资源id获取指定大小的Bitmap对象
	 * @param context   应用程序上下文
	 * @param id        资源id
	 * @param height    高度
	 * @param width     宽度
	 * @return
	 */
	public static Bitmap getBitmapFromResource(Context context, int id, int height, int width){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;//只读取图片，不加载到内存中
		BitmapFactory.decodeResource(context.getResources(), id, options);
		options.inSampleSize = calculateSampleSize(height, width, options);
		options.inJustDecodeBounds = false;//加载到内存中
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id, options);
		return bitmap;
	}
	/**
	 *  11、根据文件路径获取指定大小的Bitmap对象
	 * @param path      文件路径
	 * @param height    高度
	 * @param width     宽度
	 * @return
	 */
	public static Bitmap getBitmapFromFile(String path, int height, int width){
		if (TextUtils.isEmpty(path)) {
			throw new IllegalArgumentException("参数为空，请检查你选择的路径:" + path);
		}
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;//只读取图片，不加载到内存中
		BitmapFactory.decodeFile(path, options);
		options.inSampleSize = calculateSampleSize(height, width, options);
		options.inJustDecodeBounds = false;//加载到内存中
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		return bitmap;
	}
	/**
	 *  12、获取指定大小的Bitmap对象
	 * @param bitmap    Bitmap对象
	 * @param height    高度
	 * @param width     宽度
	 * @return
	 */
	public static Bitmap getThumbnailsBitmap(Bitmap bitmap, int height, int width){
		if (bitmap == null) {
			throw new IllegalArgumentException("图片为空，请检查你的参数");
		}
		return ThumbnailUtils.extractThumbnail(bitmap, width, height);
	}

	/**
	 * 计算所需图片的缩放比例
	 * @param height
	 * @param width
	 * @param options
	 * @return
	 */
	private static int calculateSampleSize(int height, int width, BitmapFactory.Options options){
		int realHeight = options.outHeight;
		int realWidth = options.outWidth;
		int heigthScale = realHeight / height;
		int widthScale = realWidth / width;
		if(widthScale > heigthScale){
			return widthScale;
		}else{
			return heigthScale;
		}
	}

	/**
	 * 13、将压缩的bitmap保存到SDCard卡临时文件夹，用于上传
	 * @param bit
	 * @param scale 压缩大小为该控件大小的的N倍，主要用于放大后不失真
	 * @return
	 */
	public static File saveMyBitmap(String path, String picName, String bit, int scale) {
        FileOutputStream fOut = null;
		File file=new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		final File f = new File(bit, picName);
        Bitmap bitmap = BitmapFactory.decodeFile(bit);
        try {
			fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return f;
	}
}
