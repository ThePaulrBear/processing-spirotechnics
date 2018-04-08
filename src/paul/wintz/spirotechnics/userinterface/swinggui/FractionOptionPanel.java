package paul.wintz.spirotechnics.userinterface.swinggui;

import javax.swing.*;
import javax.swing.event.*;

import paul.wintz.uioptiontypes.FractionOption;

@SuppressWarnings("serial")
class FractionOptionPanel extends OptionPanel<FractionOption> {

	FractionOptionPanel(JPanel parentPanel, FractionOption option) {
		super(parentPanel, option);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}

	private class NumeratorSpinner extends JSpinner {
		public NumeratorSpinner(final FractionOption option) {
			setValue(option.getNumerator());

			addChangeListener(pass -> option.setNumerator((int) getValue()));
			option.addOnValueChangedListener(newValue -> setValue(newValue.getNumerator()));

		}
	}

	private class DenominatorSpinner extends JSpinner {
		public DenominatorSpinner(final FractionOption option) {
			setValue(option.getDenominator());
			addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					skipZeroValue(option);
				}

				private void skipZeroValue(final FractionOption option) {
					if ((int) getValue() == 0) {
						if (option.getDenominator() < 0) {
							setValue(1);
						} else {
							setValue(-1);
						}
					} else {
						option.setDenominator((int) getValue());
					}
				}
			});

			option.addOnValueChangedListener(newValue -> setValue(option.getDenominator()));
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