package dungen.ui;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class GeneratedDialog extends JDialog {
	JScrollPane sp = new JScrollPane();
	JTextArea ta = new JTextArea();

	public static void main(String[] args) {
		GeneratedDialog gd = new GeneratedDialog();
		gd.setVisible(true);
	}

	public void setText(String text) {
		ta.setText(text);
		ta.revalidate();
		ta.repaint();
	}

	public GeneratedDialog() {
		setSize(400, 400);
		setLocation(400, 200);
		ta.setSize(200, 200);
		ta.setColumns(10);
		ta.setLineWrap(true);
		ta.setWrapStyleWord(true);
		sp.setViewportView(ta);
		setContentPane(sp);
	}

}
