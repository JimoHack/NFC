package com.nfctool.library;

import android.util.Pair;

import java.util.List;

/**
 * 蓝牙串口通信接口
 * 要求接收到的字节(8位)数据放到缓冲区里，缓冲区要求访问速度较快
 * 蓝牙串口方面参考: https://www.jianshu.com/p/68fda037c336
 */
interface IBluetooth {

    /**
     * 检查是否有访问蓝牙以及对蓝牙操作的权限，没有的话，则申请权限。
     * @return 返回最终是否有权限访问蓝牙
     */
    boolean checkPermission() ;


    /**
     * 根据蓝牙的MAC地址连接蓝牙
     * @param mac 蓝牙的MAC地址
     * @return 连接是否成功
     */
    boolean connect(String mac) ;


    /**
     * 扫描蓝牙，得到蓝牙设备列表
     * @return (蓝牙的名称,蓝牙的MAC地址)[]
     */
    Pair<String,String>[] getBluetoothList() ;


    /**
     * 通过蓝牙串口发送消息
     * @param msg
     * @return 是否发送成功
     */
    boolean sendMessage(byte[] msg) ;
    boolean sendMessage(List<Byte> msg) ;


    /**
     * 得到缓冲区当前接收到的所有数据，不清空缓冲区
     * @return 缓冲区的数据
     */
    Byte[] getMessageByte() ;


    /**
     * 得到缓冲区当前接收到的所有数据，同时清空缓冲区
     * @return 缓冲区的数据
     */
    Byte[] popMessageByte() ;


    /**
     * 得到当前缓冲区最早收到的字符，不删除该字符
     * @return 当前缓冲区最早收到的字符
     */
    byte front() ;


    /**
     * 得到当前缓冲区最晚收到的字符，不删除该字符
     * @return 当前缓冲区最晚收到的字符
     */
    byte back() ;


    /**
     * 得到当前缓冲区最早收到的字符，并删除该字符
     * @return 当前缓冲区最早收到的字符
     */
    byte pop_front() ;


    /**
     * 得到当前缓冲区最晚收到的字符，并删除该字符
     * @return 当前缓冲区最晚收到的字符
     */
    byte pop_back() ;

}

//public class Bluetooth implements IBluetooth {
//
//}