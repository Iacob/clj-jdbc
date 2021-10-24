(ns clj-jdbc.common
  (:require [clojure.java.io :as io])
  (:gen-class))

(def config-file "config.properties")

(def config-props (atom nil))

(defn load-props []
  (let [configFile (io/file "config.properties") configProps (new java.util.Properties)]
    (if (. configFile exists)
      (do
        (with-open [configStream (io/input-stream configFile)]
          (. configProps load configStream)
          (reset! config-props configProps) )
        configProps )
      nil) ) )

(def config (load-props))

(defn get-config []
  @config-props )
