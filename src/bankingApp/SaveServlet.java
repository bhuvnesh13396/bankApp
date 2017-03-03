package bankingApp;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SaveServlet
 */

public class SaveServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SaveServlet() {
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
	//	doGet(request, response);
		response.setContentType("text/html");  
        PrintWriter out=response.getWriter();
        
        String name=request.getParameter("userName");  
        String password=request.getParameter("userPass");
        
        Customer c=new Customer();
        c.setName(name);
        c.setPassword(password);
        c.setBalance(1000);
        int status=CustomerDao.saveCustomerDetails(c);
        
        if(status>0){
        	  out.print("<p>Record saved successfully!</p>");  
              request.getRequestDispatcher("index.html").include(request, response); 
              
        }else{
        	 out.println("Sorry! unable to save record");  
        }
        
        out.close();
        
        
		
	}

}
