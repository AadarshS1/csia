import java.sql.*;


public class sql {
	public static void main(String[] args) throws Exception {
        
	       getConnection();
	        
	    }
	    
	      public static Connection getConnection(){
	       
	          Connection con = null;
	          try{
	              Class.forName("com.mysql.jdbc.Driver");
	              con = DriverManager.getConnection("jdbc:mysql://127.0.0.1/addressbook", "root", "pavithra15");
	              String query = "insert into addressbook.contact (name, nick_name, address) VALUES ('hello', 'hi', 'house');";
	              
	              PreparedStatement preparedStmt = con.prepareStatement(query);
	              preparedStmt.execute();

	          }
	          catch (Exception ex){
	              
	              System.out.println(ex.getMessage());
	              
	          }
	          System.out.print("it worked");
	          return con;
	                      
	                      
	          }

}
