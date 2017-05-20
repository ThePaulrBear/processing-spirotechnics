package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import paul.wintz.spirotechnics.userinterface.HasValue;
import paul.wintz.spirotechnics.userinterface.optiontypes.UserInputOption;

@SuppressWarnings("serial")
abstract class AbstractOptionPanel <T extends UserInputOption> extends JPanel {
	protected final T option;

	protected JLabel label;
	

	
	AbstractOptionPanel(JPanel parentPanel, T option){
		super();
		this.option = option;
		
		parentPanel.add(this);
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		createLabel();
		updateLabel();
		createControl();

		Component spacer = Box.createVerticalStrut(10);
		this.add(spacer);		
	} 
	
	protected void createLabel(){
		label = new JLabel(option.getDescription());
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setAlignmentX(CENTER_ALIGNMENT);
		label.setMinimumSize(new Dimension(3000, 0));
		this.add(label);
	}
	
	protected abstract void createControl();
	
	protected void updateLabel(){
		StringBuilder sb = new StringBuilder(option.getDescription());
		if(option instanceof HasValue<?>){
			sb.append(": ").append(((HasValue<?>) option).getValue().toString());
		}
		label.setText(sb.toString());
	}
	
	
}