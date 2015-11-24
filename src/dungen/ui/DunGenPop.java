package dungen.ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class DunGenPop extends JFrame {

	private JPanel contentPane;
	JTextArea textArea = new JTextArea();

	public void setText(String text) {
		textArea.setText(text);
		textArea.revalidate();
		textArea.repaint();
	}

	public DunGenPop() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		textArea.setSize(200, 200);
		textArea.setColumns(10);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		scrollPane.setViewportView(textArea);
		JButton btnAddToRoom = new JButton("Add To Room");
		btnAddToRoom.addActionListener(e -> {
			Controls.roomDetails.append("\n" + textArea.getText());
			this.dispose();
		});
		contentPane.add(btnAddToRoom, BorderLayout.SOUTH);
	}

}
