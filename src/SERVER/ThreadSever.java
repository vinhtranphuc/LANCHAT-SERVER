package SERVER;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import DaTa.UserDal;
/** ỨNG DỤNG CHAT TRONG MẠNG LAN
 ** Bản quyền thuộc về: Trần Phúc Vinh - DT13CTT02 - Đại Học Quảng Nam
 ** Email: vinhtranphuc@gmail.com, Sdt: 01682607172 */
public class ThreadSever extends Thread{
    public Socket client;
	public Server server;
	private String Ten;
	private DataOutputStream dos;
	private DataInputStream dis;
	private boolean run;
	private String checkUser;
	private String checkPass;
	private String Us="";
	private String Ps="";
	private String Name="";
	UserDal us=new UserDal();
	public ThreadSever()
	{	
	}
        
	public ThreadSever(Server server, Socket client){
		try {
			this.server=server;
			this.client=client;
			dos= new DataOutputStream(client.getOutputStream());
			dis= new DataInputStream(client.getInputStream());
			run=true;
			this.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
	while(run){
	  // xử lý tín hiệu
		String tinhieu=thuDl();
		if(tinhieu.equals("DangNhap")) XuLyLogin();
		else if(tinhieu.equals("DangKy")) XuLyDangKy();
		else XuLyChat(tinhieu);
	}
  }
	
	public void XuLyLogin()
	{
	String msg=null;
		while(run)
		{
			int stt = Integer.parseInt(thuDl());
			switch(stt)
			{
			case 12: 
				msg=thuDl();
				checkUser=msg;
				System.out.println("Server: "+checkUser);
				break;
			case 13:
				msg=thuDl();
				checkPass=msg;
				System.out.println("Server: "+checkPass);
				break;
		    case 14: 
		       String Name=us.layHoTen(checkUser,checkPass);
		       if(Name.equals("false"))
		       {
		      this.guiDl("15","Sai tài khoản hoặc mật khẩu");
		    //  System.out.println("Sver: Sai tài khoản hoặc mật khẩu");
		       }
		       else this.guiDl("16",Name);
		    break;
			}
		}	
	}
	
	public void XuLyChat(String tinhieu)
	{
	String msg=null;
		while(run){
			Ten=tinhieu; System.err.println("ten: "+  Ten);
			if(Ten.compareTo("0")==0){
				logout();
			}
			else {
			// nếu tên đang lưu trong listtk > logout
				if(ktraTen(Ten)){
					guiDl("0");
				}
				else{
					server.thongbao.append(Ten+" mới zô phòng\n");
					server.guiTatCa(Ten,Ten+" mới zô phòng\n");
					server.listTk.put(Ten,this);
					server.loadOnl();
					server.capNhatDsOnl(Ten);
					guiDl("1");
					// gửi danh sách onl tới client (tinhieu=4)
					diplayAllUser();
					while(run){
						int stt = Integer.parseInt(thuDl());
						switch(stt){
							case 0:
								diskChat(this.Ten);
								server.loadOnl();
								break;
							case 1:
								msg = thuDl();
								server.guiTatCa(Ten,Ten+" : "+msg);
								break;
						}
					}
						
					}
				}
			}
	}
	
	public void diskChat(String Ten)
	{
		run=false;
		server.listTk.remove(Ten);
		exit(Ten);	
	}
	
	public void XuLyDangKy()
	{
		String msg=null;
			while(run)
			{
				int stt = Integer.parseInt(thuDl());
				switch(stt)
				{
				case 8: 
					msg=thuDl();
					Us=msg;
					break;
				case 9: 
					msg=thuDl();
					Ps=msg;
					break;
				case 10:
					msg=thuDl();
					Name=msg;
				    break;
				case 11: 
				if(!us.checkName(Name)) Name=Name+" - "+Us;
				if(!us.checkUs(Us)) 
					{
					this.guiDl("17","Trùng tên đăng nhập, đổi đi!!");
					}
				else
				if(this.themCSDl(Us,Ps,Name)==true)
				{
				server.thongbao.append(Name+" mới đăng ký chat \n");
				System.out.println("Sver: đký Ok! - tên j đây: "+Name);
				this.guiDl("17","Đăng ký thành công!");
				server.Load();
				}
				else 
				{
				//	System.out.println("Sver: dm`, éo hỉu lỗi cái je`");
					this.guiDl("17","lỗi nữa rồi");
				};
				break;
				}
			}
	}
	
	public boolean themCSDl(String Us,String Ps,String Name)
	{
	UserDal us=new UserDal();
	return us.themUs(Us,Ps,Name);
	}
	
	private void logout() {
		try {
			dos.close();
			dis.close();
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void exit(String Ten){
		try {
			server.capNhatDsOnl(Ten);
			dos.close();
			dis.close();
			client.close();
			server.thongbao.append(Ten+" đã thoát\n");
			server.guiTatCa(Ten,Ten+" đã thoát\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean ktraTen(String ten){
               
		return server.listTk.containsKey(ten);
	}
	
	private void guiDl(String data){
		try {
			dos.writeUTF(data);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void guiDl(String msg1,String msg2){
		guiDl(msg1);
		guiDl(msg2);
	}
	private String thuDl(){
		String data=null;
		try {
			data=dis.readUTF();              
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}
	private void diplayAllUser(){
		String name = server.getAllName();
		guiDl("4");
		guiDl(name);
	}
  
}
