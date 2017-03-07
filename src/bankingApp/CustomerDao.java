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
	
	public static int getCustomerId(String name,String password){
		String query="select cust_id from cust_details where name='"+name+"' and password='"+password+"' ";
		int id=0;
		try{
			
			Connection con=CustomerDao.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(query);
			
			if(rs.next()){
				id=rs.getInt(1);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return id;
	}
	
	public static Pair saveCustomerDetails(Customer c){
		int status=0;
		int auto=0;
		try{
			
		
		Connection	con=CustomerDao.getConnection();
		String sql = "insert into cust_details(name,password,balance) values(?,?,?)";	
		PreparedStatement ps=con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		
		ps.setString(1, c.getName());
		ps.setString(2, c.getPassword());
		ps.setInt(3, c.getBalance());
		
		status=ps.executeUpdate();
		
		 auto=0;
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
		
		Pair p=new Pair();
		p.id=auto;
		p.status=status;
		
		return p;
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
		
		boolean validate_credited_acc=CustomerDao.ValidateAccount(credited_acc);
		boolean validate_debited_acc=CustomerDao.ValidateParticularUserAccount(c_id,debited_acc);
		
		if(validate_credited_acc==false ||  validate_debited_acc==false) 
			return false;
		
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

					
					String query1="update account set balance=balance-'"+amount+"' where cust_id ='"+c_id+"' and acc_no='"+debited_acc+"' ";
					
					String query2="update account set balance=balance+'"+amount+"' where acc_no='"+credited_acc+"' ";
					
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
	
	public static Transaction ListTransaction(int userAccNo,int cid,String name){
		
		boolean ret=CustomerDao.ValidateParticularUserAccount(cid,userAccNo);
		if(ret==false) 
			return null;
		
		System.out.println(ret);

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
	
	public static boolean ValidateAccount(int acc_no){
		// Validate debited_acc whether it belongs to that particular user or not.
		String query1="select acc_no from account where acc_no='"+acc_no+"'";
		boolean ret=false;
		try{
			Connection con=CustomerDao.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(query1);
			
			if(rs.next()){
				ret=true;
			}else{
				ret=false;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public static boolean ValidateParticularUserAccount(int c_id,int account_no){
		String query="select acc_no from account where cust_id='"+c_id+"' ";
		boolean ret=false;
		try{
			Connection con=CustomerDao.getConnection();
			Statement stmt=con.createStatement();
			ResultSet rs=stmt.executeQuery(query);
			
			while(rs.next()){
				
				int acc=rs.getInt(1);
				if(acc==account_no){
					ret=true;
					break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return ret;
		
	}
	
	
}