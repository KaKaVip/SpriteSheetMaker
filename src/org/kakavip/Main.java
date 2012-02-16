/**
 * Author: KakaVip
 * Email: haipq@ymeo.net
 * 
 */

package org.kakavip;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.List;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

public class Main    {

	private JFrame frmSpriteSheetMaker;
	private int rows;
	private int cols;
	private int padding;
	private int chunks;
	private int chunkWidth, chunkHeight; 
	private int type;
	private List list;
	private JFileChooser jFile;
	private Task task;
	private String baseDir = "", saveDir = "";
	
	
	private JButton btn_make;
	private JProgressBar progressBar;
	private JTextField txt_filesave;
	private JTextField txt_cols;
	private JTextField txt_rows;
	private JTextField txt_padding;
	private JCheckBox cb_automatrix;
	
	private BufferedImage finalImg;
	private JTextField txt_width;
	private JTextField txt_height;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frmSpriteSheetMaker.setVisible(true);
					//window.frmSpriteSheetMaker.set
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}
	
	

	/**
	 * Initialize the contents of the frame.
	 */
	protected void setStatus(String mStatus){
		
		frmSpriteSheetMaker.setTitle("Sprite Sheet Maker v1.3- by KaKaVip! Status: " +  mStatus);
		
	}
	
	class Task extends SwingWorker<Void, Void>{

		@Override
		protected Void doInBackground() throws Exception {
				//int i = 0;
				btn_make.setEnabled(false);
				progressBar.setForeground(Color.decode("#00a8ff"));
				getInfo();
			return null;
		}
		
		@Override
		protected void done() {
			btn_make.setEnabled(true);
			setStatus(" 100% completed !");
		}
		
	}
	
	private void getInfo(){
		chunks = list.getItemCount();
		String OS = "";
		if(Utils.isWindows()){
			System.out.println("isWindows");
			OS = "\\";
		}else if(Utils.isUnix()){
			System.out.println("isUnix");
			OS = "/";
		}
		File[] imgFiles = new File[chunks];
        for (int i = 0; i < chunks; i++) {
            imgFiles[i] = new File(baseDir + OS + list.getItem(i).toString() );
            System.out.println(baseDir + OS + list.getItem(i).toString());
        }

       //creating a bufferd image array from image files
        BufferedImage[] buffImages = new BufferedImage[chunks];
        for (int i = 0; i < chunks; i++) {
            try {
				buffImages[i] = ImageIO.read(imgFiles[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        type = buffImages[0].getType();
        chunkWidth = buffImages[0].getWidth();
        chunkHeight = buffImages[0].getHeight();
        
        cols = Integer.parseInt(txt_cols.getText().toString());
        rows = Integer.parseInt(txt_rows.getText().toString());
        padding = Integer.parseInt(txt_padding.getText().toString());
        
        int fixX = 0;
        int fixY = 0;
        
        if(cols == 1){
        	fixX =0;
        }else{
        	fixX = padding * (cols -1);
        }
        
        if(rows == 1){
        	fixY =0;
        }else{
        	fixY = padding * (rows -1);
        }
        
        
        int maxWidth = chunkWidth * cols + (padding * 2 * (cols)) - fixX;
        int maxHeight = chunkHeight * rows + (padding * 2 * (rows)) - fixY;
        
        System.out.println("maxWidth:" + maxWidth + "maxHeight:" + maxHeight);
        
        txt_width.setText(""+getWidthHeight(maxWidth));
        txt_height.setText(""+getWidthHeight(maxHeight));
        
        finalImg = new BufferedImage(maxWidth, maxHeight, type);
		progressBar.setMaximum(chunks);
		System.out.println("chunks" + chunks);
		
		int num = 0;
		int widthFix = 0;
		int HeightFix = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
            	if(num < list.getItemCount()){
            		 finalImg.createGraphics().drawImage(buffImages[num], chunkWidth * j + padding * (j+1) , padding * (i+1) + chunkHeight * i, null);
                     System.out.println("num" + num);
                     num++;
                     progressBar.setValue(num);
            	}else{
            		break;
            	}
               
            }
        }
        
        try {
			ImageIO.write(finalImg, "png", new File(txt_filesave.getText().toString()));
		} catch (IOException e) {
			progressBar.setForeground(Color.decode("#ff0000"));
		}
	}
	
	private void autoMatrix(){
		if(cb_automatrix.isSelected()){
			
			int r = 1;
			int c = 1;
			
			double s = Math.sqrt(list.getItemCount());
			
			r = (int) Math.round(s);
			c = (int) Math.ceil(s);
			
			txt_rows.setText("" + r);
			txt_cols.setText("" + c);
			
			
			
		}
	}
	
	private void initialize() {
		frmSpriteSheetMaker = new JFrame();
		frmSpriteSheetMaker.setResizable(false);
		frmSpriteSheetMaker.setIconImage(Toolkit.getDefaultToolkit().getImage("icon.png"));
		frmSpriteSheetMaker.setTitle("Sprite Sheet Maker v1.3 - by KaKaVip");
		frmSpriteSheetMaker.getContentPane().setBackground(new Color(0, 0, 0));
		frmSpriteSheetMaker.setBounds(100, 100, 576, 305);
		frmSpriteSheetMaker.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSpriteSheetMaker.getContentPane().setLayout(null);
		
		txt_filesave = new JTextField();
		txt_filesave.setForeground(new Color(153, 153, 153));
		txt_filesave.setBackground(new Color(51, 51, 51));
		txt_filesave.setBounds(10, 220, 315, 20);
		frmSpriteSheetMaker.getContentPane().add(txt_filesave);
		txt_filesave.setColumns(10);
		
		JButton btn_Browser = new JButton("Browser");
		btn_Browser.setForeground(new Color(255, 255, 255));
		btn_Browser.setBackground(new Color(0, 0, 0));
		btn_Browser.setBounds(340, 219, 96, 23);
		frmSpriteSheetMaker.getContentPane().add(btn_Browser);
		btn_Browser.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				saveFiles();
			}
		});
		
		JLabel lblCollum = new JLabel("Columns");
		lblCollum.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCollum.setForeground(new Color(255, 255, 255));
		lblCollum.setBounds(455, 20, 46, 14);
		frmSpriteSheetMaker.getContentPane().add(lblCollum);
		
		txt_cols = new JTextField();
		txt_cols.setForeground(new Color(255, 255, 255));
		txt_cols.setBackground(new Color(0, 0, 0));
		txt_cols.setText("1");
		txt_cols.setBounds(517, 16, 44, 20);
		frmSpriteSheetMaker.getContentPane().add(txt_cols);
		txt_cols.setColumns(10);
		
		JLabel lblRows = new JLabel("Rows");
		lblRows.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblRows.setForeground(new Color(255, 255, 255));
		lblRows.setBounds(455, 52, 46, 14);
		frmSpriteSheetMaker.getContentPane().add(lblRows);
		
		txt_rows = new JTextField();
		txt_rows.setForeground(new Color(255, 255, 255));
		txt_rows.setBackground(new Color(0, 0, 0));
		txt_rows.setText("1");
		txt_rows.setBounds(517, 49, 44, 20);
		frmSpriteSheetMaker.getContentPane().add(txt_rows);
		txt_rows.setColumns(10);
		
		JLabel lblPadding = new JLabel("Padding");
		lblPadding.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPadding.setForeground(new Color(255, 255, 255));
		lblPadding.setBounds(455, 84, 46, 14);
		frmSpriteSheetMaker.getContentPane().add(lblPadding);
		
		txt_padding = new JTextField();
		txt_padding.setText("0");
		txt_padding.setForeground(new Color(255, 255, 255));
		txt_padding.setBackground(new Color(0, 0, 0));
		txt_padding.setBounds(517, 80, 44, 20);
		frmSpriteSheetMaker.getContentPane().add(txt_padding);
		txt_padding.setColumns(10);
		
		JButton btn_Open= new JButton("Add files");
		btn_Open.setForeground(new Color(255, 255, 255));
		btn_Open.setBackground(new Color(0, 0, 0));
		btn_Open.setBounds(340, 16, 96, 23);
		frmSpriteSheetMaker.getContentPane().add(btn_Open);
		
		btn_Open.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				openFiles();
			}
		});
		
		
		list = new List();
		list.setForeground(new Color(255, 255, 255));
		list.setMultipleSelections(false);
		list.setBackground(new Color(51, 51, 51));
		list.setBounds(10, 16, 315, 186);
		frmSpriteSheetMaker.getContentPane().add(list);
		list.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(arg0.isPopupTrigger()){
					openFiles();
				}
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {}
			
			@Override
			public void mouseExited(MouseEvent arg0) {}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {}
		});
		

		JButton btn_removeOne = new JButton("Remove selected");
		btn_removeOne.setToolTipText("Remove selected");
		btn_removeOne.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn_removeOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(list.getSelectedIndex()>=0){
					System.out.println("Selected: " + list.getSelectedIndex());
					list.remove(list.getSelectedIndex());
					setStatus("Total " + list.getItemCount() + " files");
					autoMatrix();
				}
			}
		});
		btn_removeOne.setHorizontalAlignment(SwingConstants.LEFT);
		btn_removeOne.setBackground(new Color(0, 0, 0));
		btn_removeOne.setForeground(new Color(255, 255, 255));
		btn_removeOne.setBounds(340, 52, 96, 23);
		frmSpriteSheetMaker.getContentPane().add(btn_removeOne);
		
		JButton btn_removeAll = new JButton("Remove all");
		btn_removeAll.setFont(new Font("Tahoma", Font.PLAIN, 10));
		btn_removeAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				list.removeAll();
				setStatus("Remove all files");
				autoMatrix();
			}
		});
		
		btn_removeAll.setHorizontalAlignment(SwingConstants.LEFT);
		btn_removeAll.setBackground(new Color(0, 0, 0));
		btn_removeAll.setForeground(new Color(255, 255, 255));
		btn_removeAll.setBounds(340, 85, 96, 23);
		frmSpriteSheetMaker.getContentPane().add(btn_removeAll);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(10, 251, 550, 14);
		progressBar.setBackground(Color.BLACK);
		progressBar.setForeground(Color.decode("#00a8ff"));
		frmSpriteSheetMaker.getContentPane().add(progressBar);
		
		btn_make = new JButton("Make Sprite");
		btn_make.setForeground(new Color(255, 255, 255));
		btn_make.setBackground(new Color(0, 0, 0));
		btn_make.setBounds(455, 186, 106, 54);
		frmSpriteSheetMaker.getContentPane().add(btn_make);
		
		txt_width = new JTextField();
		txt_width.setEditable(false);
		txt_width.setBackground(Color.BLACK);
		txt_width.setForeground(Color.WHITE);
		txt_width.setBounds(517, 110, 44, 22);
		frmSpriteSheetMaker.getContentPane().add(txt_width);
		txt_width.setColumns(10);
		
		JLabel lblWidth = new JLabel("Width");
		lblWidth.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblWidth.setForeground(Color.WHITE);
		lblWidth.setBounds(455, 113, 56, 16);
		frmSpriteSheetMaker.getContentPane().add(lblWidth);
		
		txt_height = new JTextField();
		txt_height.setEditable(false);
		txt_height.setForeground(Color.WHITE);
		txt_height.setBackground(Color.BLACK);
		txt_height.setBounds(517, 145, 44, 22);
		frmSpriteSheetMaker.getContentPane().add(txt_height);
		txt_height.setColumns(10);
		
		JLabel lblHeight = new JLabel("Height");
		lblHeight.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblHeight.setForeground(Color.WHITE);
		lblHeight.setBounds(455, 149, 56, 16);
		frmSpriteSheetMaker.getContentPane().add(lblHeight);
		
		cb_automatrix = new JCheckBox("Auto matrix");
		cb_automatrix.setSelected(true);
		cb_automatrix.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if(event.getID() == MouseEvent.MOUSE_CLICKED){
					if(cb_automatrix.isSelected()){
						cb_automatrix.setSelected(false);
						txt_rows.setEditable(true);
						txt_cols.setEditable(true);
					}else{
						cb_automatrix.setSelected(true);
						txt_rows.setEditable(false);
						txt_cols.setEditable(false);
					}
				}

			}
		});
		cb_automatrix.setForeground(Color.WHITE);
		cb_automatrix.setBackground(Color.BLACK);
		cb_automatrix.setBounds(340, 120, 113, 25);
		frmSpriteSheetMaker.getContentPane().add(cb_automatrix);

		btn_make.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(txt_filesave.getText().equals("")){
					setStatus("- please choose where to save !");
					btn_make.setForeground(Color.decode("#ff0000"));
				}else if(
						Integer.parseInt(txt_cols.getText().toString()) >=20 && Integer.parseInt(txt_rows.getText().toString()) >=20
						&& Integer.parseInt(txt_cols.getText().toString()) <= 0 && Integer.parseInt(txt_rows.getText().toString()) <= 0){
					setStatus("- limit of the row and cols of 20 !");
					btn_make.setForeground(Color.decode("#ff0000"));
				}else if(list.getItemCount() <= 0){
					setStatus("- please insert files !");
					btn_make.setForeground(Color.decode("#ff0000"));
				}else{
					btn_make.setForeground(Color.decode("#ffffff"));
					task = new Task();
			        task.execute();
				}
			}
		});

	}
	
	private void openFiles(){
		jFile = new JFileChooser();
		jFile.setFileFilter(new ImageFilter());
		jFile.setAcceptAllFileFilterUsed(false);
		jFile.setMultiSelectionEnabled(true);
		
		ImagePreviewPanel preview = new ImagePreviewPanel();
		jFile.setAccessory(preview);
		jFile.addPropertyChangeListener(preview);
		
		try {
			jFile.setCurrentDirectory(new File(baseDir));
		} catch (Exception e2) {
			
			
		}
		
		
		int returnVal = jFile.showOpenDialog(null);
		if(returnVal == JFileChooser.APPROVE_OPTION ){
			File files[] = jFile.getSelectedFiles();
			baseDir = jFile.getCurrentDirectory().toString();
			System.out.println("Da chon file baseDir" + baseDir);
			for(int i = 0; i < files.length; i++){
				list.add(files[i].getName(), list.getItemCount() + i);
			}
			
			setStatus("Total " + list.getItemCount() + " files");
			autoMatrix();
		}
		System.out.println("openFiles");
	}
	
	private void saveFiles(){
		
		JFileChooser jFilesave = new JFileChooser();
		
		jFilesave.setFileFilter(new ImageFilter());
		jFilesave.setAcceptAllFileFilterUsed(false);
		jFilesave.setMultiSelectionEnabled(true);
		if(saveDir.equals("")){
			saveDir = baseDir;
		}
		try {
			jFilesave.setCurrentDirectory(new File(saveDir));
		} catch (Exception e2) {}
		
		int returnVal = jFilesave.showSaveDialog(null);
		
		if(returnVal == JFileChooser.APPROVE_OPTION ){
				saveDir = jFilesave.getCurrentDirectory().toString();
				txt_filesave.setText(jFilesave.getSelectedFile().toString() + ".png");
		}
		System.out.println("openFiles");
	}
	
	private int getWidthHeight(int nV){
		int nReturn = 2;
		int nValue = 2;
			while (true) {
				if(nValue >= nV){
					nReturn = nValue;
					break;
				}
				nValue *= 2 ; 
			}
		return nReturn;		
	}
}


