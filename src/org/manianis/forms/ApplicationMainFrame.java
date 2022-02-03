package org.manianis.forms;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ApplicationMainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnCleanFolders;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ApplicationMainFrame frame = new ApplicationMainFrame();
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
	public ApplicationMainFrame() {
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
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(3, 1, 7, 7));
		
		JButton btnDownloadFrame = new JButton("Download Files...");
		btnDownloadFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResourceDownloaderFrame mainFrame = new ResourceDownloaderFrame();
				mainFrame.setVisible(true);
			}
		});
		btnDownloadFrame.setFont(new Font("Tahoma", Font.PLAIN, 32));
		btnDownloadFrame.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(btnDownloadFrame);
		
		JButton btnUploadFrame = new JButton("Upload Files...");
		btnUploadFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FilesUploaderFrame uploaderFrame = new FilesUploaderFrame();
				uploaderFrame.setVisible(true);
			}
		});
		btnUploadFrame.setFont(new Font("Tahoma", Font.PLAIN, 32));
		btnUploadFrame.setAlignmentX(Component.CENTER_ALIGNMENT);
		contentPane.add(btnUploadFrame);
		
		btnCleanFolders = new JButton("Clean Folders...");
		btnCleanFolders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CleanWorkingFolder cleanFolder = new CleanWorkingFolder();
				cleanFolder.setVisible(true);
			}
		});
		btnCleanFolders.setFont(new Font("Tahoma", Font.PLAIN, 32));
		btnCleanFolders.setAlignmentX(0.5f);
		contentPane.add(btnCleanFolders);
	}

}
