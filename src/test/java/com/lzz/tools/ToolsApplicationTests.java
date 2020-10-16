package com.lzz.tools;

import com.lzz.tools.helper.FileHelper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class ToolsApplicationTests {

	@Test
	void contextLoads() {
		try {
			FileHelper.copy("E:/测试复制.txt","E:/copy.txt");
			FileHelper.fileChannelCopy("E:/测试复制.txt","E:/copy2.txt");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
