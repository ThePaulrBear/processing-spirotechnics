package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import paul.wintz.spirotechnics.userinterface.optiontypes.FractionOption;

class FractionOptionPanel extends AbstractOptionPanel<FractionOption>{	
	private static final int SPINNER_WIDTH = 150;
	private static final int SPINNER_HEIGHT = 20;
	
	FractionOptionPanel(JPanel parentPanel, FractionOption option) {
		super(parentPanel, option);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}

	@Override
	protected void createControl() {

		final JSpinner numeratorSpinner = new JSpinner();
		this.add(numeratorSpinner);
		numeratorSpinner.setMaximumSize(new Dimension(SPINNER_WIDTH, SPINNER_HEIGHT));
		numeratorSpinner.setValue(option.getNumerator());
		numeratorSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				option.setNumerator( (int) numeratorSpinner.getValue());
				
			}
		});
		
		JLabel label = new JLabel(" / ");
		this.add(label);
		
		final JSpinner denominatorSpinner = new JSpinner();
		this.add(denominatorSpinner);
		denominatorSpinner.setMaximumSize(new Dimension(SPINNER_WIDTH, SPINNER_HEIGHT));
		denominatorSpinner.setValue(option.getDenominator());
		denominatorSpinner.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				option.setDenominator((int) denominatorSpinner.getValue());
				
			}
		});
	}	
}