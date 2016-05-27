package com.example.tutorialblurportion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class WorkTask extends AsyncTask<Void, Integer, Bitmap> {
	private Context context;
	private ImageView imageView;
	private int blurAmount;
	private Bitmap source;
	private Bitmap mask;
	private float maskPosX;
	private float maskPosY;

	private Bitmap result;

	public WorkTask(Context c, ImageView imageView, int blurAmount, Bitmap source,float maskPosX, float maskPosY, int maskSize) {
		this.context = c;
		this.imageView = imageView;
		this.blurAmount = blurAmount;
		this.source = source;
		this.mask = BitmapUtils.createRadialBlurMask(maskSize / 2f, maskSize, maskSize );
		this.maskPosX = maskPosX;
		this.maskPosY = maskPosY;
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected Bitmap doInBackground(Void... params) {
		// Get the sharp part of the picture
		
		Bitmap sharp = BitmapUtils.applyMask(source, mask, (int)maskPosX, (int)maskPosY);

		Log.e("TAG", "D3:" + sharp.getDensity());
		Log.e("TAG", "D4:" + source.getDensity());
		// Blur it twice
		Bitmap blurryBackground = BitmapUtils.renderScriptBlur(source, context, blurAmount);
		//blurryBackground = BitmapUtils.renderScriptBlur(blurryBackground, context, blurAmount);
		Log.e("TAG", "D5:" + blurryBackground.getDensity());
		// Put all those together
		Canvas canvas = new Canvas(blurryBackground);
		
		Paint paint = new Paint();
		canvas.drawBitmap(sharp, maskPosX, maskPosY, paint);

		result = blurryBackground;

		return null;
	}

	@Override
	protected void onPostExecute(Bitmap res) {
		imageView.setImageBitmap(result);
	}
}
