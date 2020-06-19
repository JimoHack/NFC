package com.nfctool.library;

import android.util.Pair;
import java.io.File;

interface IFileView {


    /**
     * 检查是否有访问文件以及对文件操作的权限，没有的话，则申请权限。
     * @return 返回最终是否有权限访问文件
     */
    boolean checkPermission() ;


    /**
     * 得到当前目录下的所有文件以及文件夹
     * @return 当前目录下所有文件和文件夹的引用
     */
    File[] getAllFileList() ;


    /**
     * 得到当前目录下的所有文件
     * @return (文件,该文件在getAllFileList中从零算起的位置)
     */
    Pair<File,Integer>[] getFileList() ;


    /**
     * 得到当前目录下的所有文件夹
     * @return (文件夹,该文件夹在getAllFileList中从零算起的位置)
     */
    Pair<File,Integer>[] getDirList() ;


    /**
     * 切换到当前目录下的某一个文件夹
     * @param index
     * @return 是否切换成功
     */
    boolean switchInto(int index) ;


    /**
     * 切换到某一个文件夹(不一定是当前目录)
     * @param dir
     * @return 是否切换成功
     */
    boolean switchInto(File dir)  ;
}

//public class FileView implements IFileView {
//
//}