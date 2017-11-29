(ns aws-creds.http-specs
  (:require [clojure.test :refer :all]
            [aws-creds.core :refer :all]
            [clj-http.client :as http]))

(deftest health-request
  (testing "heartbeat"
    (let [response (http/get "https://orders.shaharma.com/heartbeat"
     {:socket-timeout 1000  ;; in milliseconds                                                         
      :conn-timeout 1000    ;; in milliseconds                                                         
      :accept :json})]
    (println (str "[INFO] http response " response ))
    (is (= (:status response) 200)))))

(deftest images-request
  (testing "images"
    (let [response (http/get "http://data.shaharma.com/v2/product/id/sku/1000000"
     {:headers {"X-REQUEST-ID" "someting"}
      :socket-timeout 1000  ;; in milliseconds                                              
      :conn-timeout 1000    ;; in milliseconds                                                         
      :accept :json})]
    (println (str "[INFO] http response " response ))
    (is (= (:status response) 200)))))

(deftest get-request
  (testing "sending a request"
    (let [response (http/get "https://orders.shaharma.com/orders/SHM01-28"
     {:headers {"X-CLIENT" "NLU-AGENT"
                "CLIENT-ID" "RQ9dZ9kbEJuR6VGWRQRBYbBMsktKefJkGgV7H6Fa43Xh3We3"
                "CLIENT-SECRET" "GrkD5yurTxYEnb3YXjU444JLHE7sh4sENgBN6nuTEeSZ2TVsgVJEbS7DREu5uHSwNXPUVnsE2"} 
      :content-type :json                                                                 
      :socket-timeout 1000  ;; in milliseconds                                                         
      :conn-timeout 1000    ;; in milliseconds                                                         
      :accept :json})]
    (println (str "[INFO] http response " response ))
    (is (= (:status response) 200))
    )))
