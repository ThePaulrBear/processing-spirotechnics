package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import paul.wintz.userinterface.optiontypes.NumberOption;

@SuppressWarnings("serial")
class NumberOptionPanel extends OptionPanel<NumberOption> {

	private static final int SPINNER_WIDTH = 150;
	private static final int SPINNER_HEIGHT = 20;

	NumberOptionPanel(JPanel parentPanel, NumberOption option) {
		super(parentPanel, option);
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}

	@Override
	protected void createControl() {
		final JSpinner spinner = new JSpinner();
		spinner.setMaximumSize(new Dimension(SPINNER_WIDTH, SPINNER_HEIGHT));
		spinner.setMinimumSize(new Dimension(SPINNER_WIDTH, SPINNER_HEIGHT));
		spinner.setValue(option.getValue());
		spinner.addChangeListener(new ChangeListener() {	
			@Override
			public void stateChanged(ChangeEvent e) {
				option.setValue((int) spinner.getValue());
				updateLabel();
				
			}
		});
		this.add(spinner);
	}
}