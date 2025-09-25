import br.com.dio.java.percistence.ConnectionUtil;
import br.com.dio.java.percistence.EmployeeDAO;
import br.com.dio.java.percistence.EmployeeParamDAO;
import br.com.dio.java.percistence.entity.EmployeeEntity;
import org.flywaydb.core.Flyway;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Properties;

public class Main {
    /* --> Vunerable from sqlIjection
    public static EmployeeDAO employeeDAO = new EmployeeDAO();

     */
    public static EmployeeParamDAO employeeDAO = new EmployeeParamDAO();


    public static void main(String[] args) throws IOException {


        Properties props = new Properties();
        /// Flyway recebe imput da application.properties
        try(InputStream input = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
            props.load(input);
            if (input == null) {
                throw new FileNotFoundException("application.properties não encontrado");
            }
            /// Migração do flyway
            var flyway = Flyway.configure().dataSource(props.getProperty("db.url"),
                            props.getProperty("db.user"),
                            props.getProperty("db.password")).load();
            flyway.migrate();


            System.out.println("Migração bem feita");

        }catch (Exception e){
            /// Mostra o erro
            e.printStackTrace();
        }

        /// Delete



        /// Escolhendo employees por id
        /*
        System.out.println(employeeDAO.findById(1));
        */

        /// Consulta todos employees
        /*
        employeeDAO.findAll().forEach(System.out::println);
        */

        /// Inserindo employees

        var insert = new EmployeeEntity();
        insert.setName("Marco Aurelio");
        insert.setSalary(new BigDecimal("50000"));
        insert.setBirthday(OffsetDateTime.now().minusYears(40));
        employeeDAO.insert(insert);


        ///Update
        /*
        var update = new EmployeeEntity();
        update.setId(insert.getId());
        update.setName("Gabriel");
        update.setSalary(new BigDecimal(7000));
        update.setBirthday(OffsetDateTime.now().minusYears(40));
        employeeDAO.update(update);
        */

        ///Delete
        /*
        employeeDAO.delete();
        */


        /// Test connection
            /*
        try (var connection = ConnectionUtil.getConnection()){
            System.out.println("Conected");
        }catch (SQLException e){
            e.printStackTrace();
        }
             */
    }

}
