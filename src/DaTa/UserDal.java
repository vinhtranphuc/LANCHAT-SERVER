package DaTa;
import javax.swing.table.DefaultTableModel;
/** ỨNG DỤNG CHAT TRONG MẠNG LAN
 ** Bản quyền thuộc về: Trần Phúc Vinh - DT13CTT02 - Đại Học Quảng Nam
 ** Email: vinhtranphuc@gmail.com, Sdt: 01682607172 */
public class UserDal extends DataProvider{
	public DefaultTableModel xemDs()
	{
	String query="select * from tb_taikhoan";
	String[] tieude={"UserName","PassWord","Name"};
	String[] files={"UserName","PassWord","HoTen"};
	if(tieude.length!=files.length) System.out.println("tieude != files");
	return getTb(query,tieude,files);
	}
	
	public String layHoTen(String TenTk,String Mk)
	{
	String query="select * from tb_taikhoan where UserName=N'"+TenTk+"' and PassWord=N'"+Mk+"'";
	String col="HoTen";
    return cellTb(query,col);
	}
	
	public boolean xoaUs(String UserName)
	{
	try 
    {  
    PreSta("delete from tb_taikhoan where UserName=N'"+UserName+"'");	
    return true;
	} catch (Exception e) {
	System.out.println("Loi UserDal/xoaUs: "+e);
	} 
    return false;
	} 	
	
	public boolean themUs(String usName,String Pass,String Name)
	{
	try{
	String query="insert into tb_taikhoan values(N'"+Ai("tb_taikhoan","Id")+"',N'"+usName+"',N'"+Pass+"',N'"+Name+"')";	
	exU(query);
	return true;
	}catch(Exception ex)
	{
	System.out.println("Lỗi UserDal/themUs "+ex);
	}
	return false;
	}
	
	public boolean checkUs(String usName)
	{
	try{
	String query="select * from tb_taikhoan where UserName=N'"+usName+"' ";	
	int sodong=1;
	sodong=demDong(query);
    if(sodong==0) return true;
	}catch(Exception ex)
	{
	System.out.println("Lỗi UserDal/checkUs "+ex);
	}
	return false;
	}
	
	public boolean checkName(String Name)
	{
	try{
	String query="select * from tb_taikhoan where HoTen=N'"+Name+"' ";	
	int sodong=1;
	sodong=demDong(query);
    if(sodong==0) return true;
	}catch(Exception ex)
	{
	System.out.println("Lỗi UserDal/checkName "+ex);
	}
	return false;
	}
}

