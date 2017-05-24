package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import paul.wintz.spirotechnics.modes.ModeList;
import paul.wintz.userinterface.optiontypes.OptionGroup;

/**
 * This class will allow switching between different paul.wintz.spirotechnics.modes. It will be a JPanel with a drop-down 
 * selection box. When an item from the box is selected, than all of the controls change to match
 * that mode.
 * 
 * In effect, a ModeList is a special type of OptionGroup that has a list of OptionGroups that can be selection.
 * @author PaulWintz
 *
 */
@SuppressWarnings("serial")
class ModeListPanel extends OptionsGroupPanel {
	
	private class ModesComboBox extends JComboBox<OptionGroup> {
		
		ModesComboBox(final ModeList<?> modes){
			for(OptionGroup mode : modes.getList()){
				addItem(mode);
			}
			
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					modes.setCurrent((OptionGroup)  getSelectedItem());
					loadCurrentOptions(modes);
				}
			});
		}
	}
	
	public ModeListPanel (JPanel parent, final ModeList<? extends OptionGroup> modeList) {
		super(parent, modeList, null);
					
		//add the combo box at the top
		final JComboBox<OptionGroup> modeSelectionBox = new ModesComboBox(modeList);
		
		this.add(modeSelectionBox);
		
		loadCurrentOptions(modeList);
	}

	/**
	 * @param modeList
	 */
	private void loadCurrentOptions(ModeList<? extends OptionGroup> modeList) {
		this.updateList(modeList.getCurrent(), 2 /*FIXME: create constant*/, null);
	}
	
	
}