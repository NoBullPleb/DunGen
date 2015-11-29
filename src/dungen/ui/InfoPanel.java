package dungen.ui;

import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import dungen.generators.Tables;

@SuppressWarnings("serial")
public class InfoPanel extends JFrame {

	private static transient JPanel contentPane = new JPanel();
	private static transient JLabel lbl1 = new JLabel();
	private static transient JList<Integer> partyLevelList = new JList<>();

	private static transient DefaultListModel<Integer> levels = new DefaultListModel<Integer>();
	private static transient DefaultListModel<Integer> sizes = new DefaultListModel<Integer>();
	private static transient JScrollPane scrollPane = null;
	private static transient JList<Integer> sizeList = new JList<>();
	public static int maxPartySize = 6;
	static {
		for (int i = 1; i < 21; i++)
			levels.addElement(i);
		for (int i = 1; i <= maxPartySize; i++)
			sizes.addElement(i);
	}

	public transient static int partyLevel = 1;
	public transient static int partySize = 4;

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
		scrollPane = new JScrollPane(partyLevelList);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(77, 4, 35, 20);
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

		JLabel lblSize = new JLabel();
		lblSize.setText("Size");
		lblSize.setBounds(116, 4, 32, 20);
		contentPane.add(lblSize);

		sizeList.setVisibleRowCount(1);
		sizeList.addListSelectionListener(e -> {
			partySize = sizeList.getSelectedValue();
			System.out.println("PARTY SIZE: " + partySize);
		});
		sizeList.setModel(sizes);
		sizeList.setSelectedIndex(3);
		sizeList.setAutoscrolls(true);
		JScrollPane sp2 = new JScrollPane(sizeList);
		sp2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sp2.getVerticalScrollBar().setValue(52);
		sp2.setBounds(145, 4, 35, 20);
		contentPane.add(sp2);
	}
}
