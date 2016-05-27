package com.example.tutorialblurportion;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class MainActivity extends Activity {
	private ImageView imageView;
	private SeekBar sbSize, sbBlur;
	
	private Bitmap texture;

	private float lastTouchedPositionX;
	private float lastTouchedPositionY;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        sbSize = (SeekBar) findViewById(R.id.sbSize);
        sbBlur = (SeekBar) findViewById(R.id.sbBlur);
        
        imageView = (ImageView) findViewById(R.id.imageView1);
        imageView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				lastTouchedPositionX = event.getX();
				lastTouchedPositionY = event.getY();
				
				if (event.getAction() == MotionEvent.ACTION_UP) {
					refreshImageView();
					
				}

				return true;
			}
		});
        
        sbSize.setOnSeekBarChangeListener(seekbarList);
        sbBlur.setOnSeekBarChangeListener(seekbarList);
        
		lastTouchedPositionX = -1;
		lastTouchedPositionY = -1;
		
		
		
		texture = BitmapUtils.getDrawableAsBitmap(MainActivity.this, R.drawable.texture);
		Log.e("TAG", "Loaded and ready");
    }


    private void refreshImageView() {
		if (lastTouchedPositionX == -1 || lastTouchedPositionY == -1)
			return;


		int blurAmount = sbBlur.getProgress() +1;
		int maskSize = sbSize.getProgress();
		if (maskSize < BitmapUtils.MASK_MIN_SIZE) {
			maskSize = BitmapUtils.MASK_MIN_SIZE;
		}
		
		// The user thinks that he touched the center of the mask.
		// Instead, we need its upper left corner position
		//Log.e("TAG", "Blur:" + blurAmount);
		//Log.e("TAG", "pos1: X:" + lastTouchedPositionX + " Y: " + lastTouchedPositionY);
		float cx = lastTouchedPositionX - (maskSize/2f);
		float cy = lastTouchedPositionY - (maskSize/2f);
		//Log.e("TAG", "pos1: X:" + cx + " Y: " + cy);
		// Get touched position on bitmap (we've got touched position on ImageView for now)
		//float posX = texture.getWidth() * cx / imageView.getWidth();
		//float posY = texture.getHeight() * cy / imageView.getHeight();
		
		//How much the image has been scaled
		float diffX = 0;
		float diffY = 0;
		float newX = cx;
		float newY = cy;
		Log.e("TAG", "t: W:" + texture.getWidth() + " H: " + texture.getHeight());
		
		//Fixing the mask position so it will be correct no matter the image size or density
		if (texture.getHeight() > imageView.getHeight()) {
			//Scaled down
			diffY = (float)texture.getHeight() / (float)imageView.getHeight();
			newY = cy * diffY;
		} else {
			//Scaled up
			diffY = (float)imageView.getHeight() / (float)texture.getHeight();
			newY = cy / diffY;
		}

		if (texture.getWidth() > imageView.getWidth()) {
			//Scaled down
			diffX = (float)texture.getWidth() / (float)imageView.getWidth();
			newX = cx * diffX;
		} else {
			//Scaled up
			diffX = (float)imageView.getWidth() / (float)texture.getWidth();
			newX = cx / diffX;
		}
		
		
		
		Log.e("TAG", "pos1: X:" + newX + " Y: " + newY);
		newX = NumbUtils.fixPos(newX, texture.getWidth(), maskSize);
		newY = NumbUtils.fixPos(newY, texture.getHeight(), maskSize);
		
		new WorkTask(this, imageView, blurAmount, texture, newX, newY,maskSize).execute();
		
		int maxSize = 0;
		if (texture.getWidth() > texture.getHeight()) {
			maxSize = texture.getHeight();
		} else {
			maxSize = texture.getWidth();
		}
		Log.e("TAG", "Max mask size:" + maxSize );
		sbSize.setMax(maxSize);
	}
    
    
    private OnSeekBarChangeListener seekbarList = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			if (lastTouchedPositionX == -1 || lastTouchedPositionY == -1) {
				lastTouchedPositionX = imageView.getWidth() / 2f;
				lastTouchedPositionY = imageView.getHeight() / 2f;
			}
			refreshImageView();
		}
    	
    };

}
