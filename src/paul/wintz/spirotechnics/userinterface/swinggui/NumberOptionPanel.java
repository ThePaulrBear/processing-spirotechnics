package paul.wintz.spirotechnics.userinterface.swinggui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import paul.wintz.userinterface.optiontypes.NumberOption;
import paul.wintz.userinterface.optiontypes.UserInputOption.OptionUpdatedCallback;

@SuppressWarnings("serial")
class NumberOptionPanel extends OptionPanel<NumberOption> {

	NumberOptionPanel(JPanel parentPanel, NumberOption option) {
		super(parentPanel, option);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}

	private class NumberOptionSpinner extends JSpinner {
		public NumberOptionSpinner(final NumberOption option) {
			setValue(option.getValue());

			addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent e) {
					option.setValue((int) getValue());
					updateLabel();
				}
			});

			option.addOptionUpdatedCallback(new OptionUpdatedCallback() {
				@Override
				public void onUpdate() {
					setValue(option.getValue());
					updateLabel();
				}
			});
		}
	}

	@Override
	protected void createControl() {
		final JSpinner spinner = new NumberOptionSpinner(option);
		this.add(spinner);
	}
}