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
	private static transient JList<String> dungeonSizes = new JList<>();

	private static transient DefaultListModel<Integer> levels = new DefaultListModel<>();
	private static transient DefaultListModel<Integer> sizes = new DefaultListModel<>();
	private static transient DefaultListModel<String> dunSizes = new DefaultListModel<>();
	private static transient JScrollPane scrollPane = null;
	private static transient JList<Integer> sizeList = new JList<>();
	public static int maxPartySize = 6;
	static {
		for (int i = 1; i < 21; i++)
			levels.addElement(i);
		for (int i = 1; i <= maxPartySize; i++)
			sizes.addElement(i);
		dunSizes.addElement("Larger");
		dunSizes.addElement("Normal");
		dunSizes.addElement("Smaller");
	}

	private static JCheckBox[] types = new JCheckBox[Tables.monsterTypes.length];
	private static JCheckBox symetryBox = new JCheckBox("Symmetric?");
	private final JLabel lblDungeonShape = new JLabel("Dungeon Shape");
	static JCheckBox spwnNPCs = new JCheckBox("Spawn NPCS?");
	static JCheckBox spwnTraps = new JCheckBox("Spawn Traps?");
	static JCheckBox spwnEncounters = new JCheckBox("Spawn Encounters?");
	static JCheckBox spwnHazards = new JCheckBox("Spawn Hazards?");

	public static boolean getSpwnNpcs() {
		return spwnNPCs.isSelected();
	}

	public static boolean getSpwnHazards() {
		return spwnHazards.isSelected();
	}

	public static boolean getSpwnTraps() {
		return spwnTraps.isSelected();
	}

	public static boolean getSpwnEncounters() {
		return spwnEncounters.isSelected();
	}

	public static boolean getTruth(int i) {
		return types[i].isSelected();
	}

	public static void setPartyLevel(int i) {
		partyLevelList.setSelectedIndex(i - 1);
	}

	public static int getPartyLevel() {
		return partyLevelList.getSelectedValue();
	}

	public static int getPartySize() {
		return sizeList.getSelectedValue();
	}

	public static void setPartySize(int i) {
		sizeList.setSelectedIndex(i - 1);
	}

	public static void setTruth(int i, boolean truth) {
		types[i].setSelected(truth);
	}

	public static boolean isSymmmetric() {
		return symetryBox.isSelected();

	}

	public static void setSymmetric(boolean truth) {
		symetryBox.setSelected(truth);

	}

	public static int dungeonSize() {
		return (dungeonSizes.getSelectedIndex()) + 1;

	}

	public static void setAllTypesFalse() {
		for (int i = 0; i < types.length; i++) {
			types[i].setSelected(false);
		}
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
		scrollPane = new JScrollPane(partyLevelList);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(77, 4, 35, 20);
		contentPane.add(scrollPane);
		for (int i = 0; i < types.length; i++) {
			types[i] = new JCheckBox(Tables.monsterTypes[i]);
			types[i].setBounds(59, 21 + 23 * i, 118, 23);
			types[i].setSelected(false);
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
		sizeList.setModel(sizes);
		sizeList.setSelectedIndex(3);
		sizeList.setAutoscrolls(true);
		JScrollPane sp2 = new JScrollPane(sizeList);
		sp2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		sp2.getVerticalScrollBar().setValue(52);
		sp2.setBounds(145, 4, 35, 20);
		contentPane.add(sp2);

		symetryBox.setBounds(5, 365, 107, 20);
		symetryBox.setSelected(true);
		contentPane.add(symetryBox);
		lblDungeonShape.setBounds(6, 347, 106, 16);

		contentPane.add(lblDungeonShape);
		dungeonSizes.setModel(dunSizes);
		dungeonSizes.setSelectedIndex(1);
		dungeonSizes.setVisibleRowCount(1);

		JScrollPane sp3 = new JScrollPane(dungeonSizes);
		JLabel ds = new JLabel("Dungeon Size:");
		ds.setBounds(2, 390, 106, 20);
		sp3.setBounds(95, 390, 80, 20);
		sp3.getVerticalScrollBar().setValue(19);
		sp3.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(ds);
		contentPane.add(sp3);

		spwnNPCs.setSelected(true);
		spwnNPCs.setBounds(6, 415, 171, 20);
		contentPane.add(spwnNPCs);

		spwnTraps.setSelected(true);
		spwnTraps.setBounds(6, 437, 171, 20);
		contentPane.add(spwnTraps);

		spwnEncounters.setSelected(true);
		spwnEncounters.setBounds(6, 460, 171, 20);
		contentPane.add(spwnEncounters);

		spwnHazards.setSelected(true);
		spwnHazards.setBounds(6, 483, 171, 20);
		contentPane.add(spwnHazards);
	}
}
