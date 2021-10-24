(ns clj-jdbc.db-test
  (:require [clojure.test :refer :all]
            [clj-jdbc.db :refer :all]))

(deftest deserialize-row-test
  (testing "a-test"
    (init)
    (db-start
     (fn [conn]
       (let [rs (db-query conn "select * from t_sys_user")]
         (while (. rs next)
           ;;(println (. rs getInt "user_id"))
           (println (deserialize-row rs [{:column "user_id" :type :int}
                                         {:column "name" :type :string}]))
           ) ) ) ) ) )
