package SERVER;
import javax.swing.JFrame;
import javax.swing.JPanel;
import DaTa.UserDal;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.GridLayout;

import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Hashtable;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
/** ỨNG DỤNG CHAT TRONG MẠNG LAN
 ** Bản quyền thuộc về: Trần Phúc Vinh - DT13CTT02 - Đại Học Quảng Nam
 ** Email: vinhtranphuc@gmail.com, Sdt: 01682607172 */
public class Server extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable table;
	public JTextArea thongbao;
	private ServerSocket server;
	private JList<String> listOnl;
	DefaultListModel<String> listModel = new DefaultListModel<String>();
	public Hashtable<String, ThreadSever> listTk;
	String chonUs="";

	public Server() {
		super("Server - Chat LAN");
		setTitle("Server - Chat LAN");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				try {
					server.close();
					System.exit(0);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			@Override
			public void windowOpened(WindowEvent arg0) {
			Load();
			}
		});
		setSize(371, 382);
		giaodien();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	protected void Load() {
		UserDal da=new UserDal();
		table.setModel(da.xemDs());
		loadOnl();
	}
	public void loadOnl()
	{
		listModel=new DefaultListModel<String>();
		Enumeration<String> e = listTk.keys();
		while(e. hasMoreElements()){
		listModel.addElement(e.nextElement());
		}
		listOnl.setModel(listModel);
	}
	public void giaodien()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 371, 417);
		contentPane = new JPanel();
		contentPane.setBorder(null);
		contentPane.setBackground(new Color(204, 204, 255));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setForeground(new Color(51, 0, 0));
		tabbedPane.setBackground(Color.WHITE);
		tabbedPane.setBorder(null);
		tabbedPane.setBounds(10, 11, 335, 312);
		contentPane.add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Trạng thái Server", null, panel, null);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		thongbao = new JTextArea(10, 20);
		thongbao.setBackground(new Color(255, 255, 255));
		thongbao.setBorder(null);
		thongbao.setLineWrap(true);
		thongbao.setEditable(false);
		panel.add(thongbao);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.WHITE);
		tabbedPane.addTab("DS thành viên", null, panel_1, null);
		panel_1.setLayout(null);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(null);
		panel_2.setBounds(0, 0, 205, 252);
		panel_2.setBackground(Color.WHITE);
		panel_1.add(panel_2);
		panel_2.setLayout(new GridLayout(1, 0, 0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBorder(null);
		scrollPane.setBackground(new Color(255, 255, 204));
		panel_2.add(scrollPane);
		
		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
		chonUs=table.getModel().getValueAt(table.getSelectedRow(),0).toString();	
			}
		});
		table.setBackground(Color.WHITE);
		table.setForeground(new Color(51, 0, 0));
		scrollPane.setViewportView(table);
		
		listOnl = new JList<String>();
		listOnl.setBackground(Color.WHITE);
		listOnl.setBorder(new TitledBorder(null, "Thành viên Onl", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		listOnl.setBounds(208, 0, 118, 284);
		listOnl.setForeground(new Color(51, 0, 0));
		panel_1.add(listOnl);
		
		JButton button = new JButton("Xóa");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		UserDal us=new UserDal();
		if(chonUs.equals("")) JOptionPane.showConfirmDialog(null, "Chưa chọn User");   
		else us.xoaUs(chonUs);
		table.setModel(us.xemDs());
			}
		});
		button.setForeground(new Color(51, 0, 0));
		button.setFont(new Font("Tahoma", Font.BOLD, 14));
		button.setBackground(new Color(102, 153, 255));
		button.setBounds(0, 254, 205, 30);
		panel_1.add(button);
		
		JButton btnDong = new JButton("Đóng");
		btnDong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		System.exit(0);
			}
		});
		btnDong.setIcon(new ImageIcon(Server.class.getResource("/IconServer/Close Window-30.png")));
		btnDong.setBorder(null);
		btnDong.setBounds(10, 327, 335, 40);
		btnDong.setFont(new Font("Monospaced", Font.BOLD, 21));
		btnDong.setBackground(new Color(102, 153, 255));
		btnDong.setForeground(new Color(51, 0, 0));
		contentPane.add(btnDong);
	}
	private void khoidong(){
		try {
			listTk = new Hashtable<String, ThreadSever>();
			server = new ServerSocket(2207);
			thongbao.append("Server đã bắt đầu!\n");
			while(true){
				Socket client = server.accept();     
				new ThreadSever(this,client);       
			}
		} catch (IOException e) {
			thongbao.append("Không khởi động được Server\n");
		}
	}

	public static void main(String[] args) {
		new Server().khoidong();
	}

	public void actionPerformed(ActionEvent e) {
			try {
				server.close();
			} catch (IOException e1) {
				thongbao.append("Server không thể dừng lại\n");
			}
			System.exit(0);
	}
   
	public void guiTatCa(String from,String msg){
		Enumeration<String> e = listTk.keys();
		String name=null;
		while(e. hasMoreElements()){
			name=(String) e.nextElement();
		//if Tên !equal("0")
			if(name.compareTo(from)!=0){ 
				// chuyển tn tới client
                   listTk.get(name).guiDl("3",msg);
                }
		}
	}
	// lưu tên chat vào danh sách onl & gửi tới client
	public void capNhatDsOnl(String from){
		Enumeration<String> e = listTk.keys();
		String name=null;
		while(e. hasMoreElements()){
			name=(String) e.nextElement();
			if(name.compareTo(from)!=0) listTk.get(name).guiDl("4",getAllName());
		}
	}
	// lấy tất cả các tên trong list chat
	public String getAllName(){
		Enumeration<String> e = listTk.keys();
		String name="";
		while(e. hasMoreElements()){
			name+=(String) e.nextElement()+"\n";
		}
		return name;
	} 
}
