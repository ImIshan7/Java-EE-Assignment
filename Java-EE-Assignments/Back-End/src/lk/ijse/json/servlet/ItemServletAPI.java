package lk.ijse.json.servlet;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

import static java.lang.Class.forName;

@WebServlet(urlPatterns = "/pages/item")
public class ItemServletAPI extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        try {

            resp.addHeader("Access-Control-Allow-Origin", "*");


            forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");
            PreparedStatement pstm = connection.prepareStatement("select * from item");
            ResultSet rst = pstm.executeQuery();


            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            while (rst.next()) {
                String code = rst.getString(1);
                String description = rst.getString(2);
                String qty = rst.getString(3);
                String unitPrice = rst.getString(4);

                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("code", code);
                objectBuilder.add("description", description);
                objectBuilder.add("qty", qty);
                objectBuilder.add("unitPrice", unitPrice);
                arrayBuilder.add(objectBuilder.build());
            }
            resp.setContentType("application/json");
            resp.getWriter().print(arrayBuilder.build());


        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.addHeader("Access-Control-Allow-Origin", "*");

        String code = req.getParameter("code");
        String itemName = req.getParameter("description");
        String qty = req.getParameter("qty");
        String unitPrice = req.getParameter("unitPrice");
        String option = req.getParameter("option");
//
        try {
            forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");

         /*   switch (option) {
                case "add":*/
                    PreparedStatement pstm = connection.prepareStatement("INSERT INTO item values(?,?,?,?)");
                    pstm.setObject(1, code);
                    pstm.setObject(2, itemName);
                    pstm.setObject(3, qty);
                    pstm.setObject(4, unitPrice);
                    if (pstm.executeUpdate() > 0) {
                        // resp.getWriter().println("Item Added..!");
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("message", "Item Added..!");
                        resp.setContentType("application/json");
                        resp.getWriter().print(objectBuilder.build());

                    } else {
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("message", "Item Not Added..!");
                        resp.setContentType("application/json");
                        resp.setStatus(400);
                        resp.getWriter().print(objectBuilder.build());
                    }
                    /*break;*/

          //  }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("error", e.getMessage());
            resp.setContentType("application/json");
            resp.setStatus(400);
            resp.getWriter().print(objectBuilder.build());

        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");

        String code = req.getParameter("code");
        String itemName = req.getParameter("description");
        String qty = req.getParameter("qty");
        String unitPrice = req.getParameter("unitPrice");


        try {
            forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");


            PreparedStatement pstm3 = connection.prepareStatement("update item set description=?, qty=?, unitPrice=? where code=?");

            pstm3.setObject(1, itemName);
            pstm3.setObject(2, qty);
            pstm3.setObject(3, unitPrice);
            pstm3.setObject(4, code);
            System.out.println("sql" + pstm3);
            if (pstm3.executeUpdate() > 0) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", "success");
                objectBuilder.add("message", "Item Updated..!");
                resp.setContentType("application/json");
                resp.getWriter().print(objectBuilder.build());

            } else {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", "fail");
                objectBuilder.add("message", "Item Not Updated..!");
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");

        String code = req.getParameter("code");


        try {
            forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/shop", "root", "1234");


            PreparedStatement pstm2 = connection.prepareStatement("delete from item where code=?");
            pstm2.setObject(1,code);

            System.out.println("Sql"+pstm2);
            if (pstm2.executeUpdate()>0){
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status","success");
                objectBuilder.add("message","Item Deleted..!");
                resp.setContentType("application/json");
                resp.getWriter().print(objectBuilder.build());
            }else {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status","fail");
                objectBuilder.add("message","Item Not Deleted..!");
                resp.setContentType("application/json");
                resp.setStatus(400);
                resp.getWriter().print(objectBuilder.build());
            }

        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        } catch (SQLException e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status","success");
            objectBuilder.add("message",e.getMessage());
            objectBuilder.add("data",e.getErrorCode());
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

