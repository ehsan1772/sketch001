package data;

public class Color {

	public float r;
	public float g;
	public float b;
	public float alpha;
	
	public int MAX = 255;
	
	public Color(int red, int blue, int green, float alpha) {
		r = red / MAX;
		g = green / MAX;
		b = blue / MAX;
		this.alpha = alpha;
	}
	
	public Color(int red, int blue, int green) {
		this(red, blue, green, 1f);
	}
	
	public Color() {
		this(0, 0, 0);
	}
	
}
