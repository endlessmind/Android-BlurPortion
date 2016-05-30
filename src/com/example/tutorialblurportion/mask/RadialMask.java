package com.example.tutorialblurportion.mask;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Bitmap.Config;
import android.graphics.Shader.TileMode;

public class RadialMask extends Mask {

	int width, height, radius, density;
	
	/**
	 * 
	 * @param w Width
	 * @param h Height
	 * @param r Radius
	 * @param d Density
	 */
	public RadialMask(int w, int h, int r, int d) {
		this.width = w;
		this.height = h;
		this.radius = r;
		this.density = d;
	}
	
	@Override
	public Bitmap CreateMask() {
		RadialGradient gradient = new RadialGradient(width / 2, height / 2, radius, 0xFF000000, 0x00FFFFFF, TileMode.CLAMP);
	    Paint p = new Paint();
	    p.setDither(true);
	    p.setShader(gradient);

	    
	    Bitmap bitmap = Bitmap.createBitmap(width,height, Config.ARGB_8888);
	    bitmap.setDensity(density);
	    Canvas c = new Canvas(bitmap);
	    
	    c.drawCircle(width / 2, height / 2, radius, p);
		return bitmap;
	}


}
