package br.com.dio.java.percistence;

import br.com.dio.java.percistence.entity.EmployeeEntity;
import com.mysql.cj.jdbc.StatementImpl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.ZoneOffset.UTC;



/// Crud
public class EmployeeParamDAO {

    private String formateOffSetDateTime(final OffsetDateTime dateTime){
        return dateTime.format(DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss"));
    }

    public void insert(final EmployeeEntity entity){
        try(var connection = ConnectionUtil.getConnection();
            var statemente = connection.prepareStatement("INSERT INTO employees(name, salary, birthday) values (?, ?, ?);"

            )
        ) {

            statemente.setString(1, entity.getName());
            statemente.setBigDecimal(2,entity.getSalary());
            statemente.setTimestamp(3, Timestamp.valueOf(entity.getBirthday().atZoneSimilarLocal(UTC).toLocalDateTime()));

            statemente.executeUpdate();


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
            var statemente = connection.prepareStatement("UPDATE employees set name = ?, slarry = ?, birthday = ?, WHERE id = ?")
        ) {


            statemente.setString(1, entity.getName());
            statemente.setBigDecimal(2,entity.getSalary());
            statemente.setTimestamp(3, Timestamp.valueOf(entity.getBirthday().atZoneSimilarLocal(UTC).toLocalDateTime()));


            statemente.executeUpdate();


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
            var statemente = connection.prepareStatement("DELETE FROM employees WHERE id = ?")
        ) {
            statemente.setLong(1, id);
            statemente.executeUpdate();


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
            var statemente = connection.prepareStatement("SELECT * FROM employees WHERE id = ?");
        ) {
            statemente.setLong(1, id);
            statemente.executeQuery();

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


    /// Criando operações em lotes

    public void insertBatch(final List<EmployeeEntity> entities){

        try(
            var connection = ConnectionUtil.getConnection();
            ){
            var sql = "INSERT INTO employees(name, salary, birthday) values (?, ?, ?);";
            try(var stateemente = connection.prepareStatement(sql)) {
                connection.setAutoCommit(false);
                for (int i = 0; i < entities.size(); i++) {



                    stateemente.setString(1, entities.get(i).getName());
                    stateemente.setBigDecimal(2, entities.get(i).getSalary());
                    stateemente.setDate(3, java.sql.Date.valueOf(entities.get(i).getBirthday().toLocalDate()));

                    stateemente.addBatch();

                    if(i % 1000 == 0 || i == entities.size() -1 ){
                        stateemente.executeBatch();
                    }
                }

                connection.commit();

            }catch (SQLException e){
                connection.rollback();
                e.printStackTrace();
            }

        }catch (SQLException e){
            e.printStackTrace();
        }


    }
}
