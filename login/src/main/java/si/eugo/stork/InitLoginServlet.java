package si.eugo.stork;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InitLoginServlet extends HttpServlet {

	private static final Logger log = LoggerFactory.getLogger(InitLoginServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	
		
		String initialURI = (String) req.getAttribute("javax.servlet.forward.request_uri");
		log.info("Initial URI = " + initialURI);//String origRequestUrl = request.getAttribute("javax.servlet.forward.request_uri")
		HttpSession session = req.getSession(true);
		
		session.setAttribute("initialURI", initialURI);
		
		PrintWriter out = resp.getWriter();
		out.write("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
		out.write("<html>");
		out.write("<head>");
		out.write("<title>peps-redirect</title>");
		out.write("</head>");
		out.write("<body style=\"text-align: center; background: url('http://www.eugo.gov.si/typo3conf/ext/a3_ekt_template/Resources/Public/Images/404-bgr.png') repeat scroll 0 0 #F5F5F5; font-family: arial, tahoma, verdana\">"); //onload=\"document.redirectForm.submit();\"
		
		out.write("<header class=\"header vcard\" role=\"banner\">" + 
	"<div class=\"inner\" style=\" margin: 0 auto;    position: relative;    width: 960px;height: 170px;\">" +
		"<h1 id=\"brand\"  style=\"  background-image: url('http://eugo.gov.si/typo3conf/ext/a3_ekt_template/Resources/Public/Images/logo-beta.png');    float: left;    height: 100px;    margin-bottom: 10px;    overflow: hidden;    width: 410px;\">" +
			"<a target=\"_top\" href=\"http://www.eugo.gov.si/\"> </a>" + 
		"</h1>" + 
	"</div>" + 
"</header>");
		
		out.write("<div class=\"UILogin\"" +
		"style=\"color: #FFF; font-size: 14px; width: 960px; margin: auto; padding: 0;\">" +
		"<div class=\"LoginHeader\"></div>" +
		"		<div class=\"LoginContent\">" +
		"			<div class=\"CenterLoginContent\">" +
				
				"<form name=\"loginForm\" action=" + req.getContextPath() + "/strorkservlet" +
						"					method=\"post\" style=\"margin: 0px;\">");
		
					
		out.write("<div style=\"padding: 20px; border: #fff 1px solid\">" +
				"						<div style=\"padding: 20px;\">The system will guide you" +
				"							through several steps to verify your identity and execute the" +
				"							selected service.</div>" +
				"						<div class=\"UserNameField\">" +
				"							<div class=\"FieldLabel\">Select your country:</div>" +
				"							<div>" +
				"								<select name=\"citizen\" id=\"citizen\" style=\"width: 140px;\">" +
				"									<option></option>" +
				"									<option value=\"SI\"" +
				"										icon=\"/login/skin/imgages/banderas/SI-PEPS.gif\">Slovenija</option>" +
				"									<option value=\"LT\" icon=\"/login/skin/imgages/banderas/LT.gif\">Lietuva</option>" +
				"" +
				"								</select>" +
				"							</div>" +
				"						</div>" +
				"				<div><p></p></div>" +
				"						<div class=\"MiddleButton\"" +
				"												style=\"background: #ddd; height: 40px; line-height: 36px; margin: auto; width: 80px;\" onclick=\"login();\">" +
				"												<a href=\"#\">Sign in</a>" +
				"											</div>" +
				"					</div>" +
				"					<div class=\"ClearLeft\">" +
				"						<span></span>" +
				"					</div>" +
				"					<script type='text/javascript'>" +
				"						function login() {" +
				"							var e = document.getElementById(\"citizen\");" +
				"							var strUser = e.options[e.selectedIndex].value;" +
				"							if(strUser != ''){							" +
				"								document.loginForm.submit();" +
				"							}" +
				"						}" +
				"					</script>" +
				"				</form>" +
				"			</div>" +
				"		</div>" +
				"	</div>" +
				"</body>");
		out.write("</html>");

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req, resp);
	}
}
