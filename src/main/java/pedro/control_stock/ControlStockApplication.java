package pedro.control_stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import pedro.control_stock.vista.ProductoForm;

import java.awt.*;

@SpringBootApplication
public class ControlStockApplication {

	public static void main(String[] args) {
		// SpringApplication.run(ControlStockApplication.class, args);
		ConfigurableApplicationContext contextoSpring =
				new SpringApplicationBuilder(ControlStockApplication.class)
						.headless(false)
						.web(WebApplicationType.NONE)
						.run(args);

		// Cargar el formulario
		EventQueue.invokeLater(()->{
			// Obtener form a traves de spring
			ProductoForm productoForm = contextoSpring.getBean(ProductoForm.class);
			productoForm.setVisible(true);
		});
	}

}
