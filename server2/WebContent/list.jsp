<%@ page import="java.io.File,java.util.ArrayList" %>
<p>You are logged in as   <span  style="color:red;font-size:24px"><% out.print(session.getAttribute("name")); %></span></p>
<a href=login.jsp >LOGOUT</a>
<br>
<br>
<%  
// retrieve your list from the request, with casting 
ArrayList<String> prices = (ArrayList<String>) request.getAttribute("prices");
ArrayList<String> adrs = (ArrayList<String>) request.getAttribute("adrs");
ArrayList<File> imgs = (ArrayList<File>) request.getAttribute("imgs");%>
<a style="color:green;font-size:26px" href="home.jsp">Add new house </a>
<%for(int i = 0 ; i < prices.size();i++) { %>
   <p>Pret: <% out.println(prices.get(i));%> $ <p>
    <p>Adresa: <% out.println(adrs.get(i));%><p>
   <p>Imagine<p><img  height="480" width="680" src = <%out.println("images/"+imgs.get(i).getName()); %>>
   <br>
    <br>
<% }%>
