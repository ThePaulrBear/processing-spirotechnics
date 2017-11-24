package paul.wintz.spirotechnics.userinterface.swinggui;

import javax.swing.*;

import paul.wintz.uioptiontypes.BooleanOption;

@SuppressWarnings("serial")
class ToggleOptionPanel extends OptionPanel<BooleanOption> {

	ToggleOptionPanel(JPanel parentPanel, BooleanOption option) {
		super(parentPanel, option);
	}

	private class OptionToggle extends JToggleButton {
		public OptionToggle(final BooleanOption option) {
			setText(option.getDescription());
			setAlignmentX(CENTER_ALIGNMENT);

			setSelected(option.getValue());
			addChangeListener(event -> {
				option.setValue(isSelected());
				updateLabel();
			});

			option.addOnValueChangedListener(newValue -> {
				setSelected(newValue);
				updateLabel();
			});
		}
	}

	@Override
	protected void createControl() {
		final JToggleButton button = new OptionToggle(option);
		this.add(button);

	}

	@Override
	protected void createLabel() {
		super.createLabel();
		label.setVisible(false);
	}
}