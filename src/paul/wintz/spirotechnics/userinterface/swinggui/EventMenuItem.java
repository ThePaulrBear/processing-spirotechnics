package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import paul.wintz.userinterface.optiontypes.EventOption;

@SuppressWarnings("serial")
class EventMenuItem extends JMenuItem {

	EventMenuItem(final EventOption option, JMenu eventMenu) {

		JMenuItem menuItem = new JMenuItem(option.getDescription());
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				option.addToQueue();
			}
		});

		eventMenu.add(menuItem);
	}
}