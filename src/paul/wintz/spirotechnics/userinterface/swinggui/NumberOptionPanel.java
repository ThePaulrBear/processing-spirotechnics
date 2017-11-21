package paul.wintz.spirotechnics.userinterface.swinggui;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.swing.*;

import paul.wintz.userinterface.optiontypes.FloatOption;
import paul.wintz.userinterface.optiontypes.integers.NumberOption;

@SuppressWarnings("serial")
class NumberOptionPanel extends OptionPanel<NumberOption<?>> {

	NumberOptionPanel(JPanel parentPanel, NumberOption<?> option) {
		super(checkNotNull(parentPanel), checkNotNull(option));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}

	private class NumberOptionSpinner<N extends Number> extends JSpinner {
		public NumberOptionSpinner(final NumberOption<N> option) {
			setValue(option.getValue());

			addChangeListener(pass -> {
				option.setValue((N) getValue());
				updateLabel();
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
			setValue((int) (100 * option.getValue()));

			addChangeListener(arg0 -> {
				option.setValue(0.01f * getValue());
				updateLabel();
			});

			option.addOnValueChangedListener(newValue -> setValue((int) (100 * newValue)));
		}
	}

	@Override
	protected void createControl() {
		if(option instanceof FloatOption) {
			this.add(new OptionSlider((FloatOption) option));
		} else {
			this.add(new NumberOptionSpinner<>(option));
		}
	}

}