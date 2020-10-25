/*
 * Copyright (c) 2020. pinggonglve
 */

package me.pglvee.parse

import me.pglvee.database.entity.Book
import org.jsoup.Jsoup

/**
 * created by 2020/10/25
 * @author pinggonglve
 **/
object ParseUtil {

    fun find(page: Int): List<Book> =
        Jsoup.connect("https://www.qidian.com/rank/recom?dateType=3&page=$page")
            .get()
            .select(".book-img-text > ul > li")
            .map { element ->
                Book(
                    bookName = element.select(".book-mid-info > h4").text(),
                    author = element.select(".author > a")[0].text(),
                    latestDate = element.select(".update > span").text(),
                    latestChapterName = element.select(".update > a").text(),
                    latestChapterUrl = "https:${element.select(".update > a").attr("href")}",
                    bookUrl = "https:${element.select(".book-mid-info > h4 > a").attr("href")}",
                    pictureUrl = "https:${element.select(".book-img-box > a > img").attr("src")}",
                    description = element.select(".intro").text()
                )
            }
}