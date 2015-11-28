package dungen.ui;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import dungen.generators.Tables;

@SuppressWarnings("serial")
public class InfoPanel extends JFrame {

	private static transient JPanel contentPane = new JPanel();
	private static transient JLabel lbl1 = new JLabel();
	private static transient JList<Integer> partyLevelList = new JList<>();
	private static transient DefaultListModel<Integer> levels = new DefaultListModel<Integer>();
	private static transient JScrollPane scrollPane = null;
	static {
		for (int i = 1; i < 21; i++)
			levels.addElement(i);
	}

	public transient static int partyLevel = 1;

	public static void setLevel(int i) {
		partyLevelList.setSelectedValue(i, true);
	}

	private static JCheckBox[] types = new JCheckBox[Tables.monsterTypes.length];

	public static boolean getTruth(int i) {
		return types[i].isSelected();
	}

	public static void setTruth(int i, boolean truth) {
		types[i].setSelected(truth);
	}

	public InfoPanel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(Controls.mapView.getX() + Controls.mapView.getWidth(), 50);
		setSize(183, 580);

		setContentPane(contentPane);
		contentPane.setLayout(null);

		lbl1.setBounds(6, 4, 71, 20);
		lbl1.setText("Party Level:");
		contentPane.add(lbl1);
		partyLevelList.setModel(levels);
		partyLevelList.setSelectedIndex(0);
		partyLevelList.setVisibleRowCount(1);
		partyLevelList.addListSelectionListener(e -> {
			partyLevel = partyLevelList.getSelectedValue();

		});
		partyLevelList.setBounds(85, 4, 50, 20);
		scrollPane = new JScrollPane(partyLevelList);
		scrollPane.setBounds(partyLevelList.getBounds());
		contentPane.add(scrollPane);
		for (int i = 0; i < types.length; i++) {
			types[i] = new JCheckBox(Tables.monsterTypes[i]);
			types[i].setBounds(59, 21 + 23 * i, 118, 23);
			types[i].setSelected(true);
			contentPane.add(types[i]);
		}
		JLabel lblInclude = new JLabel("Include: ");
		lblInclude.setBounds(6, 25, 61, 16);
		contentPane.add(lblInclude);

	}
}
