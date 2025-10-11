package tm.kumush.shengci.data

import androidx.annotation.StringRes
import tm.kumush.shengci.R


data class ShengCiData(
    val id: Int,
    @StringRes val title: Int,
)

val units = listOf(
    ShengCiData(1, R.string.diyike),
    ShengCiData(2, R.string.dierke),
    ShengCiData(3, R.string.disanke),
    ShengCiData(4, R.string.disike),
    ShengCiData(5, R.string.diwuke),
    ShengCiData(6, R.string.diliuke),
    ShengCiData(7, R.string.diqike),
    ShengCiData(8, R.string.dibake),
    ShengCiData(9, R.string.dijiuke),
    ShengCiData(10, R.string.dishike),
    ShengCiData(11, R.string.dishiyike),
    ShengCiData(12, R.string.dishierke),
    ShengCiData(13, R.string.dishisanke),
    ShengCiData(14, R.string.dishisike),
    ShengCiData(15, R.string.dishiwuke)
)