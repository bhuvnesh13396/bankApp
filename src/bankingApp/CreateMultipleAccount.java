package bankingApp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CreateMultipleAccount
 */
//@WebServlet("/CreateMultipleAccount")
public class CreateMultipleAccount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateMultipleAccount() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		
		HttpSession session=request.getSession();
		//Customer cust=(Customer)session.getAttribute("c");
		//String tmp=(String)session.getAttribute("cid");
		int cid=(int) session.getAttribute("cid");
		//int cid=Integer.parseInt(tmp);
		
		out.println(cid);
		
		int status=CustomerDao.createAnotherAccount(cid);
		
		if(status==1){
			out.println("Congratualation..New Account Created");
		}else{
			out.println("Sorry Account can not be created.");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
