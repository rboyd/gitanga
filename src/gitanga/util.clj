(ns gitanga.util
  "Utility functions for gitanga"
  (:require [clojure.java.io :refer [input-stream]]
            [gloss.core :refer [defcodec string repeated ordered-map
                                string-integer compile-frame header]]
            [gitanga.time :refer [parse-date]])
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
  (ordered-map :type :tree-entry
               :mode (string :utf-8 :delimiters [" "])
               :name (string :utf-8 :delimiters ["\0"])
               :obj-id binary-obj-id))

(defcodec tree
  (ordered-map :type :tree
               :length (string-integer :utf-8 :delimiters ["\0"])
               :tree-entries (repeated tree-entry :prefix :none)))

(defn hex-obj-id [type]
  (compile-frame
    (string :utf-8 :delimiters ["\n"])
    identity
    #(->> (drop (count type) %) (apply str))))

(defcodec commit-parents
  (repeated (hex-obj-id "parent ") :prefix :none :delimiters ["author "]))

(defn extract-times
  [commit-contents]
  (let [{:keys [author committer]} commit-contents
        extract-time #(-> (re-find #"[^>]+> (.*)" %) second)]
    (merge commit-contents {:author    {:string    author
                                        :date-time (-> (extract-time author)
                                                       parse-date)}
                            :committer {:string    committer
                                        :date-time (-> (extract-time committer)
                                                       parse-date)}})))

(def commit-contents
  (compile-frame
    (ordered-map :type :commit-contents
                 :tree-obj-id (hex-obj-id "tree ")
                 :parents commit-parents
                 :author (string :utf-8 :delimiters ["\ncommitter "])
                 :committer (string :utf-8 :delimiters ["\n\n"])
                 :data (string :utf-8))
    identity
    extract-times))

(defcodec commit
  (ordered-map :type :commit
               :length (string-integer :utf-8 :delimiters ["\0"])
               :contents commit-contents))

(defcodec git-obj
  (header
   (string :utf-8 :delimiters [" "])
   {"tree"   tree
    "commit" commit}
   identity))
