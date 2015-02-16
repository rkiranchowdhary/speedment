/**
 *
 * Copyright (c) 2006-2015, Speedment, Inc. All Rights Reserved.
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
package com.speedment.codegen.model.parameter;

import com.speedment.codegen.Nameable;
import com.speedment.codegen.model.AbstractModifiableCodeModel;
import com.speedment.codegen.model.CodeModel;
import com.speedment.codegen.model.CodeModel.Type;
import com.speedment.codegen.model.type.Type_;
import com.speedment.codegen.model.annotation.Annotatable;
import com.speedment.codegen.model.annotation.Annotation_;
import com.speedment.codegen.model.modifier.Modifiable;
import com.speedment.codegen.model.modifier.ParameterModifier_;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author pemi
 */
public class Parameter_ extends AbstractModifiableCodeModel<Parameter_, ParameterModifier_> implements CodeModel, Nameable, Modifiable<ParameterModifier_>, Annotatable {

    private final Set<ParameterModifier_> modifiers;
    private final List<Annotation_> annotations;
    private Type_ type;
    private CharSequence name;

    public Parameter_() {
        this.modifiers = EnumSet.noneOf(ParameterModifier_.class);
        this.annotations = new ArrayList<>();
    }

    public Parameter_(Type_ type, CharSequence name) {
        this();
        setType(type);
        setName(name);
    }

    @Override
    public CharSequence getName() {
        return name;
    }

    @Override
    public Parameter_ setName(CharSequence name_) {
        return with(name_, n -> this.name = n);
    }

    public Parameter_ setType(Type_ type) {
        return with(type, t -> this.type = t);
    }

    @Override
    public Type getModelType() {
        return Type.PARAMETER;
    }

    public Type_ getType() {
        return type;
    }

    public void setType_(Type_ type) {
        this.type = type;
    }

    @Override
    public Set<ParameterModifier_> getModifiers() {
        return modifiers;
    }

    @Override
    public Parameter_ add(final Annotation_ annotation) {
        return with(annotation, getAnnotations()::add);
    }

    @Override
    public List<Annotation_> getAnnotations() {
        return annotations;
    }

    @Override
    public boolean has(Annotation_ annotation_) {
        return annotations.contains(annotation_);
    }

    public Parameter_ final_() {
        return add(ParameterModifier_.FINAL);
    }

}