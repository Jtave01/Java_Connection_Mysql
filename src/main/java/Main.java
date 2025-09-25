import br.com.dio.java.percistence.ConnectionUtil;
import br.com.dio.java.percistence.EmployeeDAO;
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
    public static EmployeeDAO employeeDAO = new EmployeeDAO();


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
        /*
          employeeDAO.delete(4);
         */

        ///Update
        /*
        var employee = new EmployeeEntity();
        employee.setId(3);
        employee.setName("Gabriel");
        employee.setSalary(new BigDecimal(7000));
        employee.setBirthday(OffsetDateTime.now().minusYears(40));
        employeeDAO.update(employee);*
         */

        /// Escolhendo employees por id
        /*
        System.out.println(employeeDAO.findById(1));
        */

        /// Consulta todos employees
        /*
        employeeDAO.findAll().forEach(System.out::println);
        */

        /// Inseriondo employees
        /*
        var employee = new EmployeeEntity();
        employee.setName("Pedro");
        employee.setSalary(new BigDecimal("1000"));
        employee.setBirthday(OffsetDateTime.now().minusYears(19));
        employeeDAO.insert(employee);
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
