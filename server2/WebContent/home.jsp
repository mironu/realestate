
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<p>You are logged in as   <span  style="color:red;font-size:24px"><% out.print(session.getAttribute("name")); %></span></p>
<a href=login.jsp >LOGOUT</a>

<p style="font-size:24px;"><bold>Add new house</bold></p>
<form action="AddHouse" method="post" enctype="multipart/form-data">
        <h3>Price</h3>
        <input type="text" name="price" placeholder="Insert the price">
        <br>
        <br>
        <h3>Choose a picture of the house</h3>
        <input type="file" name="photo">
        <br>
        <h3>Insert the address of the house</h3>
        <input type="text" name="address">
        <br>
        <button type="submit">Submit</button>
        </form>
        <br>
        <br>
        <a style="color:green;font-size:26px" href="ListHouse">List all houses</a>
</body>
</html>
