/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * created by 2020/8/29
 * @author pinggonglve
 **/
@Entity
data class User(
    @PrimaryKey
    val userId: String, //ID号
    val nickName: String, //昵称
    val realName: String? = null, //备注
    val avatar: String? = null, //头像
    val mobile: String? = null, //手机号
    val description: String? = null, //描述
    val gender: Int = 0, //性别
    val age: Int = 0, //年龄
    val area: String? = null, //地区
    val signature: String? = null, //签名
    val background: String? = null, //背景图片
    val sortKey: String? = null, //排序关键字
    val source: Int = 0, //来源
    val type: Int = 0, //类型
//    val albums: List<String>? = null, //相册
    val birthday: String? = null, //出生日期，格式: 年-月-日 v2.4.0
    val hometown: String? = null, //家乡 "CN"
    val region: String? = null, //在区域信息
    val userRelation: Int = 0, //与用户的关系
    val blocked: Boolean = false, //黑名单用户
    val dnd: Boolean = false, //勿扰用户
    val secret: Boolean = false, //密友用户
    val verify: Boolean = false, //需要验证用户
    val avatarFrameId: Int = 0, //头像挂件
    val bubbleFrameId: Int = 0, //气泡
//    val tags: List<Long>? = null //标签
)