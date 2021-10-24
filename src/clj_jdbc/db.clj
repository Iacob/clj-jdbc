(ns clj-jdbc.db
  (:require [clj-jdbc.common :as common])
  (:gen-class))

(defn init []
  (Class/forName (get common/config "jdbc.driver")))

(defn db-start [proc]
  (let [connAm (atom nil)]
    (try
      (reset! connAm
              (java.sql.DriverManager/getConnection
               (get common/config "jdbc.url")
               (get common/config "jdbc.username")
               (get common/config "jdbc.password")))
      
      (proc @connAm)
      
      (catch Throwable t (println t))
      (finally (. @connAm close))
      )
    
    )
  )

(defn db-query [conn sql & params]
  (let [stmt (. conn prepareStatement sql)]
    (doseq [idx (range 1 (inc (count params)))]
      (. stmt setObject idx (nth params (dec idx))))
    (let [rs (. stmt executeQuery)]
      rs ) ) )

(defn db-update [conn sql & params]
  (let [stmt (. conn prepareStatement sql)]
    (doseq [idx (range 1 (inc (count params)))]
      (. stmt setObject idx (nth params (dec idx))))
    (let [cnt (. stmt executeUpdate)]
      cnt ) ) )



(defn deserialize-row [rs row-def]
  (let [row1 (transient [])]
    (doseq [col-def row-def]
      (let [col-name (:column col-def)
            key-name (:key col-def col-name)]
        (case (:type col-def)
          :array (conj! row1 [key-name (. rs getArray col-name)])
          :ascii-stream (conj! row1 [key-name (. rs getAsciiStream col-name)])
          :bigdecimal (conj! row1 [key-name (. rs getBigDecimal col-name)])
          :binary-stream (conj! row1 [key-name (. rs getBinaryStream col-name)])
          :blob (conj! row1 [key-name (. rs getBlob col-name)])
          :boolean (conj! row1 [key-name (. rs getBoolean col-name)])
          :bool (conj! row1 [key-name (. rs getBoolean col-name)])
          :byte (conj! row1 [key-name (. rs getByte col-name)])
          :bytes (conj! row1 [key-name (. rs getBytes col-name)])
          :character-stream (conj! row1 [key-name (. rs getCharacterStream col-name)])
          :clob (conj! row1 [key-name (. rs getClob col-name)])
          :date (conj! row1 [key-name (. rs getDate col-name)])
          :time (conj! row1 [key-name (. rs getTime col-name)])
          :timestamp (conj! row1 [key-name (. rs getTimestamp col-name)])
          :double (conj! row1 [key-name (. rs getDouble col-name)])
          :float (conj! row1 [key-name (. rs getFloat col-name)])
          :int (conj! row1 [key-name (. rs getInt col-name)])
          :long (conj! row1 [key-name (. rs getLong col-name)])
          :nclob (conj! row1 [key-name (. rs getNClob col-name)])
          :nstring (conj! row1 [key-name (. rs getNString col-name)])
          :object (conj! row1 [key-name (. rs getObject col-name)])
          :ref (conj! row1 [key-name (. rs getRef col-name)])
          :row-id (conj! row1 [key-name (. rs getRowId col-name)])
          :short (conj! row1 [key-name (. rs getShort col-name)])
          :string (conj! row1 [key-name (. rs getString col-name)])
          :url (conj! row1 [key-name (. rs getUrl col-name)])
          nil )
        )
       
      )
    (persistent! row1)
    )
  
  )



(defn build-default-row-def [rs]
  (let [metaData (. rs getMetaData)
        colCnt (. metaData getColumnCount)
        defResult (transient [])]
    (doseq [colIdx (range 1 (inc colCnt))]
      (let [colType (. metaData getColumnType)]
        (let [typeDef (case colType
                        java.sql.Types.ARRAY :array
                        java.sql.Types.BIGINT :bigdecimal
                        java.sql.Types.BINARY :object
                        java.sql.Types.BIT :byte
                        java.sql.Types.BLOB :blob
                        java.sql.Types.BOOLEAN :boolean
                        java.sql.Types.CHAR :string
                        java.sql.Types.CLOB :clob
                        java.sql.Types.DATALINK :object
                        java.sql.Types.DATE :date
                        java.sql.Types.DECIMAL :bigdecimal
                        java.sql.Types.DISTINCT :object
                        java.sql.Types.DOUBLE :double
                        java.sql.Types.FLOAT :float
                        java.sql.Types.INTEGER :int
                        java.sql.Types.JAVA_OBJECT :object
                        java.sql.Types.LONGNVARCHAR :string
                        java.sql.Types.LONGVARBINARY :bytes
                        java.sql.Types.LONGVARCHAR :string
                        java.sql.Types.NCHAR :string
                        java.sql.Types.NCLOB :nclob
                        java.sql.Types.NULL :object
                        java.sql.Types.NUMERIC :bigdecimal
                        java.sql.Types.NVARCHAR :nstring
                        java.sql.Types.OTHER :object
                        java.sql.Types.REAL :bigdecimal
                        java.sql.Types.REF :ref
                        java.sql.Types.REF_CURSOR :object
                        java.sql.Types.ROWID :row-id
                        java.sql.Types.SMALLINT :short
                        java.sql.Types.SQLXML :object
                        java.sql.Types.STRUCT :object
                        java.sql.Types.TIME :time
                        java.sql.Types.TIME_WITH_TIMEZONE :time
                        java.sql.Types.TIMESTAMP :timestamp
                        java.sql.Types.TIMESTAMP_WITH_TIMEZONE :timestamp
                        java.sql.Types.TINYINT :short
                        java.sql.Types.VARBINARY :bytes
                        java.sql.Types.VARCHAR :string
                        nil)]
          (conj! defResult {:column (. metaData getColumnLabel)
                            :key (. metaData getColumnLabel)
                            :type typeDef})
          
          )
        )
      )

    (persistent! defResult)
    )
  )
