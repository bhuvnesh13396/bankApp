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
import javax.websocket.Session;

/**
 * Servlet implementation class DoTransaction
 */
//@WebServlet("/DoTransaction")
public class DoTransaction extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DoTransaction() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		String debit=request.getParameter("debited_acc");
		String credit=request.getParameter("credited_acc");
		String amt=request.getParameter("amount");
		
		int debited_acc=Integer.parseInt(debit);
		int credited_acc=Integer.parseInt(credit);
		int amount=Integer.parseInt(amt);
		

		HttpSession session=request.getSession();
		Customer cust=(Customer)session.getAttribute("c");
		
		boolean check=	CustomerDao.TranserAmount(credited_acc ,debited_acc,amount,cust.getId());
		if(check==true){
			out.println("Transaction Succesfull..!!");
		}else{
			out.println("Sorry.. You do not have enough balance for this transaction to executed.Please enter lower amount..!!");
		}
		
		out.close();
	}

}
