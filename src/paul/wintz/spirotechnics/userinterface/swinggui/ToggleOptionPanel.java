package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import paul.wintz.userinterface.optiontypes.BooleanOption;
import paul.wintz.userinterface.optiontypes.UserInputOption.OptionUpdatedCallback;

@SuppressWarnings("serial")
class ToggleOptionPanel extends OptionPanel<BooleanOption> {
	
	ToggleOptionPanel(JPanel parentPanel, BooleanOption option) {
		super(parentPanel, option);
	}

	private class OptionToggle extends JToggleButton {
		public OptionToggle(final BooleanOption option) {
			setText(option.getDescription());
			setAlignmentX(CENTER_ALIGNMENT);
			
			addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					
					option.setValue(isSelected());
				}
			});
			setSelected(option.getValue());
			
			addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent arg0) {
					option.setValue(isSelected());
					updateLabel();
				}
			});
			
			option.addOptionUpdatedCallback(new OptionUpdatedCallback() {
				
				@Override
				public void onUpdate() {
					setSelected(option.getValue());
					updateLabel();
				}
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