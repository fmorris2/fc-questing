package scripts.fc.fcquester;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;

import scripts.fc.api.utils.Utils;
import scripts.fc.fcquester.data.QuestLoader;
import scripts.fc.framework.mission.Mission;
import scripts.fc.missions.fcdoricsquest.FCDoricsQuest;
import scripts.fc.missions.fctutorial.FCTutorial;


public class FCQuesterGUI extends javax.swing.JFrame 
{

	private static final long serialVersionUID = -5312313580474411772L;
	
	private FCQuester script;
	private DefaultListModel<Mission> questModel;
	
	public boolean hasFilledOut;
	
	public FCQuesterGUI(FCQuester script) 
    {
		this.script = script;
		this.script.questLoader = new QuestLoader(script);
        initComponents();
        prepareOptions();
        Utils.handleGui(this);
        this.setResizable(false);
    }
	
	private void prepareOptions()
	{
		questsBox.setModel(new DefaultComboBoxModel<Mission>(script.questLoader.getQuests()));
		questList.setModel(new DefaultListModel<Mission>());
		questList.setPrototypeCellValue(script.questLoader.getQuests()[0]);
		questModel = (DefaultListModel<Mission>) (questList.getModel());
	}                             
    

	private void initComponents() 
	{
        titleLabel = new javax.swing.JLabel();
        questsBox = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        questList = new javax.swing.JList<>();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        startButton = new javax.swing.JButton();
        moveUpButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("FC Questing");
        setAlwaysOnTop(true);

        titleLabel.setFont(new java.awt.Font("Serif", 2, 24)); // NOI18N
        titleLabel.setText("FC Questing");

        jScrollPane1.setViewportView(questList);

        addButton.setText("Add");
        addButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.setText("Remove");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });

        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        moveUpButton.setText("Up");
        moveUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                moveUpButtonActionPerformed(evt);
            }
        });

        downButton.setText("Down");
        downButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(140, 140, 140)
                        .addComponent(removeButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(moveUpButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(questsBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(downButton)))
                .addContainerGap(63, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(startButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(moveUpButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addButton)
                .addGap(52, 52, 52))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(titleLabel)
                        .addGap(18, 18, 18)
                        .addComponent(questsBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(downButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(removeButton)
                .addGap(3, 3, 3)
                .addComponent(startButton)
                .addGap(24, 24, 24))
        );

        pack();
    }// </editor-fold>           
    
    private void moveUpButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {      
    	int index = questList.getSelectedIndex();
    	
    	if(index == 0)
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
    	Mission q = (Mission) questsBox.getSelectedItem();
    	
    	if(!questModel.contains(q) && q.getMissionName().length() > 0)
    	{
    		questModel.addElement(q);
    		questsBox.removeItem(q);
    	}
    }                                         

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {
    	if(!questModel.isEmpty())
    		questsBox.addItem(questModel.remove(questList.getSelectedIndex()));
    }                                            

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) 
    {
    	setVisible(false);
    	hasFilledOut = true;
    	script.getSetMissions().clear();
    	
    	Mission[] quests = new Mission[questModel.size()];
    	questModel.copyInto(quests);
    	
    	for(Mission q : quests)
    		script.getSetMissions().add(q);
    }         
    
    public void randomlyAddQuests(boolean sevenQp)
    {
    	List<Mission> quests = new ArrayList<>();
    	
    	for(Mission q : script.questLoader.getQuests())
    	{
    		if(sevenQp && q instanceof FCDoricsQuest)
    			continue;
    		
    		if(!(q instanceof FCTutorial))
    			quests.add(q);
    	}
    	
    	Collections.shuffle(quests);
    	
    	script.getSetMissions().add(new FCTutorial(script));
    	
    	for(Mission q : quests)
    		script.getSetMissions().add(q);
    	
    	hasFilledOut = true;
    	this.dispose();
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton addButton;
    private javax.swing.JButton downButton;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton moveUpButton;
    private javax.swing.JList<Mission> questList;
    private javax.swing.JComboBox<Mission> questsBox;
    private javax.swing.JButton removeButton;
    private javax.swing.JButton startButton;
    private javax.swing.JLabel titleLabel;
}
