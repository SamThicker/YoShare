package com.yo.fileservice.util;

import com.yo.yoshare.mbg.model.CmsFileTransInfo;

public interface CallBack {
    void doCallBack(CmsFileTransInfo info) throws Exception;
}
