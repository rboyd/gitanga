(ns gitanga.time-test
  (:use expectations
        gitanga.time)
  (:import (org.joda.time DateTime DateTimeZone)))

; parses git timezone properly
(expect (parse-zone "-0230") (DateTimeZone/forOffsetHoursMinutes -2 30))

; parses git timezone properly
(expect (parse-zone "+0400") (DateTimeZone/forOffsetHoursMinutes 4 0))

; parses git date properly"
(expect (parse-date "1365905378 -0500")
        (DateTime. 2013 04 14 2 9 38 0 (DateTimeZone/forOffsetHours -5)))
