package com.example.ObjectCounter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Locale;

@SpringBootTest
class ObjectCountApplicationTests {

	@Test
	void contextLoads() {
		Locale a=Locale.forLanguageTag("fr_CA");
		System.out.println(Locale.UK.getCountry());
		System.out.println(Locale.forLanguageTag("fr-CA").getCountry());
		System.out.println(Locale.UK.getCountry());
	}
}
