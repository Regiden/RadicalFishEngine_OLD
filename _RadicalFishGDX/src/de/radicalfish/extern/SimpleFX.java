package de.radicalfish.extern;

/**
 * SimpleFX to move from on value to another.
 * 
 * @author davedes
 * @author Stefan Lange (added doc, cleaning and small changes)
 */
public class SimpleFX {
	
	private Easing easing;
	
	private float duration, start, end, value;
	private float time;
	private boolean finished;
	
	public SimpleFX(float start, float end, float duration, Easing easing) {
		this(start, end, duration, easing, false);
	}
	public SimpleFX(float start, float end, float duration, Easing easing, boolean finished) {
		this.start = this.value = start;
		this.end = end;
		this.duration = duration;
		this.easing = easing;
		this.finished = finished;
	}
	
	// METHODS
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void update(float delta) {
		if (!finished) {
			time += delta;
			animate(time);
		}
	}
	public void finish() {
		value = end;
		finished = true;
	}
	public void restart() {
		finished = false;
		time = 0;
		value = start;
	}
	public void flip() {
		float t = getStart();
		setStart(getEnd());
		setEnd(t);
	}
	
	protected void animate(float time) {
		float change = end - start;
		value = easing.ease(time, start, change, duration);
		if (time > duration) {
			finished = true;
		}
	}
	
	// GETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public Easing getEasing() {
		return easing;
	}
	public boolean finished() {
		return finished;
	}
	public float getValue() {
		return value;
	}
	public float getEnd() {
		return end;
	}
	public float getStart() {
		return start;
	}
	
	// SETTER
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public void setEasing(Easing easing) {
		this.easing = easing;
	}
	public void setDuration(float duration) {
		this.duration = duration;
	}
	public void setStart(float start) {
		this.start = start;
	}
	public void setEnd(float end) {
		this.end = end;
	}
	public void setValue(float value) {
		this.value = value;
	}
	
}