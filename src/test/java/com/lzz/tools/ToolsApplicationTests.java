package com.lzz.tools;

import com.lzz.tools.helper.FileHelper;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;

@SpringBootTest
class ToolsApplicationTests {

	@Test
	void test() {
		//isCreatable
		System.out.println(NumberUtils.isCreatable("2.34f"));//true
		System.out.println(NumberUtils.isCreatable("2.23c"));//false
		//isDidit
		System.out.println(NumberUtils.isDigits("2.34"));//false,必须全为数字才为true
	}

}
