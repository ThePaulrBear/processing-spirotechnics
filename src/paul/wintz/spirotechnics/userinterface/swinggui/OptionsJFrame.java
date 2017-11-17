package paul.wintz.spirotechnics.userinterface.swinggui;

import static com.google.common.base.Preconditions.checkState;

import java.awt.CardLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import paul.wintz.logging.Lg;
import paul.wintz.userinterface.optiontypes.*;

@SuppressWarnings("serial")
public class OptionsJFrame extends JFrame {
	protected static final String TAG = Lg.makeTAG(OptionsJFrame.class);

	private final JPanel contentPane = createContentPane();

	public OptionsJFrame(OptionGroup optionGroup) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);

		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane);

		// Each top level tab will have it's own tab
		for (final OptionItem group : optionGroup) {
			checkState(group instanceof OptionGroup);
			new TabOptionsGroupPanel(contentPane, tabbedPane, (OptionGroup) group);
		}
	}

	private JPanel createContentPane() {
		JPanel pane = new JPanel();
		pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		pane.setLayout(new CardLayout(0, 0));
		setContentPane(pane);
		return pane;
	}

}
