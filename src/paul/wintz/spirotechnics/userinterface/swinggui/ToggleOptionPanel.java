package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import paul.wintz.spirotechnics.userinterface.optiontypes.BooleanOption;

@SuppressWarnings("serial")
class ToggleOptionPanel extends AbstractOptionPanel<BooleanOption> {
	private static final int WIDTH = 200;
	
	ToggleOptionPanel(JPanel parentPanel, BooleanOption option) {
		super(parentPanel, option);
	}

	@Override
	protected void createControl() {
		final JToggleButton button = new JToggleButton();
		button.setAlignmentX(CENTER_ALIGNMENT);
		button.setMinimumSize(new Dimension(WIDTH, 0));
		this.add(button);
		//this.setBackground(Color.RED);
		button.setText(option.getDescription());
		button.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				
				option.setValue(button.isSelected());
			}
		});
		button.setSelected(option.getValue());
	}

	@Override
	protected void createLabel() {
		// TODO Auto-generated method stub
		super.createLabel();
		label.setVisible(false);
	}


	
	
	
}