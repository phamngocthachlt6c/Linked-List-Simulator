package object;

import java.awt.Container;

import animation.AnimatorValue;

public abstract class MyObject {
	private float coorX, coorY;
	private Container context;
	protected OnAnimationFinish onAnimationFinish;
	protected int currentState;
	protected long speed;
	private AnimatorValue pauseAnimation;
	
	public MyObject(Container context) {
		this.context = context;
		pauseAnimation = new AnimatorValue(500);
		pauseAnimation.setAnimatorUpdate(new AnimatorValue.AnimatorUpdate() {

			@Override
			public void updateAnimation(float fraction) {
			}

			@Override
			public void endAnimation() {
				step();
			}
		});
	}
	
	protected abstract void step();
	
	public void setOnAnimationFinish(OnAnimationFinish onAnimationFinish) {
		this.onAnimationFinish = onAnimationFinish;
	}
	
	public Container getContext() {
		return context;
	}
	
	public float getCoorX() {
		return coorX;
	}

	public void setCoorX(float coorX) {
		this.coorX = coorX;
	}

	public float getCoorY() {
		return coorY;
	}

	public void setCoorY(float coorY) {
		this.coorY = coorY;
	}
	
	protected void finishDelayAnimation() {
		pauseAnimation.start();
	}
	
	protected void finishAnimation() {
		if (onAnimationFinish != null) {
			onAnimationFinish.onFinish();
		}
	}
	
	public interface OnAnimationFinish {
		void onFinish();
	}
	
}
