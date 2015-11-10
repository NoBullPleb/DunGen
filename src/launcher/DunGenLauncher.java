package launcher;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import dungen.ui.Controls;

public final class DunGenLauncher {
	// GUI members
	private static final JFrame jd = new JFrame();;
	private static final JButton updater = new JButton();
	private static final JFileChooser fileChooser = new JFileChooser();
	private static final JLabel waitMessage = new JLabel(
			"Please wait while it finishes");
	// version members
	private static String currentVersion;
	private static final String version = currentVersion = Controls.version;

	final public static void main(String[] args) {

		try {

			currentVersion = getCurrentVersion();

			if (!currentVersion.equals(version)) {
				jd.setTitle("Poke v" + version);
				jd.setLocationRelativeTo(null);
				jd.setResizable(false);
				jd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				jd.setBounds(100, 100, 300, 100);
				updater.setText("Update DunGen to " + currentVersion + "!");
				updater.addActionListener(DunGenLauncher::update);
				jd.add(updater);
				jd.setVisible(true);
			} else {
				Controls.main(new String[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Controls.main(new String[0]);
		}
	}

	private final static void update(ActionEvent e) {
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.showSaveDialog(null);
		jd.setTitle("Updating Now...");
		jd.remove(updater);
		jd.add(waitMessage, BorderLayout.CENTER);
		jd.revalidate();
		jd.repaint();

		try {
			File file = fileChooser.getSelectedFile();
			if (!file.exists())
				file.mkdirs();
			download(file + "/DunGen v" + currentVersion + ".jar");
			jd.setTitle("Complete!");
			jd.remove(waitMessage);
			jd.add(new JLabel("Your download is complete. Please run that jar."),
					BorderLayout.CENTER);
			jd.revalidate();
			jd.repaint();
		} catch (Exception err) {
			jd.setTitle("Sorry - cannot write file");
			jd.remove(waitMessage);
			jd.add(new JLabel("Unable to update."), BorderLayout.CENTER);
			err.printStackTrace();
			jd.revalidate();
			jd.repaint();
		}

	}

	private final static void download(String target) throws IOException {
		Files.copy(
				new URL(
						"https://dl.dropboxusercontent.com/u/11902673/DunGen%20Launcher.jar")
						.openStream(), Paths.get(target),
				StandardCopyOption.REPLACE_EXISTING);

	}

	private final static String getCurrentVersion() throws IOException {
		String subversion = "-1";
		URL oracle = new URL(
				"https://dl.dropboxusercontent.com/u/11902673/DunGenversion.txt");
		BufferedReader in = new BufferedReader(new InputStreamReader(
				oracle.openStream()));
		subversion = (in.readLine()).trim();
		in.close();
		return subversion;
	}
}
