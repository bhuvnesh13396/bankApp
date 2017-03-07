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
 * Servlet implementation class signIn
 */
//@WebServlet("/signIn")
public class signIn extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public signIn() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	//	response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		
		response.setContentType("text/html");
		PrintWriter out=response.getWriter();
		

		String name=request.getParameter("name");
		String password = request.getParameter("password");
		
		Customer c=new Customer();
		c.setName(name);
		c.setPassword(password);
		
		
		
		//int status=CustomerDao.userSignIn(c);
		int id=CustomerDao.getCustomerId(name, password);
		c.setId(id);
		
		Customer status=CustomerDao.userSign(c);
		if(status != null){
			out.print("You are succesfully logged in !");
			out.print("<br> Welcome , " + name);
			
			HttpSession session=request.getSession();
			session.setAttribute("c", c);
			int cid=c.getId();
			session.setAttribute("cid", cid);
			System.out.println(cid);
			request.getRequestDispatcher("profile.html").include(request, response);
			
		}else{
			out.print("Sorry username or password is wrong..Please try again !");
			request.getRequestDispatcher("login.html").include(request, response); 
		}
		
		out.close();
	}

}
