package dungen.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSplitPane;

import dungen.resourceLoader.ResourceLoader;

@SuppressWarnings("serial")
public class MonsterPop extends JFrame {

	private static JPanel contentPane;

	JScrollPane sp = new JScrollPane();
	JComboBox<String> comboBox = new JComboBox<String>();

	public MonsterPop(String x) {
		new MonsterPop();
		comboBox.setSelectedItem(x);
	}

	public MonsterPop(ActionEvent e) {
		new MonsterPop();
	}

	public MonsterPop() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane, BorderLayout.NORTH);

		JLabel lblMonster = new JLabel("Monster");
		splitPane.setLeftComponent(lblMonster);
		JTextArea textPane = new JTextArea();
		textPane.setLineWrap(true);
		textPane.setWrapStyleWord(true);
		textPane.setEditable(false);
		sp.setViewportView(textPane);
		comboBox.addItemListener(e -> {
			String x = comboBox.getSelectedItem().toString();
			if (!x.trim().isEmpty())
				textPane.setText(ResourceLoader.getMonster(x));
		});
		AutoCompletion.enable(comboBox);
		splitPane.setRightComponent(comboBox);
		comboBox.setEditable(true);
		ResourceLoader.getMonsters()//
				.forEach(comboBox::addItem);

		sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentPane.add(sp, BorderLayout.CENTER);
		this.setTitle("Monster Stat Lookup!");
	}
}
