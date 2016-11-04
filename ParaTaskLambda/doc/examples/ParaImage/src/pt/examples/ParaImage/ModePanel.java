package pt.examples.ParaImage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ModePanel extends JPanel implements ActionListener {
	
	private MainFrame mainFrame;
	private JLabel lblParallelOn = new JLabel(new ImageIcon(Utils.getImg("parallelOn.png")));
	private JLabel lblSequentialOn = new JLabel(new ImageIcon(Utils.getImg("parallelOff.png")));

	private String switchToPar = "Switch to parallel";
	private String switchToSeq = "Switch to sequential";
	
	private JButton btnSwitch = new JButton(switchToSeq);
//	private JButton btnSequential = new JButton("Start sequential");

	private boolean modeParallel = true; 
	
	public ModePanel(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
		setPreferredSize(new Dimension(mainFrame.getWidth()-50, 45));
		
		setLayout(null);

		Dimension size = lblSequentialOn.getPreferredSize();
		lblSequentialOn.setBounds(70, 5, size.width, size.height);
		lblParallelOn.setBounds(70, 5, size.width, size.height);

//		btnParallel.setBackground(Color.WHITE);
//		btnSequential.setBackground(Color.WHITE);
//		btnParallel.addActionListener(this);
//		btnSequential.addActionListener(this);
//		add(btnParallel);
//		add(btnSequential);
		
//		size = btnSequential.getPreferredSize();
//		btnParallel.setBounds(470, 10, size.width, size.height);
//		btnSequential.setBounds(630, 10, size.width, size.height);
		
		btnSwitch.setBackground(Color.WHITE);
		btnSwitch.addActionListener(this);
		add(btnSwitch);
		size = btnSwitch.getPreferredSize();
		btnSwitch.setBounds(500, 10, size.width, size.height);
		
		changeMode();
	}
	
	public boolean isParallel() {
		return modeParallel;
	}
	
	private void changeMode() {
		modeParallel = !modeParallel ; 
		mainFrame.isParallel = modeParallel;
		if (modeParallel) {
//			btnSequential.setEnabled(true);
//			btnParallel.setEnabled(false);
			btnSwitch.setText(switchToSeq);
			setBackground(Color.GREEN);
			add(lblParallelOn);
			remove(lblSequentialOn);
		} else {
//			btnSequential.setEnabled(false);
//			btnParallel.setEnabled(true);
			btnSwitch.setText(switchToPar);
			setBackground(Color.RED);
			add(lblSequentialOn);
			remove(lblParallelOn);
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		changeMode();
	}
}