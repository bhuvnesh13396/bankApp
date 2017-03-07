package bankingApp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class listTransaction
 */
//@WebServlet("/listTransaction")
public class listTransaction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public listTransaction() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		
		HttpSession session=request.getSession();
		Customer cust=(Customer)session.getAttribute("c");
		
		
		String acc=request.getParameter("debited_acc");
		int debited_acc=Integer.parseInt(acc);
		Transaction t=CustomerDao.ListTransaction(debited_acc,cust.getId(),cust.getName());
		if(t==null){
			
			out.println("Please enter your own account number..!!");
			
		}else{
			out.println(acc+" credited to following persons");
			for(int i=0;i<t.result1.size();i++){
				out.print("<h5>"+t.result1.get(i)+"          "+t.amount1.get(i)+ "</h5>" );
			}
			out.println("<br/");
			out.println("<br/");
			out.println("<br/");
			
			
			out.println("<p>" + acc+" debited from following persons" +"</p>");
			
			for(int i=0;i<t.result2.size();i++){

				out.println("<h5>"+t.result2.get(i)+ "          "+t.amount2.get(i)+"</h5>");
			}
			
		}
		
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
		
		
	}

}
