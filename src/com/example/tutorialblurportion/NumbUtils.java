package com.example.tutorialblurportion;

public class NumbUtils {
	
	public static float fixPos(float value, float imgWH, float maskWH) {
		if (value < 0)
			value = 0;
		
		if ((value + maskWH) >= imgWH)
			value = imgWH - maskWH;
		
		return value;
	}
	
	
}
