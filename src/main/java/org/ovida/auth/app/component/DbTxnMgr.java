package org.ovida.auth.app.component;

import java.util.function.Supplier;

public interface DbTxnMgr {

  void doInTx(Runnable runnable);

  <T> T doInTx(Supplier<T> supplier);
}
