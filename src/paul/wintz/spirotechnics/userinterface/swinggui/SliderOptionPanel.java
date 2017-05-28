package paul.wintz.spirotechnics.userinterface.swinggui;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import paul.wintz.userinterface.optiontypes.IntegerRangeOption;
import paul.wintz.userinterface.optiontypes.UserInputOption.OptionUpdatedCallback;

@SuppressWarnings("serial")
class SliderOptionPanel extends OptionPanel<IntegerRangeOption> {

	SliderOptionPanel(JPanel parentPanel, IntegerRangeOption option) {
		super(parentPanel, option);
	}

	private class OptionSlider extends JSlider {
		public OptionSlider(final IntegerRangeOption option) {
			setMaximum(option.getMax());
			setMinimum(option.getMin());

			if (option.getNumberOfValuesInRange() < 15) {
				setPaintTicks(true);
				setMinorTickSpacing(1);
				setSnapToTicks(true);
			}
			setValue(option.getValue());

			addChangeListener(new ChangeListener() {
				@Override
				public void stateChanged(ChangeEvent arg0) {
					option.setValue(getValue());
					updateLabel();
				}
			});

			option.addOptionUpdatedCallback(new OptionUpdatedCallback() {
				@Override
				public void onUpdate() {
					setValue(option.getValue());
				}
			});
		}
	}

	@Override
	protected void createControl() {
		final JSlider slider = new OptionSlider(option);

		this.add(slider);
	}
}