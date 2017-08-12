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
    (is (= (:body response) "{\n  \"name\": \"updprayag\",\n  \"id\": 101\n}"))
)))
