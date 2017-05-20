package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import paul.wintz.spirotechnics.modes.ModeList;
import paul.wintz.spirotechnics.userinterface.optiontypes.BooleanOption;
import paul.wintz.spirotechnics.userinterface.optiontypes.EventOption;
import paul.wintz.spirotechnics.userinterface.optiontypes.FractionOption;
import paul.wintz.spirotechnics.userinterface.optiontypes.ListOption;
import paul.wintz.spirotechnics.userinterface.optiontypes.NumberOption;
import paul.wintz.spirotechnics.userinterface.optiontypes.OptionGroup;
import paul.wintz.spirotechnics.userinterface.optiontypes.OptionItem;
import paul.wintz.spirotechnics.userinterface.optiontypes.SliderOption;
import paul.wintz.spirotechnics.userinterface.optiontypes.UserInputOption;

class OptionsGroupPanel extends JPanel {
	public static final int WIDTH = 400;
	private JLabel jLabel;
	
	OptionsGroupPanel(JPanel parent, OptionGroup optionList, JMenu eventMenu){
		this.setMaximumSize(new Dimension(WIDTH, Integer.MAX_VALUE));
		this.setMinimumSize(new Dimension(WIDTH, 0));
		
		this.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setAlignmentX(CENTER_ALIGNMENT);
		this.setAlignmentY(TOP_ALIGNMENT);
		parent.setAlignmentY(TOP_ALIGNMENT);
		
		
		//this.setBackground(Color.YELLOW);
		jLabel = new JLabel(optionList.getDescription());
		jLabel.setAlignmentX(CENTER_ALIGNMENT);
		//jLabel.setBackground(Color.ORANGE);
		this.add(jLabel);
		parent.add(this);
		
		updateList(optionList, 1, eventMenu);
	}

	private void addList(OptionGroup optionList, JMenu eventMenu) {
		for(OptionItem o : optionList){
			add(o, eventMenu);					
		}
	}
	
	public void add(OptionItem o, JMenu eventMenu) {
		if(o instanceof ModeList){
			new ModeListPanel(this, (ModeList<?>) o);
		} else if(o instanceof OptionGroup){					
			new OptionsGroupPanel(this, (OptionGroup) o, eventMenu);
		} else if(o instanceof UserInputOption){
			addOption((UserInputOption) o, eventMenu);
		}
	}
	
	/**
	 * @param eventMenu TODO
	 * @param options
	 */
	private void addOption(UserInputOption opt, JMenu eventMenu) {
		if(opt == null) return;

		if(opt instanceof SliderOption){
			new SliderOptionPanel(this, (SliderOption) opt);
		} else if(opt instanceof NumberOption){
			new NumberOptionPanel(this, (NumberOption) opt);
		} else if(opt instanceof EventOption){
			new EventOptionPanel ((EventOption) opt, eventMenu);
		} else if(opt instanceof BooleanOption){
			new ToggleOptionPanel(this, (BooleanOption) opt);
		} else if (opt instanceof FractionOption){
			new FractionOptionPanel(this, (FractionOption) opt);
		} else if(opt instanceof ListOption<?>){
			new ListOptionPanel<>(this, (ListOption<?>) opt);
		} else {
			System.err.println("Option not supported by GUI: " + opt.getClass().getSimpleName());
		}	
	}

/**
 * 
 * @param optionList
 * @param afterNdx the first item to delete/replace
 * @param eventMenu TODO
 */
	public void updateList(OptionGroup optionList, int afterNdx, JMenu eventMenu){
		while(this.getComponentCount() > afterNdx){
			this.remove(afterNdx);
		}
	
		addList(optionList, eventMenu);		
		
	}
}