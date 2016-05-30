package com.example.tutorialblurportion;

import com.example.tutorialblurportion.mask.Mask;
import com.example.tutorialblurportion.mask.TiltShiftMask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;


public class WorkTask extends AsyncTask<Void, Integer, Bitmap> {
	
	public interface WorkTaskListener {
		public void onWorkCompleted(WorkTask task);
	}
	
	public enum MaskType {
		Radical,
		Tilt,
		None
	}
	
	private Context context;
	//private ImageView imageView;
	private int blurAmount;
	private int density;
	private Bitmap source;
	
	private Bitmap mask;
	private Mask maskObj;
	private float maskPosX;
	private float maskPosY;

	private boolean needBlur;
	
	public Bitmap result;
	public Bitmap bluredSource;
	
	private WorkTaskListener mListener;

	public WorkTask(Context c, WorkTaskListener list,Mask m, int blurAmount, Bitmap source, Bitmap blurSource,float maskPosX, float maskPosY, int maskSize, boolean blur, int den) {
		this.context = c;
		//this.imageView = imageView;
		this.bluredSource = blurSource;
		this.mListener = list;
		this.blurAmount = blurAmount;
		this.source = source;
		//this.mask = BitmapUtils.createRadialBlurMask(maskSize / 2f, maskSize, maskSize , density);
		//this.mask = BitmapUtils.CreateTiltShiftMask(maskSize, source.getWidth(), density);
		this.maskObj = m;
		this.mask = m.CreateMask();
		this.maskPosX = maskPosX;
		this.maskPosY = maskPosY;
		this.needBlur = blur;
		this.density = den;
	}

	@Override
	protected void onPreExecute() {
	}

	@Override
	protected Bitmap doInBackground(Void... params) {
		// Get the sharp part of the picture
		if (maskObj instanceof TiltShiftMask) {
			maskPosX = 0;
		}
		Bitmap sharp = BitmapUtils.applyMask(source, mask, (int)maskPosX, (int)maskPosY);
		
		Bitmap blurryBackground;
		if (needBlur) {
			this.bluredSource = BitmapUtils.renderScriptBlur(source, context, blurAmount, density);
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
