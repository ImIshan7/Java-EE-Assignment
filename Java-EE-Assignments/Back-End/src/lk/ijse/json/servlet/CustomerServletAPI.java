package lk.ijse.json.servlet;

import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;


import static java.lang.Class.forName;

//http://localhost:8080/pos_one/customer
//http://localhost:8080/pos_one/pages/customer? 404
//http://localhost:8080/customer? 404

//http://localhost:8080/pos_one/pages/customer//
//http:://localhost:8080/pos_one/pages/customer
//http:://localhost:8080/pos_one/pages/customer
@WebServlet(urlPatterns = "/pages/customer")
public class CustomerServletAPI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            /*<!--when the response received catch it and set it to the table-->*/

            forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");

            String option = req.getParameter("option");

            switch (option) {
                case "GetAll":
                    PreparedStatement pstm = connection.prepareStatement("select * from customer");
                    ResultSet rst = pstm.executeQuery();
                    resp.addHeader("Access-Control-Allow-Origin", "*");


                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                    while (rst.next()) {
                        String id = rst.getString(1);
                        String name = rst.getString(2);
                        String address = rst.getString(3);
                        String contact = rst.getString(4);

                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("id", id);
                        objectBuilder.add("name", name);
                        objectBuilder.add("address", address);
                        objectBuilder.add("contact", contact);
                        arrayBuilder.add(objectBuilder.build());
                    }
                    resp.setContentType("application/json");
                    resp.getWriter().print(arrayBuilder.build());
                    break;

                case "GetIds":
                    PreparedStatement pstm2 = connection.prepareStatement("SELECT id FROM customer ORDER BY id DESC LIMIT 1;");
                    ResultSet rst2 = pstm2.executeQuery();
                    resp.addHeader("Access-Control-Allow-Origin", "*");

                    //System.out.println("GetIds"+pstm2);

                    JsonArrayBuilder arrayBuilder2 = Json.createArrayBuilder();
                    while (rst2.next()) {
                        String id = rst2.getString("id");
                        int newCustomerId=Integer.parseInt(id.replace("C0-",""))+1;
                        arrayBuilder2.add(newCustomerId);
                    }
                    resp.setContentType("application/json");
                    resp.getWriter().print(arrayBuilder2.build());

                    break;

                case "search":
                    PreparedStatement pstm3 = connection.prepareStatement("select * from customer where id=?");
                    pstm3.setObject(1, req.getParameter("cusID"));
                    ResultSet rst3 = pstm3.executeQuery();
                    resp.addHeader("Access-Control-Allow-Origin", "*");

                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    if (rst3.next()) {
                        String id = rst3.getString(1);
                        String name = rst3.getString(2);
                        String address = rst3.getString(3);
                        String contact = rst3.getString(4);

                        objectBuilder.add("id", id);
                        objectBuilder.add("name", name);
                        objectBuilder.add("address", address);
                        objectBuilder.add("contact", contact);
                    }
                    resp.setContentType("application/json");
                    resp.getWriter().print(objectBuilder.build());
                    break;

            }


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cusID = req.getParameter("cusID");
        String cusName = req.getParameter("cusName");
        String cusAddress = req.getParameter("cusAddress");
        String cusSalary = req.getParameter("contact");
        String option = req.getParameter("option");



        try {
            forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");

          /*  switch (option) {
                case "add":*/
            PreparedStatement pstm = connection.prepareStatement("Insert into customer values(?,?,?,?)");
            resp.addHeader("Access-Control-Allow-Origin", "*");

            pstm.setObject(1, cusID);
            pstm.setObject(2, cusName);
            pstm.setObject(3, cusAddress);
            pstm.setObject(4, cusSalary);

            if (pstm.executeUpdate() > 0) {
                /* resp.getWriter().println("Customer Added..!");*/
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", "success");
                objectBuilder.add("message", "Customer Added..!");
                resp.setContentType("application/json");
                resp.getWriter().print(objectBuilder.build());


            } else {
                resp.getWriter().println("Customer Not Added..!");
                String jsonResponse = "{\"status\": \"fail\", \"message\": \"Customer Not Added..!\"}";
                resp.getWriter().println(jsonResponse);
            }



        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", "success");
            objectBuilder.add("message", e.getMessage());
            objectBuilder.add("data", e.getErrorCode());
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print(objectBuilder.build());

        }
    }



    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Access-Control-Allow-Origin", "*");

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();

        String cusID = jsonObject.getString("cusID");
        String cusName = jsonObject.getString("cusName");
        String cusAddress = jsonObject.getString("cusAddress");
        String contact = jsonObject.getString("contact");
        System.out.println(cusID + " " + cusName + " " + cusAddress + " " + contact);

        try {
            forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");


            PreparedStatement pstm3 = connection.prepareStatement("update customer set name=?, address=?, contact=? where id=?");

            pstm3.setObject(1, cusName);
            pstm3.setObject(2, cusAddress);
            pstm3.setObject(3, contact);
            pstm3.setObject(4, cusID);
            System.out.println("sql" + pstm3);
            if (pstm3.executeUpdate() > 0) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", "success");
                objectBuilder.add("message", "Customer Updated..!");
                resp.setContentType("application/json");
                resp.getWriter().print(objectBuilder.build());

            } else {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", "fail");
                objectBuilder.add("message", "Customer Not Updated..!");
                resp.setContentType("application/json");
                //resp.addHeader("Content-type", "application/json");
                resp.setStatus(400);
                resp.getWriter().print(objectBuilder.build());
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", "success");
            objectBuilder.add("message", e.getMessage());
            objectBuilder.add("data", e.getErrorCode());
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print(objectBuilder.build());


        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String cusID = req.getParameter("cusID");
        String option = req.getParameter("option");

        try {
            forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");


            PreparedStatement pstm2 = connection.prepareStatement("delete from customer where id=?");
            resp.addHeader("Access-Control-Allow-Origin", "*");
            pstm2.setObject(1, cusID);

            System.out.println("Sql" + pstm2);
            if (pstm2.executeUpdate() > 0) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", "success");
                objectBuilder.add("message", "Customer Deleted..!");
                resp.setContentType("application/json");
                resp.getWriter().print(objectBuilder.build());
            } else {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", "fail");
                objectBuilder.add("message", "Customer Not Deleted..!");
                resp.setContentType("application/json");
                resp.setStatus(400);
                resp.getWriter().print(objectBuilder.build());
            }

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", "success");
            objectBuilder.add("message", e.getMessage());
            objectBuilder.add("data", e.getErrorCode());
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print(objectBuilder.build());

        }

    }


    @Override
    protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Headers", "Content-Type");
        resp.addHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE");
    }
}
