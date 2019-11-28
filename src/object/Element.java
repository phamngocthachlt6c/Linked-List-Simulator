package object;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;

import animation.AnimatorValue;
import ui.MainFrame;

public class Element extends MyObject {

	public static final int WIDTH = 80;
	public static final int HEIGHT = 50;
	private static final int RECT_HEADER_WIDTH = 20;
	private static final int TEXT_OFFSETX = 40;
	private static final int TEXT_OFFSETY = 30;

	public static final int STATE_IDLE = 1;
	public static final int STATE_IDLE_NOT_SHOW_VALUE = 0;
	public static final int STATE_ADD_VALUE = 2;
	public static final int STATE_MOVE_TO_NEXT = 3;
	public static final int STATE_GET_VALUE = 4;

	private int value;
	private Element previousElement;

	// for animation
	private float textFirstCoorX, textFirstCoorY, firstCoorX, firstCoorY, secondCoorX, secondCoorY;
	private float textAnimCoorX, textAnimCoorY, textSecondCoorX, textSecondCoorY;

	public Element(Container context, int value, Element previous) {
		super(context);
		this.value = value;
		previousElement = previous;
	}
	
	public int getValue() {
		return value;
	}
	
	public Element getPrevious() {
		return previousElement;
	}

	public int getCurrentState() {
		return currentState;
	}

	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect((int) getCoorX(), (int) getCoorY(), WIDTH, HEIGHT);
		g.drawLine((int) getCoorX() + RECT_HEADER_WIDTH, (int) getCoorY(), (int) getCoorX() + RECT_HEADER_WIDTH,
				(int) getCoorY() + HEIGHT);

		if (previousElement == null) {
			g.drawLine((int) getCoorX(), (int) getCoorY(), (int) getCoorX() + RECT_HEADER_WIDTH,
					(int) getCoorY() + HEIGHT);
		} else {
			g.drawLine((int) getCoorX(), (int) getCoorY() + HEIGHT / 2, (int) previousElement.getCoorX() + WIDTH,
					(int) previousElement.getCoorY() + HEIGHT / 2);
			g.fillOval((int) previousElement.getCoorX() + WIDTH - 5, (int) previousElement.getCoorY() + HEIGHT / 2 - 5,
					10, 10);
		}
		if (currentState == STATE_ADD_VALUE || currentState == STATE_GET_VALUE
				|| currentState == STATE_IDLE_NOT_SHOW_VALUE) {
			g.drawString(String.valueOf(value), (int) textAnimCoorX, (int) textAnimCoorY);
		} else {
			g.drawString(String.valueOf(value), (int) getCoorX() + TEXT_OFFSETX, (int) getCoorY() + TEXT_OFFSETY);
		}
	}

	@Override
	protected void step() {
		switch (currentState) {
		case STATE_ADD_VALUE:
			doAddValue();
			break;
		case STATE_MOVE_TO_NEXT:
			doMoveElement();
			break;
		case STATE_IDLE:
		case STATE_IDLE_NOT_SHOW_VALUE:
			finishAnimation();
			break;
		case STATE_GET_VALUE:
			break;
		}
	}
	
	public void doAnimationPop(long speed, float textSecondCoorX, float textSecondCoorY) {
		currentState = STATE_GET_VALUE;
		float startCoorX = getCoorX() + TEXT_OFFSETX;
		float startCoorY = getCoorY() + TEXT_OFFSETY;
		this.speed = speed;
		AnimatorValue animator = new AnimatorValue(speed);
		animator.setAnimatorUpdate(new AnimatorValue.AnimatorUpdate() {
			
			@Override
			public void updateAnimation(float fraction) {
				textAnimCoorX = (textSecondCoorX - startCoorX) * fraction + startCoorX;
				textAnimCoorY = (textSecondCoorY - startCoorY) * fraction + startCoorY;
				getContext().repaint();
			}
			
			@Override
			public void endAnimation() {
				currentState = STATE_IDLE_NOT_SHOW_VALUE;
				finishDelayAnimation();
			}
		});
		animator.start();
	}

	public void doAnimationPush(long speed, float textFirstCoorX, float textFirstCoorY, float firstCoorX,
			float firstCoorY, float secondCoorX, float secondCoorY) {
		setCoorX(firstCoorX);
		setCoorY(firstCoorY);
		this.textFirstCoorX = textFirstCoorX;
		this.textFirstCoorY = textFirstCoorY;
		this.firstCoorX = firstCoorX;
		this.firstCoorY = firstCoorY;
		this.secondCoorX = secondCoorX;
		this.secondCoorY = secondCoorY;
		this.speed = speed;
		currentState = STATE_ADD_VALUE;
		doAddValue();
	}

	private void doAddValue() {
		AnimatorValue animator = new AnimatorValue(speed);
		animator.setAnimatorUpdate(new AnimatorValue.AnimatorUpdate() {

			@Override
			public void updateAnimation(float fraction) {
				float secondTextCoorX = getCoorX() + TEXT_OFFSETX;
				float secondTextCoorY = getCoorY() + TEXT_OFFSETY;
				textAnimCoorX = (secondTextCoorX - textFirstCoorX) * fraction + textFirstCoorX;
				textAnimCoorY = (secondTextCoorY - textFirstCoorY) * fraction + textFirstCoorY;
				getContext().repaint();
			}

			@Override
			public void endAnimation() {
				currentState = STATE_MOVE_TO_NEXT;
				finishDelayAnimation();
			}
		});
		animator.start();
	}

	private void doMoveElement() {
		AnimatorValue animator = new AnimatorValue(speed);
		animator.setAnimatorUpdate(new AnimatorValue.AnimatorUpdate() {

			@Override
			public void updateAnimation(float fraction) {
				setCoorX((secondCoorX - firstCoorX) * fraction + firstCoorX);
				setCoorY((secondCoorY - firstCoorY) * fraction + firstCoorY);
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

}
