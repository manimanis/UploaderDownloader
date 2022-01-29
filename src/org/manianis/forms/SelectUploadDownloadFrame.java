package org.manianis.forms;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SelectUploadDownloadFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SelectUploadDownloadFrame frame = new SelectUploadDownloadFrame();
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
	public SelectUploadDownloadFrame() {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(2, 1, 7, 7));
		
		JButton btnDownloadFrame = new JButton("Download Files...");
		btnDownloadFrame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainFrame mainFrame = new MainFrame();
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
	}

}
