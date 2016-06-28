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
package com.speedment.internal.transaction;

import com.speedment.Speedment;
import com.speedment.config.db.Dbms;
import com.speedment.db.DbmsHandler;
import com.speedment.db.SqlBiConsumer;
import com.speedment.db.SqlConsumer;
import com.speedment.transaction.Transaction;
import com.speedment.transaction.TransactionHandler;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author Per Minborg
 */
public class TransactionHandlerImpl implements TransactionHandler {

    private final Speedment speedment;
    private final Dbms dbms;
    private final SqlConsumer<Transaction> initializer;
    private final SqlConsumer<Transaction> finisher;
    private final SqlBiConsumer<Transaction, Throwable> handler;
    private final Executor/*Service*/ executor;
    private final int transactionIsolation;
    //
    private final DbmsHandler dbmsHandler;

    public TransactionHandlerImpl(
        final Speedment speedment,
        final Dbms dbms,
        final SqlConsumer<Transaction> initializer,
        final SqlConsumer<Transaction> finisher,
        final SqlBiConsumer<Transaction, Throwable> handler,
        final Executor executor,
        final int transactionIsolation
    ) {
        this.speedment = speedment;
        this.dbms = dbms;
        this.initializer = initializer;
        this.finisher = finisher;
        this.handler = handler;
        this.executor = executor;
        this.transactionIsolation = transactionIsolation;
        this.dbmsHandler = speedment.getDbmsHandlerComponent().get(dbms);
    }

    @Override
    public void invoke(Consumer<Transaction> action) throws InterruptedException, ExecutionException {
        final Connection connection = dbmsHandler.getConnection(dbms);
        final Transaction tx = new TransactionImpl(connection);
        try {
            initializer.accept(tx);
            executor.execute(() -> action.accept(tx));
            finisher.accept(tx);
        } catch (RejectedExecutionException | SQLException ex) {
            try {
                handler.accept(tx, ex);
            } catch (SQLException sqle) {

            }
            throw new ExecutionException("Unable to invoke transaction", ex);
        }
    }

    @Override
    public <R> R invoke(Function<Transaction, R> mapper) throws InterruptedException, ExecutionException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <R> R invoke(Function<Transaction, R> mapper, long timeout, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public CompletableFuture<Void> submit(Consumer<Transaction> action) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public <R> CompletableFuture<R> submit(Function<Transaction, R> mapper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
