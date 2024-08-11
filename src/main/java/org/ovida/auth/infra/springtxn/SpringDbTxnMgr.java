package org.ovida.auth.infra.springtxn;

import java.util.function.Supplier;
import org.ovida.auth.app.component.DbTxnMgr;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SpringDbTxnMgr implements DbTxnMgr {

  @Override
  @Transactional
  public void doInTx(Runnable runnable) {
    runnable.run();
  }

  @Override
  @Transactional
  public <T> T doInTx(Supplier<T> supplier) {
    return supplier.get();
  }
}
