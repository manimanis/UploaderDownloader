/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package org.manianis.forms;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.manianis.ApplicationsParameters;
import org.manianis.FileDownloader;
import org.manianis.utils.StringUtil;

/**
 *
 * @author hp
 */
public class ResourceDownloaderFrame extends javax.swing.JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ApplicationsParameters appParams;

	/**
	 * Creates new form MainFrame
	 */
	public ResourceDownloaderFrame() {
		initComponents();
		appParams = ApplicationsParameters.getInstance();
		// initParameters();
		setUserName(appParams.getUserName());
		setWorkingPath(appParams.getWorkingDirectory());
		setClef(appParams.getKeyword());
		setHostUrl(appParams.getLastUrlAddress());
	}

	public File selectFolder(String title, String currentDir) {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File(currentDir));
		chooser.setDialogTitle(title);
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			return chooser.getSelectedFile();
		} else {
			return null;
		}
	}

	private void setWorkingPath(String newWorkingPath) {
		// String newWorkingPath = txtFolder.getText();
		txtFolder.setText(newWorkingPath);
		if (!newWorkingPath.isEmpty()) {
			if (!appParams.getWorkingDirectory().equalsIgnoreCase(newWorkingPath)) {
				appParams.setWorkingDirectory(newWorkingPath).save();
			}
		}
		btnOpenFolder.setEnabled(!appParams.getUserFolder().isEmpty());
	}

	private void setUserName(String newUserName) {
		// String newUserName =
		// StringUtil.filterLetters(StringUtil.removeSpaces(txtName.getText()));
		txtName.setText(newUserName);
		if (!newUserName.isEmpty()) {
			if (!appParams.getUserName().equalsIgnoreCase(newUserName)) {
				appParams.setUserName(newUserName).save();
			}
		}
		btnOpenFolder.setEnabled(!appParams.getUserFolder().isEmpty());
	}

	private void setHostUrl(String newUrl) {
		txtURL.setText(newUrl);
		if (!newUrl.isEmpty()) {
			if (!appParams.getLastUrlAddress().equalsIgnoreCase(newUrl)) {
				appParams.setLastUrlAddress(newUrl).save();
			}
		}
		btnDownload.setEnabled(!appParams.getResourceUrl().isEmpty());
	}

	private void setClef(String newClef) {
		txtClef.setText(newClef);
		if (!newClef.isEmpty()) {
			if (!appParams.getKeyword().equalsIgnoreCase(newClef)) {
				appParams.setKeyword(newClef).save();
			}
		}
		btnDownload.setEnabled(!appParams.getResourceUrl().isEmpty());
	}

	private void openUserFolder() {
		try {
			String userFolder = appParams.getUserFolder();
			if (FileDownloader.createFolder(userFolder)) {
				String cmd = "explorer.exe " + appParams.getUserFolder();
				Runtime.getRuntime().exec(cmd);
			} else {
				JOptionPane.showMessageDialog(null, "Le dossier \"" + userFolder + "\" n'a pas pu être créé.",
						"Echec de création de dossier", JOptionPane.OK_OPTION | JOptionPane.ERROR_MESSAGE);
			}
		} catch (IOException ex) {
			Logger.getLogger(ResourceDownloaderFrame.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void downloadFiles() throws InterruptedException {
		String[] filesArr = null;
		try {
			filesArr = FileDownloader.getFilesList(appParams.getLastUrlAddress(), appParams.getKeyword());
		} catch (IOException | InterruptedException e) {
			JOptionPane.showMessageDialog(null,
					"Ne peut pas trouver la liste de fichiers à télécharger :\n" + e.toString(), "Erreur de connexion",
					JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
			return;
		}
		if (filesArr.length == 0) {
			JOptionPane.showMessageDialog(null, "Aucun fichier à télécharger. Mot Clef incorrect ou inexistant.",
					"Erreur de téléchargment", JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
			return;
		}
		String errors = "";
		boolean ask = true;
		int downloaded = 0, failed = 0;
		for (String filename : filesArr) {
			String localPath = Paths.get(appParams.getUserFolder(), filename).toString();
			if (ask && FileDownloader.fileExists(localPath)) {
				String[] options = { "Oui pour tous", "Oui", "Non" };
				int confirm = JOptionPane.showOptionDialog(null,
						"Le fichier '" + filename + "' existe déjà.\nVoulez-vous le télécharger une nouvelle fois ?",
						"Ecraser fichier existant", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
						null, options, options[2]);
				if (confirm == JOptionPane.YES_OPTION) {
					ask = false;
				} else if (confirm == JOptionPane.CANCEL_OPTION) {
					continue;
				}
			}
			try {
				downloaded++;
				FileDownloader.downloadFile(appParams.getLastUrlAddress(), appParams.getKeyword(), filename, localPath);
			} catch (IOException e) {
				failed++;
				errors += "Le fichier '" + filename + "' n'a pas été téléchargé !\n";
			}
		}
		if (downloaded == 0) {
			JOptionPane.showMessageDialog(null, "Aucun fichier à télécharger !", "Fin téléchargement",
					JOptionPane.INFORMATION_MESSAGE | JOptionPane.OK_OPTION);
		} else if (failed == 0) {
			JOptionPane.showMessageDialog(null,
					downloaded + "/" + filesArr.length + " fichiers ont été téléchargés avec succes !",
					"Fin téléchargement", JOptionPane.INFORMATION_MESSAGE | JOptionPane.OK_OPTION);
		} else {
			JOptionPane.showMessageDialog(null,
					failed + "/" + filesArr.length + " fichiers n'ont pas pu être téléchargé :\n" + errors,
					"Fin téléchargement", JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
		}
	}

	private void enableAllControls(boolean enabled) {
		progressBar.setVisible(!enabled);
		progressBar.setIndeterminate(!enabled);
		txtURL.setEnabled(enabled);
		txtClef.setEnabled(enabled);
		txtFolder.setEnabled(enabled);
		txtName.setEnabled(enabled);
		btnDownload.setEnabled(enabled);
	}

	@Override
	public void run() {
		try {
			downloadFiles();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				enableAllControls(true);
			}
		});
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
	// <editor-fold defaultstate="collapsed" desc="Generated
	// Code">//GEN-BEGIN:initComponents
	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		jButton1 = new javax.swing.JButton();
		lblTitle = new javax.swing.JLabel();
		lblURL = new javax.swing.JLabel();
		txtURL = new javax.swing.JTextField();
		lblClef = new javax.swing.JLabel();
		txtClef = new javax.swing.JTextField();
		lblName = new javax.swing.JLabel();
		txtName = new javax.swing.JTextField();
		lblFolder = new javax.swing.JLabel();
		txtFolder = new javax.swing.JTextField();
		btnChangeFolder = new javax.swing.JButton();
		btnDownload = new javax.swing.JButton();
		btnOpenFolder = new javax.swing.JButton();
		progressBar = new javax.swing.JProgressBar();

		jButton1.setText("jButton1");

		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Resources Downloader");
		java.awt.GridBagLayout layout = new java.awt.GridBagLayout();
		layout.columnWidths = new int[] { 100, 300, 100 };
		layout.columnWeights = new double[] { 1.0, 3.0, 1.0 };
		layout.rowWeights = new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0 };
		getContentPane().setLayout(layout);

		lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
		lblTitle.setForeground(new java.awt.Color(0, 102, 0));
		lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		lblTitle.setText("Resources Downloader");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.ipady = 15;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		getContentPane().add(lblTitle, gridBagConstraints);

		lblURL.setText("Adresse URL");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 1;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		getContentPane().add(lblURL, gridBagConstraints);

		txtURL.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				txtURLFocusLost(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		getContentPane().add(txtURL, gridBagConstraints);

		lblClef.setText("Mot clef");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		getContentPane().add(lblClef, gridBagConstraints);

		txtClef.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				txtClefFocusLost(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		getContentPane().add(txtClef, gridBagConstraints);

		lblName.setText("Nom et prénom");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 3;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		getContentPane().add(lblName, gridBagConstraints);

		txtName.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusLost(java.awt.event.FocusEvent evt) {
				txtNameFocusLost(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		getContentPane().add(txtName, gridBagConstraints);

		lblFolder.setText("Dossier local");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		getContentPane().add(lblFolder, gridBagConstraints);

		txtFolder.setEditable(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		getContentPane().add(txtFolder, gridBagConstraints);

		btnChangeFolder.setText("Changer...");
		btnChangeFolder.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnChangeFolderActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 4;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		getContentPane().add(btnChangeFolder, gridBagConstraints);

		btnDownload.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
		btnDownload.setText("Télécharger les fichiers...");
		btnDownload.setEnabled(false);
		btnDownload.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnDownloadActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		getContentPane().add(btnDownload, gridBagConstraints);

		btnOpenFolder.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
		btnOpenFolder.setText("Ouvrir le dossier...");
		btnOpenFolder.setEnabled(false);
		btnOpenFolder.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnOpenFolderActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 6;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		getContentPane().add(btnOpenFolder, gridBagConstraints);

		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setIndeterminate(false);
		progressBar.setValue(-1);
		progressBar.setVisible(false);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridy = 7;
		gridBagConstraints.gridwidth = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
		getContentPane().add(progressBar, gridBagConstraints);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void btnChangeFolderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnChangeFolderActionPerformed
		File selectedFile = selectFolder("Choix du dossier de travail", ".");
		if (selectedFile != null) {
			System.out.println(selectedFile.getAbsoluteFile());
		}
	}// GEN-LAST:event_btnChangeFolderActionPerformed

	private void txtURLFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtURLFocusLost
		setHostUrl(txtURL.getText());
	}// GEN-LAST:event_txtURLFocusLost

	private void txtClefFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtClefFocusLost
		setClef(txtClef.getText());
	}// GEN-LAST:event_txtClefFocusLost

	private void txtNameFocusLost(java.awt.event.FocusEvent evt) {// GEN-FIRST:event_txtNameFocusLost
		String newUserName = StringUtil.filterLetters(StringUtil.removeSpaces(txtName.getText()));
		setUserName(newUserName);
	}// GEN-LAST:event_txtNameFocusLost

	private void btnOpenFolderActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnOpenFolderActionPerformed
		openUserFolder();
	}// GEN-LAST:event_btnOpenFolderActionPerformed

	private void btnDownloadActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDownloadActionPerformed
		enableAllControls(false);
		Thread thread = new Thread(this);
		thread.start();
	}// GEN-LAST:event_btnDownloadActionPerformed

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
		// (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(ResourceDownloaderFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(ResourceDownloaderFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ResourceDownloaderFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(ResourceDownloaderFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

// FileDownloader.loadFileFromUrl(theUrl, filePath);

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new ResourceDownloaderFrame().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton btnChangeFolder;
	private javax.swing.JButton btnDownload;
	private javax.swing.JButton btnOpenFolder;
	private javax.swing.JButton jButton1;
	private javax.swing.JLabel lblClef;
	private javax.swing.JLabel lblFolder;
	private javax.swing.JLabel lblName;
	private javax.swing.JLabel lblTitle;
	private javax.swing.JLabel lblURL;
	private javax.swing.JTextField txtClef;
	private javax.swing.JTextField txtFolder;
	private javax.swing.JTextField txtName;
	private javax.swing.JTextField txtURL;
	private javax.swing.JProgressBar progressBar;
	// End of variables declaration//GEN-END:variables
}
