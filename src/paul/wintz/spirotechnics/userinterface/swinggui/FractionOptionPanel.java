package paul.wintz.spirotechnics.userinterface.swinggui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import paul.wintz.userinterface.optiontypes.FractionOption;
import paul.wintz.userinterface.optiontypes.UserInputOption.OptionUpdatedCallback;

@SuppressWarnings("serial")
class FractionOptionPanel extends OptionPanel<FractionOption> {

	FractionOptionPanel(JPanel parentPanel, FractionOption option) {
		super(parentPanel, option);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}

	private class NumeratorSpinner extends JSpinner {
		public NumeratorSpinner(final FractionOption option) {
			// setMaximumSize(new Dimension(SPINNER_WIDTH, SPINNER_HEIGHT));
			setValue(option.getNumerator());
			addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					option.setNumerator((int) getValue());
				}
			});

			option.addOptionUpdatedCallback(new OptionUpdatedCallback() {

				@Override
				public void onUpdate() {
					setValue(option.getNumerator());
				}
			});

		}
	}

	private class DenominatorSpinner extends JSpinner {
		public DenominatorSpinner(final FractionOption option) {
			// setMaximumSize(new Dimension(SPINNER_WIDTH, SPINNER_HEIGHT));
			setValue(option.getDenominator());
			addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					skipZeroValue(option);
				}

				/**
				 * @param option
				 */
				private void skipZeroValue(final FractionOption option) {
					if ((int) getValue() == 0) {
						if (option.getDenominator() < 0)
							setValue(1);
						else
							setValue(-1);
					} else {
						option.setDenominator((int) getValue());
					}
				}
			});

			option.addOptionUpdatedCallback(new OptionUpdatedCallback() {

				@Override
				public void onUpdate() {
					setValue(option.getDenominator());
				}
			});
		}
	}

	@Override
	protected void createControl() {

		final JSpinner numeratorSpinner = new NumeratorSpinner(option);
		final JLabel label = new JLabel(" / ");
		final JSpinner denominatorSpinner = new DenominatorSpinner(option);

		this.add(numeratorSpinner);
		this.add(label);
		this.add(denominatorSpinner);

	}
}