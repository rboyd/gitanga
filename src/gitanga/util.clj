(ns gitanga.util
  "Utility functions for gitanga"
  (:require [clojure.java.io :refer [input-stream]])
  (:import (java.util.zip InflaterInputStream)) 
  (:import (org.apache.commons.io IOUtils)))

(defn inflate
  "Inflates the git object in file f."
  [f]
  (-> (input-stream f)
      InflaterInputStream.
      IOUtils/toByteArray))
