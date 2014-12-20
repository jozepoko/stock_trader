package jozepoko.stock_trader.core.domain.service.util.datetime

import org.joda.time.DateTime

trait HolidayDistinguisher {
  val dateTime: DateTime

  //土日、祝日の場合true
  def isHoliday: Boolean = {
    isWeekEnd ||
      isNationalHoliday ||
      isNationalHolidayInLieu ||
      isBetweenNationalHoliday
  }

  //土日の場合true
  def isWeekEnd: Boolean = {
    dateTime.getWeek match {
      case WeekEnum.Saturday | WeekEnum.Sunday => true
      case _ => false
    }
  }

  //祝日の場合true。ただし振替休日、国民の休日は除く。
  def isNationalHoliday: Boolean = {
    isNewYearsDay ||
      isComingOfAgeDay ||
      isNationalFoundationDay ||
      isVernalEquinoxHoliday ||
      isShowaDay ||
      isConstitutionDay ||
      isGreeneryDay ||
      isChildrensDay ||
      isMarineDay ||
      isMountainDay ||
      isRespectForTheAgedDayHoliday ||
      isAutumnEquinoxHoliday ||
      isHealthSportsDay ||
      isCultureDay ||
      isLaborThanksgivingDay ||
      isEmperorsBirthday
  }

  //元旦 1月1日
  def isNewYearsDay: Boolean = dateTime.isSameMonthDay(1, 1)

  //成人の日 1月の第2月曜日
  def isComingOfAgeDay: Boolean = isSameMonthWeekCount(1, WeekEnum.Monday, 2)

  //建国記念日 2月11日
  def isNationalFoundationDay: Boolean = dateTime.isSameMonthDay(2, 11)

  /**
   * 春分の日
   * 西暦年数の4での剰余が0の場合
   *   1900年 - 1956年までは3月21日
   *   1960年 - 2088年までは3月20日
   *   2092年 - 2096年までは3月19日
   * 西暦年数の4での剰余が1の場合
   *   1901年 - 1989年までは3月21日
   *   1993年 - 2097年までは3月20日
   * 西暦年数の4での剰余が2の場合
   *   1902年 - 2022年までは3月21日
   *   2026年 - 2098年までは3月20日
   * 西暦年数の4での剰余が3の場合
   *   1903年 - 1923年までは3月22日
   *   1927年 - 2055年までは3月21日
   *   2059年 - 2099年までは3月20日
   */
  def isVernalEquinoxHoliday: Boolean = {
    val year = dateTime.getYear
    dateTime.getMonthOfYear == 3 && {
      val day = year % 4 match {
        case 0 => year match {
          case y if y <= 1956 => 21
          case y if y <= 2088 => 20
          case y if y <= 2096 => 19
          case _              => 20  //適当
        }
        case 1 => year match {
          case y if y <= 1989 => 21
          case y if y <= 2097 => 20
          case _              => 20  //適当
        }
        case 2 => year match {
          case y if y <= 2022 => 21
          case y if y <= 2098 => 20
          case _              => 20  //適当
        }
        case 3 => year match {
          case y if y <= 1923 => 22
          case y if y <= 2055 => 21
          case y if y <= 2099 => 20
          case _              => 20  //適当
        }
        case _ => 20  //計算上はありえないが、例外のキャッチが面倒なので例外は吐かず適当に20を返しておく。
      }
      dateTime.getDayOfMonth == day
    }
  }

  //昭和の日 4月29日
  def isShowaDay: Boolean = dateTime.isSameMonthDay(4, 29)

  //憲法記念日 5月3日
  def isConstitutionDay: Boolean = dateTime.isSameMonthDay(5, 3)

  //みどりの日 5月4日
  def isGreeneryDay: Boolean = dateTime.isSameMonthDay(5, 4)

  //こどもの日 5月5日
  def isChildrensDay: Boolean = dateTime.isSameMonthDay(5, 5)

  //海の日 7月の第3月曜日
  def isMarineDay : Boolean = isSameMonthWeekCount(7, WeekEnum.Monday, 3)

  //山の日 8月の11日
  //2016年より施行
  def isMountainDay : Boolean = {
    dateTime.getYear >= 2016 && dateTime.isSameMonthDay(8, 11)
  }

  //敬老の日 9月の第3月曜日
  def isRespectForTheAgedDayHoliday: Boolean = isSameMonthWeekCount(9, WeekEnum.Monday, 3)

  /**
   * 西暦年数の4での剰余が0の場合
   *   1900年 - 2008年までは9月23日
   *   2012年 - 2096年までは9月22日
   * 西暦年数の4での剰余が1の場合
   *   1901年 - 1917年までは9月24日
   *   1921年 - 2041年までは9月23日
   *   2045年 - 2097年までは9月22日
   * 西暦年数の4での剰余が2の場合
   *   1902年 - 1946年までは9月24日
   *   1950年 - 2074年までは9月23日
   *   2078年 - 2098年までは9月22日
   * 西暦年数の4での剰余が3の場合
   *   1903年 - 1979年までは9月24日
   *   1983年 - 2099年までは9月23日
   */
  def isAutumnEquinoxHoliday: Boolean = {
    val year = dateTime.getYear
    dateTime.getMonthOfYear == 9 && {
      val day = year % 4 match {
        case 0 => year match {
          case y if y <= 2008 => 23
          case y if y <= 2096 => 22
          case _              => 23  //適当
        }
        case 1 => year match {
          case y if y <= 1917 => 24
          case y if y <= 2041 => 23
          case y if y <= 2097 => 22
          case _              => 23  //適当
        }
        case 2 => year match {
          case y if y <= 1946 => 24
          case y if y <= 2074 => 23
          case y if y <= 2098 => 22
          case _              => 23  //適当
        }
        case 3 => year match {
          case y if y <= 1979 => 24
          case y if y <= 2099 => 23
          case _              => 23  //適当
        }
        case _ => 23  //計算上はありえないが、例外のキャッチが面倒なので例外は吐かず適当に20を返しておく。
      }
      dateTime.getDayOfMonth == day
    }
  }

  //体育の日 10月の第2月曜日
  def isHealthSportsDay: Boolean = isSameMonthWeekCount(10, WeekEnum.Monday, 2)

  //文化の日 11月3日
  def isCultureDay: Boolean = dateTime.isSameMonthDay(11, 3)

  //勤労感謝の日 11月23日
  def isLaborThanksgivingDay: Boolean = dateTime.isSameMonthDay(11, 23)

  //天皇誕生日 12月23日
  def isEmperorsBirthday: Boolean = dateTime.isSameMonthDay(12, 23)

  //振替休日 祝日が日曜日になった時、代わりにその日以降の最初の祝日でない日が振替休日となる
  def isNationalHolidayInLieu: Boolean = {
    def collectConsecutiveNationalHoliday(days: List[DateTime]): List[DateTime] = {
      val beforeDay = days.head.minusDays(1)
      if (beforeDay.isNationalHoliday) collectConsecutiveNationalHoliday(beforeDay :: days)
      else days
    }
    dateTime.getWeek != WeekEnum.Sunday && !dateTime.isNationalHoliday && collectConsecutiveNationalHoliday(List(dateTime)).exists(_.getWeek == WeekEnum.Sunday)
  }

  //国民の休日 祝日に挟まれた日は祝日となる
  def isBetweenNationalHoliday = {
    val beforeDay = dateTime.minusDays(1)
    val afterDay = dateTime.plusDays(1)
    (beforeDay.isNationalHoliday || beforeDay.isNationalHolidayInLieu) &&
      (afterDay.isNationalHoliday || afterDay.isNationalHolidayInLieu)
  }

  private def isSameMonthWeekCount(month: Int, week: Week, count: Int): Boolean = {
    dateTime.getMonthOfYear == month && dateTime.isSameWeekCount(week, count)
  }
}
