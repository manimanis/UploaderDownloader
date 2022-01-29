package org.manianis.forms;

import javax.swing.JPanel;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import java.awt.BorderLayout;

public class SourceCodePanel extends JPanel {

	public enum SupportedLanguage {
		LANGUAGE_PYTHON("Python", SyntaxConstants.SYNTAX_STYLE_PYTHON, ".py", "#", ""),
		LANGUAGE_HTML("HTML", SyntaxConstants.SYNTAX_STYLE_HTML, ".html", "<!--", "-->"),
		LANGUAGE_CSS("CSS", SyntaxConstants.SYNTAX_STYLE_CSS, ".css", "/*", "*/"),
		LANGUAGE_JAVASCRIPT("JavaScript", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT, ".js", "//", ""),
		LANGUAGE_PHP("PHP", SyntaxConstants.SYNTAX_STYLE_PHP, ".php", "//", ""),
		LANGUAGE_SQL("SQL", SyntaxConstants.SYNTAX_STYLE_SQL, ".sql", "\"", ""),
		LANGUAGE_OTHER("Autre", SyntaxConstants.SYNTAX_STYLE_NONE, ".txt", "//", "");

		public static final SupportedLanguage[] LANGUAGES = new SupportedLanguage[] { LANGUAGE_PYTHON, LANGUAGE_HTML,
				LANGUAGE_CSS, LANGUAGE_JAVASCRIPT, LANGUAGE_PHP, LANGUAGE_SQL, LANGUAGE_OTHER };

		private String languageName;
		private String syntaxConstant;
		private String fileExtension;
		private String lineCommentsStart;
		private String lineCommentsEnd;

		private SupportedLanguage() {
		}

		private SupportedLanguage(String languageName, String syntaxConstant, String fileExtension,
				String lineCommentsStart, String lineCommentsEnd) {
			this.languageName = languageName;
			this.syntaxConstant = syntaxConstant;
			this.fileExtension = fileExtension;
			this.lineCommentsStart = lineCommentsStart;
			this.lineCommentsEnd = lineCommentsEnd;
		}

		public String getLanguageName() {
			return languageName;
		}

		public String getSyntaxConstant() {
			return syntaxConstant;
		}

		public String getFileExtension() {
			return fileExtension;
		}

		public String getLineCommentsStart() {
			return lineCommentsStart;
		}
		
		public String getLineCommentsEnd() {
			return lineCommentsEnd;
		}
		
		@Override
		public String toString() {
			return languageName;
		}

		public static SupportedLanguage getLanguageByName(String languageName) {
			for (SupportedLanguage lang : SupportedLanguage.values()) {
				if (lang.languageName.equals(languageName)) {
					return lang;
				}
			}
			return SupportedLanguage.LANGUAGE_OTHER;
		}
		
		public static String addComment(SupportedLanguage language, String comment) {
			return language.getLineCommentsStart() + " " + comment + language.getLineCommentsEnd();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private SupportedLanguage supportedLanguage = SupportedLanguage.LANGUAGE_PYTHON;
	private RSyntaxTextArea textArea;

	/**
	 * Create the panel.
	 */
	public SourceCodePanel() {
		textArea = new RSyntaxTextArea();
		textArea.setWhitespaceVisible(true);
		textArea.setTabsEmulated(true);
		textArea.setTabSize(4);
		textArea.setSyntaxEditingStyle(supportedLanguage.getSyntaxConstant());
		textArea.setCodeFoldingEnabled(true);
		setLayout(new BorderLayout(0, 0));

		RTextScrollPane scrollPane = new RTextScrollPane(textArea);
		this.add(scrollPane);

	}

	public SupportedLanguage getSupportedLanguage() {
		return supportedLanguage;
	}

	public void setSupportedLanguage(SupportedLanguage supportedLanguage) {
		this.supportedLanguage = supportedLanguage;
		textArea.setSyntaxEditingStyle(supportedLanguage.getSyntaxConstant());
	}

	public String getSourceCode() {
		return textArea.getText();
	}

	public void reset() {
		textArea.setText("");
	}
}
