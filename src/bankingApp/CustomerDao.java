package bankingApp;
import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

import java.sql.*;  

public class CustomerDao{
	public static Connection getConnection(){
		
		Connection con=null;
		try{
			Class.forName("com.mysql.jdbc.Driver");
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bank?verifyServerCertificate=false&useSSL=true","root","root");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return con;
		
	}
	
	public static int saveCustomerDetails(Customer c){
		int status=0;
		try{
			
		
		Connection	con=CustomerDao.getConnection();
		String sql = "insert into cust_details(name,password,balance) values(?,?,?)";	
		PreparedStatement ps=con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		
		ps.setString(1, c.getName());
		ps.setString(2, c.getPassword());
		ps.setInt(3, c.getBalance());
		
		status=ps.executeUpdate();
		
		int auto=0;
        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) {
        auto=rs.getInt(1);
         System.out.println("Auto Generated Primary Key " + auto);
        }
        
        String q ="INSERT INTO account(balance,cust_id) values(?,?)";
        
        PreparedStatement s1 = con.prepareStatement(q);
        s1.setInt(1,1000);
        s1.setInt(2,auto);
        int i=s1.executeUpdate();
		con.close();
		
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return status;
		
	}
	
	public static int userSignIn(Customer c){
		int status=0;
		String name1=c.getName();
		String password=c.getPassword();
		String query = "select password from cust_details where name=\""+name1+"\"";
		
	//  int cid=c.getId();
	//	session.setAttribute("cid", cid);
		try{
		Connection con=CustomerDao.getConnection();
		Statement stmt=con.createStatement();
		ResultSet rs=stmt.executeQuery(query);
		
		while(rs.next()){
			String p=rs.getString(1);
			if(p.equals(password)){
				status=1;
			}else{
				status=0;
			}
		}
		
		con.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		return status;
	}
	
	public static Customer userSign(Customer c){
		int status=0;
		String name1=c.getName();
		String password=c.getPassword();
		String query = "select password from cust_details where name=\""+name1+"\"";
		
		//int cid=c.getId();
		//session.setAttribute("cid", cid);
		
		try{
		Connection con=CustomerDao.getConnection();
		Statement stmt=con.createStatement();
		ResultSet rs=stmt.executeQuery(query);
		
		while(rs.next()){
			String p=rs.getString(1);
			if(p.equals(password)){
				status=1;
			
			}else{
				status=0;
				
			}
		}
		
		con.close();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		if(status==1){ 
			return c;
		}else{
			return null;
		}
		
	}
	
	public static boolean TranserAmount(int credited_acc,int debited_acc,int amount,int c_id){
		// Query for validation..if amount entered is less than or equal to 
		// balance than only do transaction.
		String query="select balance from account where cust_id='"+c_id+"';";
		
		boolean tranferable=false;
		try{
			Connection con=CustomerDao.getConnection();
			Statement stmt = con.createStatement();
			
			ResultSet r=stmt.executeQuery(query);
			
			// Validation
			if(r.next()){
				int bal=r.getInt("balance");
				if(bal>=amount){
					tranferable=true;
					PreparedStatement ps=con.prepareStatement("insert into transaction(credited_acc,debited_acc,amount) values(?,?,?)");
					
					ps.setInt(1,credited_acc );
					ps.setInt(2, debited_acc);
					ps.setInt(3, amount);
					
					int status=ps.executeUpdate();

					
					String query1="update account set balance=balance-\""+amount+"\" where cust_id =\""+c_id+"\"";
					
					String query2="update account set balance=balance+\""+amount+"\"where cust_id =\""+c_id+"\"";
					
					stmt.executeUpdate(query1);
					stmt.executeUpdate(query2);
					
				}else{
					System.out.println("Sorry..You do not have enough balance to be transfered..!!");
				}
			}
			
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return tranferable;
		
	}
	
	public static Transaction ListTransaction(int userAccNo){

		String query1="select credited_acc from transaction where debited_acc='"+userAccNo+"';";
		String query2="select debited_acc from transaction where credited_acc='"+userAccNo+"';";
		String query3="select amount from transaction where debited_acc='"+userAccNo+"';";
		String query4="select amount from transaction where credited_acc='"+userAccNo+"';";
		Transaction t=new Transaction();
		
		Connection con=null;
		try{
			con=CustomerDao.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(query1);
			
			while(rs.next()){
				
				String tmp=rs.getString("credited_acc");
				t.result1.add(tmp);
			}
			
			ResultSet rsAmount1=stmt.executeQuery(query3);
			
			while(rsAmount1.next()){
				System.out.println("Debuging amount");
				int tmp=rsAmount1.getInt("amount");
				System.out.println("amount : "+tmp);
				t.amount1.add(tmp);
			}
			
			
			ResultSet rs1=stmt.executeQuery(query2);
			while(rs1.next()){
				
				String tmp=rs1.getString("debited_acc");
				
				t.result2.add(tmp);
				}
			
			ResultSet rsAmount2=stmt.executeQuery(query4);
			
			while(rsAmount2.next()){
				
				int tmp=rsAmount2.getInt("amount");
				t.amount2.add(tmp);
			}
			
						
		}catch(Exception e){
			e.printStackTrace();
		}
		
	
		
		return t;
		
	}
	
	public static int createAnotherAccount(int cid){
		 String q ="INSERT INTO account(balance,cust_id) values(?,?)";
		 int status=0;
		 try{
			 Connection con=CustomerDao.getConnection();
			 Statement stmt=con.createStatement();
			 PreparedStatement s1 = con.prepareStatement(q);
		        s1.setInt(1,1000);
		        s1.setInt(2,cid);
		         status=s1.executeUpdate();
			 
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 
		 return status;
	        
	}
	
	
}