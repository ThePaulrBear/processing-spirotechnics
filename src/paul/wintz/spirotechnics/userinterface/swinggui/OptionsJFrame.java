package paul.wintz.spirotechnics.userinterface.swinggui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import paul.wintz.spirotechnics.userinterface.optiontypes.OptionGroup;
import paul.wintz.spirotechnics.userinterface.optiontypes.ValuedBooleanOption;
import paul.wintz.spirotechnics.userinterface.optiontypes.ValuedSliderOption;

import java.awt.CardLayout;
import java.awt.EventQueue;

import javax.swing.JTabbedPane;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuBar;
import javax.swing.JMenu;


@SuppressWarnings("serial")
public class OptionsJFrame extends JFrame {
	private static OptionsJFrame instance;
	
	
	private JPanel contentPane;
	private JMenu menu;
	/**
	 * Create the frame.
	 * @param optionGroups
	 */
	public OptionsJFrame(List<OptionGroup> optionGroups) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 500);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menu = new JMenu("Options");
		menuBar.add(menu);
			
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new CardLayout(0, 0));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane);	
				
		//Each top level tab will have it's own tab
		for(OptionGroup group: optionGroups){
			
			new TabOptionsGroupPanel(contentPane, tabbedPane, group, menu);
			
		}		
	}

	//For testing
	public static void main(String... args){

		final List<OptionGroup> optionList = new ArrayList<>();
		optionList.add(new OptionGroup("Tab 1", 
				new OptionGroup("Option Group 1",
						new ValuedSliderOption(5, 20, 100, "Slider 1", false),
						new ValuedBooleanOption(true, "Boolean Option")
						)
				)
		);
		optionList.add(new OptionGroup("Tab 2", 
				new OptionGroup("Option Group 2",
						new ValuedSliderOption(1, 0, 10, "Slider 2", true),
						new ValuedSliderOption(1, 0, 5, "Slider 3", true)
						)
				)
		);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OptionsJFrame frame = new OptionsJFrame(optionList);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	private static int count = 0;
	public static synchronized OptionsJFrame getInstance() {
		if(instance == null){
			if(++count > 1) new RuntimeException("This instance #" + count).printStackTrace();
			instance = new OptionsJFrame(null);
		}
		return instance;
	}
}
