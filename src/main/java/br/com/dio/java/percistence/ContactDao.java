package br.com.dio.java.percistence;

import br.com.dio.java.percistence.entity.ContactEntity;
import br.com.dio.java.percistence.entity.EmployeeEntity;
import com.mysql.cj.jdbc.StatementImpl;

import java.sql.SQLException;
import java.sql.Timestamp;

import static java.time.ZoneOffset.UTC;

public class ContactDao {
    public void insert(final ContactEntity entity){
        try(var connection = ConnectionUtil.getConnection();
            var statemente = connection.prepareStatement("INSERT INTO contacts(description, type, employee_id) values (?, ?, ?);"

            )
        ) {

            statemente.setString(1, entity.getDescription());
            statemente.setString(2, entity.getType());
            statemente.setLong(3,entity.getEmployee().getId());

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
}
