package jozepoko.stock_trader.core.domain.service.util.datetime

import jozepoko.stock_trader.test.TestBase
import org.joda.time.DateTime

class MyDateTimeSpec extends TestBase {
  "HolidayDistinguisher" should {
    "土曜日の判定ができる" in {
      val day = new DateTime(2014, 12, 6, 0, 0, 0)
      day.isWeekEnd must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "日曜日の判定ができる" in {
      val day = new DateTime(2014, 12, 7, 0, 0, 0)
      day.isWeekEnd must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "元旦の判定ができる" in {
      val day = new DateTime(2014, 1, 1, 0, 0, 0)
      day.isNewYearsDay must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "成人の日の判定ができる" in {
      val day = new DateTime(2014, 1, 13, 0, 0, 0)
      day.isComingOfAgeDay must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "建国記念日の判定ができる" in {
      val day = new DateTime(2014, 2, 11, 0, 0, 0)
      day.isNationalFoundationDay must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "春分の日の判定ができる" in {
      val day = new DateTime(2014, 3, 21, 0, 0, 0)
      day.isVernalEquinoxHoliday must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "昭和の日の判定ができる" in {
      val day = new DateTime(2014, 4, 29, 0, 0, 0)
      day.isShowaDay must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "憲法記念日の判定ができる" in {
      val day = new DateTime(2014, 5, 3, 0, 0, 0)
      day.isConstitutionDay must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "みどりの日判定ができる" in {
      val day = new DateTime(2014, 5, 4, 0, 0, 0)
      day.isGreeneryDay must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "こどもの日の判定ができる" in {
      val day = new DateTime(2014, 5, 5, 0, 0, 0)
      day.isChildrensDay must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "海の日の判定ができる" in {
      val day = new DateTime(2014, 7, 21, 0, 0, 0)
      day.isMarineDay must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "山の日の判定ができる" in {
      val day = new DateTime(2016, 8, 11, 0, 0, 0)
      day.isMountainDay must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "敬老の日の判定ができる" in {
      val day = new DateTime(2014, 9, 15, 0, 0, 0)
      day.isRespectForTheAgedDayHoliday must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "秋分の日の判定ができる" in {
      val day = new DateTime(2014, 9, 23, 0, 0, 0)
      day.isAutumnEquinoxHoliday must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "体育の日の判定ができる" in {
      val day = new DateTime(2014, 10, 13, 0, 0, 0)
      day.isHealthSportsDay must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "文化の日の判定ができる" in {
      val day = new DateTime(2014, 11, 3, 0, 0, 0)
      day.isCultureDay must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "勤労感謝の日の判定ができる" in {
      val day = new DateTime(2014, 11, 23, 0, 0, 0)
      day.isLaborThanksgivingDay must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "天皇誕生日の判定ができる" in {
      val day = new DateTime(2014, 12, 23, 0, 0, 0)
      day.isEmperorsBirthday must beTrue
      day.isHoliday must beTrue
      day.isTradableDateTime must beFalse
    }

    "振替休日の判定ができる" in  {
      val day1 = new DateTime(2014, 5, 6, 0, 0, 0)
      day1.isNationalHolidayInLieu must beTrue
      day1.isHoliday must beTrue
      day1.isTradableDateTime must beFalse
      val day2 = new DateTime(2014, 11, 24, 0, 0, 0)
      day2.isNationalHolidayInLieu must beTrue
      day2.isHoliday must beTrue
      day2.isTradableDateTime must beFalse
    }

    "年末の判定ができる" in {
      new DateTime(2014, 12, 31, 0, 0, 0).isTradableDateTime must beFalse
    }

    "年始の判定ができる" in {
      new DateTime(2014, 1, 1, 0, 0, 0).isTradableDateTime must beFalse
      new DateTime(2014, 1, 2, 0, 0, 0).isTradableDateTime must beFalse
      new DateTime(2014, 1, 3, 0, 0, 0).isTradableDateTime must beFalse
    }

    "2011年5月の全ての日の取引可能かの判定ができる" in {
      new DateTime(2011, 5, 1, 0, 0, 0).isTradableDateTime must beFalse
      new DateTime(2011, 5, 2, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 3, 0, 0, 0).isTradableDateTime must beFalse
      new DateTime(2011, 5, 4, 0, 0, 0).isTradableDateTime must beFalse
      new DateTime(2011, 5, 5, 0, 0, 0).isTradableDateTime must beFalse
      new DateTime(2011, 5, 6, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 7, 0, 0, 0).isTradableDateTime must beFalse
      new DateTime(2011, 5, 8, 0, 0, 0).isTradableDateTime must beFalse
      new DateTime(2011, 5, 9, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 10, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 11, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 12, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 13, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 14, 0, 0, 0).isTradableDateTime must beFalse
      new DateTime(2011, 5, 15, 0, 0, 0).isTradableDateTime must beFalse
      new DateTime(2011, 5, 16, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 17, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 18, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 19, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 20, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 21, 0, 0, 0).isTradableDateTime must beFalse
      new DateTime(2011, 5, 22, 0, 0, 0).isTradableDateTime must beFalse
      new DateTime(2011, 5, 23, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 24, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 25, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 26, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 27, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 28, 0, 0, 0).isTradableDateTime must beFalse
      new DateTime(2011, 5, 29, 0, 0, 0).isTradableDateTime must beFalse
      new DateTime(2011, 5, 30, 0, 0, 0).isTradableDateTime must beTrue
      new DateTime(2011, 5, 31, 0, 0, 0).isTradableDateTime must beTrue
    }
  }
}
