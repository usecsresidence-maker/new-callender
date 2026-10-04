package com.threecalendar.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import android.graphics.BitmapFactory
import java.time.format.TextStyle
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ThreeCalendarApp() }
    }
}

@Composable
fun ThreeCalendarApp() {
    var month by remember { mutableStateOf(YearMonth.now()) }
    var selected by remember { mutableStateOf(LocalDate.now()) }
    var urdu by remember { mutableStateOf(false) }
    var dark by remember { mutableStateOf(false) }
    val developerPhoto = remember { BitmapFactory.decodeResource(androidx.compose.ui.platform.LocalContext.current.resources, R.drawable.developer_photo) }

    MaterialTheme(colorScheme = if (dark) darkColorScheme() else lightColorScheme()) {
        Surface(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize().padding(16.dp)) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            bitmap = developerPhoto.asImageBitmap(),
                            contentDescription = "Developer photo",
                            modifier = Modifier
                                .size(52.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        Spacer(Modifier.width(10.dp))
                        Column {
                            Text("3 Calendar", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                            Text(if (urdu) "عیسوی • ہجری • پنجابی" else "Gregorian • Islamic • Punjabi")
                        }
                    }
                    Row {
                        TextButton(onClick = { urdu = !urdu }) { Text(if (urdu) "EN" else "اردو") }
                        TextButton(onClick = { dark = !dark }) { Text("☾") }
                    }
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { month = month.minusMonths(1) }) { Text("‹", style = MaterialTheme.typography.headlineMedium) }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(month.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH), fontWeight = FontWeight.Bold)
                        Text(month.year.toString())
                    }
                    IconButton(onClick = { month = month.plusMonths(1) }) { Text("›", style = MaterialTheme.typography.headlineMedium) }
                }

                Spacer(Modifier.height(8.dp))

                val weekNames = listOf("Mon","Tue","Wed","Thu","Fri","Sat","Sun")
                Row(Modifier.fillMaxWidth()) {
                    weekNames.forEach { d ->
                        Box(Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(d, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(Modifier.height(6.dp))

                val cells = calendarCells(month)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(cells) { date ->
                        if (date == null) {
                            Box(Modifier.aspectRatio(0.78f))
                        } else {
                            val holiday = PakistanHolidays.holiday(date)
                            val hijri = Hijri.fromGregorian(date)
                            val punjabi = Bikrami.fromGregorian(date)

                            Card(
                                modifier = Modifier
                                    .aspectRatio(0.78f)
                                    .clickable { selected = date },
                                colors = CardDefaults.cardColors(
                                    containerColor = when {
                                        date == LocalDate.now() -> MaterialTheme.colorScheme.primaryContainer
                                        holiday != null -> MaterialTheme.colorScheme.tertiaryContainer
                                        else -> MaterialTheme.colorScheme.surfaceVariant
                                    }
                                )
                            ) {
                                Column(
                                    Modifier.fillMaxSize().padding(4.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("${date.dayOfMonth}", fontWeight = FontWeight.Bold)
                                    Text("${hijri.day}", style = MaterialTheme.typography.labelSmall)
                                    Text("${punjabi.day}", style = MaterialTheme.typography.labelSmall)
                                    if (holiday != null) Text("🇵🇰", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                val h = Hijri.fromGregorian(selected)
                val p = Bikrami.fromGregorian(selected)
                Card(Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(14.dp)) {
                        Text(selected.toString(), fontWeight = FontWeight.Bold)
                        Text("${h.day} ${h.monthName} ${h.year} AH")
                        Text("${p.day} ${p.monthName} ${p.year} BK")
                        PakistanHolidays.holiday(selected)?.let {
                            Text("🇵🇰 $it", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))
                Card(Modifier.fillMaxWidth()) {
                    Row(
                        Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            bitmap = developerPhoto.asImageBitmap(),
                            contentDescription = "Developer photo",
                            modifier = Modifier
                                .size(58.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                if (urdu) "دستیاب کنندہ / تخلیق کار" else "Developer",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                if (urdu) "3 Calendar" else "3 Calendar",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                Spacer(Modifier.height(6.dp))
                Text(
                    if (urdu) "نوٹ: قمری تاریخیں رویتِ ہلال کے مطابق ایک دن آگے یا پیچھے ہو سکتی ہیں۔"
                    else "Note: Moon-sighting dates may differ by one day; a Hijri adjustment can be added in Settings.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

fun calendarCells(month: YearMonth): List<LocalDate?> {
    val first = month.atDay(1)
    val leading = (first.dayOfWeek.value - DayOfWeek.MONDAY.value + 7) % 7
    val result = MutableList<LocalDate?>(leading) { null }
    for (d in 1..month.lengthOfMonth()) result.add(month.atDay(d))
    while (result.size % 7 != 0) result.add(null)
    return result
}

data class HijriDate(val day: Int, val month: Int, val year: Int) {
    val monthName: String get() = listOf(
        "Muharram","Safar","Rabi al-Awwal","Rabi al-Thani","Jumada al-Awwal","Jumada al-Thani",
        "Rajab","Sha'ban","Ramadan","Shawwal","Dhu al-Qadah","Dhu al-Hijjah"
    )[month - 1]
}

object Hijri {
    // Tabular Islamic civil calendar. Local moon-sighting adjustments should be supported later.
    fun fromGregorian(g: LocalDate): HijriDate {
        val jd = gregorianToJulian(g.year, g.monthValue, g.dayOfMonth)
        var l = jd - 1948440 + 10632
        val n = ((l - 1) / 10631)
        l = l - 10631 * n + 354
        val j = (((10985 - l) / 5316) * ((50 * l) / 17719)) +
                ((l / 5670) * ((43 * l) / 15238))
        l = l - (((30 - j) / 15) * ((17719 * j) / 50)) -
                ((j / 16) * ((15238 * j) / 43)) + 29
        val m = (24 * l) / 709
        val d = l - (709 * m) / 24
        val y = 30 * n + j - 30
        return HijriDate(d, m, y)
    }

    private fun gregorianToJulian(y: Int, m: Int, d: Int): Int {
        val a = (14 - m) / 12
        val yy = y + 4800 - a
        val mm = m + 12 * a - 3
        return d + (153 * mm + 2) / 5 + 365 * yy + yy / 4 - yy / 100 + yy / 400 - 32045
    }
}

data class BikramiDate(val day: Int, val month: Int, val year: Int) {
    val monthName: String get() = listOf(
        "Chet","Vaisakh","Jeth","Harh","Sawan","Bhadon",
        "Assu","Katak","Maghar","Poh","Magh","Phagun"
    )[month - 1]
}

object Bikrami {
    // Practical Punjabi/Bikrami presentation layer for the modern Gregorian calendar.
    // Month boundaries vary by traditional calculation; this MVP uses fixed modern boundaries.
    fun fromGregorian(g: LocalDate): BikramiDate {
        val starts = listOf(
            LocalDate.of(g.year, 3, 14), LocalDate.of(g.year, 4, 14),
            LocalDate.of(g.year, 5, 15), LocalDate.of(g.year, 6, 15),
            LocalDate.of(g.year, 7, 16), LocalDate.of(g.year, 8, 16),
            LocalDate.of(g.year, 9, 16), LocalDate.of(g.year, 10, 17),
            LocalDate.of(g.year, 11, 16), LocalDate.of(g.year, 12, 16),
            LocalDate.of(g.year + 1, 1, 14), LocalDate.of(g.year + 1, 2, 13)
        )
        var idx = 0
        for (i in starts.indices) {
            if (!g.isBefore(starts[i])) idx = i
        }
        val start = starts[idx]
        val year = if (g.monthValue < 3 || (g.monthValue == 3 && g.dayOfMonth < 14)) g.year + 56 else g.year + 57
        return BikramiDate(java.time.temporal.ChronoUnit.DAYS.between(start, g).toInt() + 1, idx + 1, year)
    }
}

object PakistanHolidays {
    private val holidays = mapOf(
        "2026-02-05" to "Kashmir Solidarity Day",
        "2026-03-20" to "Eid-ul-Fitr",
        "2026-03-21" to "Eid-ul-Fitr",
        "2026-03-23" to "Pakistan Day / Eid-ul-Fitr",
        "2026-05-01" to "Labour Day",
        "2026-05-28" to "Youm-e-Takbeer",
        "2026-05-26" to "Eid-ul-Adha",
        "2026-05-27" to "Eid-ul-Adha",
        "2026-05-28" to "Eid-ul-Adha / Youm-e-Takbeer",
        "2026-06-25" to "Ashura",
        "2026-06-26" to "Ashura",
        "2026-08-14" to "Independence Day",
        "2026-08-26" to "Eid Milad-un-Nabi",
        "2026-11-09" to "Allama Iqbal Day",
        "2026-12-25" to "Quaid-e-Azam Day / Christmas",
        "2026-12-26" to "Day after Christmas (Christians only)"
    )
    fun holiday(date: LocalDate): String? = holidays[date.toString()]
}
