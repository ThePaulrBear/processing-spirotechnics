package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JToggleButton;

import paul.wintz.userinterface.optiontypes.ListOption;

@SuppressWarnings("serial")
public class ListOptionPanel<T> extends OptionPanel<ListOption<T>> {

	public ListOptionPanel(JPanel parentPanel, ListOption<T> listOption) {
		super(parentPanel, listOption);
	}

	class ItemSelectionButton extends JToggleButton {
		public ItemSelectionButton(final T item) {
			setText(item.toString());
			addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					if (isSelected())
						option.setSelected(item);
				}
			});
		}
	}

	@Override
	protected void createControl() {
		JPanel radioPanel = new JPanel(new GridLayout(0, 1));
		for (final T item : option) {
			final JToggleButton butt = new ItemSelectionButton(item);

			radioPanel.add(butt);
		}
		this.add(radioPanel);
	}
}
