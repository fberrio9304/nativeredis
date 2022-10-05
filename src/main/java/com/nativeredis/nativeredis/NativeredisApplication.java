package com.nativeredis.nativeredis;

import com.nativeredis.nativeredis.service.FileDetailService;
import com.nativeredis.nativeredis.service.impl.FileDetailServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.nativex.hint.TypeHint;

@TypeHint(types = FileDetailService.class, typeNames = "com.example.webclient.FileDetailService$FileDetailServiceImpl")
@SpringBootApplication
@EnableCaching
public class NativeredisApplication {

	public static void main(String[] args) {
		SpringApplication.run(NativeredisApplication.class, args);
	}

}
