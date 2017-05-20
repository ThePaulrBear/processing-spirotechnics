package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import paul.wintz.spirotechnics.modes.ModeList;
import paul.wintz.spirotechnics.userinterface.optiontypes.OptionGroup;

/**
 * This class will allow switching between different paul.wintz.spirotechnics.modes. It will be a JPanel with a drop-down 
 * selection box. When an item from the box is selected, than all of the controls change to match
 * that mode.
 * 
 * In effect, a ModeList is a special type of OptionGroup that has 
 * @author PaulWintz
 *
 */
class ModeListPanel extends OptionsGroupPanel {
	
	public ModeListPanel (JPanel parent, final ModeList<? extends OptionGroup> option) {
		super(parent, option, null);
			
		//add the combo box at the top
		final JComboBox<OptionGroup> listItems_comboBox = new JComboBox<>();
		for(OptionGroup mode : option.getList()){
			listItems_comboBox.addItem(mode);
		}
		listItems_comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				option.setCurrent((OptionGroup)  listItems_comboBox.getSelectedItem());
				loadCurrentOptions(option);
			}
		});
		listItems_comboBox.setMaximumSize(new Dimension(300, 30));
		this.add(listItems_comboBox);
		
		loadCurrentOptions(option);
	}

	/**
	 * @param modeList
	 */
	private void loadCurrentOptions(ModeList<? extends OptionGroup> modeList) {
		
		this.updateList(modeList.getCurrent(), 2, null);
	}
	
	
}