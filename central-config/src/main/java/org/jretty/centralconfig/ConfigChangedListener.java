/* 
 * Copyright (C) 2016-2017 the CSTOnline Technology Co.
 * Create by ZollTy on 2016-5-16 (zoutianyong@cstonline.com)
 */
package org.jretty.centralconfig;

import java.util.Properties;
import java.util.Set;

/**
 * 当配置发生变更时，调用此接口通知外部去处理
 * @author zollty
 * @since 2016-5-16
 */
public interface ConfigChangedListener {

    void onChanged(String configFilename, Properties curProps,
            Properties oldProps, Set<String> addedKeys,
            Set<String> changedKeys, Set<String> removedKeys);

}
