package paul.wintz.spirotechnics.userinterface.swinggui;

import javax.swing.*;

import paul.wintz.uioptiontypes.ListOption;

@SuppressWarnings("serial")
public class ListOptionPanel<T> extends OptionPanel<ListOption<T>> {

    public ListOptionPanel(JPanel parentPanel, ListOption<T> listOption) {
        super(parentPanel, listOption);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    @Override
    protected void createControl() {

        final ListOptionComboBox comboBox = new ListOptionComboBox(option);

        this.add(comboBox);

        updateLabel();

    }

    private class ListOptionComboBox extends JComboBox<T> {

        @SuppressWarnings("unchecked")
        public ListOptionComboBox(final ListOption<T> listOption) {
            for (final T mode : listOption) {
                addItem(mode);
            }

            setSelectedItem(option.getSelected());
            addActionListener(unused -> listOption.setSelected((T) getSelectedItem()));

        }
    }
}
