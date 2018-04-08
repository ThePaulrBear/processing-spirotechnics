package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.CardLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import paul.wintz.uioptiontypes.OptionGroup;
import paul.wintz.utils.logging.Lg;

@SuppressWarnings("serial")
public class OptionsJFrame extends JFrame {
	protected static final String TAG = Lg.makeTAG(OptionsJFrame.class);

	private final JPanel contentPane = createContentPane();

	private JTabbedPane tabbedPane;

	public OptionsJFrame() {
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(100, 100, 1000, 700);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane);
	}

	private JPanel createContentPane() {
		JPanel pane = new JPanel();
		pane.setBorder(new EmptyBorder(5, 5, 5, 5));
		pane.setLayout(new CardLayout(0, 0));
		setContentPane(pane);
		return pane;
	}

	public void addTab(OptionGroup optionGroup) {
		new TabOptionsGroupPanel(contentPane, tabbedPane, optionGroup);
	}

}
