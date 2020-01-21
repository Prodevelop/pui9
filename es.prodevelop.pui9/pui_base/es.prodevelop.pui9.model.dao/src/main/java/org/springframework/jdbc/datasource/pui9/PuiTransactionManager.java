package org.springframework.jdbc.datasource.pui9;

import java.util.concurrent.atomic.AtomicLong;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;

import es.prodevelop.pui9.eventlistener.ThreadDaoEvents;

/**
 * This class is the new Manager of the Transactions for PUI using JDBC. It
 * extends Spring DataSourceTransactionManager implementation, and supports
 * executing Dao Events when transaction finishes correctly
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
public class PuiTransactionManager extends DataSourceTransactionManager {

	private static final long serialVersionUID = 1L;
	private static final AtomicLong seq = new AtomicLong(0);

	@Autowired
	private ThreadDaoEvents threadDaoEvents;

	public PuiTransactionManager(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	protected Object doGetTransaction() {
		Object obj = super.doGetTransaction();
		threadDaoEvents.initialize();
		return obj;
	}

	@Override
	protected void doCommit(DefaultTransactionStatus status) {
		super.doCommit(status);
		threadDaoEvents.process(seq.getAndIncrement());
	}

	@Override
	protected void doRollback(DefaultTransactionStatus status) {
		super.doRollback(status);
		threadDaoEvents.remove();
	}

}
