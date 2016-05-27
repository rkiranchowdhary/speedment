/**
 *
 * Copyright (c) 2006-2016, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.plugins.springgenerator.internal;

import com.speedment.common.codegen.Generator;
import static com.speedment.common.codegen.internal.model.constant.DefaultType.STRING;
import com.speedment.common.codegen.internal.model.value.TextValue;
import com.speedment.common.codegen.model.AnnotationUsage;
import com.speedment.common.codegen.model.Class;
import com.speedment.common.codegen.model.Field;
import com.speedment.common.codegen.model.File;
import com.speedment.common.codegen.model.Import;
import com.speedment.common.codegen.model.Method;
import com.speedment.common.codegen.model.Type;
import com.speedment.generator.TranslatorSupport;
import com.speedment.generator.internal.DefaultJavaClassTranslator;
import com.speedment.plugins.springgenerator.configuration.SpeedmentConfiguration;
import com.speedment.runtime.Speedment;
import com.speedment.runtime.config.Project;
import com.speedment.runtime.config.Table;
import com.speedment.runtime.config.trait.HasEnabled;
import static com.speedment.runtime.internal.util.document.DocumentDbUtil.traverseOver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 *
 * @author Emil Forslund
 * @since  1.0.0
 */
public final class GeneratedConfigurationTranslator 
extends DefaultJavaClassTranslator<Project, Class> {

    public GeneratedConfigurationTranslator(
            Speedment speedment, Generator generator, Project document) {
        
        super(speedment, generator, document, Class::of);
    }
    
    @Override
    protected String getClassOrInterfaceName() {
        return "Generated" + getSupport().typeName() + "Configuration";
    }
    
    @Override
    protected String getJavadocRepresentText() {
        return "The spring configuration file";
    }

    @Override
    public boolean isInGeneratedPackage() {
        return true;
    }

    @Override
    protected Class makeCodeGenModel(File file) {
        return newBuilder(file, getClassOrInterfaceName())
            .forEveryProject((clazz, project) -> {
                clazz.public_();
                clazz.setSupertype(Type.of(SpeedmentConfiguration.class));
                
                // Add constants
                clazz.add(Field.of("URL_PROPERTY", STRING)
                    .protected_().final_().static_()
                    .set(new TextValue("jdbc.url"))
                );
                
                clazz.add(Field.of("USERNAME_PROPERTY", STRING)
                    .protected_().final_().static_()
                    .set(new TextValue("jdbc.username"))
                );
                
                clazz.add(Field.of("PASSWORD_PROPERTY", STRING)
                    .protected_().final_().static_()
                    .set(new TextValue("jdbc.password"))
                );
                
                // Add environment variable
                clazz.add(Field.of("env", Type.of(Environment.class))
                    .protected_()
                    .add(AnnotationUsage.of(Type.of(Autowired.class)))
                );
                
                // Add application bean
                final Type appType        = Type.of(getSupport().basePackageName() + "." + getSupport().typeName() + "Application");
                final String appBuilder   = getSupport().typeName() + "ApplicationBuilder";
                final Type appBuilderType = Type.of(getSupport().basePackageName() + "." + appBuilder);
                
                file.add(Import.of(appBuilderType));
                
                clazz.add(Method.of("application", appType)
                    .public_()
                    .add(AnnotationUsage.of(Type.of(Bean.class)))
                    .add(
                        "final " + appBuilder + " builder =",
                        "    new " + appBuilder + "();",
                        "",
                        "if (env.containsProperty(URL_PROPERTY)) {",
                        "    builder.withConnectionUrl(env.getProperty(URL_PROPERTY));",
                        "}", 
                        "",
                        "if (env.containsProperty(USERNAME_PROPERTY)) {",
                        "    builder.withUsername(env.getProperty(USERNAME_PROPERTY));",
                        "}", 
                        "",
                        "if (env.containsProperty(PASSWORD_PROPERTY)) {",
                        "    builder.withPassword(env.getProperty(PASSWORD_PROPERTY));",
                        "}", 
                        "",
                        "return builder.build();"
                    )
                );
                
                // Add manager beans
                traverseOver(project, Table.class)
                        .filter(HasEnabled::isEnabled)
                        .forEachOrdered(table -> {
                            
                    final TranslatorSupport<Table> support = 
                        new TranslatorSupport<>(getSpeedment(), table);
                    
                    file.add(Import.of(support.entityType()));
                    
                    clazz.add(Method.of(support.variableName(), support.managerType())
                        .public_()
                        .add(Field.of("app", appType))
                        .add(AnnotationUsage.of(Type.of(Bean.class)))
                        .add("return (" + support.managerName() + ") app.managerOf(" + support.typeName() + ".class);")
                    );
                });
            }).build();
    }
}