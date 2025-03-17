package cashflow.register;

import cashflow.CashFlowRegisterApplication;
import org.springframework.boot.SpringApplication;

public class TestCashFlowRegisterApplication {

	public static void main(String[] args) {
		SpringApplication.from(CashFlowRegisterApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
