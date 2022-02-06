package org.manianis.forms;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.border.SoftBevelBorder;

import org.manianis.utils.FolderUtil;

import javax.swing.border.BevelBorder;
import javax.swing.JScrollPane;

public class CleanWorkingFolder extends JFrame {

	private JPanel contentPane;
	private final ButtonGroup buttonGroup = new ButtonGroup();
	private JLabel lblFoldersToClean;
	private JCheckBox chkDesktop;
	private JCheckBox chkDocuments;
	private JCheckBox chkDownloads;
	private JCheckBox chkOthers;
	private JButton btnAddFolder;
	private JButton btnRemoveFolder;
	private JList listFolders;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CleanWorkingFolder frame = new CleanWorkingFolder();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CleanWorkingFolder() {
		setTitle("Nettoyage Windows");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 583, 454);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{516, 0};
		gbl_contentPane.rowHeights = new int[] {300};
		gbl_contentPane.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0};
		contentPane.setLayout(gbl_contentPane);
		
		JPanel panel = new JPanel();
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {98, 98, 98, 98, 98};
		gbl_panel.rowHeights = new int[] {23, 23, 23, 23};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 0.0, 1.0, 1.0};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0};
		panel.setLayout(gbl_panel);
		
		lblFoldersToClean = new JLabel("Dossiers à nettoyer");
		GridBagConstraints gbc_lblFoldersToClean = new GridBagConstraints();
		gbc_lblFoldersToClean.fill = GridBagConstraints.BOTH;
		gbc_lblFoldersToClean.insets = new Insets(5, 5, 5, 5);
		gbc_lblFoldersToClean.gridx = 0;
		gbc_lblFoldersToClean.gridy = 0;
		panel.add(lblFoldersToClean, gbc_lblFoldersToClean);
		
		chkDesktop = new JCheckBox("Bureau");
		chkDesktop.setSelected(true);
		GridBagConstraints gbc_chkDesktop = new GridBagConstraints();
		gbc_chkDesktop.fill = GridBagConstraints.BOTH;
		gbc_chkDesktop.insets = new Insets(5, 5, 5, 5);
		gbc_chkDesktop.gridx = 1;
		gbc_chkDesktop.gridy = 0;
		panel.add(chkDesktop, gbc_chkDesktop);
		
		chkDocuments = new JCheckBox("Documents");
		GridBagConstraints gbc_chkDocuments = new GridBagConstraints();
		gbc_chkDocuments.fill = GridBagConstraints.BOTH;
		gbc_chkDocuments.insets = new Insets(5, 5, 5, 5);
		gbc_chkDocuments.gridx = 2;
		gbc_chkDocuments.gridy = 0;
		panel.add(chkDocuments, gbc_chkDocuments);
		
		chkDownloads = new JCheckBox("Téléchargements");
		GridBagConstraints gbc_chkDownloads = new GridBagConstraints();
		gbc_chkDownloads.fill = GridBagConstraints.BOTH;
		gbc_chkDownloads.insets = new Insets(5, 5, 5, 5);
		gbc_chkDownloads.gridx = 3;
		gbc_chkDownloads.gridy = 0;
		panel.add(chkDownloads, gbc_chkDownloads);
		
		chkOthers = new JCheckBox("Autres");
		GridBagConstraints gbc_chkOthers = new GridBagConstraints();
		gbc_chkOthers.fill = GridBagConstraints.BOTH;
		gbc_chkOthers.insets = new Insets(5, 5, 5, 0);
		gbc_chkOthers.gridx = 4;
		gbc_chkOthers.gridy = 0;
		panel.add(chkOthers, gbc_chkOthers);
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		
		btnAddFolder = new JButton("Ajouter...");
		GridBagConstraints gbc_btnAddFolder = new GridBagConstraints();
		gbc_btnAddFolder.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAddFolder.insets = new Insets(5, 5, 5, 5);
		gbc_btnAddFolder.gridx = 4;
		gbc_btnAddFolder.gridy = 1;
		panel.add(btnAddFolder, gbc_btnAddFolder);
		
		btnRemoveFolder = new JButton("Supprimer...");
		GridBagConstraints gbc_btnRemoveFolder = new GridBagConstraints();
		gbc_btnRemoveFolder.insets = new Insets(5, 5, 5, 5);
		gbc_btnRemoveFolder.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRemoveFolder.gridx = 4;
		gbc_btnRemoveFolder.gridy = 2;
		panel.add(btnRemoveFolder, gbc_btnRemoveFolder);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(5, 5, 5, 5);
		gbc_scrollPane.gridheight = 3;
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		panel.add(scrollPane, gbc_scrollPane);
		
		listFolders = new JList();
		scrollPane.setViewportView(listFolders);
		listFolders.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		
		JPanel panel_1 = new JPanel();
		
		JLabel lblCleanProcedure = new JLabel("Procédure de nettoyage");
		
		JRadioButton rdMoveToFolder = new JRadioButton("Regrouper dans un dossier");
		rdMoveToFolder.setSelected(true);
		buttonGroup.add(rdMoveToFolder);
		
		JRadioButton rdRemove = new JRadioButton("Supprimer définitivement");
		buttonGroup.add(rdRemove);
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 1;
		contentPane.add(panel_1, gbc_panel_1);
		panel_1.setLayout(new GridLayout(1, 3, 5, 5));
		panel_1.add(lblCleanProcedure);
		panel_1.add(rdMoveToFolder);
		panel_1.add(rdRemove);
		initInterface();
	}
	
	private void initInterface() {
		System.out.println(FolderUtil.getDesktopFolder());
		System.out.println(FolderUtil.getDownloadsFolder());
		System.out.println(FolderUtil.getDocumentsFolder());
		System.out.println(FolderUtil.getHomeFolder());
	}

}
