package paul.wintz.spirotechnics.userinterface.swinggui;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.swing.*;

import paul.wintz.uioptiontypes.FileOption;

@SuppressWarnings("serial")
class FileOptionPanel extends OptionPanel<FileOption>{

	FileOptionPanel(JPanel parentPanel, FileOption option) {
		super(checkNotNull(parentPanel), checkNotNull(option));

	}

	@Override
	protected void createControl() {
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JButton openFileChooserButton = new JButton();
		openFileChooserButton.setText(option.getDescription());
		openFileChooserButton.addChangeListener(event -> {
			if(!openFileChooserButton.getModel().isPressed()) return;
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				option.setValue(fc.getSelectedFile());
			}

		});

		this.add(openFileChooserButton);
	}

}