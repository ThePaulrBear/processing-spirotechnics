package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.event.*;

import javax.swing.*;

import paul.wintz.userinterface.optiontypes.ListOption;

@SuppressWarnings("serial")
public class ListOptionPanel<T> extends OptionPanel<ListOption<T>> {

	public ListOptionPanel(JPanel parentPanel, ListOption<T> listOption) {
		super(parentPanel, listOption);
	}

	@Override
	protected void createControl() {

		final ListOptionComboBox comboBox = new ListOptionComboBox(option);

		this.add(comboBox);

	}

	private class ListOptionComboBox extends JComboBox<T> {

		public ListOptionComboBox(final ListOption<T> listOption) {
			for (final T mode : listOption) {
				addItem(mode);
			}

			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					listOption.setSelected((T) getSelectedItem());
				}
			});

			setSelectedItem(option.getSelected());
		}
	}
}
