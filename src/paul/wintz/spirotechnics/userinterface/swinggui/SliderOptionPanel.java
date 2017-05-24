package paul.wintz.spirotechnics.userinterface.swinggui;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import paul.wintz.userinterface.optiontypes.SliderOption;
import paul.wintz.userinterface.optiontypes.UserInputOption.OptionUpdatedCallback;

@SuppressWarnings("serial")
class SliderOptionPanel extends OptionPanel<SliderOption>{	

	SliderOptionPanel(JPanel parentPanel, SliderOption option) {
		super(parentPanel, option);
	}
	
	private class OptionSlider extends JSlider {
		public OptionSlider(final SliderOption option) {
			setMaximum(option.getSliderUpperLimit());
			setMinimum(option.getSliderLowerLimit());
					
			if(option.isShowTicks()){
				setPaintTicks(true);
				setMinorTickSpacing(1);
				setMajorTickSpacing(100);
				setSnapToTicks(option.isShowTicks());
			}
			setValue(option.getValue());
			
			addChangeListener(new ChangeListener() {
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