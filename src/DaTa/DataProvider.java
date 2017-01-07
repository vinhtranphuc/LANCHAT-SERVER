package DaTa;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.table.DefaultTableModel;

/** ỨNG DỤNG CHAT TRONG MẠNG LAN
 ** Bản quyền thuộc về: Trần Phúc Vinh - DT13CTT02 - Đại Học Quảng Nam
 ** Email: vinhtranphuc@gmail.com, Sdt: 01682607172 */

public class DataProvider {
	DateFormat ngay = new SimpleDateFormat("dd-MM-yyyy");
	DateFormat gio= new SimpleDateFormat("HH:mm");
	Calendar cal = Calendar.getInstance();
	public String ngayHt=ngay.format(cal.getTime());
	public String gioHt=gio.format(cal.getTime());
	
    private Connection con=null;
    private Statement stmt = null;
    private PreparedStatement pstmt=null;
    private ResultSet rs = null;
    private String url, serverName="VINH", user="", pass="", dbName="ChatLan";
    public DataProvider() {
    }
 //Khai báo connect
    public DataProvider(String serName, String user, String pass, String dbName) {
        this.serverName = serName;
        this.user = user;
        this.pass = pass;
        this.dbName = dbName;
    }
    
  //Lấy kết nối
    public Connection getCon() throws SQLException {
        url = "jdbc:sqlserver://"+serverName+";databaseName="+dbName+";integratedSecurity=true";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(url,user,pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
   // Lấy statement
    public Statement getSta() throws SQLException {
      stmt = getCon().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        return stmt;
    }
   // Lấy Rs
    public ResultSet exQ(String query) throws SQLException {
    rs=getSta().executeQuery(query);
    return rs;
     }
  // xoa, dk
    public void PreSta(String query) throws SQLException{
    pstmt=getCon().prepareStatement(query);
    pstmt.executeUpdate();	
    }
    // Chèn, sửa
    public void exU(String query) throws SQLException {
    	getSta().executeUpdate(query);
     }
     
    // Lấy dữ liệu bảng
	public DefaultTableModel getTb(String query,String[] tieude,String[] files)
	{
	DefaultTableModel tb=new DefaultTableModel();
	try {
	String _query=query;
	ResultSet rs = exQ(_query);
	ArrayList<String[]> dulieubang = new ArrayList<String[]>();
		while(rs.next())
		{
			String[] dong = new String[tieude.length];
			for(int i=0;i<=tieude.length-1;i++)
			dong[i]=rs.getString(files[i]);
			dulieubang.add(dong);
		}
	String[][] data = new String[dulieubang.size()][tieude.length];
	for(int i=0; i<dulieubang.size(); i++)
	{
		data[i]=dulieubang.get(i);
	}
	tb.setDataVector(data,tieude);
	return tb;
	    }
	catch(Exception ex){
    System.out.println("Loi: "+ex);
    return null;
	}		
	}
	// Id tự tăng
	public String Ai(String tb,String ma)
		{
		    int id1=0,id2=0;
			String id = null;
			try {
				exQ("select * from "+tb+"");
					while(rs.next())
					{
						id = new String();
						id = rs.getString(ma);
						id1=Integer.parseInt(id);
						if(id1>=id2)
						{
							id2=id1;
						}
					}
					id=String.valueOf(id2+1);
					return id;
				}
				catch(Exception ex){
			    System.out.println("Lỗi Id - DAL/Connect/Id "+ex);
				return null;
				}
		}
	// Đếm số dòng
	public int demDong(String query)
	{
		int countRow=0;
		try {
	        exQ(query);
				while(rs.next())
				{
					countRow++;
				}
				return countRow;
			}
			catch(Exception ex){
				System.out.println("Lỗi đếm dòng - DAL/Connect: "+ex);
				return 1;
			}
	}
	
	public String cellTb(String query,String col)
	{
		String cell="";
		try {
			exQ(""+query+"");
				while(rs.next())
				{
					cell = rs.getString(col);;
				}
				if(!cell.equals("")) return cell;
				return "false";
			}
			catch(Exception ex){
		System.out.println("Lỗi: "+ex);
				return "Lỗi";
			}
	}
    public void setSta(Statement stmt) {
        this.stmt = stmt;
    }
    public void setRs(ResultSet rs) {
        this.rs=rs;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getServerName() {
        return serverName;
    }
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getPass() {
        return pass;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }
    public String getDbName() {
        return dbName;
    }
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}

