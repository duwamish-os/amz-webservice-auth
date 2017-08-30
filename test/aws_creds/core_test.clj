(ns aws-creds.core-test
  (:require [clojure.test :refer :all]
            [aws-creds.core :refer :all]
            [clj-http.client :as http]))

(deftest hits-the-endpoint
  (testing "hits the endpoint"
    (is (= 1 1))))

(deftest post-request
  (testing "sending a request"
    (let [response (http/post "https://jsonplaceholder.typicode.com/posts"                              
     {:body "{\"name\": \"updprayag\"}"                                                                
      :headers {"X-Api-Version" "2"}                                                                   
      :content-type :json                                                                              
      :socket-timeout 1000  ;; in milliseconds                                                         
      :conn-timeout 1000    ;; in milliseconds                                                         
      :accept :json})]
    (println (str "[INFO] http response " response ))
    (is (= (:status response) 201))
    (is (= (:body response) "{\n  \"name\": \"updprayag\",\n  \"id\": 101\n}")))))

;; https://clojure.org/guides/threading_macros

(defn transform-customer [customer]
  (update (assoc customer :item-color :gray) :age inc))

;; the arrow macro expands at compile time into the original code
(defn transform [customer]
  (-> customer
      (assoc :sku-color :blue)
      (update :age inc)))

(deftest updates-color
  (testing "adds item-color, updates age"

    (is (= (transform-customer {:name "prayagupd", :age 28}) {:name "prayagupd" :age 29 :item-color :gray}))

    (is (= (transform {:name "emant", :age 24}) {:name "emant" :age 25 :sku-color :blue}))))
