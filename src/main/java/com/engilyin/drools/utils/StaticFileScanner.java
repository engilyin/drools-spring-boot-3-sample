package com.engilyin.drools.utils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.stream.Streams;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class StaticFileScanner {

	private static final List<String> DRL_FILE_EXTENSIONS = List.of(".drl", ".drl.xls", ".drl.xlsx", ".drl.csv");

	private static final String RESOURCES_BASE_PATH = "classpath:/com/engilyin/drools/ruleunits/**";

	public static Stream<Resource> scan() throws IOException {

		var resolver = new PathMatchingResourcePatternResolver();

		Resource[] resources = resolver.getResources(RESOURCES_BASE_PATH);

		return Streams.of(resources).filter(StaticFileScanner::matchDrlExtension);
	}

	private static boolean matchDrlExtension(Resource resource) {
		String filename = resource.getFilename();
		return DRL_FILE_EXTENSIONS.stream().anyMatch(filename::endsWith);
	}
}
