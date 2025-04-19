/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.engilyin.drools.providers;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.KieModuleKieProject;
import org.drools.model.codegen.ExecutableModelProject;
import org.drools.model.codegen.execmodel.CanonicalModelKieProject;
import org.drools.ruleunits.api.RuleUnit;
import org.drools.ruleunits.api.RuleUnitData;
import org.drools.ruleunits.api.RuleUnitProvider;
import org.drools.ruleunits.api.conf.RuleConfig;
import org.drools.ruleunits.impl.InternalRuleUnit;
import org.drools.ruleunits.impl.NamedRuleUnitData;
import org.drools.ruleunits.impl.RuleUnitGenerationException;
import org.drools.ruleunits.impl.conf.RuleConfigImpl;
import org.kie.api.KieServices;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.io.KieResources;
import org.kie.api.io.Resource;

import com.engilyin.drools.utils.StaticFileScanner;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpringBootRuleUnitProvider implements RuleUnitProvider {

	private static final String FILE_URL_PREFIX = "file:/";
	private final Map<String, RuleUnit> ruleUnitMap = new HashMap<>();

	@Override
	public int servicePriority() {
		return 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends RuleUnitData> RuleUnit<T> getRuleUnit(T ruleUnitData) {
		String ruleUnitName = getRuleUnitName(ruleUnitData);
		RuleUnit<T> ruleUnit = ruleUnitMap.get(ruleUnitName);
		if (ruleUnit != null) {
			return ruleUnit;
		}
		ruleUnitMap.putAll(generateRuleUnit(ruleUnitData));
		return ruleUnitMap.get(ruleUnitName);
	}

	protected <T extends RuleUnitData> Map<String, RuleUnit> generateRuleUnit(T ruleUnitData) {
		InternalKieModule kieModule = createRuleUnitKieModule(ruleUnitData.getClass());
		KieModuleKieProject kieModuleKieProject = createRuleUnitKieProject(kieModule);
		return loadRuleUnits(kieModuleKieProject.getClassLoader());
	}

	private Map<String, RuleUnit> loadRuleUnits(ClassLoader classLoader) {
		Map<String, RuleUnit> map = new HashMap<>();
		ServiceLoader<RuleUnit> loader = ServiceLoader.load(RuleUnit.class, classLoader);
		for (RuleUnit impl : loader) {
			map.put(getRuleUnitName(((InternalRuleUnit) impl).getRuleUnitDataClass()), impl);
		}
		return map;
	}

	protected String getRuleUnitName(RuleUnitData ruleUnitData) {
		if (ruleUnitData instanceof NamedRuleUnitData) {
			return ((NamedRuleUnitData) ruleUnitData).getUnitName();
		}
		return getRuleUnitName(ruleUnitData.getClass());
	}

	protected String getRuleUnitName(Class<? extends RuleUnitData> ruleUnitDataClass) {
		return ruleUnitDataClass.getCanonicalName();
	}

	static InternalKieModule createRuleUnitKieModule(Class<?> unitClass) {
		KieServices ks = KieServices.get();
		KieFileSystem kfs = ks.newKieFileSystem();
		for (Resource drlResource : ruleResourcesForUnitClass(ks, unitClass)) {
			kfs.write(drlResource);
		}
		return (InternalKieModule) ks.newKieBuilder(kfs).getKieModule(ExecutableModelProject.class);
	}

	static KieModuleKieProject createRuleUnitKieProject(InternalKieModule kieModule) {
		return new CanonicalModelKieProject(kieModule, kieModule.getModuleClassLoader());
	}

	private static Collection<Resource> ruleResourcesForUnitClass(KieServices ks, Class<?> unitClass) {
		KieResources kieResources = ks.getResources();
		try {
			return StaticFileScanner.scan().map(r -> toDroolsResource(kieResources, r)).toList();
		} catch (IOException e) {
			throw new RuleUnitGenerationException("Exception while creating KieModule", e);
		}
	}

	@SneakyThrows
	private static Resource toDroolsResource(KieResources kieResources, org.springframework.core.io.Resource resource) {
		String url = resource.getURL().toString();
		if (url.startsWith(FILE_URL_PREFIX)) {
			return kieResources.newFileSystemResource(url.substring(FILE_URL_PREFIX.length()));
		} else {
			return kieResources.newClassPathResource(url);
		}
	}

	@Override
	public RuleConfig newRuleConfig() {
		return new RuleConfigImpl();
	}

	@Override
	public <T extends RuleUnitData> int invalidateRuleUnits(Class<T> ruleUnitDataClass) {
		if (NamedRuleUnitData.class.isAssignableFrom(ruleUnitDataClass)) {
			// NamedRuleUnitData may create multiple RuleUnits
			List<String> invalidateKeys = ruleUnitMap
					.entrySet()
						.stream()
						.filter(entry -> hasSameRuleUnitDataClass(entry.getValue(), ruleUnitDataClass))
						.map(Map.Entry::getKey)
						.collect(Collectors.toList());
			invalidateKeys.forEach(ruleUnitMap::remove);
			return invalidateKeys.size();
		} else {
			String ruleUnitName = getRuleUnitName(ruleUnitDataClass);
			RuleUnit remove = ruleUnitMap.remove(ruleUnitName);
			return remove == null ? 0 : 1;
		}
	}

	private static <T extends RuleUnitData> boolean hasSameRuleUnitDataClass(RuleUnit ruleUnit,
			Class<T> ruleUnitDataClass) {
		if (ruleUnit instanceof InternalRuleUnit) {
			return ((InternalRuleUnit) ruleUnit).getRuleUnitDataClass().equals(ruleUnitDataClass);
		} else {
			return false;
		}
	}
}
