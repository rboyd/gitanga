(ns gitanga.time
  "Time functions for gitanga"
  (:require [clj-time.core :refer [time-zone-for-offset from-time-zone]]
            [clj-time.coerce :refer [from-long]]))

(defn parse-zone
  [tz-str]
  (let [sign      (first tz-str)
        sub-start (if (= \+ sign) 1 0)
        hours     (subs tz-str sub-start 3)
        minutes   (subs tz-str 3 5)]
    (time-zone-for-offset (Integer. hours) (Integer. minutes))))

(defn parse-date
  [git-date]
  (let [[unix-sec offset] (clojure.string/split git-date #" ")]
    (-> (from-long (* 1000 (java.lang.Long/parseLong unix-sec)))
        (from-time-zone (parse-zone offset)))))
