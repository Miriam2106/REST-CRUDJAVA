package mx.edu.utez.controller;

import mx.edu.utez.model.Employee;
import mx.edu.utez.util.ConnectionMySQL;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Path("/employee")
public class Service {
    Connection con;
    PreparedStatement pstm;
    Statement statement;
    ResultSet rs;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Employee> getEmployees(){ //CONSULTAR TODOS
        List<Employee> employees = new ArrayList<>();
        try{
            con = ConnectionMySQL.getConnection();
            String query = "SELECT employees.employeeNumber, employees.firstName, employees.lastName, employees.extension, " +
                    "employees.email, employees.officeCode, employees.reportsTo, employees.jobTitle FROM employees";
            statement = con.createStatement();
            rs = statement.executeQuery(query);
            while (rs.next()){
                Employee employee = new Employee();
                employee.setEmployeeNumber(rs.getInt("employeeNumber"));
                employee.setFirstName(rs.getString("firstName"));
                employee.setLastName(rs.getString("lastName"));
                employee.setExtension(rs.getString("extension"));
                employee.setEmail(rs.getString("email"));
                employee.setOfficeCode(rs.getInt("officeCode"));
                employee.setReportsTo(rs.getInt("reportsTo"));
                employee.setJobTitle(rs.getString("jobTitle"));
                employees.add(employee);
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection();
        }
        return employees;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Employee getEmployee(@PathParam("id") int employeeNumber){ //CONSULTA POR ID
        Employee employee = new Employee();
        try{
            con = ConnectionMySQL.getConnection();
            String query = "SELECT employees.employeeNumber, employees.firstName, employees.lastName, employees.extension, employees.email, employees.officeCode, employees.reportsTo, employees.jobTitle FROM employees WHERE employees.employeeNumber=?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1,employeeNumber);
            rs = pstm.executeQuery();
            if (rs.next()){
                employee.setEmployeeNumber(rs.getInt("employeeNumber"));
                employee.setFirstName(rs.getString("firstName"));
                employee.setLastName(rs.getString("lastName"));
                employee.setExtension(rs.getString("extension"));
                employee.setEmail(rs.getString("email"));
                employee.setOfficeCode(rs.getInt("officeCode"));
                employee.setReportsTo(rs.getInt("reportsTo"));
                employee.setJobTitle(rs.getString("jobTitle"));
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            closeConnection();
        }
        return employee;
    }

    @POST
    @Path("/{id}/{name}/{lastname}/{extension}/{email}/{code}/{report}/{job}")
    @Produces(MediaType.APPLICATION_JSON)
    //INSERTAR
    public String insertEmployee(@PathParam("id") int employeeNumber, @PathParam("name") String firstName, @PathParam("lastname") String lastName, @PathParam("extension") String extension, @PathParam("email") String email, @PathParam("code") int code, @PathParam("report") int report, @PathParam("job") String job){
        boolean state = false;
        String res;
        try{
            con = ConnectionMySQL.getConnection();
            String query = "insert into employees(employees.employeeNumber, employees.firstName, employees.lastName, employees.extension, employees.email, employees.officeCode, employees.reportsTo, employees.jobTitle) \n" +
                    "values(?, ?, ?, ?, ?, ?, ?, ?);";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, employeeNumber);
            pstm.setString(2, firstName);
            pstm.setString(3, lastName);
            pstm.setString(4, extension);
            pstm.setString(5, email);
            pstm.setInt(6, code);
            pstm.setInt(7, report);
            pstm.setString(8, job);
            state = pstm.executeUpdate() ==1;
        }catch(SQLException ex){
            ex.printStackTrace();
        }finally{
            closeConnection();
        }
        if(state){
            res = "Registro exitoso";
        }else{
            res = "No se pudo registrar";
        }
        return res;
    }

    @PUT
    @Path("/{id}/{name}/{lastname}/{extension}/{email}/{code}/{report}/{job}")
    @Produces(MediaType.APPLICATION_JSON)
    //ACTUALIZAR
    public String updateEmployee(@PathParam("id") int employeeNumber, @PathParam("name") String firstName, @PathParam("lastname") String lastName, @PathParam("extension") String extension, @PathParam("email") String email, @PathParam("code") int code, @PathParam("report") int report, @PathParam("job") String job){
        boolean state = false;
        String res;
        try{
            con = ConnectionMySQL.getConnection();
            String query = "update employees set employees.firstName = ?, employees.lastName = ?, employees.extension = ?, employees.email = ?, employees.officeCode = ?, employees.reportsTo = ?, employees.jobTitle = ? WHERE employees.employeeNumber = ?;";
            pstm = con.prepareStatement(query);
            pstm.setString(1, firstName);
            pstm.setString(2, lastName);
            pstm.setString(3, extension);
            pstm.setString(4, email);
            pstm.setInt(5, code);
            pstm.setInt(6, report);
            pstm.setString(7, job);
            pstm.setInt(8, employeeNumber);
            state = pstm.executeUpdate() ==1;
        }catch(SQLException ex){
            ex.printStackTrace();
        }finally{
            closeConnection();
        }
        if(state){
            res = "Empleado actualizado exitosamente";
        }else{
            res = "No se pudo actualizar el empleado";
        }
        return res;
    }


    @DELETE
    @Path("/{idD}")
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteEmployee(@PathParam("idD") int idD){ //ELIMINAR
        boolean state = false;
        String res;
        try{
            con = ConnectionMySQL.getConnection();
            String queryP = "delete from employees where employeeNumber = ?;";
            pstm = con.prepareStatement(queryP);
            pstm.setInt(1, idD);
            state = pstm.executeUpdate() == 1;
        }catch(SQLException ex){
            ex.printStackTrace();
        }finally{
            closeConnection();
        }
        if(state){
            res = "Empleado eliminado";
        }else{
            res = "No se pudo eliminar al empleado";
        }
        return res;
    }

    public void closeConnection(){
        try{
            if(con != null){
                con.close();
            }
            if(pstm != null){
                pstm.close();
            }
            if(rs != null){
                rs.close();
            }
            if(statement != null){
                statement.close();
            }
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
}
