package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Stack;

import javax.swing.JPanel;

import animation.AnimatorValue;
import object.Element;
import object.MyObject;
import object.TopPoint;

public class MonitorPanel extends JPanel {

	private static final int TEXT_TITLE_COORX = 50;
	private static final int TEXT_TITLE_COORY = 50;
	public static final int FIRST_ELEMENT_COORX = 50;
	public static final int FIRST_ELEMENT_COORY = TEXT_TITLE_COORY + 100;
	private static final int DISTANCE_NEW_ELEMENT = 130;

	private MainFrame mainFrame;

	private Stack<Element> elementStack;
	private TopPoint topPoint;

	private String title = "";
	private String valueTitle = "";

	public MonitorPanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		elementStack = new Stack<Element>();
		topPoint = new TopPoint(this);
		topPoint.setCoorX(FIRST_ELEMENT_COORX + 20);
		topPoint.setCoorY(FIRST_ELEMENT_COORY + Element.HEIGHT + 30);
		topPoint.setIsEmptyTop(true);
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.black);
		g.drawString(title, TEXT_TITLE_COORX, TEXT_TITLE_COORY);
		drawObjects(g);
	}

	public void doPush() {
		int value = mainFrame.getValueTextField();
		if (value == MainFrame.INVALID_VALUE) {
			return;
		}
		
		mainFrame.setEnableTopControl(false);

		title = "pushing:";
		valueTitle = "";

		Element e = new Element(this, value, elementStack.size() == 0 ? null : elementStack.peek());
		topPoint.setOnAnimationFinish(null);
		e.setOnAnimationFinish(new MyObject.OnAnimationFinish() {
			
			@Override
			public void onFinish() {
				topPoint.changeStatusForPush(e, mainFrame.getSpeed());
				mainFrame.setEnableTopControl(true);
				repaint();
			}
		});
		e.doAnimationPush(mainFrame.getSpeed(), TEXT_TITLE_COORX + 50, TEXT_TITLE_COORY, TEXT_TITLE_COORX + 100,
				TEXT_TITLE_COORY - Element.HEIGHT / 2,
				elementStack.size() == 0 ? FIRST_ELEMENT_COORX : elementStack.peek().getCoorX() + DISTANCE_NEW_ELEMENT,
				elementStack.size() == 0 ? FIRST_ELEMENT_COORY : elementStack.peek().getCoorY());
		elementStack.push(e);
	}
	
	public void doPop() {
		if(elementStack.size() == 0) {
			return;
		}
		
		mainFrame.setEnableTopControl(false);
		
		title = "popping:";
		valueTitle = "";
		Element e = elementStack.peek();
		topPoint.setOnAnimationFinish(new MyObject.OnAnimationFinish() {
			
			@Override
			public void onFinish() {
				elementStack.pop();
				valueTitle = String.valueOf(e.getValue());
				repaint();
				mainFrame.setEnableTopControl(true);
			}
		});
		e.setOnAnimationFinish(new MyObject.OnAnimationFinish() {
			
			@Override
			public void onFinish() {
				topPoint.changeStatusForPop(e.getPrevious(), mainFrame.getSpeed());
			}
		});
		e.doAnimationPop(mainFrame.getSpeed(), TEXT_TITLE_COORX + 50, TEXT_TITLE_COORY);
	}

	private void drawObjects(Graphics g) {
		topPoint.draw(g);
		for (Element e : elementStack) {
			e.draw(g);
		}
		g.setColor(Color.BLACK);
		g.drawString(String.valueOf(valueTitle), TEXT_TITLE_COORX + 50, TEXT_TITLE_COORY);
	}
}
