package scripts.fc.fcquester.gui;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;

import org.tribot.api.General;
import org.tribot.util.Util;

import scripts.fc.api.utils.Utils;
import scripts.fc.fcquester.FCQuester;
import scripts.fc.fcquester.data.FCQuestingProfile;
import scripts.fc.fcquester.data.QuestLoader;
import scripts.fc.fcquester.data.QuestMissionWrapper;
import scripts.fc.fcquester.tasks.WalkToGe;
import scripts.fc.framework.data.Vars;
import scripts.fc.framework.mission.Mission;
import scripts.fc.framework.mission.MissionManager;
import scripts.fc.framework.mission.impl.OneTaskMission;
import scripts.fc.missions.fccooksassistant.FCCooksAssistant;
import scripts.fc.missions.fcromeoandjuliet.FCRomeoAndJuliet;
import scripts.fc.missions.fcsheepshearer.FCSheepShearer;
import scripts.fc.missions.fctutorial.FCTutorial;


public class FCQuestingGUI
{	
	private static final String ICON_PATH = "/scripts/fc/fcquester/gui/quest_icon.png";
	private static final String ACCOUNT_PATH = Utils.getTribotDir() + "/FC_Scripts/FC_Questing/accounts/";
	
	public boolean hasFilledOut;
	
	private JFrame mainFrame;
	private JFileChooser fileChooser;
	private JComboBox<QuestMissionWrapper> questBox;
	private JList<QuestMissionWrapper> questList;
	private JCheckBox abc2CheckBox;
	private FCQuester script;
	private DefaultListModel<QuestMissionWrapper> questModel;
	private boolean isUsingArgs, isInitialized;
	

	/**
	 * Create the application.
	 */
	public FCQuestingGUI(FCQuester script)
	{
		this.script = script;
		this.script.questLoader = new QuestLoader(script);
		fileChooser = new JFileChooser();
	}
	
	private void createDirs() {
		try
		{
			File f = new File(FCQuestingProfile.PROFILE_PATH);
			f.mkdirs();
			File f2 = new File(ACCOUNT_PATH);
			f2.mkdirs();
		} catch(Exception e) {
			e.printStackTrace();
			General.println("[FC Questing Error] Could not create necessary directories");
		}
	}
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void init()
	{
		createDirs();
		
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
		
		setQuestBox(availableQuests);
		return true;
	}
	
	private void setQuestBox(Mission[] availableQuestMissions) {	
		QuestMissionWrapper[] availableQuests = Arrays.stream(availableQuestMissions)
				.map(m -> new QuestMissionWrapper(m.toString()))
				.toArray(QuestMissionWrapper[]::new);
		
		questBox.setModel(new DefaultComboBoxModel<QuestMissionWrapper>(availableQuests));
		questList.setModel(new DefaultListModel<QuestMissionWrapper>());
		questList.setPrototypeCellValue(availableQuests[0]);
		questModel = (DefaultListModel<QuestMissionWrapper>) (questList.getModel());
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
		mainFrame.setBounds(100, 100, 270, 295);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		
		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		mainPanel.setBounds(10, 10, 244, 245);
		mainFrame.getContentPane().add(mainPanel);
		mainPanel.setLayout(null);
		
		JButton startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startButtonActionPerformed(e);
			}
		});
		startButton.setBounds(134, 220, 101, 20);
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
		addQuestButton.setBounds(9, 170, 58, 20);
		mainPanel.add(addQuestButton);
		
		JButton removeButton = new JButton("Del");
		removeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeButtonActionPerformed(e);
			}
		});
		removeButton.setBounds(70, 170, 58, 20);
		mainPanel.add(removeButton);
		
		JButton upButton = new JButton("\u2191");
		upButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				upButtonActionPerformed(e);
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 4, 224, 138);
		mainPanel.add(scrollPane);
		
		questList = new JList<>();
		scrollPane.setViewportView(questList);
		questList.setBorder(null);
		upButton.setBounds(134, 144, 46, 22);
		mainPanel.add(upButton);
		
		JButton downButton = new JButton("\u2193");
		downButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				downButtonActionPerformed(e);
			}
		});
		downButton.setBounds(189, 144, 46, 22);
		mainPanel.add(downButton);
		
		abc2CheckBox = new JCheckBox("Antiban");
		abc2CheckBox.addActionListener((e) -> Vars.get().add("abc2Enabled", abc2CheckBox.isSelected()));
		abc2CheckBox.setToolTipText("Check this box if you would like to enable ABC2 antiban.");
		abc2CheckBox.setSelected(true);
		abc2CheckBox.setBounds(10, 220, 70, 20);
		mainPanel.add(abc2CheckBox);
		
		JButton loadAccountsButton = new JButton("Load Accts");
		loadAccountsButton.addActionListener((e) -> loadAccounts(ACCOUNT_PATH));
		loadAccountsButton.setBounds(134, 170, 101, 20);
		mainPanel.add(loadAccountsButton);
		
		JButton loadProfileButton = new JButton("Load Profile");
		loadProfileButton.addActionListener((e) -> loadProfile(FCQuestingProfile.PROFILE_PATH));
		loadProfileButton.setBounds(134, 195, 101, 20);
		mainPanel.add(loadProfileButton);
		
		JButton saveProfileButton = new JButton("Save Profile");
		saveProfileButton.addActionListener((e) -> saveProfile(FCQuestingProfile.PROFILE_PATH));
		saveProfileButton.setBounds(10, 195, 118, 20);
		mainPanel.add(saveProfileButton);
	}
	
	private void loadAccounts(String path) {
		try {
			fileChooser.setCurrentDirectory(new File(path));
			final int RETURN_VAL = fileChooser.showOpenDialog(mainFrame);
			switch(RETURN_VAL) {
				case JFileChooser.APPROVE_OPTION:
					parseAccountsFromFile(fileChooser.getSelectedFile());
				break;
			}
		} catch(Exception e) {
			General.println("Falling back on working directory...");
			loadAccounts(Util.getWorkingDirectory().getAbsolutePath());
		}
	}
	
	private void loadProfile(String path) {
		try {
			fileChooser.setCurrentDirectory(new File(path));
			final int RETURN_VAL = fileChooser.showOpenDialog(mainFrame);
			switch(RETURN_VAL) {
				case JFileChooser.APPROVE_OPTION:
					parseProfile(fileChooser.getSelectedFile());
				break;
			}
		} catch(Exception e) {
			General.println("Falling back on working directory...");
			loadProfile(Util.getWorkingDirectory().getAbsolutePath());
		}
	}
	
	private void saveProfile(String path) {
		try {
			fileChooser.setCurrentDirectory(new File(path));
			final int RETURN_VAL = fileChooser.showSaveDialog(mainFrame);
			switch(RETURN_VAL) {
				case JFileChooser.APPROVE_OPTION:
					saveProfile(fileChooser.getSelectedFile());
				break;
			}
		} catch(Exception e) {
			General.println("Falling back on working directory...");
			saveProfile(Util.getWorkingDirectory().getAbsolutePath());
		}
	}
	
	private void parseProfile(File file) {
		try(FileInputStream fIn = new FileInputStream(file);
			ObjectInputStream oIn = new ObjectInputStream(fIn);) {
			FCQuestingProfile profile = (FCQuestingProfile)oIn.readObject();
			abc2CheckBox.setSelected(profile.ABC2_ENABLED);
			Vars.get().add("abc2Enabled", abc2CheckBox.isSelected());
			General.println("Profile w/ " + profile.QUEST_QUEUE.size() + " quests loaded: " + file);
			addProfileToQuestBox(profile);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private void removeCurrentQuests() {
		while(!questModel.isEmpty()) {
			QuestMissionWrapper m = questModel.remove(0);
			
			if(!doesQuestBoxContainItem(m)) {
				questBox.addItem(m);
			}
		}
	}
	
	private boolean doesQuestBoxContainItem(QuestMissionWrapper w) {
		for(int i = 0; i < questBox.getItemCount(); i++) {
			if(questBox.getItemAt(i).equals(w)) {
				return true;
			}
		}
		
		return false;
	}
	
	private void addProfileToQuestBox(FCQuestingProfile profile) {
		removeCurrentQuests();
		profile.QUEST_QUEUE.forEach(m -> {
			if(!questModel.contains(m)) {
				((MissionManager)(m.getMission())).setScript(script);
				questModel.addElement(m);
			}
			for(int i = 0; i < questBox.getItemCount(); i++) {
				if(questBox.getItemAt(i).equals(m)) {
					questBox.removeItemAt(i);
				}
			}
		});
	}
	
	private void saveProfile(File file) {
		QuestMissionWrapper[] quests = new QuestMissionWrapper[questModel.size()];
    	questModel.copyInto(quests);
		Queue<QuestMissionWrapper> questQueue = new LinkedList<>(Arrays.asList(quests));
		FCQuestingProfile profile = new FCQuestingProfile(questQueue, abc2CheckBox.isSelected());
		try(FileOutputStream fOut = new FileOutputStream(file);
			ObjectOutputStream oOut = new ObjectOutputStream(fOut)) {
			oOut.writeObject(profile);
			General.println("Successfully saved current profile to " + file);
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void parseAccountsFromFile(File file) {
		General.println("Accounts file selected: " + file);
		try(FileReader fR = new FileReader(file);
			BufferedReader bR = new BufferedReader(fR)) {
			String line;
			while((line = bR.readLine()) != null) {
				String[] parts = line.split(":");
				if(parts.length > 0) {
					script.addAccountToQueue(parts[0], parts[1]);
				}
			}
			General.println("Added " + script.getAccountQueueSize() + " accounts to the queue");
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
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
    	
    	QuestMissionWrapper toRemove = questList.getModel().getElementAt(index - 1);
    	
    	questModel.remove(index - 1);
    	questModel.add(index, toRemove);
    	questList.setSelectedIndex(index - 1);
    } 

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {
    	int index = questList.getSelectedIndex();
    	
    	if(index + 1 == questModel.getSize() || questModel.getSize() <= 1)
    		return;
    	
    	QuestMissionWrapper toRemove = questList.getModel().getElementAt(index + 1);
    	
    	questModel.remove(index + 1);
    	questModel.add(index, toRemove);
    	questList.setSelectedIndex(index + 1);
    }                                          

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {
    	QuestMissionWrapper q = ((QuestMissionWrapper) questBox.getSelectedItem());
    	
    	if(q == null || q.getMission() == null)
    		return;
    	
    	if(!questModel.contains(q) && q.getMission().getMissionName().length() > 0)
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
    	addSelectedQuestsToScript(false);
    	script.setLoginBotState(script.getAccountQueueSize() == 0);
    }
    
    public void addSelectedQuestsToScript(boolean resetQuestMissions) { 
    	script.getSetMissions().clear();
    	
    	QuestMissionWrapper[] quests = new QuestMissionWrapper[questModel.size()];
    	questModel.copyInto(quests);
    	
    	for(QuestMissionWrapper q : quests) {
    		if(resetQuestMissions) {
    			q.resetMission();
    		}
    		
    		script.getSetMissions().add(q.getMission());
    	}
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
    
    public void useProfile(FCQuestingProfile profile) {
    	isUsingArgs = true;
    	profile.QUEST_QUEUE.stream()
    		.forEach(w -> {
    			w.resetMission();
    			script.getSetMissions().add(w.getMission());
    		});
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
