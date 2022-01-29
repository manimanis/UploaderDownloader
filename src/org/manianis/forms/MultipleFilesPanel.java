package org.manianis.forms;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class MultipleFilesPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblListTitle;
	private JList<FileEntry> listFiles;
	private JButton btnInsert;
	private JButton btnDelete;
	private JScrollPane scrollPane;
	private JLabel lblFilesCount;

	private ListModel<FileEntry> filesModel = new ListModel<>();
	private boolean acceptMultipleFiles = true;

	/**
	 * Create the panel.
	 */
	public MultipleFilesPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 150, 150, 150, 150 };
		gridBagLayout.rowHeights = new int[] { 30, 40, 30 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0, 1.0, 1.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 5.0, 0.0 };
		setLayout(gridBagLayout);

		lblListTitle = new JLabel("Liste des fichiers à exporter");
		lblListTitle.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblListTitle.setHorizontalAlignment(SwingConstants.CENTER);
		GridBagConstraints gbc_lblListTitle = new GridBagConstraints();
		gbc_lblListTitle.insets = new Insets(5, 5, 5, 0);
		gbc_lblListTitle.fill = GridBagConstraints.BOTH;
		gbc_lblListTitle.gridwidth = 4;
		gbc_lblListTitle.gridx = 0;
		gbc_lblListTitle.gridy = 0;
		add(lblListTitle, gbc_lblListTitle);

		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);

		listFiles = new JList<>(filesModel);
		listFiles.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				listFilesSelectionChanged(e);
			}
		});
		scrollPane.setViewportView(listFiles);

		btnInsert = new JButton("Ajouter...");
		GridBagConstraints gbc_btnInsert = new GridBagConstraints();
		gbc_btnInsert.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnInsert.insets = new Insets(5, 5, 5, 5);
		gbc_btnInsert.gridx = 0;
		gbc_btnInsert.gridy = 2;
		add(btnInsert, gbc_btnInsert);

		btnDelete = new JButton("Supprimer...");
		btnDelete.setEnabled(false);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnDeleteClicked();
			}
		});
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.insets = new Insets(5, 5, 5, 5);
		gbc_btnDelete.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDelete.gridx = 1;
		gbc_btnDelete.gridy = 2;
		add(btnDelete, gbc_btnDelete);

		lblFilesCount = new JLabel("");
		GridBagConstraints gbc_lblFilesCount = new GridBagConstraints();
		gbc_lblFilesCount.anchor = GridBagConstraints.WEST;
		gbc_lblFilesCount.gridwidth = 2;
		gbc_lblFilesCount.insets = new Insets(0, 0, 5, 5);
		gbc_lblFilesCount.gridx = 2;
		gbc_lblFilesCount.gridy = 2;
		add(lblFilesCount, gbc_lblFilesCount);

		initPanel();
	}

	protected void btnDeleteClicked() {
		int[] selectedIndices = listFiles.getSelectedIndices();
		for (int i = selectedIndices.length - 1; i >= 0; i--) {
			filesModel.remove(selectedIndices[i]);
		}
		updateFilesCount();
	}

	protected void listFilesSelectionChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			btnDelete.setEnabled(listFiles.getSelectedIndex() != -1);
		}
	}

	private void initPanel() {
		updateFilesCount();
		DropTarget dropTarget = new DropTarget() {
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt.getTransferable()
							.getTransferData(DataFlavor.javaFileListFlavor);
					addFileList(droppedFiles);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		};
		listFiles.setDropTarget(dropTarget);
		// lblDropFiles.setDropTarget(dropTarget);
	}

	public void updateFilesCount() {
		lblFilesCount.setText(String.format("Nombre de fichiers : %d", filesModel.size()));
	}

	/**
	 * @return the acceptMultipleFiles
	 */
	public boolean isAcceptMultipleFiles() {
		return acceptMultipleFiles;
	}

	/**
	 * @param acceptMultipleFiles the acceptMultipleFiles to set
	 */
	public void setAcceptMultipleFiles(boolean acceptMultipleFiles) {
		this.acceptMultipleFiles = acceptMultipleFiles;
		if (!acceptMultipleFiles) {
			while (filesModel.size() > 1) {
				filesModel.remove(0);
			}
		}
		updateFilesCount();
	}

	public void addFile(File newFile) {
		FileEntry newFileEntry = new FileEntry(newFile.getAbsolutePath());
		if (acceptMultipleFiles) {
			if (!filesModel.contains(newFileEntry)) {
				filesModel.add(newFileEntry);
			}
		} else {
			filesModel.clear();
			filesModel.add(newFileEntry);
		}
		updateFilesCount();
	}

	public void addFileList(List<File> manyFiles) {
		if (acceptMultipleFiles) {
			for (File file : manyFiles) {
				FileEntry newFileEntry = new FileEntry(file.getAbsolutePath());
				if (!filesModel.contains(newFileEntry)) {
					filesModel.add(newFileEntry);
				}

			}
		} else {
			FileEntry newFileEntry = new FileEntry(manyFiles.get(manyFiles.size() - 1).getAbsolutePath());
			filesModel.clear();
			filesModel.add(newFileEntry);
		}
		updateFilesCount();
	}

	public List<FileEntry> getFilesList() {
		return filesModel.subList(0, filesModel.size());
	}

	public void reset() {
		filesModel.clear();
		updateFilesCount();
	}
}
