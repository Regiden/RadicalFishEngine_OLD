/*
 * Copyright (c) 2012, Stefan Lange
 * 
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of Stefan Lange nor the names of its contributors may
 *       be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package de.radicalfish;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl.LwjglFiles;
import com.badlogic.gdx.backends.lwjgl.LwjglPreferences;
import com.badlogic.gdx.files.FileHandle;
import de.radicalfish.GameContainer;
import de.radicalfish.tests.utils.RadicalFishTest;
import de.radicalfish.tests.utils.RadicalFishTests;
import de.radicalfish.util.RadicalFishException;

/**
 * Use this calls to start any test! This idea comes for libgdx tests! it makes starting all the test easy as eating pie
 * :)
 * 
 * @author Stefan Lange
 * @version 0.0.0
 * @since 21.08.2012
 */
public class TestStarter extends JFrame {
	private static final long serialVersionUID = 1L;
	
	// MAIN AND C'TOR
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	public TestStarter() {
		super("libgdx Tests");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(new TestList());
		pack();
		setSize(300, 600);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new TestStarter();
			}
		});
	}
	
	// INNER CLASSES
	// ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
	private class TestList extends JPanel {
		private static final long serialVersionUID = 1L;
		
		public TestList() {
			setLayout(new BorderLayout());
			
			final JButton button = new JButton("Run Test");
			
			final JList list = new JList(RadicalFishTests.getNames());
			JScrollPane pane = new JScrollPane(list);
			
			DefaultListSelectionModel m = new DefaultListSelectionModel();
			m.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			m.setLeadAnchorNotificationEnabled(false);
			list.setSelectionModel(m);
			
			list.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent event) {
					if (event.getClickCount() == 2)
						button.doClick();
				}
			});
			list.addKeyListener(new KeyAdapter() {
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER)
						button.doClick();
				}
			});
			
			final Preferences prefs = new LwjglPreferences(new FileHandle(new LwjglFiles().getExternalStoragePath() + ".prefs/desktop-tests"));
			list.setSelectedValue(prefs.getString("last", null), true);
			
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String testName = (String) list.getSelectedValue();
					RadicalFishTest test = RadicalFishTests.newTest(testName);
					
					LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
					config.width = test.getWidth();
					config.height = test.getHeight();
					config.title = test.getTitle();
					config.useGL20 = test.needsGL20();
					config.forceExit = false;
					config.useCPUSynch = false;
					
					GameContainer app = null;
					try {
						app = new GameContainer(config.title, test, config.width, config.height, config.useGL20);
					} catch (RadicalFishException e1) {
						e1.printStackTrace();
					}
					
					if(app != null) {
						dispose();
						test.initContainer(app);
						new LwjglApplication(app, config);
						prefs.putString("last", testName);
						prefs.flush();
					}
					
					
				}
			});
			
			add(pane, BorderLayout.CENTER);
			add(button, BorderLayout.SOUTH);
			
		}
	}
}
