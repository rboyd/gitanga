(ns gitanga.util
  "Utility functions for gitanga"
  (:require [clojure.java.io :refer [input-stream]]
            [gloss.core :refer [defcodec string repeated ordered-map
                                string-integer compile-frame]])
  (:import (java.util.zip InflaterInputStream)) 
  (:import (org.apache.commons.io IOUtils)))

(defn inflate
  "Inflates the git object in file f."
  [f]
  (with-open [in (input-stream f)]
    (-> (InflaterInputStream. in)
        IOUtils/toByteArray)))

(defn unhexify [hex]
  (apply str
    (map 
      (fn [[x y]] (char (Integer/parseInt (str x y) 16))) 
      (partition 2 hex))))

(defn hexify [s]
  (apply str
    (map #(format "%02x" (int %)) s)))

(def binary-obj-id
  (compile-frame
   (take 20 (repeat :ubyte))
   unhexify
   hexify))

(defcodec tree-entry
  (ordered-map :mode   (string :utf-8 :delimiters [" "])
               :name   (string :utf-8 :delimiters ["\0"])
               :obj-id binary-obj-id))

(defcodec tree
  (ordered-map :type (string :utf-8 :delimiters [" "])
               :length (string-integer :utf-8 :delimiters ["\0"])
               :tree-entries (repeated tree-entry :prefix :none)))
