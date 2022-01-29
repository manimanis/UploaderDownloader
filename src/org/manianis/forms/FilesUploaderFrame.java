package org.manianis.forms;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.manianis.ApplicationsParameters;
import org.manianis.forms.SourceCodePanel.SupportedLanguage;
import org.manianis.utils.StringUtil;

public class FilesUploaderFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblNomPrenom;
	private MultipleFilesPanel panelSelectFiles;
	private SourceCodePanel panelSourceCode;
	private JComboBox<SupportedLanguage> txtLanguage;
	private JTextField txtURL;
	private JTextField txtNomPrenom;
	private JComboBox<String> txtClasse;
	private JLabel lblUploadResults;

	private boolean b_isValidUploadUrl = false;
	private boolean b_isSendingFiles = false;
	private boolean b_canSubmit = false;
	private JLabel lblNewLabel;
	private JButton btnSubmit;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FilesUploaderFrame frame = new FilesUploaderFrame();
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
	public FilesUploaderFrame() {
		setTitle("Files Uploader");

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 630, 530);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50 };
		gbl_contentPane.rowHeights = new int[] { 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30, 30 };
		gbl_contentPane.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPane.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0 };
		contentPane.setLayout(gbl_contentPane);

		lblNewLabel = new JLabel("Adresse URL");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.gridwidth = 3;
		gbc_lblNewLabel.insets = new Insets(5, 5, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);

		txtURL = new JTextField();
		txtURL.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				updateUploadUrl(txtURL.getText());
			}
		});
		GridBagConstraints gbc_txtURL = new GridBagConstraints();
		gbc_txtURL.gridy = 0;
		gbc_txtURL.insets = new Insets(5, 5, 5, 5);
		gbc_txtURL.gridwidth = 10;
		gbc_txtURL.gridx = 3;
		gbc_txtURL.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(txtURL, gbc_txtURL);

		lblNomPrenom = new JLabel("Nom & Prénom");
		GridBagConstraints gbc_lblNomPrenom = new GridBagConstraints();
		gbc_lblNomPrenom.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNomPrenom.gridwidth = 3;
		gbc_lblNomPrenom.insets = new Insets(5, 5, 5, 5);
		gbc_lblNomPrenom.gridx = 0;
		gbc_lblNomPrenom.gridy = 1;
		contentPane.add(lblNomPrenom, gbc_lblNomPrenom);

		txtNomPrenom = new JTextField();
		txtNomPrenom.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				updateSubmitButton();
			}
		});
		GridBagConstraints gbc_txtNomPrenom = new GridBagConstraints();
		gbc_txtNomPrenom.gridy = 1;
		gbc_txtNomPrenom.insets = new Insets(5, 5, 5, 0);
		gbc_txtNomPrenom.gridwidth = 10;
		gbc_txtNomPrenom.gridx = 3;
		gbc_txtNomPrenom.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(txtNomPrenom, gbc_txtNomPrenom);

		JLabel lblClasse = new JLabel("Classe");
		GridBagConstraints gbc_lblClasse = new GridBagConstraints();
		gbc_lblClasse.anchor = GridBagConstraints.WEST;
		gbc_lblClasse.gridwidth = 3;
		gbc_lblClasse.insets = new Insets(5, 5, 5, 5);
		gbc_lblClasse.gridx = 0;
		gbc_lblClasse.gridy = 2;
		contentPane.add(lblClasse, gbc_lblClasse);

		txtClasse = new JComboBox<String>();
		txtClasse.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				updateSubmitButton();
			}
		});
		txtClasse.setModel(new DefaultComboBoxModel<>(new String[] { "", "2TI", "4ECO", "4T" }));
		GridBagConstraints gbc_txtClasse = new GridBagConstraints();
		gbc_txtClasse.gridy = 2;
		gbc_txtClasse.insets = new Insets(5, 5, 5, 0);
		gbc_txtClasse.gridwidth = 10;
		gbc_txtClasse.gridx = 3;
		gbc_txtClasse.fill = GridBagConstraints.HORIZONTAL;
		contentPane.add(txtClasse, gbc_txtClasse);

		ButtonGroup buttonGroup = new ButtonGroup();

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.gridwidth = 13;
		gbc_panel.insets = new Insets(5, 5, 5, 0);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 0;
		gbc_panel.gridy = 3;
		contentPane.add(panel, gbc_panel);
		panel.setLayout(new GridLayout(1, 4, 5, 5));

		JLabel lblObjectType = new JLabel("Type de l'objet");
		lblObjectType.setFont(new Font("Tahoma", Font.BOLD, 11));
		panel.add(lblObjectType);

		JRadioButton rdManyFiles = new JRadioButton("Des fichiers");
		rdManyFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayFilesSelector();
			}
		});
		panel.add(rdManyFiles);

		JRadioButton rdSourceCode = new JRadioButton("Code source");
		rdSourceCode.setSelected(true);
		rdSourceCode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayCodeEditor();
			}
		});
		panel.add(rdSourceCode);
		buttonGroup.add(rdManyFiles);
		buttonGroup.add(rdSourceCode);

		txtLanguage = new JComboBox<SupportedLanguage>();
		txtLanguage.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				updateSelectedLanguage();
			}
		});
		txtLanguage.setModel(new DefaultComboBoxModel<SupportedLanguage>(SourceCodePanel.SupportedLanguage.LANGUAGES));
		panel.add(txtLanguage);

		panelSelectFiles = new MultipleFilesPanel();
		panelSelectFiles.setVisible(false);
		GridBagConstraints gbc_panelSelectFiles = new GridBagConstraints();
		gbc_panelSelectFiles.insets = new Insets(5, 5, 5, 5);
		gbc_panelSelectFiles.fill = GridBagConstraints.BOTH;
		gbc_panelSelectFiles.gridheight = 7;
		gbc_panelSelectFiles.gridwidth = 12;
		gbc_panelSelectFiles.gridx = 0;
		gbc_panelSelectFiles.gridy = 4;
		contentPane.add(panelSelectFiles, gbc_panelSelectFiles);

		panelSourceCode = new SourceCodePanel();
		GridBagConstraints gbc_panelSourceCode = new GridBagConstraints();
		gbc_panelSourceCode.insets = new Insets(5, 5, 5, 5);
		gbc_panelSourceCode.fill = GridBagConstraints.BOTH;
		gbc_panelSourceCode.gridheight = 7;
		gbc_panelSourceCode.gridwidth = 12;
		gbc_panelSourceCode.gridx = 0;
		gbc_panelSourceCode.gridy = 4;
		contentPane.add(panelSourceCode, gbc_panelSourceCode);

		btnSubmit = new JButton("Soumettre...");
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				submitFiles();
			}
		});
		GridBagConstraints gbc_btnSubmit = new GridBagConstraints();
		gbc_btnSubmit.fill = GridBagConstraints.BOTH;
		gbc_btnSubmit.gridwidth = 4;
		gbc_btnSubmit.insets = new Insets(5, 5, 0, 5);
		gbc_btnSubmit.gridx = 0;
		gbc_btnSubmit.gridy = 11;
		contentPane.add(btnSubmit, gbc_btnSubmit);

		JProgressBar progressBar = new JProgressBar();
		GridBagConstraints gbc_progressBar = new GridBagConstraints();
		gbc_progressBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_progressBar.gridwidth = 5;
		gbc_progressBar.insets = new Insets(5, 5, 0, 5);
		gbc_progressBar.gridx = 4;
		gbc_progressBar.gridy = 11;
		contentPane.add(progressBar, gbc_progressBar);

		lblUploadResults = new JLabel("Non enyoyé");
		lblUploadResults.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblUploadResults.setHorizontalAlignment(SwingConstants.CENTER);
		lblUploadResults.setOpaque(true);
		lblUploadResults.setForeground(new Color(255, 250, 250));
		lblUploadResults.setBackground(new Color(139, 0, 0));
		GridBagConstraints gbc_lblUploadResults = new GridBagConstraints();
		gbc_lblUploadResults.fill = GridBagConstraints.BOTH;
		gbc_lblUploadResults.gridwidth = 4;
		gbc_lblUploadResults.insets = new Insets(5, 5, 0, 0);
		gbc_lblUploadResults.gridx = 9;
		gbc_lblUploadResults.gridy = 11;
		contentPane.add(lblUploadResults, gbc_lblUploadResults);

		setFileIsSent(false);

		updateUploadUrl(ApplicationsParameters.getInstance().getUploadUrl());
	}
	
	private void updateSubmitButton() {
		String nomPrenom = StringUtil.removeSpaces(StringUtil.filterLetters(txtNomPrenom.getText()));
		String classe = txtClasse.getSelectedItem().toString();
		b_canSubmit = b_isValidUploadUrl && !nomPrenom.isEmpty() && !classe.isEmpty();
		btnSubmit.setEnabled(b_canSubmit);
	}

	protected void updateUploadUrl(String uploadUrl) {
		String oldUploadUrl = ApplicationsParameters.getInstance().getUploadUrl();
		txtURL.setText(uploadUrl);
		b_isValidUploadUrl = isValidUploadUrl(uploadUrl); 
		if (b_isValidUploadUrl && !uploadUrl.equals(oldUploadUrl)) {
			ApplicationsParameters.getInstance().setUploadUrl(uploadUrl).save();
		}
		updateSubmitButton();
	}

	protected void submitFiles() {
		setFileIsSent(false);
		String uploadUrl = txtURL.getText();
		String nomPrenom = StringUtil.removeSpaces(StringUtil.filterLetters(txtNomPrenom.getText()));
		String classe = txtClasse.getSelectedItem().toString();
		String error = "";
		if (b_canSubmit) {
			if (b_isSendingFiles) {
				int filesCount = panelSelectFiles.getFilesList().size();
				if (filesCount == 0) {
					error += "Indiquer les fichiers à soumettre.\n";
				}
			} else {
				int charCount = panelSourceCode.getSourceCode().length();
				if (charCount == 0) {
					error += "Copier/coller le code source à soumettre.\n";
				}
			}
		}
		if (!error.isEmpty()) {
			JOptionPane.showMessageDialog(null, error, "Erreur d'envoi",
					JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
			return;
		}

		SupportedLanguage language = panelSourceCode.getSupportedLanguage();
		String sourceCode = panelSourceCode.getSourceCode();
		List<FileEntry> files = null;
		if (b_isSendingFiles) {
			files = panelSelectFiles.getFilesList();
		} else {
			Path sourceFile = null;
			try {
				sourceFile = createSourceFile(sourceCode, language, nomPrenom, classe);
			} catch (IOException e) {
				error = "Ne peut pas créer le fichier du code source.";
				JOptionPane.showMessageDialog(null, error, "Erreur d'envoi",
						JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
				return;
			}
			files = List.of(new FileEntry(sourceFile.toString()));
		}
		uploadUserCode(uploadUrl, nomPrenom, classe, files);
	}

	protected void updateSelectedLanguage() {
		panelSourceCode.setSupportedLanguage((SupportedLanguage) txtLanguage.getSelectedItem());
	}

	protected void displayCodeEditor() {
		b_isSendingFiles = false;
		txtLanguage.setEnabled(true);
		panelSourceCode.setVisible(true);
		panelSelectFiles.setVisible(false);
	}

	protected void displayFilesSelector() {
		b_isSendingFiles = true;
		txtLanguage.setEnabled(false);
		panelSourceCode.setVisible(false);
		panelSelectFiles.setVisible(true);
	}

	private void uploadUserCode(String uploadUrl, String nomPrenom, String classe, List<FileEntry> files) {
		Path zipFile = null;
		String res = "";
		try {
			zipFile = createZipFile(files);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Ne peut pas compresser les fichiers indiqués :\n" + e.getLocalizedMessage(),
					"Erreur de compression", JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
			return;
		}

		try {
			res = sendData(uploadUrl, nomPrenom, classe, zipFile.toString());
		} catch (IOException | ParseException e) {
			JOptionPane.showMessageDialog(null, "Ne peut pas soumettre les fichiers :\n" + e.getLocalizedMessage(),
					"Erreur de compression", JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
			return;
		}

		JSONObject resJson = new JSONObject(res);
		if (resJson.has("error")) {
			JLabel errorLabel = new JLabel("<html><body>" + resJson.get("error") + "</body></html>");
			JOptionPane.showMessageDialog(null, errorLabel, "Erreur d'upload",
					JOptionPane.ERROR_MESSAGE | JOptionPane.OK_OPTION);
		} else {
			setFileIsSent(true);
			JLabel errorLabel = new JLabel("<html><body>" + resJson.get("success") + "</body></html>");
			JOptionPane.showMessageDialog(null, errorLabel, "Upload terminé",
					JOptionPane.INFORMATION_MESSAGE | JOptionPane.OK_OPTION);
		}
	}

	private void setFileIsSent(boolean isSent) {
		if (!isSent) {
			lblUploadResults.setBackground(new Color(139, 0, 0));
			lblUploadResults.setText("Non envoyé");
		} else {
			lblUploadResults.setBackground(new Color(0, 139, 0));
			lblUploadResults.setText("Succès");
			txtNomPrenom.setText("");
			txtClasse.setSelectedItem("");
			panelSelectFiles.reset();
			panelSourceCode.reset();
		}
		updateSubmitButton();
	}

	public Path createSourceFile(String sourceCode, SupportedLanguage supportedLanguage, String nomPrenom,
			String classe) throws IOException {
		Path sourceFile = Files.createTempFile("source_code", supportedLanguage.getFileExtension());
		FileOutputStream fos = new FileOutputStream(sourceFile.toFile());
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-MM HH:mm:ss.SSS");
		fos.write(SupportedLanguage.addComment(supportedLanguage, String.format("Nom & Prénom : %s\n", nomPrenom))
				.getBytes("utf-8"));
		fos.write(SupportedLanguage.addComment(supportedLanguage, String.format("Classe : %s\n", classe))
				.getBytes("utf-8"));
		fos.write(SupportedLanguage
				.addComment(supportedLanguage, String.format("Date & heure : %s\n\n", dateFormat.format(cal.getTime())))
				.getBytes("utf-8"));
		fos.write(sourceCode.getBytes("utf-8"));
		fos.close();
		return sourceFile;
	}

	public Path createZipFile(List<FileEntry> files) throws IOException {
		Path zipFile = Files.createTempFile("tempfile", "tmp.zip");
		FileOutputStream fos = new FileOutputStream(zipFile.toFile());
		ZipOutputStream zipOut = new ZipOutputStream(fos);
		for (FileEntry srcFile : files) {
			addZipEntry(srcFile.getFilePath(), srcFile.getFileName(), zipOut);
		}
		zipOut.close();
		fos.close();

		return zipFile;
	}

	public void addZipEntry(String srcFile, String filename, ZipOutputStream zipOut) throws IOException {
		File fileToZip = new File(srcFile);
		if (fileToZip.isDirectory()) {
			if (filename.endsWith("/")) {
				zipOut.putNextEntry(new ZipEntry(filename));
				zipOut.closeEntry();
			} else {
				zipOut.putNextEntry(new ZipEntry(filename + "/"));
				zipOut.closeEntry();
			}
			File[] children = fileToZip.listFiles();
			for (File childFile : children) {
				addZipEntry(childFile.toString(), filename + "/" + childFile.getName(), zipOut);
			}
			return;
		}
		FileInputStream fis = new FileInputStream(fileToZip);
		ZipEntry zipEntry = new ZipEntry(filename);
		zipOut.putNextEntry(zipEntry);
		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zipOut.write(bytes, 0, length);
		}
		fis.close();
	}

	public String sendData(String destUrl, String nomPrenom, String classe, String filePath)
			throws IOException, ParseException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(destUrl);

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("nom_prenom", nomPrenom);
		builder.addTextBody("classe", classe);
		builder.addTextBody("upload", "upload");
		builder.addBinaryBody("files", new File(filePath), ContentType.APPLICATION_OCTET_STREAM, "file.zip");

		HttpEntity multipart = builder.build();
		httpPost.setEntity(multipart);

		CloseableHttpResponse response = client.execute(httpPost);
		String res = EntityUtils.toString(response.getEntity(), "UTF-8");
		client.close();

		return res;
	}

	public boolean isValidUploadUrl(String destUrl) {
		if (destUrl.isEmpty()) {
			return false;
		}
		try {
			JSONObject res = sendHelloServer(destUrl);
			return res.has("success");
		} catch (Exception e) {
			return false;
		}
	}

	public JSONObject sendHelloServer(String destUrl) throws IOException, ParseException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(destUrl);

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("hello", "hello");

		HttpEntity multipart = builder.build();
		httpPost.setEntity(multipart);

		CloseableHttpResponse response = client.execute(httpPost);
		String res = EntityUtils.toString(response.getEntity(), "UTF-8");
		client.close();

		try {
			JSONObject jsonRes = new JSONObject(res);
			return jsonRes;
		} 
		catch (JSONException e) {
			return new JSONObject().put("error", "Erreur");
		}
	}

}
