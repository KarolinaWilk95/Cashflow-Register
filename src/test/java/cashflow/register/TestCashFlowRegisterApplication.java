package cashflow.register;

import org.springframework.boot.SpringApplication;

public class TestCashFlowRegisterApplication {

	public static void main(String[] args) {
		SpringApplication.from(CashFlowRegisterApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
