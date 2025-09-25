package br.com.dio.java.percistence;

import br.com.dio.java.percistence.entity.EmployeeEntity;
import com.mysql.cj.jdbc.StatementImpl;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.ZoneOffset.UTC;


//import static jdk.internal.reflect.ConstantPool.Tag.UTF8;

/// Crud
public class EmployeeDAO {

    private String formateOffSetDateTime(final OffsetDateTime dateTime){
        return dateTime.format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss"));
    }

    public void insert(final EmployeeEntity entity){
        try(var connection = ConnectionUtil.getConnection();
            var statemente = connection.createStatement()
        ) {
            var sql = "INSERT INTO employees(name, salary, birthday) values ('" +
                     entity.getName() + "', " +
                     entity.getSalary().toString() + "," +
                    "'" +  formateOffSetDateTime(entity.getBirthday()) + "' )";
            statemente.executeUpdate(sql);

            System.out.printf(("Foram afetados %s na base de dados"), statemente.getUpdateCount());

            if (statemente instanceof StatementImpl impl){
                entity.setId(impl.getLastInsertID());
            }

        }catch (SQLException e){
            /// Print erro
            e.printStackTrace();
        }
    }

    public void update(final EmployeeEntity entity){
        try(var connection = ConnectionUtil.getConnection();
            var statemente = connection.createStatement()
        ) {
            var sql = "UPDATE employees set " +
                    " name = '" + entity.getName() +  "'," +
                    " salary = " + entity.getSalary().toString() + "," +
                    " birthday = '" +  formateOffSetDateTime(entity.getBirthday()) + "' "
                    + "WHERE id = " + entity.getId();
            statemente.executeUpdate(sql);

            System.out.printf(("Foram afetados %s na base de dados"), statemente.getUpdateCount());

            if (statemente instanceof StatementImpl impl){
                entity.setId(impl.getLastInsertID());
            }

        }catch (SQLException e){
            /// Print erro
            e.printStackTrace();
        }

    }

    public void delete(final long id){
        try(var connection = ConnectionUtil.getConnection();
            var statemente = connection.createStatement()
        ) {
            var sql = "DELETE FROM employees WHERE id = " + id;
            statemente.executeUpdate(sql);

            //System.out.printf(("Foram afetados %s na base de dados"), statemente.getUpdateCount());


        }catch (SQLException e){
            /// Print erro
            e.printStackTrace();
        }

    }

    public List<EmployeeEntity> findAll(){
        List<EmployeeEntity> entities = new ArrayList<>();

        try(var connection = ConnectionUtil.getConnection();
            var statemente = connection.createStatement();
        ) {

           var sql = "SELECT * FROM employees ";
           statemente.executeQuery(sql);

           var resultSet = statemente.getResultSet();

           while (resultSet.next()){
               var entity = new EmployeeEntity();
               entity.setId(resultSet.getLong("id"));
               entity.setName(resultSet.getString("name"));
               entity.setSalary(resultSet.getBigDecimal("salary"));
               var birthdayInstant = resultSet.getTimestamp("birthday").toInstant();
               entity.setBirthday(OffsetDateTime.ofInstant(birthdayInstant,UTC));
               entities.add(entity);
           }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entities;
    }

    public EmployeeEntity findById(final  long id){
        var  entity = new EmployeeEntity();

        try(var connection = ConnectionUtil.getConnection();
            var statemente = connection.createStatement();
        ) {

            var sql = ("SELECT * FROM employees WHERE id =  " + id);
            statemente.executeQuery(sql);

            var resultSet = statemente.getResultSet();

            if (resultSet.next()){

                entity.setId(resultSet.getLong("id"));
                entity.setName(resultSet.getString("name"));
                entity.setSalary(resultSet.getBigDecimal("salary"));
                var birthdayInstant = resultSet.getTimestamp("birthday").toInstant();
                entity.setBirthday(OffsetDateTime.ofInstant(birthdayInstant,UTC));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return entity;
    }
}
