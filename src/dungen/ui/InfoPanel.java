package dungen.ui;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class InfoPanel extends JFrame {

	private static JPanel contentPane = new JPanel();
	private static JLabel lbl1 = new JLabel();
	private static JList<Integer> partyLevelList = new JList<>();
	private static DefaultListModel<Integer> levels = new DefaultListModel<Integer>();
	private static JScrollPane scrollPane = null;
	static {
		for (int i = 1; i < 21; i++)
			levels.addElement(i);
	}

	public static int partyLevel = 1;

	public InfoPanel() {
		System.out.println("InfoPanel Loaded");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(683, 50);
		setSize(183, 137);

		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		lbl1.setBounds(10, 5, 83, 33);
		lbl1.setText("Party Level:");
		contentPane.add(lbl1);
		partyLevelList.setModel(levels);
		partyLevelList.setSelectedIndex(0);
		partyLevelList.setVisibleRowCount(1);
		partyLevelList.addListSelectionListener(e -> {
			partyLevel = partyLevelList.getSelectedValue();

		});
		partyLevelList.setBounds(85, 10, 50, 20);
		scrollPane = new JScrollPane(partyLevelList);
		scrollPane.setBounds(partyLevelList.getBounds());
		contentPane.add(scrollPane);
		this.setVisible(true);
	}
}
