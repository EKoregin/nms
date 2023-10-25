package com.ekoregin.nms.util;

import com.ekoregin.nms.entity.Check;
import com.ekoregin.nms.entity.CheckResult;
import com.ekoregin.nms.entity.Customer;

public interface CheckExecutor {

    CheckResult checkExecute(Check check, Customer customer);
}
