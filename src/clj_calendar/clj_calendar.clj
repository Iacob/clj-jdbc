
(ns clj-calendar.clj-calendar
  (:gen-class))

(defn build-calendar [params]
  (let [{:keys [year month day hour minite second millisecond timezone]
         :or {year 1 month 1 day 1 hour 0 minite 0 second 0 millisecond 0 timezone (java.util.TimeZone/getDefault)}} params
        builder1 (new java.util.Calendar$Builder)]
    (. builder1 setDate year (dec month) day)
    (. builder1 setTimeOfDay hour minite second millisecond)
    (. builder1 setTimeZone timezone)
    (. builder1 build)
    )
  )

