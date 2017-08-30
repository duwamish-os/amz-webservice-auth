(ns aws-creds.perf-specs

  (:import (java.util Date)
           (com.specs.database DatabaseComponentSpecs)
           (org.apache.commons.dbcp BasicDataSource))

  (:require [clojure.test :refer :all]
            [aws-creds.core :refer :all]
            [clj-http.client :as http]))

(defn db-connection
  "connections"  []
  (let [ds (new BasicDataSource)]
    (.setUrl ds "jdbc:as400://10.10.10.10:50000;prompt=false")
    (.setUsername ds "test")
    (.setPassword ds "test")
    (.setDriverClassName ds "com.ibm.as400.access.AS400JDBCDriver")
    (.getConnection ds)))

(deftest test-db []
  (let [limit 400]
    (doseq [x     (range 6)
            :when (< x 100)
            :let  [y (* x x)]]
      (println [x y]))))

(deftest queries-db []
  (let [limit      501
        identifier "yyy"
        username   "prayagupd"
        query (str "select COUNT(*) as orders from db.orders where ord_num LIKE '" identifier "%'")
        statement (.createStatement (db-connection))
        start (System/currentTimeMillis)]

    (doseq [counter  (range 1 limit)
            :let     [customerorder (str identifier "" counter)
                      request
                      (format "{\"MessageHeader\":{\"Producer\":\"Test\",\"EventName\":\"COCreate\",\"CorrelationId\":%s,\"CreatedDateTime\":\"2017-08-29T16:28:34-07:00\"},\"MessageBody\":{\"Details\":[{\"ColorDescription\":\"Orange Mandarin\",\"OrderLinePromotionId\":\"\",\"RetailPrice\":195,\"OrderLineQty\":1,\"OrderLineNo\":1,\"GlobalLineItemId\":\"5348d1bb-a6c1-4aca-9a09-f091da9bc5d0\",\"PromisedDateRange\":\"20170720-20170815\",\"SKUDescription\":\"Nordstrom men care\",\"ThirdPartyServiceId\":\"\",\"SizeDescription\":\"15.5 32\",\"EPMSKUID\":1181632333},{\"ColorDescription\":\"Orange Mandarin\",\"OrderLinePromotionId\":\"\",\"RetailPrice\":195,\"OrderLineQty\":1,\"OrderLineNo\":2,\"GlobalLineItemId\":\"5348d1bb-a6c1-4aca-9a09-f091da9bc5d0\",\"PromisedDateRange\":\"20170720-20170815\",\"SKUDescription\":\"Duwamish men care\",\"ThirdPartyServiceId\":\"\",\"SizeDescription\":\"15.5 32\",\"EPMSKUID\":1181632333}],\"Header\":{\"OrderCreationDate\":20170712,\"OrderTypeCode\":\"DL\",\"OrderEntryType\":\"WEB\",\"SoldtoAddressLine2\":\" \",\"SoldtoAddressLine1\":\"300 West 5thStreet #619\",\"SoldtoCountry\":\"US\",\"TelephoneNumber\":\"4257851454\",\"CustomerOrderNo\":\"%s\",\"OrderCreationTimestamp\":\"2017-08-29T16:28:34-07:00\",\"CustomerName\":\"prayagupd\",\"EnteredBy\":\"%s\",\"CountryCode\":\"US\",\"SoldtoZip\":\"28202\",\"BackOrderFlag\":\"\",\"TotalUnits\":2,\"SoldtoCity\":\"Charlotte\",\"ZipCode\":\"28202\",\"PlannedShipVia\":\"24\",\"City\":\"Charlotte\",\"SoldtoCustomerName\":\"hfkjds\",\"AddressLine2\":\" \",\"AddressLine1\":\"300 West 5thStreet #619\",\"OrderPromotionId\":\"\",\"SoldtoState\":\"NC\",\"PreSelectFlag\":\"\",\"State\":\"NC\",\"POBoxFlag\":\"N\",\"ShipNode\":\"881\",\"OrderReleaseNo\":1}}}"
                              counter customerorder username)
                      response (http/post "http://customerorder-dev.us-west-2.elasticbeanstalk.com/ingest"
                                          {:body         (str request)
                                           :socket-timeout 1000
                                           :conn-timeout 1000
                                           :content-type :json
                                           :accept :json})]]

      (println (str "[INFO] sent event-source-id#" counter))
      ;;(println (str "INFO " request))
      ;;(println (str "INFO " response))
      )

    (println (str "[INFO] " (- limit 1) " requests ingested / " (- (System/currentTimeMillis) start) " millis"))

    (loop [inserted-set (.executeQuery statement query)]
      (let [hasnext (.next inserted-set)
            orders (.getInt inserted-set "orders")
            matches (and hasnext (= (- limit 1) orders))]
        (println (str "[INFO] found " orders " orders"))
        (when (= false matches)
              ;;(println "[INFO] waiting for db to get updated")
              (recur (.executeQuery statement query)))))

    (println (str "[INFO] " (- limit 1) " requests processed / " (- (System/currentTimeMillis) start) " millis"))
    )
  )