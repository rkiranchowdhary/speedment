/**
 *
 * Copyright (c) 2006-2017, Speedment, Inc. All Rights Reserved.
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
package com.speedment.runtime.field.method;

import com.speedment.runtime.field.trait.HasFloatValue;
import java.util.function.UnaryOperator;
import javax.annotation.Generated;

/**
 * Represents a set-operation with all the metadata contained.
 * 
 * @param <ENTITY> entity type
 * @param <D>      database type
 * 
 * @author Emil Forslund
 * @since  3.0.0
 */
@Generated(value = "Speedment")
public interface SetToFloat<ENTITY, D> extends UnaryOperator<ENTITY> {
    
    /**
     * Returns the field that this setter sets.
     * 
     * @return the field
     */
    HasFloatValue<ENTITY, D> getField();
    
    /**
     * Returns the value that this setter will set in the field when it is
     * applied.
     * 
     * @return the field
     */
    float getValue();
}