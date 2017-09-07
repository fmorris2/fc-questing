package scripts.fc.fcquester.gui;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import org.tribot.api.General;

import scripts.fc.fcquester.FCQuester;
import scripts.fc.fcquester.data.QuestLoader;
import scripts.fc.fcquester.tasks.WalkToGe;
import scripts.fc.framework.mission.Mission;
import scripts.fc.framework.mission.impl.OneTaskMission;
import scripts.fc.missions.fccooksassistant.FCCooksAssistant;
import scripts.fc.missions.fcromeoandjuliet.FCRomeoAndJuliet;
import scripts.fc.missions.fcsheepshearer.FCSheepShearer;
import scripts.fc.missions.fctutorial.FCTutorial;


public class FCQuestingGUI
{	
	private static final String ICON_PATH = "/scripts/fc/fcquester/gui/quest_icon.png";
	public boolean hasFilledOut;
	
	private JFrame mainFrame;
	private JComboBox<Mission> questBox;
	private JList<Mission> questList;
	private FCQuester script;
	private DefaultListModel<Mission> questModel;
	private boolean isUsingArgs, isInitialized;
	

	/**
	 * Create the application.
	 */
	public FCQuestingGUI(FCQuester script)
	{
		this.script = script;
		this.script.questLoader = new QuestLoader(script);
		
	}
	
	public void init()
	{
		if(isUsingArgs || isInitialized)
			return;
		
		initialize();
		
		if(prepareOptions())
			setupGUI();
		
		isInitialized = true;
	}
	
	private void setupGUI()
	{
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setAlwaysOnTop(true);
		
		//Position GUI in center of screen
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();		
		mainFrame.setLocation((int)(screenSize.getWidth() / 2), (int)(screenSize.getHeight() / 2));
		
		//Make GUI visible
		mainFrame.setVisible(true);
	}
	
	private boolean prepareOptions()
	{
		Mission[] availableQuests = script.questLoader.getQuests();
		if(availableQuests.length == 0)
		{
			General.println("You have already completed all of the quests this script has to offer! Ending...");
			script.setIsRunning(false);
			return false;
		}
		
		questBox.setModel(new DefaultComboBoxModel<Mission>(availableQuests));
		questList.setModel(new DefaultListModel<Mission>());
		questList.setPrototypeCellValue(availableQuests[0]);
		questModel = (DefaultListModel<Mission>) (questList.getModel());
		return true;
	}  

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		mainFrame = new JFrame();
		loadIcon();
		mainFrame.setResizable(false);
		mainFrame.setAlwaysOnTop(true);
		mainFrame.setTitle("FC Questing");
		mainFrame.setBounds(100, 100, 250, 250);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		mainPanel.setBounds(10, 10, 224, 200);
		mainFrame.getContentPane().add(mainPanel);
		mainPanel.setLayout(null);
		
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startButtonActionPerformed(e);
			}
		});
		startButton.setBounds(134, 177, 81, 20);
		mainPanel.add(startButton);
		
		questBox = new JComboBox<>();
		questBox.setBounds(10, 145, 117, 20);
		mainPanel.add(questBox);
		
		JButton addQuestButton = new JButton("Add");
		addQuestButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addButtonActionPerformed(e);
			}
		});
		addQuestButton.setBounds(9, 177, 58, 20);
		mainPanel.add(addQuestButton);
		
		JButton removeButton = new JButton("Del");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeButtonActionPerformed(e);
			}
		});
		removeButton.setBounds(70, 177, 58, 20);
		mainPanel.add(removeButton);
		
		questList = new JList<>();
		questList.setBorder(null);
		questList.setBounds(10, 11, 204, 134);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setViewportBorder(null);
		scrollPane.setViewportView(questList);
		scrollPane.setBounds(10, 4, 204, 138);
		mainPanel.add(scrollPane);
		
		JButton upButton = new JButton("\u2191");
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				upButtonActionPerformed(e);
			}
		});
		upButton.setBounds(134, 145, 41, 20);
		mainPanel.add(upButton);
		
		JButton downButton = new JButton("\u2193");
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				downButtonActionPerformed(e);
			}
		});
		downButton.setBounds(174, 145, 41, 20);
		mainPanel.add(downButton);
	}
	
	private void loadIcon()
	{
		try
		{
			File file = new File(getClass().getResource(ICON_PATH).toURI());
			if(file.exists())
				mainFrame.setIconImage(ImageIO.read(file));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//LISTENERS
	private void upButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {      
    	int index = questList.getSelectedIndex();
    	
    	if(index <= 0)
    		return;
    	
    	Mission toRemove = questList.getModel().getElementAt(index - 1);
    	
    	questModel.remove(index - 1);
    	questModel.add(index, toRemove);
    	questList.setSelectedIndex(index - 1);
    } 

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {
    	int index = questList.getSelectedIndex();
    	
    	if(index + 1 == questModel.getSize() || questModel.getSize() <= 1)
    		return;
    	
    	Mission toRemove = questList.getModel().getElementAt(index + 1);
    	
    	questModel.remove(index + 1);
    	questModel.add(index, toRemove);
    	questList.setSelectedIndex(index + 1);
    }                                          

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {
    	Mission q = (Mission) questBox.getSelectedItem();
    	
    	if(q == null)
    		return;
    	
    	if(!questModel.contains(q) && q.getMissionName().length() > 0)
    	{
    		questModel.addElement(q);
    		questBox.removeItem(q);
    	}
    }                                         

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {
    	int index = questList.getSelectedIndex();
    	if(index < 0)
    		return;
    	
    	if(!questModel.isEmpty())
    		questBox.addItem(questModel.remove(index));
    }                                            

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {
    	mainFrame.setVisible(false);
    	hasFilledOut = true;
    	script.getSetMissions().clear();
    	
    	Mission[] quests = new Mission[questModel.size()];
    	questModel.copyInto(quests);
    	
    	for(Mission q : quests)
    		script.getSetMissions().add(q);
    }
    
    public void dispose()
    {
    	if(mainFrame != null)
    		mainFrame.dispose();
    }
    
    public void randomlyAddQuests()
    {
    	isUsingArgs = true;
    	List<Mission> quests = new ArrayList<>();
    	
    	for(Mission q : script.questLoader.getQuests())
    	{    		
    		if(!(q instanceof FCTutorial) && !q.hasReachedEndingCondition())
    			quests.add(q);
    	}
	    	
    	Collections.shuffle(quests);
    	
    	script.getSetMissions().add(new FCTutorial(script));
    	script.getSetMissions().addAll(quests);
    	
    	hasFilledOut = true;
    }
    
    public void add7qp()
    {
    	
    	isUsingArgs = true;
    	
    	List<Mission> quests = Arrays.asList(new FCCooksAssistant(script), new FCSheepShearer(script), new FCRomeoAndJuliet(script));
    	Collections.shuffle(quests);
    	script.getSetMissions().add(new FCTutorial(script));
    	script.getSetMissions().addAll(quests);
    	
    	hasFilledOut = true;
    }
    
    public void addTutGe()
    {
    	
    	isUsingArgs = true;
    	
    	script.getSetMissions().add(new FCTutorial(script));
    	script.getSetMissions().add(new OneTaskMission(new WalkToGe(), "Walk to GE"));
    	
    	hasFilledOut = true;
    }
    
    public void addTut()
    {
    	isUsingArgs = true;
    	script.getSetMissions().add(new FCTutorial(script));
    	hasFilledOut = true;
    }
	
	public JFrame getFrame()
	{
		return mainFrame;
	}
}
