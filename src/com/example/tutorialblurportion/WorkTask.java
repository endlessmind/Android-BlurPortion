package com.example.tutorialblurportion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;


public class WorkTask extends AsyncTask<Void, Integer, Bitmap> {
	
	public interface WorkTaskListener {
		public void onWorkCompleted(WorkTask task);
	}
	
	private Context context;
	//private ImageView imageView;
	private int blurAmount;
	private int dencity;
	private Bitmap source;
	
	private Bitmap mask;
	private float maskPosX;
	private float maskPosY;

	private boolean needBlur;
	
	public Bitmap result;
	public Bitmap bluredSource;
	
	private WorkTaskListener mListener;

	public WorkTask(Context c, WorkTaskListener list, int blurAmount, Bitmap source, Bitmap blurSource,float maskPosX, float maskPosY, int maskSize, boolean blur, int density) {
		this.context = c;
		//this.imageView = imageView;
		this.bluredSource = blurSource;
		this.mListener = list;
		this.blurAmount = blurAmount;
		this.source = source;
		this.mask = BitmapUtils.createRadialBlurMask(maskSize / 2f, maskSize, maskSize , dencity);
		this.maskPosX = maskPosX;
		this.maskPosY = maskPosY;
		this.needBlur = blur;
		this.dencity = dencity;
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected Bitmap doInBackground(Void... params) {
		// Get the sharp part of the picture
		
		Bitmap sharp = BitmapUtils.applyMask(source, mask, (int)maskPosX, (int)maskPosY);
		
		Bitmap blurryBackground;
		if (needBlur) {
			this.bluredSource = BitmapUtils.renderScriptBlur(source, context, blurAmount, dencity);
		}
		
		blurryBackground = Bitmap.createBitmap(bluredSource);

		// Put all those together
		Canvas canvas = new Canvas(blurryBackground);
		
		Paint paint = new Paint();
		canvas.drawBitmap(sharp, maskPosX, maskPosY, paint);
		

		result = blurryBackground;

		return null;
	}

	@Override
	protected void onPostExecute(Bitmap res) {
		if (mListener != null)
			mListener.onWorkCompleted(this);
		
		//imageView.setImageBitmap(result);
	}
}
