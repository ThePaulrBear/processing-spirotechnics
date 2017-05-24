package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import paul.wintz.userinterface.HasValue;
import paul.wintz.userinterface.optiontypes.BooleanOption;
import paul.wintz.userinterface.optiontypes.EventOption;
import paul.wintz.userinterface.optiontypes.FractionOption;
import paul.wintz.userinterface.optiontypes.ListOption;
import paul.wintz.userinterface.optiontypes.NumberOption;
import paul.wintz.userinterface.optiontypes.SliderOption;
import paul.wintz.userinterface.optiontypes.UserInputOption;

@SuppressWarnings("serial")
abstract class OptionPanel <T extends UserInputOption> extends JPanel {
	protected final T option;

	protected JLabel label;
	
	OptionPanel(JPanel parentPanel, T option){
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
	
	public static OptionPanel<?> makeOptionPanel(UserInputOption option, OptionsGroupPanel parent){
		if(option == null) throw new IllegalArgumentException("Option cannot be null");
		if(parent == null) throw new IllegalArgumentException("Parent panel cannot be null");

		if(option instanceof SliderOption){
			return new SliderOptionPanel(parent, (SliderOption) option);
		} else if(option instanceof NumberOption){
			return new NumberOptionPanel(parent, (NumberOption) option);
		} else if(option instanceof BooleanOption){
			return new ToggleOptionPanel(parent, (BooleanOption) option);
		} else if (option instanceof FractionOption){
			return new FractionOptionPanel(parent, (FractionOption) option);
		} else if(option instanceof ListOption<?>){
			return new ListOptionPanel<>(parent, (ListOption<?>) option);
		} else {
			throw new RuntimeException("Option type not supported: " + option.getClass().getSuperclass().getSimpleName());
		}	
	}


	
}