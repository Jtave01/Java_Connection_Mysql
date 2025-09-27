import br.com.dio.java.percistence.ConnectionUtil;
import br.com.dio.java.percistence.ContactDao;
import br.com.dio.java.percistence.EmployeeDAO;
import br.com.dio.java.percistence.EmployeeParamDAO;
import br.com.dio.java.percistence.entity.ContactEntity;
import br.com.dio.java.percistence.entity.EmployeeEntity;
import org.flywaydb.core.Flyway;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Properties;
import java.util.stream.Stream;
import net.datafaker.Faker;

import static java.time.ZoneOffset.UTC;

public class Main {
    /* --> Vunerable from sqlIjection
    private final static EmployeeDAO employeeDAO = new EmployeeDAO();
     */

    private final static EmployeeParamDAO employeeDAO = new EmployeeParamDAO();
    private final static ContactDao contactDao = new ContactDao();
    private final static Faker faker = new Faker();

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

        System.out.println(employeeDAO.findById(2258));



        ///Inserindo contacts
        /*var employee = new EmployeeEntity();
        employee.setName("Joao");
        employee.setSalary(new BigDecimal("750070"));
        employee.setBirthday(OffsetDateTime.now().minusYears(50));
        employeeDAO.insert(employee);
        System.out.println(employee);

        var contact1 = new ContactEntity();
        contact1.setDescription("email");
        contact1.setType("Luiz@gmail.com");
        contact1.setEmployee(employee);
        contactDao.insert(contact1);

        var contact2 = new ContactEntity();
        contact2.setDescription("email");
        contact2.setType("35524569");
        contact2.setEmployee(employee);
        contactDao.insert(contact2);*/


        /*
       ///Iserindo cadeira de usuarios
       List<EmployeeEntity> entties =  Stream.generate(() ->
                {var employee = new EmployeeEntity();
                    employee.setName(faker.name().fullName());
                    employee.setSalary(new BigDecimal(faker.number().digits(4)));
                    employee.setBirthday(faker.date().birthdayLocalDate(20,20).atStartOfDay().atOffset(UTC));
                    return employee;

                }).limit(400).toList();
        employeeDAO.insertBatch(entties);

       */

        /// Escolhendo employees por id
        /*
        System.out.println(employeeDAO.findById(1));
        */

        /// Consulta todos employees
        /*
        employeeDAO.findAll().forEach(System.out::println);
        */

        /// Inserindo employees


        /*
        var employee = new EmployeeEntity();
        employee.setName("Luiz");
        employee.setSalary(new BigDecimal("750070"));
        employee.setBirthday(OffsetDateTime.now().minusYears(50));
        employeeDAO.employee(employee);
         */



        ///Update
        /*
        var update = new EmployeeEntity();
        update.setId(employee.getId());
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
