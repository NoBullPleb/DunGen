package dungen.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;

public class KeyPressListenerImp implements KeyPressListener {
	ActionListener doit = null;
	int keyEventID = 0;
	String direction = "";

	public KeyPressListenerImp(JButton b, int keyeventID, String direction) {
		doit = b.getActionListeners()[0];
		this.keyEventID = keyeventID;
		this.direction = direction;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == keyEventID
				&& Controls.thisRoom.hasDoor(direction))
			doit.actionPerformed(new ActionEvent(ActionEvent.ACTION_EVENT_MASK,
					0, ""));

	}
}
