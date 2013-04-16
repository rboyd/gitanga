(ns gitanga.time-test
  (:use clojure.test
        gitanga.time)
  (:import (org.joda.time DateTime DateTimeZone)))

(deftest parse-zone-test
  (testing "parses git timezone properly"
    (is (= (parse-zone "-0230")
           (DateTimeZone/forOffsetHoursMinutes -2 30)))))

(deftest parse-pos-zone-test
  (testing "parses git timezone properly"
    (is (= (parse-zone "+0400")
           (DateTimeZone/forOffsetHoursMinutes 4 0)))))

(deftest parse-time-test
  (testing "parses git date properly"
    (is (= (parse-date "1365905378 -0500")
           (DateTime. 2013 04 14 2 9 38 0 (DateTimeZone/forOffsetHours -5))))))
