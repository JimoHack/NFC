package com.nfctool.library;

import java.io.File;

public class MMHFProtocol {

    public static class Record {
        byte length  ; // 1Byte
        int  address ; // 2Byte
        byte[] data  ;
    }

    public static class Body {
        byte allRecordSize ; // 1Byte
        byte recordCnt ; // 1Byte
        Record records ;
    }

    public static class MMHF {
        byte[] header ; // 4Byte
        byte[] type ; // 6Byte
        byte   bodyCnt ; // 1Byte
        Body[] bodys ;
    }


    /**
     * 解析本地的文件到内存
     * @param file 要解析的文件
     * @return 解析得到的对象
     */
    public static MMHF parse(File file) {
        MMHF ret = new MMHF() ;
        /// 实现解析文件
        return ret ;
    }

}
