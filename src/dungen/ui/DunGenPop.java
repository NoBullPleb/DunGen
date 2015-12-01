package dungen.ui;

import java.awt.BorderLayout;
import java.util.function.Supplier;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class DunGenPop extends JFrame {
	JScrollPane scrollPane = new JScrollPane();
	private JPanel contentPane;
	JTextArea textArea = new JTextArea();

	public void setText(String text) {
		textArea.setText(text);
		textArea.revalidate();
		textArea.repaint();
	}

	public DunGenPop(String title, Supplier<String> text) {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		contentPane.add(scrollPane, BorderLayout.CENTER);
		textArea.setSize(200, 200);
		textArea.setColumns(10);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		scrollPane.setViewportView(textArea);
		JButton btnAddToRoom = new JButton("Add To Room");
		btnAddToRoom.addActionListener(e -> {
			if (!Controls.roomDetails.getText().endsWith("\n"))
				Controls.roomDetails.append("\n");
			Controls.roomDetails.append(this.getTitle() + "\n"
					+ textArea.getText());
			Controls.mapView.addEventOnRoom(Controls.mapView.getPosition(
					Controls.showX, Controls.showY), this.getTitle(), this
					.getTitle().toLowerCase().contains("npc"));
			this.dispose();
		});
		JButton regenerate = new JButton("Regenerate");
		regenerate.addActionListener(e -> setText(text.get()));

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		setText(text.get());
		setTitle(title);
		this.setVisible(true);
		panel.add(btnAddToRoom, BorderLayout.EAST);
		panel.add(regenerate, BorderLayout.WEST);
		contentPane.add(panel, BorderLayout.SOUTH);
	}

}
