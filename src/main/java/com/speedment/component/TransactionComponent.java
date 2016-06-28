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
package com.speedment.component;

import com.speedment.config.db.Dbms;
import com.speedment.transaction.TransactionHandler;
import java.util.List;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 *
 * @author Per Minborg
 */
public interface TransactionComponent extends Component {

    @Override
    public default Class<? extends Component> getComponentClass() {
        return TransactionComponent.class;
    }

    /**
     * Creates and returns a new default TransactionHandler for the single
     * database in the current Project.
     *
     * @return a new default TransactionHandler for the single database in the
     * current Project
     * @throws IllegalStateException if there are several databases in the
     * current project.
     */
    default TransactionHandler transaction() {
        return builder().build();
    }

    /**
     * Creates and returns a new default TransactionHandler Builder for the
     * single database in the current Project.
     *
     * @return a new default TransactionHandler Builder for the single database
     * in the current Project
     * @throws IllegalStateException if there are several databases in the
     * current project.
     */
    default TransactionHandler.Builder builder() {
        final List<Dbms> dbmses = getSpeedment().getProjectComponent().getProject().dbmses().collect(toList());
        if (dbmses.size() == 1) {
            return builder(dbmses.get(0));
        }
        throw new IllegalStateException("builder() called while there are several dbmses in the project. Please specify which dbms to use.");
    }

    /**
     * Creates and returns a new default TransactionHandler for the given
     * database in the current Project.
     *
     * @param dbms that the TransactionHandler shall use
     * @return a new default TransactionHandler for the given database in the
     * current Project
     * @throws IllegalArgumentException if the given database is not a part of
     * the current project.
     */
    default TransactionHandler transaction(Dbms dbms) {
        return builder(requireNonNull(dbms)).build();
    }

    /**
     * Creates and returns a new default TransactionHandler Builder for the
     * given database in the current Project.
     *
     * @param dbms that the TransactionHandler shall use
     * @return a new default TransactionHandler Builder for the given database
     * in the current Project
     * @throws IllegalArgumentException if the given database is not a part of
     * the current project.
     */
    TransactionHandler.Builder builder(Dbms dbms);

}
