package com.example.tutorialblurportion.mask;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.Shader.TileMode;

public class TiltShiftMask extends Mask {
	
	int width, height, density;
	

	/**
	 * 
	 * @param w Width
	 * @param h Height
	 * @param d Density
	 */
	public TiltShiftMask(int w, int h, int d) {
		this.width = w;
		this.height = h;
		this.density = d;
	}

	@Override
	public Bitmap CreateMask() {
		int halfH = height / 2;
		
		LinearGradient shaderTop = new LinearGradient(0,
				0,
				0,
				halfH,
				0x00FFFFFF, 0xFF00ff00,TileMode.CLAMP);
		
		LinearGradient shaderBottom = new LinearGradient(0,
				height,
				0,
				halfH,
				0x00FFFFFF, 0xFFff0000,TileMode.CLAMP);
		
		Paint p = new Paint();
		p.setDither(true);
		
		
		Bitmap bitmap = Bitmap.createBitmap(width,height, Config.ARGB_8888);
	    bitmap.setDensity(density);
	    
	    Canvas c = new Canvas(bitmap);
	    
		Rect newRect = c.getClipBounds();
		newRect.inset(-50,-50);
		c.clipRect(newRect);
		
		//c.save();
		//c.rotate(45);
	    
	    p.setShader(shaderTop);
	    c.drawRect(0, 0, width, height / 2, p);
	    p.setShader(shaderBottom);
	    c.drawRect(0, height / 2, width, height, p);
	    
	  // c.restore();
	   // Log.e("TAG", "Mask Size: w" + bitmap.getWidth() + " h:" + bitmap.getHeight());
		return bitmap;
	}

}
