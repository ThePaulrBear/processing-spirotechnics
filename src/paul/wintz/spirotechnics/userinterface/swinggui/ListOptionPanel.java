package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import paul.wintz.spirotechnics.userinterface.optiontypes.ListOption;

public class ListOptionPanel<T> extends AbstractOptionPanel<ListOption<T>> {

	public ListOptionPanel(JPanel parentPanel, ListOption<T> listOption) {
		super(parentPanel, listOption);
		
	}

	@Override
	protected void createControl() {
		ArrayList<JToggleButton> butts = new ArrayList<>();
		ButtonGroup buttGroup = new ButtonGroup();
		for(final T item : option){
			final JToggleButton butt = new JToggleButton();
			butt.setText(item.toString());
			butt.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					if(butt.isSelected()) option.setSelected(item);	
				}
			});
			butts.add(butt);
			buttGroup.add(butt);
		}

		JPanel radioPanel = new JPanel(new GridLayout(0, 1));
		for(JToggleButton butt : butts){
			radioPanel.add(butt);
		}
		this.add(radioPanel);
	}
}
