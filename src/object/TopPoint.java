package object;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;

import animation.AnimatorValue;

public class TopPoint extends MyObject {

	private static final int STATE_IDLE = 1;
	private static final int STATE_MOVE_TO_TOP = 2;
	
	public static final int SIZE = 30;
	public static final int POINT_LINE_HEIGHT = 50;
	private boolean isEmptyTop;

	public TopPoint(Container context) {
		super(context);
		currentState = STATE_IDLE;
	}
	
	public void setIsEmptyTop(boolean isEmpty) {
		isEmptyTop = isEmpty;
	}
	
	public void changeStatusForPush(Element newTop, long speed) {
		if(isEmptyTop) {
			isEmptyTop = false;
			setCoorX(newTop.getCoorX() + Element.WIDTH / 2);
			setCoorY(newTop.getCoorY() + Element.HEIGHT);
			currentState = STATE_IDLE;
			finishDelayAnimation();
		} else {
			currentState = STATE_IDLE;
			doMoveToTop(newTop, speed);
		}
	}
	
	public void changeStatusForPop(Element newTop, long speed) {
		if(newTop == null) {
			isEmptyTop = true;
			finishDelayAnimation();
		} else {
			currentState = STATE_IDLE;
			doMoveToTop(newTop, speed);
		}
	}
	
	private void doMoveToTop(Element newTop, long speed) {
		float oldCoorX = getCoorX();
		float oldCoorY = getCoorY();
		AnimatorValue animator = new AnimatorValue(speed);
		animator.setAnimatorUpdate(new AnimatorValue.AnimatorUpdate() {
			
			@Override
			public void updateAnimation(float fraction) {
				float secondCoorX = newTop.getCoorX() + Element.WIDTH / 2;
				float secondCoorY = newTop.getCoorY() + Element.HEIGHT;
				setCoorX((secondCoorX - oldCoorX) * fraction + oldCoorX);
				setCoorY((secondCoorY - oldCoorY) * fraction + oldCoorY);
				getContext().repaint();
			}
			
			@Override
			public void endAnimation() {
				currentState = STATE_IDLE;
				finishDelayAnimation();
			}
		});
		animator.start();
	}
	
	public void draw(Graphics g) {
		g.setColor(Color.black);
		if (isEmptyTop) {
			g.drawRect((int) getCoorX(), (int) getCoorY(), SIZE, SIZE);
			g.drawLine((int) getCoorX(), (int) getCoorY(), (int) getCoorX() + SIZE, (int) getCoorY() + SIZE);
		} else {
			g.drawLine((int) getCoorX(), (int) getCoorY(), (int) getCoorX(), (int) getCoorY() + POINT_LINE_HEIGHT);
			g.drawLine((int) getCoorX(), (int) getCoorY(), (int) getCoorX() - 10, (int) getCoorY() + 10);
			g.drawLine((int) getCoorX(), (int) getCoorY(), (int) getCoorX() + 10, (int) getCoorY() + 10);
			g.drawString("Top", (int) getCoorX() - 10, (int) getCoorY() + POINT_LINE_HEIGHT + 20);
		}
	}

	@Override
	protected void step() {
		switch(currentState) {
		case STATE_IDLE:
			finishAnimation();
			break;
		}
	}
}
