(ns aws-creds.db-specs

   (:import (java.util Date)
            (com.specs.database DatabaseComponentSpecs)
            (org.apache.commons.dbcp BasicDataSource))
            
   (:require [clojure.test :refer :all]
            [aws-creds.core :refer :all]
            [clj-http.client :as http]))

((defn db-connection
  "connections"  []
   (let [ds (new BasicDataSource)]
     (.setUrl ds "jdbc:as400://172.18.21.252:50000;promt=false")
     (.setUsername ds "root")
     (.setPassword ds "root")
     (.setDriverClassName ds "com.ibm.as400.access.AS400JDBCDriver")

     (.getConnection ds))))

(deftest hits-the-endpoint
  (testing "queries the database"
    (let [statement (.createStatement (db-connection))
          result-set (.executeQuery statement "select * from db.customerorder where ord_num='660077012'")]
          (while (= true (.next result-set))
            (do
              (println (.getString result-set "correlationId"))
              (println (.getString result-set "created"))))))
    (is (= 1 1)))