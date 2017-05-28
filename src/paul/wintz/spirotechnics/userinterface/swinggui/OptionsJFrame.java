package paul.wintz.spirotechnics.userinterface.swinggui;

import java.awt.CardLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import paul.wintz.userinterface.optiontypes.BooleanOption;
import paul.wintz.userinterface.optiontypes.IntegerRangeOption;
import paul.wintz.userinterface.optiontypes.OptionGroup;
import paul.wintz.userinterface.optiontypes.OptionItem;

@SuppressWarnings("serial")
public class OptionsJFrame extends JFrame {
	private static OptionsJFrame instance;

	private final JPanel contentPane;
	private final JMenu menu;

	public OptionsJFrame(OptionGroup optionGroup) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 500);

		final JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		menu = new JMenu("Options");
		menuBar.add(menu);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));

		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane);

		// Each top level tab will have it's own tab
		for (final OptionItem group : optionGroup) {
			if (!(group instanceof OptionGroup))
				throw new IllegalAccessError();
			new TabOptionsGroupPanel(contentPane, tabbedPane, (OptionGroup) group, menu);

		}
	}

	// For testing
	public static void main(String... args) {

		final OptionGroup optionList = new OptionGroup();
		optionList.addOptions(new OptionGroup("Tab 1", new OptionGroup("Option Group 1",
				new IntegerRangeOption(5, 20, 100, "Slider 1"), new BooleanOption(true, "Boolean Option"))));
		optionList.addOptions(new OptionGroup("Tab 2", new OptionGroup("Option Group 2",
				new IntegerRangeOption(1, 0, 10, "Slider 2"), new IntegerRangeOption(1, 0, 5, "Slider 3"))));
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final OptionsJFrame frame = new OptionsJFrame(optionList);
					frame.setVisible(true);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static int count = 0;

	public static synchronized OptionsJFrame getInstance() {
		if (instance == null) {
			if (++count > 1) {
				new RuntimeException("This instance #" + count).printStackTrace();
			}
			instance = new OptionsJFrame(null);
		}
		return instance;
	}
}
