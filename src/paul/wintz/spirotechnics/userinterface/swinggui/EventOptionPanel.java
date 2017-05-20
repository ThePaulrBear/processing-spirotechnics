package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import paul.wintz.spirotechnics.userinterface.optiontypes.EventOption;

class EventOptionPanel extends JMenuItem {
	
	EventOptionPanel(final EventOption option, JMenu eventMenu) {
	
		JMenuItem menuItem = new JMenuItem(option.getDescription());
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				option.doEvent();
			}
		});
		
		eventMenu.add(menuItem);
	}
}