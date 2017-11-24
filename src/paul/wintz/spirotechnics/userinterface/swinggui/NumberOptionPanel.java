package paul.wintz.spirotechnics.userinterface.swinggui;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.swing.*;

import paul.wintz.userinterface.optiontypes.FloatOption;
import paul.wintz.userinterface.optiontypes.integers.NumberOption;

@SuppressWarnings("serial")
class NumberOptionPanel extends OptionPanel<NumberOption<?>> {

	NumberOptionPanel(JPanel parentPanel, NumberOption<?> option) {
		super(checkNotNull(parentPanel), checkNotNull(option));
	}

	private class NumberOptionSpinner<N extends Number> extends JSpinner {
		public NumberOptionSpinner(final NumberOption<N> option) {
			setValue(option.getValue());
			updateLabel();

			addChangeListener(pass -> {

				N newValue = (N) getValue();
				if(option.isValidValue(newValue)) {
					option.setValue(newValue);
					updateLabel();
				} else {
					label.setText(option.getDescription() + ": value not valid");
				}
			});

			option.addOnValueChangedListener(newValue -> {
				setValue(newValue);
				updateLabel();
			});
		}
	}

	private class OptionSlider extends JSlider {
		public OptionSlider(final FloatOption option) {
			setMaximum((int) (100 * option.getMax()));
			setMinimum((int) (100 * option.getMin()));

			if (16 < 15) {
				setPaintTicks(true);
				setMinorTickSpacing(1);
				setSnapToTicks(true);
			}
			setUiValue(option.getValue());

			addChangeListener(arg0 -> {
				setEntityValue(option);
			});

			option.addOnValueChangedListener(this::setUiValue);
		}

		private void setEntityValue(final FloatOption option) {
			float newValue = 0.01f * getValue();
			if(option.isValidValue(newValue)) {
				option.setValue(newValue);
				updateLabelWithValue();
			} else {
				label.setText(option.getDescription() + ": value not valid");
			}
		}

		private void setUiValue(Float value) {
			setValue((int) (100 * value));
			updateLabelWithValue();
		}
	}

	@Override
	protected void createControl() {
		if(option instanceof FloatOption) {
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.add(new OptionSlider((FloatOption) option));
		} else {
			this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
			this.add(new NumberOptionSpinner<>(option));
		}
	}

}