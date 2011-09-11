package cryptotools;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

public class ShiftCipherApplet extends JApplet {

	private static final long serialVersionUID = 1L;

	private SpringLayout layout;

	private JLabel keyLabel, plainLabel, cipherLabel;

	private JComboBox<Integer> keyList;

	private JTextArea plainTextArea, cipherTextArea;

	private JScrollPane plainScrollPane, cipherScrollPane;

	private JButton formatButton, encryptButton;

	private int previousKey = -1;

	private ShiftCipher shiftCipher;

	private Container contentPane;

	private static final Pattern PLAINTEXT_PATTERN = Pattern.compile("[a-z]+");

	private class PlaintextFormatter implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (plainTextArea.getText().compareTo("") == 0) {
				JOptionPane.showMessageDialog(null,
						"Enter a plaintext message.", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				// everything is okay
				int key = keyList.getSelectedIndex();

				String plaintext = plainTextArea.getText();

				// initialize shift cipher if necessary
				if (key != previousKey) {
					// no need to recreate if key is same as before
					shiftCipher = new ShiftCipher(key);
				}
				// format plaintext
				plainTextArea.setText(shiftCipher.formatPlaintext(plaintext));

				// update previousKey
				previousKey = key;
			}
		}
	}

	private class Encrypter implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// check for blank fields
			if (plainTextArea.getText().compareTo("") == 0) {
				JOptionPane.showMessageDialog(null,
						"Enter a plaintext message.", "Error",
						JOptionPane.ERROR_MESSAGE);
			} else {
				int key = keyList.getSelectedIndex();

				// check plaintext for bad input
				Matcher plaintextMatcher = PLAINTEXT_PATTERN
						.matcher(plainTextArea.getText());
				if (!plaintextMatcher.matches()) {
					JOptionPane.showMessageDialog(null,
							"Plaintext format invalid. Please format.",
							"Error", JOptionPane.ERROR_MESSAGE);
				} else {
					// everything is okay
					String plaintext = plainTextArea.getText();
					if (key != previousKey) {
						// no need to recreate if key is same as before
						shiftCipher = new ShiftCipher(key);
					}
					String ciphertext = shiftCipher.encrypt(plaintext);
					cipherTextArea.setText(ciphertext);

					// update previousKey
					previousKey = key;
				}
			}
		}
	}

	public void init() {
		contentPane = getContentPane();
		layout = new SpringLayout();
		contentPane.setLayout(layout);
		addComponents();
	}

	private void addComponents() {
		// key
		keyLabel = new JLabel("Key:");
		Integer[] keys = new Integer[26];
		for (int i = 0; i < 26; i++) {
			keys[i] = i;
		}
		keyList = new JComboBox<Integer>(keys);
		keyList.setSelectedIndex(0);
		contentPane.add(keyLabel);
		contentPane.add(keyList);

		// plaintext
		plainLabel = new JLabel("Plaintext:");
		plainTextArea = new JTextArea(5, 30);

		plainScrollPane = new JScrollPane(plainTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		plainTextArea.setLineWrap(true);
		contentPane.add(plainLabel);
		contentPane.add(plainScrollPane);

		// format plaintext button
		formatButton = new JButton("Format plaintext");
		add(formatButton);
		formatButton.addActionListener(new PlaintextFormatter());

		// encrypt button
		encryptButton = new JButton("Encrypt");
		add(encryptButton);
		encryptButton.addActionListener(new Encrypter());

		// ciphertext
		cipherLabel = new JLabel("Ciphertext:");
		cipherTextArea = new JTextArea(5, 30);

		cipherScrollPane = new JScrollPane(cipherTextArea,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		cipherTextArea.setLineWrap(true);
		cipherTextArea.setEditable(false);
		cipherTextArea.setBackground(new Color(240, 240, 240));
		contentPane.add(cipherLabel);
		contentPane.add(cipherScrollPane);

		// constraints
		layout.putConstraint(SpringLayout.WEST, keyLabel, 5, SpringLayout.WEST,
				contentPane);
		layout.putConstraint(SpringLayout.NORTH, keyLabel, 5,
				SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.WEST, keyList, 75, SpringLayout.WEST,
				contentPane);
		layout.putConstraint(SpringLayout.NORTH, keyList, 5,
				SpringLayout.NORTH, contentPane);
		layout.putConstraint(SpringLayout.EAST, contentPane, 5,
				SpringLayout.EAST, keyList);

		layout.putConstraint(SpringLayout.WEST, plainLabel, 5,
				SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.NORTH, plainLabel, 5,
				SpringLayout.SOUTH, keyList);
		layout.putConstraint(SpringLayout.WEST, plainScrollPane, 75,
				SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.NORTH, plainScrollPane, 5,
				SpringLayout.SOUTH, keyList);
		layout.putConstraint(SpringLayout.EAST, contentPane, 5,
				SpringLayout.EAST, plainScrollPane);

		layout.putConstraint(SpringLayout.NORTH, formatButton, 5,
				SpringLayout.SOUTH, plainScrollPane);
		layout.putConstraint(SpringLayout.WEST, formatButton, 75,
				SpringLayout.WEST, contentPane);

		layout.putConstraint(SpringLayout.NORTH, encryptButton, 5,
				SpringLayout.SOUTH, plainScrollPane);
		layout.putConstraint(SpringLayout.WEST, encryptButton, 5,
				SpringLayout.EAST, formatButton);

		layout.putConstraint(SpringLayout.WEST, cipherLabel, 5,
				SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.NORTH, cipherLabel, 5,
				SpringLayout.SOUTH, encryptButton);
		layout.putConstraint(SpringLayout.WEST, cipherScrollPane, 75,
				SpringLayout.WEST, contentPane);
		layout.putConstraint(SpringLayout.NORTH, cipherScrollPane, 5,
				SpringLayout.SOUTH, encryptButton);
		layout.putConstraint(SpringLayout.EAST, contentPane, 5,
				SpringLayout.EAST, cipherScrollPane);
		layout.putConstraint(SpringLayout.SOUTH, contentPane, 20,
				SpringLayout.SOUTH, cipherScrollPane);
	}
}