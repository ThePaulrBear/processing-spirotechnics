package paul.wintz.spirotechnics.userinterface.swinggui;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import paul.wintz.spirotechnics.userinterface.optiontypes.SliderOption;

class SliderOptionPanel extends AbstractOptionPanel<SliderOption>{	

	SliderOptionPanel(JPanel parentPanel, SliderOption option) {
		super(parentPanel, option);
	}

	@Override
	protected void createControl() {
		final JSlider slider = new JSlider();
		//slider.setBackground(Color.CYAN);
		slider.setMaximum(option.getSliderUpperLimit());
		slider.setMinimum(option.getSliderLowerLimit());
				
		if(option.isShowTicks()){
			slider.setPaintTicks(true);
			slider.setMinorTickSpacing(1);
			slider.setMajorTickSpacing(100);
			slider.setSnapToTicks(option.isShowTicks());
		}
		slider.setValue(option.getValue());
		
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				option.setValue(slider.getValue());
				updateLabel();
			}
		});
		this.add(slider);
	}
}