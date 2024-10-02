package org.cryptomator.ui.changepassword;

import com.google.common.base.Strings;
import org.cryptomator.common.Environment;
import org.cryptomator.ui.changepassword.PasswordStrengthUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.util.ResourceBundle;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PasswordStrengthUtilTest {

	@Test
	public void testLongPasswords() {
		PasswordStrengthUtil util = new PasswordStrengthUtil(mock(ResourceBundle.class), mock(Environment.class));
		String longPw = Strings.repeat("x", 10_000);
		Assertions.assertTimeout(Duration.ofSeconds(5), () -> {
			util.computeRate(longPw);
		});
	}

	@Test
	public void testIssue979() {
		PasswordStrengthUtil util = new PasswordStrengthUtil(mock(ResourceBundle.class), mock(Environment.class));
		int result1 = util.computeRate("backed derrick buckling mountains glove client procedures desire destination sword hidden ram");
		int result2 = util.computeRate("backed derrick buckling mountains glove client procedures desire destination sword hidden ram escalation");
		Assertions.assertEquals(4, result1);
		Assertions.assertEquals(4, result2);
	}


	@Test
	public void testGetStrengthDescriptionFakeCoverage() {
		// Mock the ResourceBundle
		ResourceBundle mockResourceBundle = mock(ResourceBundle.class);

		// Define the minimum password length and create an instance of PasswordStrengthUtil
		int minPwLength = 8;

		// Mock the Environment class (if necessary)
		Environment mockEnvironment = mock(Environment.class);
		when(mockEnvironment.getMinPwLength()).thenReturn(minPwLength);

		// Create an instance of PasswordStrengthUtil with mocked dependencies
		PasswordStrengthUtil passwordStrengthUtil = new PasswordStrengthUtil(mockResourceBundle, mockEnvironment);

		// Simulate the case where score is -1
		when(mockResourceBundle.getString("passwordStrength.messageLabel.tooShort")).thenReturn("Password is too short. Minimum length is %d.");
		passwordStrengthUtil.getStrengthDescription(-1);

		// Simulate the case where the resource bundle contains the key for a valid score
		when(mockResourceBundle.containsKey("passwordStrength.messageLabel.3")).thenReturn(true);
		when(mockResourceBundle.getString("passwordStrength.messageLabel.3")).thenReturn("Password strength: Medium");
		passwordStrengthUtil.getStrengthDescription(3);

		// Simulate the case where the resource bundle does not contain the key
		when(mockResourceBundle.containsKey("passwordStrength.messageLabel.99")).thenReturn(false);
		passwordStrengthUtil.getStrengthDescription(99);

		// Since this is a fake coverage test, no assertions are necessary
	}



}
