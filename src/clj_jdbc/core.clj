(ns clj-jdbc.core
  (:require [clj-jdbc.common :as common])
  (:require [clj-jdbc.ronghenghr :as ronghenghr])
  (:require [clojure.java.io :as io])
  (:require [clj-jdbc.db :as db])
  (:gen-class))

(defn -main
  "main function"
  [& args]
  ;; (when (nil? (ronghenghr/load-props))
  ;;   (println (str "Cannot locate config file \"" ronghenghr/config-file  "\". exit."))
  ;;   (System/exit 1) )
  ;;(println (get (ronghenghr/get-config) "spring.datasource.url"))
  (db/init)
  (when (nil? common/config)
    (println (str "Cannot locate config file \"" common/config-file  "\". exit."))
    (System/exit 1) )
  ;;(println (get common/config "spring.datasource.url"))
  (ronghenghr/run) )
